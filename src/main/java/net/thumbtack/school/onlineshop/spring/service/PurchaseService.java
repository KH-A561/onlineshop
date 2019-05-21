package net.thumbtack.school.onlineshop.spring.service;

import net.thumbtack.school.onlineshop.model.Account;
import net.thumbtack.school.onlineshop.model.AccountType;
import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.model.Purchase;
import net.thumbtack.school.onlineshop.mybatis.dao.ClientDao;
import net.thumbtack.school.onlineshop.mybatis.dao.DepositDao;
import net.thumbtack.school.onlineshop.mybatis.dao.ProductDao;
import net.thumbtack.school.onlineshop.mybatis.dao.ProductInBasketDao;
import net.thumbtack.school.onlineshop.mybatis.dao.ProductInPurchaseDao;
import net.thumbtack.school.onlineshop.mybatis.dao.SessionDao;
import net.thumbtack.school.onlineshop.spring.dto.request.client.ProductClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.CategoryDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.ProductDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.BasketPurchaseClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.PurchaseClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.json.Json;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@Service
public class PurchaseService {
    private final ProductInPurchaseDao productInPurchaseDao;
    private final ClientDao clientDao;
    private final ProductDao productDao;
    private final DepositDao depositDao;
    private final ProductInBasketDao productInBasketDao;
    private final SessionDao sessionDao;

    @Autowired
    public PurchaseService(ProductInPurchaseDao productInPurchaseDao,
                           ClientDao clientDao,
                           ProductDao productDao,
                           DepositDao depositDao,
                           ProductInBasketDao productInBasketDao, SessionDao sessionDao) {
        this.productInPurchaseDao = productInPurchaseDao;
        this.clientDao = clientDao;
        this.productDao = productDao;
        this.depositDao = depositDao;
        this.productInBasketDao = productInBasketDao;
        this.sessionDao = sessionDao;
    }

    @Transactional(rollbackFor = ServerException.class)
    public PurchaseClientDtoResponse buyProduct(ProductClientDtoRequest request, String cookie) throws ServerException {
        Client client = clientDao.getByCookie(cookie);
        if (request.getCount() == null) {
            request.setCount(1);
        }
        if (request.getCount() * request.getPrice() > client.getDeposit().getAmount()) {
            throw new ServerException(ErrorCode.PURCHASE_INSUFFICIENT_FUNDS);
        }
        Product product = new Product(request.getId(), request.getName(), request.getPrice(), request.getCount());
        Product productOnStock = productDao.getById(product.getId());
        if (!product.getName().equals(productOnStock.getName())) {
            ErrorCode.PURCHASE_PRODUCT_DATA_MISMATCH.setField("name");
            throw new ServerException(ErrorCode.PURCHASE_PRODUCT_DATA_MISMATCH);
        }
        if (product.getPrice() != productOnStock.getPrice()) {
            ErrorCode.PURCHASE_PRODUCT_DATA_MISMATCH.setField("price");
            throw new ServerException(ErrorCode.PURCHASE_PRODUCT_DATA_MISMATCH);
        }
        if (product.getCount() > productOnStock.getCount()) {
            ErrorCode.PURCHASE_PRODUCT_DATA_MISMATCH.setField("count");
            throw new ServerException(ErrorCode.PURCHASE_PRODUCT_DATA_MISMATCH);
        }
        Purchase purchase = new Purchase(client, product, product.getName(), product.getPrice(), product.getCount());
        productInPurchaseDao.insert(purchase);
        productOnStock.setCount(productOnStock.getCount() - product.getCount());
        return new PurchaseClientDtoResponse(product.getId(), product.getName(), product.getPrice(), product.getCount());
    }

    @Transactional(rollbackFor = ServerException.class)
    public BasketPurchaseClientDtoResponse buyProductsInBasket(List<ProductClientDtoRequest> request, String cookie)
                                                               throws ServerException {
        Client client = clientDao.getByCookie(cookie);
        int fullPrice = 0;
        List<Product> remaining = new ArrayList<>();
        List<Product> bought = new ArrayList<>();
        List<Product> productsInRequest = request.stream().
                                                  map(r -> new Product(r.getId(), r.getName(), r.getPrice(), r.getCount())).
                                                  collect(Collectors.toList());
        List<Product> availableProductsInBasket = productInBasketDao.getAvailableProducts(productsInRequest);
        Map<Integer, Product> idAvailableProductsInBasket = availableProductsInBasket.stream()
                                                                                     .collect(Collectors.toMap(Product::getId, p -> p));

        for (Product product : productsInRequest) {
            Product availableProduct = idAvailableProductsInBasket.get(product.getId());
            if (availableProduct == null) {
                remaining.add(product);
                continue;
            }
            if (!product.getName().equals(availableProduct.getName()) || product.getPrice() != availableProduct.getPrice()) {
                remaining.add(product);
                continue;
            }
            if (product.getCount() == null || product.getCount() > availableProduct.getCount()) {
                if (availableProduct.getCount() != 0) {
                    product.setCount(availableProduct.getCount());
                } else {
                    continue;
                }
            }
            fullPrice += product.getPrice() * product.getCount();
            availableProduct.setCount(availableProduct.getCount() - product.getCount());
            bought.add(product);
        }

        if (fullPrice > client.getDeposit().getAmount()) {
            throw new ServerException(ErrorCode.PURCHASE_INSUFFICIENT_FUNDS);
        } else {
            if (bought.size() > 0) {
                client.setDeposit(client.getDeposit().getAmount() - fullPrice);
                depositDao.update(client, client.getDeposit());
                List<Purchase> purchases = bought.stream().map(p -> new Purchase(client,
                        p,
                        p.getName(),
                        p.getPrice(),
                        p.getCount())).
                        collect(Collectors.toList());
                productInPurchaseDao.batchInsert(purchases);
            }
            return new BasketPurchaseClientDtoResponse(bought, remaining);
        }
    }

    public ResponseEntity<?> getPurchases(List<Integer> categories, List<Integer> products, List<Integer> clients,
                                          String order, Integer offset, Integer limit, String cookie) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
        if (!account.getType().equals(AccountType.ADMINISTRATOR)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        List<Purchase> purchases = null;
        List<Client> clientsFromDb = null;
        if (offset == null) {
            offset = 0;
        }
        if (limit == null) {
            limit = 0;
        }
        if (categories == null && products == null && clients == null) {
            purchases = productInPurchaseDao.getAll(offset, limit);
            return ResponseEntity.ok().body(purchases.stream().map(p -> new PurchaseClientDtoResponse(p.getId(), p.getName(), p.getPrice(), p.getCount())).collect(Collectors.toList()));
        } else if (categories != null && products == null && clients == null) {
            purchases = productInPurchaseDao.getByCategories(categories.stream().map(Category::new).collect(Collectors.toList()));
            if (purchases == null || purchases.isEmpty()) {
                return ResponseEntity.ok().body(Json.createObjectBuilder().build());
            }
            if (order == null || order.equals("product")) {

            } else if (order.equals("category")) {

            } else {
                throw new ServerException(ErrorCode.INVALID_ORDER);
            }
        } else if (clients != null && products == null && categories == null) {
            clientsFromDb = clientDao.getClientsWithPurchases(clients);
            if (clientsFromDb == null || clientsFromDb.isEmpty()) {
                return ResponseEntity.ok().body(Json.createObjectBuilder().build());
            }
            if (order == null || order.equals("login")) {

            } else if (order.equals("profit")) {

            } else if (order.equals("count")) {

            } else {
                throw new ServerException(ErrorCode.INVALID_ORDER);
            }
        } else if (products != null && categories == null && clients == null) {
            Map<Product, Integer> productsFromDb = productInPurchaseDao.getBoughtProductsWithFullPrices(products.stream().map(Product::new).collect(Collectors.toList()));
            if (productsFromDb == null || productsFromDb.isEmpty()) {
                return ok().body(Json.createObjectBuilder().build());
            }
            if (order == null || order.equals("product")) {
                List<ProductDtoResponse> responses = productsFromDb.entrySet().stream().map(e -> new ProductDtoResponse<>(e.getKey().getId(),
                                                                                            e.getKey().getName(),
                                                                                            e.getKey().getPrice(),
                                                                                            e.getKey().getCount(),
                                                                                            e.getKey().getCategories().stream().map(Category::getName)
                                                                                                    .collect(Collectors.toList()),
                                                                                            e.getValue())).
                                                     sorted(Comparator.comparing(ProductDtoResponse::getName)).collect(Collectors.toList());
                String cookieString = ResponseCookie.from("JAVASESSIONID", cookie).build().toString();
                return ok().header(HttpHeaders.SET_COOKIE, cookieString).body(responses);
            } else if (order.equals("count")) {
                List<ProductDtoResponse> responses = productsFromDb.entrySet().stream().map(e -> new ProductDtoResponse<>(e.getKey().getId(),
                                                                                            e.getKey().getName(),
                                                                                            e.getKey().getPrice(),
                                                                                            e.getKey().getCount(),
                                                                                            null,
                                                                                            e.getValue())).
                                                    sorted(Comparator.comparingInt(ProductDtoResponse::getCount)).collect(Collectors.toList());
                String cookieString = ResponseCookie.from("JAVASESSIONID", cookie).build().toString();
                return ok().header(HttpHeaders.SET_COOKIE, cookieString).body(responses);
            } else if (order.equals("price")) {
                List<ProductDtoResponse> responses = productsFromDb.entrySet().stream().map(e -> new ProductDtoResponse<>(e.getKey().getId(),
                                                                                            e.getKey().getName(),
                                                                                            e.getKey().getPrice(),
                                                                                            e.getKey().getCount(),
                                                                                            null,
                                                                                            e.getValue())).
                                                     sorted(Comparator.comparingInt(ProductDtoResponse::getPrice)).collect(Collectors.toList());
                String cookieString = ResponseCookie.from("JAVASESSIONID", cookie).build().toString();
                return ok().header(HttpHeaders.SET_COOKIE, cookieString).body(responses);
            } else if (order.equals("profit")) {
                List<ProductDtoResponse> responses = productsFromDb.entrySet().stream().map(e -> new ProductDtoResponse<>(e.getKey().getId(),
                                                                                            e.getKey().getName(),
                                                                                            e.getKey().getPrice(),
                                                                                            e.getKey().getCount(),
                                                                                            null,
                                                                                            e.getValue())).
                                                     sorted(Comparator.comparingInt(ProductDtoResponse::getProfit)).collect(Collectors.toList());
                String cookieString = ResponseCookie.from("JAVASESSIONID", cookie).build().toString();
                return ok().header(HttpHeaders.SET_COOKIE, cookieString).body(responses);
            } else {
                throw new ServerException(ErrorCode.INVALID_ORDER);
            }
        }
        throw new ServerException(ErrorCode.INVALID_PARAMETERS);
    }
}

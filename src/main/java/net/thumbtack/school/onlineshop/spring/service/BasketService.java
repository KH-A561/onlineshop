package net.thumbtack.school.onlineshop.spring.service;

import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.mybatis.dao.ProductInBasketDao;
import net.thumbtack.school.onlineshop.mybatis.dao.ClientDao;
import net.thumbtack.school.onlineshop.mybatis.dao.ProductDao;
import net.thumbtack.school.onlineshop.mybatis.dao.SessionDao;
import net.thumbtack.school.onlineshop.spring.dto.request.client.ProductClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.client.PurchaseClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BasketService {
    private final ProductInBasketDao productInBasketDao;
    private final ClientDao clientDao;
    private final ProductDao productDao;
    private final SessionDao sessionDao;

    @Autowired
    public BasketService(ProductInBasketDao productInBasketDao, ClientDao clientDao, ProductDao productDao, SessionDao sessionDao) {
        this.productInBasketDao = productInBasketDao;
        this.clientDao = clientDao;
        this.productDao = productDao;
        this.sessionDao = sessionDao;
    }

    @Transactional(rollbackFor = ServerException.class)
    public List<PurchaseClientDtoResponse> addProduct(ProductClientDtoRequest request, String cookie) throws ServerException {
        Client client = clientDao.getByCookie(cookie);
        if (request.getCount() == null) {
            request.setCount(1);
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
        productInBasketDao.insert(client, product);
        List<Product> basket = productInBasketDao.getAllProductsByClient(client);
        return basket.stream().
                      map(p -> new PurchaseClientDtoResponse(p.getId(), p.getName(), p.getPrice(), p.getCount())).
                      collect(Collectors.toList());
    }

    @Transactional(rollbackFor = ServerException.class)
    public void deleteProductFromBasketById(int productId, String cookie) throws ServerException {
        Client client = clientDao.getByCookie(cookie);
        productInBasketDao.deleteProductFromBasketById(productId, client);
    }

    public List<PurchaseClientDtoResponse> editProductCountInBasket(ProductClientDtoRequest request, String cookie) throws ServerException {
        Client client = clientDao.getByCookie(cookie);

        Product productInBasket = client.getProductBasket().stream().filter(p -> p.getId() == request.getId()).findAny().
                                                            orElseThrow(() -> new ServerException(ErrorCode.PRODUCT_NOT_FOUND));
        if (!request.getName().equals(productInBasket.getName())) {
            ErrorCode.PURCHASE_PRODUCT_DATA_MISMATCH.setField("name");
            throw new ServerException(ErrorCode.PURCHASE_PRODUCT_DATA_MISMATCH);
        }
        if (request.getPrice() != productInBasket.getPrice()) {
            ErrorCode.PURCHASE_PRODUCT_DATA_MISMATCH.setField("price");
            throw new ServerException(ErrorCode.PURCHASE_PRODUCT_DATA_MISMATCH);
        }
        productInBasket.setCount(request.getCount());
        productInBasketDao.update(client, productInBasket);
        List<Product> basket = client.getProductBasket();
        return basket.stream().
                map(p -> new PurchaseClientDtoResponse(p.getId(), p.getName(), p.getPrice(), p.getCount())).
                collect(Collectors.toList());
    }

    @Transactional(rollbackFor = ServerException.class)
    public List<PurchaseClientDtoResponse> getBasket(String cookie) throws ServerException {
        Client client = clientDao.getByCookie(cookie);
        List<Product> basket = productInBasketDao.getAllProductsByClient(client);
        return basket.stream().
                map(p -> new PurchaseClientDtoResponse(p.getId(), p.getName(), p.getPrice(), p.getCount())).
                collect(Collectors.toList());
    }
}

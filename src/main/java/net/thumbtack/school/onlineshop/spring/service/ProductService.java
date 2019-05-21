package net.thumbtack.school.onlineshop.spring.service;

import net.thumbtack.school.onlineshop.model.Account;
import net.thumbtack.school.onlineshop.model.AccountType;
import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.mybatis.dao.CategoryDao;
import net.thumbtack.school.onlineshop.mybatis.dao.ProductCategoryDao;
import net.thumbtack.school.onlineshop.mybatis.dao.ProductDao;
import net.thumbtack.school.onlineshop.mybatis.dao.SessionDao;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.AddProductAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.EditProductAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.ProductDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductDao productDao;
    private final ProductCategoryDao productCategoryDao;
    private final SessionDao sessionDao;
    private final CategoryDao categoryDao;

    @Autowired
    public ProductService(ProductDao productDao, ProductCategoryDao productCategoryDao, SessionDao sessionDao, CategoryDao categoryDao) {
        this.productDao = productDao;
        this.productCategoryDao = productCategoryDao;
        this.sessionDao = sessionDao;
        this.categoryDao = categoryDao;
    }

    @Transactional(rollbackFor = ServerException.class)
    public ProductDtoResponse insert(AddProductAdminDtoRequest request, String cookie) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
        if (!account.getType().equals(AccountType.ADMINISTRATOR)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        Product product;
        if (request.getCount() == null) {
            request.setCount(0);
        }
        if (request.getCategories() == null || request.getCategories().isEmpty()) {
            product = new Product(0, request.getName(), request.getPrice(), request.getCount());
        } else {
            product = new Product(0, request.getName(), request.getPrice(), request.getCount(), request.getCategories().stream().map(Category::new).collect(Collectors.toList()));
        }
        product = productDao.insert(product);
        return new ProductDtoResponse<>(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                                        product.getCategories());
    }

    @Transactional(rollbackFor = ServerException.class)
    public ProductDtoResponse update(EditProductAdminDtoRequest request, int id, String cookie) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
        if (!account.getType().equals(AccountType.ADMINISTRATOR)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        Product newProduct = new Product();
        Product oldProduct = productDao.getById(id);
        if (request.getName() == null) {
            newProduct.setName(oldProduct.getName());
        }
        if (request.getPrice() == null) {
            newProduct.setPrice(oldProduct.getPrice());
        }
        if (request.getCount() == null) {
            newProduct.setCount(oldProduct.getCount());
        }
        if (request.getCategories() == null) {
            newProduct.setCategories(oldProduct.getCategories());
            productDao.update(newProduct);
            return new ProductDtoResponse<>(newProduct.getId(),
                                            newProduct.getName(),
                                            newProduct.getPrice(),
                                            newProduct.getCount(),
                                            newProduct.getCategories());
        } else {
            productCategoryDao.deleteAllByProductId(id);
            productDao.update(newProduct);
            if (!request.getCategories().isEmpty()) {
                newProduct.setCategories(request.getCategories());
                productCategoryDao.batchInsert(newProduct, newProduct.getCategories());
            }
            return new ProductDtoResponse<>(newProduct.getId(),
                                            newProduct.getName(),
                                            newProduct.getPrice(),
                                            newProduct.getCount(),
                                            newProduct.getCategories());
        }
    }

    @Transactional(rollbackFor = ServerException.class)
    public void deleteById(int id, String cookie) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
        if (!account.getType().equals(AccountType.ADMINISTRATOR)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        productDao.deleteById(id);
    }

    @Transactional(rollbackFor = ServerException.class)
    public ProductDtoResponse getById(int id, String cookie) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
        if (!account.getType().equals(AccountType.ADMINISTRATOR)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        Product product = productDao.getById(id);
        return new ProductDtoResponse<>(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                                        product.getCategories());
    }

    @Transactional(rollbackFor = ServerException.class)
    public List<ProductDtoResponse> getByCategories(List<Category> categories, String order, String cookie) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
        List<Product> products;
        if (categories == null) {
            products = productDao.getAll();
        } else if (categories.isEmpty()) {
            products = productDao.getNotCategorized();
        } else {
            products = productDao.getAllByCategories(categories);
        }
        if (order == null || order.equals("product")) {
            if (categories != null && !categories.isEmpty()) {
                return products.stream().map(p -> new ProductDtoResponse<>(p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getCount(),
                        p.getCategories().stream().map(Category::getName).
                                collect(Collectors.toList())))
                        .collect(Collectors.toList());
            } else {
                return products.stream().map(p -> new ProductDtoResponse<>(p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getCount(),
                        null)).collect(Collectors.toList());
            }
        } else if (order.equals("category")) {
            List<Product> notCategorizedProducts = new ArrayList<>();
            if (categories == null) {
                categories = categoryDao.getAll();
                if (categories == null) {
                    throw new ServerException(ErrorCode.CATEGORY_NOT_FOUND);
                }
            } else {
                for (int i = 0; i < categories.size(); i++) {
                    categories.set(i, categoryDao.getByIdWithoutSubcategories(categories.get(i).getId()));
                }
            }
            Map<Category, List<Product>> categoryProductsMap = new TreeMap<>(Comparator.comparing(Category::getName));
            categories.forEach(c -> categoryProductsMap.putIfAbsent(c, new ArrayList<>()));
            for (Product product : products) {
                if (product.getCategories() == null || product.getCategories().size() == 0) {
                    notCategorizedProducts.add(product);
                } else {
                    for (Category category : product.getCategories()) {
                        categoryProductsMap.get(category).add(new Product(product.getId(),
                                                                          product.getName(),
                                                                          product.getPrice(),
                                                                          product.getCount(),
                                                                          Collections.singletonList(category)));
                    }
                }
            }
            List<Product> result = new ArrayList<>(notCategorizedProducts);
            for (List<Product> value : categoryProductsMap.values()) {
                result.addAll(value);
            }
            return result.stream().map(p -> new ProductDtoResponse<>(p.getId(),
                                                                     p.getName(),
                                                                     p.getPrice(),
                                                                     p.getCount(),
                                                                     p.getCategories().stream().map(Category::getName).
                                                                                                collect(Collectors.toList()))).
                                   collect(Collectors.toList());
        } else {
            throw new ServerException(ErrorCode.INVALID_ORDER);
        }
    }
}

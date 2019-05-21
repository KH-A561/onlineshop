package net.thumbtack.school.onlineshop.mybatis.dao;


import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.model.Purchase;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;

import java.util.List;
import java.util.Map;

public interface ProductInPurchaseDao {
    Purchase insert(Purchase purchase) throws ServerException;

    void batchInsert(List<Purchase> purchases) throws ServerException;

    List<Purchase> getByCategories(List<Category> categories);

    Map<Product, Integer> getBoughtProductsWithFullPrices(List<Product> products);

    List<Purchase> getAll(Integer offset, Integer limit);
}

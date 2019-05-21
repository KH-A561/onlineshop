package net.thumbtack.school.onlineshop.mybatis.dao;


import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;

import java.util.List;

public interface ProductInBasketDao {
    void insert(Client client, Product product) throws ServerException;

    List<Product> getAllProductsByClient(Client client) throws ServerException;

    void deleteProductFromBasketById(int productId, Client client) throws ServerException;

    void update(Client client, Product product) throws ServerException;

    List<Product> getAvailableProducts(List<Product> products) throws ServerException;

}

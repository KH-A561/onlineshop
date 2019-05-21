package net.thumbtack.school.onlineshop.mybatis.dao;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;

import java.util.List;

public interface ProductDao {
    Product insert(Product product) throws ServerException;

    void update(Product newProduct) throws ServerException;

    void deleteById(int id) throws ServerException;

    Product getById(int id) throws ServerException;

    List<Product> getAll() throws ServerException;

    List<Product> getNotCategorized() throws ServerException;

    List<Product> getAllByCategories(List<Category> categories) throws ServerException;
}

package net.thumbtack.school.onlineshop.mybatis.dao;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import org.springframework.stereotype.Component;

import java.util.List;

public interface ProductCategoryDao {
    void batchInsert(Product product, List<Category> categories) throws ServerException;

    void deleteAllByProductId(int id) throws ServerException;
}

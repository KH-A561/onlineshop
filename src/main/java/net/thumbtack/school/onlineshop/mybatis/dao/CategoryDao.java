package net.thumbtack.school.onlineshop.mybatis.dao;


import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;

import java.util.List;

public interface CategoryDao {
    Category insert(Category category) throws ServerException;

    Category getByIdWithoutSubcategories(int id) throws ServerException;

    void update(Category newCategory) throws ServerException;

    void deleteById(int id) throws ServerException;

    List<Category> getAll() throws ServerException;
}

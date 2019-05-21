package net.thumbtack.school.onlineshop.model.dao;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CategoryDaoTest extends DaoTestBase {
    @Test
    public void insertCategory() throws ServerException {
        Category category;
        category = new Category("Category 1", null);
        categoryDao.insert(category);
        category = new Category("Category 2", null);
        categoryDao.insert(category);
        category = new Category("Subcategory 3", category);
        categoryDao.insert(category);
        Category categoryFromDb = categoryDao.getByIdWithoutSubcategories(category.getId());
        assertEquals(categoryFromDb, category);
    }

    @Test
    public void insertCategoryDuplicateName() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.CATEGORY_EXISTING_NAME.getErrorDescription());

        Category category;
        category = new Category("Category", null);
        categoryDao.insert(category);
        category = new Category("Category", null);
        categoryDao.insert(category);
    }

    @Test
    public void insertCategoryParentNotFound() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.CATEGORY_PARENT_NOT_FOUND.getErrorDescription());

        Category category;
        category = new Category("Category", null);
        category = new Category("Subcategory", category);
        categoryDao.insert(category);
    }

    @Test
    public void updateCategory() throws ServerException {
        Category category;
        category = new Category("Category", null);
        categoryDao.insert(category);
        category.setName("Updated Category");
        categoryDao.update(category);
        Category categoryFromDb = categoryDao.getByIdWithoutSubcategories(category.getId());
        assertEquals(categoryFromDb, category);
    }

    @Test
    public void deleteCategory() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.CATEGORY_NOT_FOUND.getErrorDescription());

        Category category;
        category = new Category("Category", null);
        categoryDao.insert(category);
        categoryDao.deleteById(category.getId());
        categoryDao.getByIdWithoutSubcategories(category.getId());
    }

    @Test
    public void getAllCategories() throws ServerException {
        Category category;
        Category subcategory;
        category = new Category("Category 1", null);
        categoryDao.insert(category);
        category = new Category("Category 2", null);
        categoryDao.insert(category);
        subcategory = new Category("Subcategory 3", category);
        categoryDao.insert(subcategory);
        List<Category> categoriesFromDb = categoryDao.getAll();
        assertTrue(categoriesFromDb.get(1).getSubcategories().contains(subcategory));
    }
}

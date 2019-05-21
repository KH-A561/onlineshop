package net.thumbtack.school.onlineshop.model.dao;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ProductCategoryDaoTest extends DaoTestBase {
    @Test
    public void insertAndDeleteProductWithCategories() throws ServerException {
        List<Category> categories = new ArrayList<>();
        Category category;
        categories.add(category = new Category("Category 1", null));
        categoryDao.insert(category);
        categories.add(category = new Category("Category 2", null));
        categoryDao.insert(category);
        categories.add(category = new Category("Subcategory 3", category));
        categoryDao.insert(category);
        Product product = new Product(0, "Product 1", 100, 3, categories);
        productDao.insert(product);
        productCategoryDao.deleteAllByProductId(product.getId());
        assertTrue(productDao.getById(product.getId()).getCategories().isEmpty());
    }
}

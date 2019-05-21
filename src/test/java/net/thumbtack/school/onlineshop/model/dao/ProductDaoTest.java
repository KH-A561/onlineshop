package net.thumbtack.school.onlineshop.model.dao;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ProductDaoTest extends DaoTestBase {
    @Test
    public void insertProduct() throws ServerException {
        Product product = new Product("Product 1", 100, 3);
        productDao.insert(product);
        assertNotEquals(0, product.getId());
        Product productFromDb = productDao.getById(product.getId());
        assertEquals(product, productFromDb);
    }

    @Test
    public void getProductByIdWithCategories() throws ServerException {
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
        assertNotEquals(0, product.getId());
        product = productDao.getById(product.getId());
        for (int i = 0; i < categories.size(); i++) {
            assertEquals(categories.get(i).getId(), product.getCategories().get(i).getId());
        }
    }

    @Test
    public void insertProductWithNonexistentCategories() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.CATEGORY_NOT_FOUND.getErrorDescription());

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Category 1", null));
        Product product = new Product(0, "Product 1", 100, 3, categories);
        productDao.insert(product);
    }

    @Test
    public void updateProduct() throws ServerException {
        Product product = new Product("Product 1", 100, 3);
        productDao.insert(product);
        assertNotEquals(0, product.getId());
        product.setName("Updated Product 1");
        productDao.update(product);
        Product productFromDb = productDao.getById(product.getId());
        assertEquals(product.getName(), productFromDb.getName());
    }

    @Test
    public void deleteProduct() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.PRODUCT_NOT_FOUND.getErrorDescription());

        Product product = new Product("Product 1", 100, 3);
        productDao.insert(product);
        assertNotEquals(0, product.getId());
        productDao.deleteById(product.getId());
        productDao.getById(product.getId());
    }

    @Test
    public void getAllProducts() throws ServerException {
        List<Category> categories = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        Category category;

        category = new Category("Category 1", null);
        categories.add(categoryDao.insert(category));
        Product product = new Product(0, "Product 1", 100, 3, Collections.singletonList(category));
        products.add(productDao.insert(product));

        category = new Category("Category 2", null);
        categories.add(categoryDao.insert(category));
        product = new Product(0, "Product 2", 200, 5, Collections.singletonList(category));
        products.add(productDao.insert(product));

        category = new Category("Category 3", category);
        categories.add(categoryDao.insert(category));
        product = new Product(0, "Product 3", 300, 7, Collections.singletonList(category));
        products.add(productDao.insert(product));

        List<Product> productsFromDb = productDao.getAll();
        assertEquals(productsFromDb, products);
        for (int i = 0; i < productsFromDb.size(); i++) {
            assertEquals(categories.get(i).getId(), productsFromDb.get(i).getCategories().get(0).getId());
        }
    }

    @Test
    public void getAllProductsByCategories() throws ServerException {
        List<Category> categories = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        Category category;
        Category buffCategory;

        category = new Category("Category 1", null);
        categoryDao.insert(category);
        Product product = new Product(0, "Product 1", 100, 3, Collections.singletonList(category));
        productDao.insert(product);

        category = new Category("Category 2", null);
        buffCategory = category;
        categories.add(categoryDao.insert(category));
        product = new Product(0, "Product 2", 200, 5, Collections.singletonList(category));
        products.add(productDao.insert(product));

        category = new Category("Category 3", category);
        categories.add(categoryDao.insert(category));
        product = new Product(0, "Product 3", 300, 7, Arrays.asList(category, buffCategory));
        products.add(productDao.insert(product));

        List<Product> productsFromDb = productDao.getAllByCategories(categories);
        assertEquals(productsFromDb, products);
    }

    @Test
    public void getNotCategorizedProducts() throws ServerException {
        List<Category> categories = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        Category category;
        Product product;
        Product productWithoutCategory = new Product("Product 1", 100, 3);
        productDao.insert(productWithoutCategory);

        category = new Category("Category 2", null);
        categories.add(categoryDao.insert(category));
        product = new Product(0, "Product 2", 200, 5, Collections.singletonList(category));
        products.add(productDao.insert(product));

        category = new Category("Category 3", category);
        categories.add(categoryDao.insert(category));
        product = new Product(0, "Product 3", 300, 7, Collections.singletonList(category));
        products.add(productDao.insert(product));

        List<Product> categorizedProducts = productDao.getAllByCategories(categories);
        assertEquals(categorizedProducts, products);
        for (int i = 0; i < categorizedProducts.size(); i++) {
            assertEquals(categories.get(i).getId(), categorizedProducts.get(i).getCategories().get(0).getId());
        }

        List<Product> notCategorizedProducts = productDao.getNotCategorized();
        assertTrue(notCategorizedProducts.contains(productWithoutCategory));
    }
}

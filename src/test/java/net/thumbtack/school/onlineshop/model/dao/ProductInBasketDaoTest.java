package net.thumbtack.school.onlineshop.model.dao;

import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ProductInBasketDaoTest extends DaoTestBase {
    @Test
    public void insertProductInBasket() throws ServerException {
        Client client = new Client("NameA",
                "SurnameA",
                null,
                "Аа123321",
                "123456789а",
                "aaa@aaa.com",
                "Aaaaa st.",
                "+79259876543");
        accountDao.insert(client);
        assertNotEquals(0, client.getId());
        clientDao.insert(client);

        Product product = new Product("Product 1", 100, 3);
        productDao.insert(product);
        assertNotEquals(0, product.getId());

        productInBasketDao.insert(client, product);
        assertEquals(Collections.singletonList(product), productInBasketDao.getAllProductsByClient(client));
    }

    @Test
    public void deleteProductFromBasket() throws ServerException {
        Client client = new Client("NameA",
                "SurnameA",
                null,
                "Аа123321",
                "123456789а",
                "aaa@aaa.com",
                "Aaaaa st.",
                "+79259876543");
        accountDao.insert(client);
        assertNotEquals(0, client.getId());
        clientDao.insert(client);

        Product product = new Product("Product 1", 100, 3);
        productDao.insert(product);
        assertNotEquals(0, product.getId());

        productInBasketDao.insert(client, product);
        productInBasketDao.deleteProductFromBasketById(product.getId(), client);
        assertEquals(Collections.emptyList(), productInBasketDao.getAllProductsByClient(client));
    }

    @Test
    public void updateProductInBasket() throws ServerException {
        Client client = new Client("NameA",
                "SurnameA",
                null,
                "Аа123321",
                "123456789а",
                "aaa@aaa.com",
                "Aaaaa st.",
                "+79259876543");
        accountDao.insert(client);
        assertNotEquals(0, client.getId());
        clientDao.insert(client);

        Product product = new Product("Product 1", 100, 3);
        productDao.insert(product);
        assertNotEquals(0, product.getId());

        productInBasketDao.insert(client, product);
        product.setCount(5);
        productInBasketDao.update(client, product);
        assertEquals(Collections.singletonList(product), productInBasketDao.getAllProductsByClient(client));
    }

    @Test
    public void getAvailableProducts() throws ServerException {
        Client client = new Client("NameA",
                "SurnameA",
                null,
                "Аа123321",
                "123456789а",
                "aaa@aaa.com",
                "Aaaaa st.",
                "+79259876543");
        accountDao.insert(client);
        assertNotEquals(0, client.getId());
        clientDao.insert(client);

        List<Product> wantedProductsFromBasket = new ArrayList<>();
        List<Product> expected = new ArrayList<>();
        List<Product> actual = new ArrayList<>();

        Product product = new Product("Product 1", 100, 3);
        productDao.insert(product);
        assertNotEquals(0, product.getId());
        productInBasketDao.insert(client, product);
        wantedProductsFromBasket.add(product);
        expected.add(product);

        product = new Product("Product 2", 100, 3);
        productDao.insert(product);
        assertNotEquals(0, product.getId());
        productInBasketDao.insert(client, product);
        wantedProductsFromBasket.add(product);
        expected.add(product);

        product = new Product("Product 3", 100, 3);
        productDao.insert(product);
        assertNotEquals(0, product.getId());
        product.setCount(5);
        productInBasketDao.insert(client, product);
        wantedProductsFromBasket.add(product);

        product = new Product("Product 4", 100, 3);
        productDao.insert(product);
        assertNotEquals(0, product.getId());
        wantedProductsFromBasket.add(product);

        product = new Product("Product 5", 100, 3);
        productDao.insert(product);
        assertNotEquals(0, product.getId());

        actual = productInBasketDao.getAvailableProducts(wantedProductsFromBasket);
        assertEquals(expected, actual);
    }
}

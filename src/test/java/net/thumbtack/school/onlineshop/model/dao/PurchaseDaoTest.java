package net.thumbtack.school.onlineshop.model.dao;

import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.model.Purchase;
import net.thumbtack.school.onlineshop.model.Session;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PurchaseDaoTest extends DaoTestBase {
    @Test
    public void getClientsWithPurchases() throws ServerException {
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
        Session session = new Session("itscookie", client);
        sessionDao.insert(session);
        assertEquals(client.getType(), sessionDao.getByCookie(session.getCookie()).getAccount().getType());
        assertEquals(client.getId(), sessionDao.getByCookie(session.getCookie()).getAccount().getId());

        List<Purchase> purchaseList = new ArrayList<>();
        Product product = new Product("Product 1", 100, 3);
        purchaseList.add(new Purchase(client, product, product.getName(), product.getPrice(), product.getCount()));
        productDao.insert(product);
        assertNotEquals(0, product.getId());
        Product productFromDb = productDao.getById(product.getId());
        assertEquals(product, productFromDb);
        product = new Product("Product 2", 100, 3);
        purchaseList.add(new Purchase(client, product, product.getName(), product.getPrice(), product.getCount()));
        productDao.insert(product);
        assertNotEquals(0, product.getId());
        productFromDb = productDao.getById(product.getId());
        product = new Product("Product 3", 100, 3);
        purchaseList.add(new Purchase(client, product, product.getName(), product.getPrice(), product.getCount()));
        productDao.insert(product);
        assertNotEquals(0, product.getId());
        productFromDb = productDao.getById(product.getId());
        assertEquals(product, productFromDb);
        assertEquals(product, productFromDb);
        product = new Product("Product 4", 100, 3);
        purchaseList.add(new Purchase(client, product, product.getName(), product.getPrice(), product.getCount()));
        productDao.insert(product);
        assertNotEquals(0, product.getId());
        productFromDb = productDao.getById(product.getId());
        assertEquals(product, productFromDb);

        productInPurchaseDao.batchInsert(purchaseList);
        List<Client> clients = clientDao.getClientsWithPurchases(Collections.singletonList(client.getId()));
    }
}

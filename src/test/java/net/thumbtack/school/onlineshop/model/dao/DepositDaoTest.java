package net.thumbtack.school.onlineshop.model.dao;

import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DepositDaoTest extends DaoTestBase {
    @Test
    public void testUpdateDeposit() throws ServerException {
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
        client.getDeposit().setAmount(100);
        depositDao.update(client, client.getDeposit());
        Client clientFromDb = clientDao.getById(client.getId());
        assertEquals(clientFromDb.getDeposit().getAmount(), 100);
    }

    @Test
    public void testUpdateDepositSetNegativeAmount() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.PURCHASE_INSUFFICIENT_FUNDS.getErrorDescription());

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
        client.getDeposit().setAmount(-1);
        depositDao.update(client, client.getDeposit());
    }

    @Test
    public void testInsertDepositForNonexistentClient() throws ServerException {
        exceptionRule.expect(ServerException.class);

        Client client = new Client("NameA",
                "SurnameA",
                null,
                "Аа123321",
                "123456789а",
                "aaa@aaa.com",
                "Aaaaa st.",
                "+79259876543");
        depositDao.insert(client, client.getDeposit());
    }

    @Test
    public void testInsertDuplicateDeposit() throws ServerException {
        exceptionRule.expect(ServerException.class);

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
        depositDao.insert(client, client.getDeposit());
    }
}

package net.thumbtack.school.onlineshop.model.dao;

import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Session;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.junit.Assert.*;

public class ClientDaoTest extends DaoTestBase {
    @Test
    public void testInsertClient() throws ServerException {
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
        Client clientFromDb = clientDao.getById(client.getId());
        assertEquals(clientFromDb, client);
    }

    @Test
    public void testInsertClientWithoutAccount() throws ServerException {
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
        client.setId(-1);
        clientDao.insert(client);
    }

    @Test
    public void testInsertClientEmailDuplicate() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.CLIENT_EMAIL_ALREADY_EXISTS.getErrorDescription());

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
        client.setId(0);
        client.setLogin("Aaa456654");
        accountDao.insert(client);
        assertNotEquals(0, client.getId());
        clientDao.insert(client);
    }

    @Test
    public void testInsertClientPhoneDuplicate() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.CLIENT_PHONE_ALREADY_EXISTS.getErrorDescription());

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
        client.setId(0);
        client.setLogin("Aaa456654");
        client.setEmail("bbb@bbb.com");
        accountDao.insert(client);
        assertNotEquals(0, client.getId());
        clientDao.insert(client);
    }

    @Test
    public void testGetNonexistentClient() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.CLIENT_NOT_FOUND_BY_ACCOUNT_ID.getErrorDescription());

        clientDao.getById(-1);
    }

    @Test
    public void testGetClients() throws ServerException {
        Client clientA = new Client("NameA",
                "SurnameA",
                null,
                "Аа123321",
                "123456789а",
                "aaa@aaa.com",
                "Aaaaa st.",
                "+79259876543");
        accountDao.insert(clientA);
        assertNotEquals(0, clientA.getId());
        clientDao.insert(clientA);
        Client clientB = new Client("NameB",
                "SurnameB",
                null,
                "Bb456654",
                "987654321b",
                "bbb@bbb.com",
                "Bbbbbb st.",
                "+79123546411");
        accountDao.insert(clientB);
        assertNotEquals(0, clientB.getId());
        clientDao.insert(clientB);
        List<Client> clients = clientDao.getClients();
        assertTrue(clients.contains(clientA));
        assertTrue(clients.contains(clientB));
    }

    @Test
    public void testGetClientsNull() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.NO_CLIENTS_FOUND.getErrorDescription());

        clientDao.getClients();
    }

    @Test
    public void testUpdateClient() throws ServerException {
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
        Client clientFromDb;
        client.setAddress("Abbbb st.");
        clientDao.update(client);
        clientFromDb = clientDao.getById(client.getId());
        assertEquals(clientFromDb, client);
    }

    @Test
    public void testUpdateClientDuplicateEmail() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.CLIENT_EMAIL_ALREADY_EXISTS.getErrorDescription());

        Client clientA = new Client("NameA",
                "SurnameA",
                null,
                "Аа123321",
                "123456789а",
                "aaa@aaa.com",
                "Aaaaa st.",
                "+79259876543");
        accountDao.insert(clientA);
        assertNotEquals(0, clientA.getId());
        clientDao.insert(clientA);
        Client clientB = new Client("NameB",
                "SurnameB",
                null,
                "Bb456654",
                "987654321b",
                "bbb@bbb.com",
                "Bbbbbb st.",
                "+79123546411");
        accountDao.insert(clientB);
        assertNotEquals(0, clientB.getId());
        clientDao.insert(clientB);
        clientA.setEmail("bbb@bbb.com");
        clientDao.update(clientA);
    }

    @Test
    public void testUpdateClientDuplicatePhone() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.CLIENT_PHONE_ALREADY_EXISTS.getErrorDescription());

        Client clientA = new Client("NameA",
                "SurnameA",
                null,
                "Аа123321",
                "123456789а",
                "aaa@aaa.com",
                "Aaaaa st.",
                "+79259876543");
        accountDao.insert(clientA);
        assertNotEquals(0, clientA.getId());
        clientDao.insert(clientA);
        Client clientB = new Client("NameB",
                "SurnameB",
                null,
                "Bb456654",
                "987654321b",
                "bbb@bbb.com",
                "Bbbbbb st.",
                "+79123546411");
        accountDao.insert(clientB);
        assertNotEquals(0, clientB.getId());
        clientDao.insert(clientB);
        clientA.setPhone("+79123546411");
        clientDao.update(clientA);
    }

    @Test
    public void testGetClientByCookie() throws ServerException {
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
        Session clientSession = new Session("itscookie123", client);
        sessionDao.insert(clientSession);
        Client clientFromDb = clientDao.getByCookie(clientSession.getCookie());
        assertEquals(clientFromDb, client);
    }

    @Test
    public void testGetNonexistentClientByCookie() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.CLIENT_NOT_FOUND_BY_ACCOUNT_ID.getErrorDescription());

        Client client = new Client("NameA",
                "SurnameA",
                null,
                "Аа123321",
                "123456789а",
                "aaa@aaa.com",
                "Aaaaa st.",
                "+79259876543");
        Session clientSession = new Session("itscookie123", client);
        clientDao.getByCookie(clientSession.getCookie());
    }
}

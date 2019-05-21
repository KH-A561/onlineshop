package net.thumbtack.school.onlineshop.model.dao;

import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Session;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SessionDaoTest extends DaoTestBase {
    @Test
    public void testInsertSession() throws ServerException {
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
    }

    @Test
    public void testInsertSessionWithoutAccount() throws ServerException {
        exceptionRule.expect(ServerException.class);

        Client client = new Client("NameA",
                "SurnameA",
                null,
                "Аа123321",
                "123456789а",
                "aaa@aaa.com",
                "Aaaaa st.",
                "+79259876543");
        Session session = new Session("itscookie2", client);
        sessionDao.insert(session);
    }

    @Test
    public void testInsertSessionForLoggedAccountAndGetByOldCookie() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.SESSION_NOT_FOUND.getErrorDescription());

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
        session = new Session("itscookie2", client);
        sessionDao.insert(session);
        assertEquals(client.getType(), sessionDao.getByCookie(session.getCookie()).getAccount().getType());
        assertEquals(client.getId(), sessionDao.getByCookie(session.getCookie()).getAccount().getId());
        sessionDao.getByCookie("itscookie");
    }

    @Test
    public void testDeleteSession() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.SESSION_NOT_FOUND.getErrorDescription());

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
        sessionDao.delete(session);
        sessionDao.getByCookie("itscookie");
    }
}

package net.thumbtack.school.onlineshop.model.dao;

import net.thumbtack.school.onlineshop.model.Account;
import net.thumbtack.school.onlineshop.model.Administrator;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AccountDaoTest extends DaoTestBase {
    @Test
    public void testInsertAccount() throws ServerException {
        Account account = new Administrator("NameA",
                                            "SurnameA",
                                            null,
                                            "Аа123321",
                                            "123456789а",
                                            "Position");
        accountDao.insert(account);
        assertNotEquals(0, account.getId());
        Account accountFromDb = accountDao.getByLogin("Аа123321");
        assertEquals(accountFromDb, account);
    }

    @Test
    public void testInsertAccountWithExistingLogin() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.ACCOUNT_LOGIN_ALREADY_EXISTS.getErrorDescription());

        Account account = new Administrator("NameA",
                "SurnameA",
                null,
                "Аа123321",
                "123456789а",
                "Position");
        accountDao.insert(account);
        assertNotEquals(0, account.getId());
        account.setId(0);
        accountDao.insert(account);
    }

    @Test
    public void testInsertExistingLogin() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.ACCOUNT_LOGIN_ALREADY_EXISTS.getErrorDescription());

        Account account = new Administrator("NameA",
                "SurnameA",
                null,
                "Аа123321",
                "123456789а",
                "Position");
        accountDao.insert(account);
        assertNotEquals(0, account.getId());
        account.setId(0);
        accountDao.insert(account);
    }

    @Test
    public void testUpdateAccount() throws ServerException {
        Account account = new Administrator("NameA",
                                            "SurnameA",
                                            null,
                                            "Аа123321",
                                            "123456789а",
                                            "Position");
        accountDao.insert(account);
        assertNotEquals(0, account.getId());
        account.setFirstName("NameB");
        account.setLastName("SurnameB");
        accountDao.update(account);
        Account accountFromDb = accountDao.getByLogin("Аа123321");
        assertEquals(accountFromDb, account);
    }

    @Test
    public void testUpdateAccountLogin() throws ServerException {
        Account account = new Administrator("NameA",
                                            "SurnameA",
                                            null,
                                            "Аа123321",
                                            "123456789а",
                                            "Position");
        accountDao.insert(account);
        assertNotEquals(0, account.getId());
        account.setFirstName("NameB");
        account.setLogin("Bb123321");
        accountDao.update(account);
        Account accountFromDb = accountDao.getByLogin("Аа123321");
        assertEquals("Аа123321", accountFromDb.getLogin());
        assertNotEquals(accountFromDb, account);
        assertEquals(accountFromDb.getId(), account.getId());
    }

    @Test
    public void testGetNonexistentAccount() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.ACCOUNT_NOT_FOUND_BY_LOGIN.getErrorDescription());

        accountDao.getByLogin("");
    }
}

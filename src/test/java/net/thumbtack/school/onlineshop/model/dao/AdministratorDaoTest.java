package net.thumbtack.school.onlineshop.model.dao;

import net.thumbtack.school.onlineshop.model.Administrator;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AdministratorDaoTest extends DaoTestBase {
    @Test
    public void testInsertAdministrator() throws ServerException {
        Administrator admin = new Administrator("NameA",
                                                    "SurnameA",
                                                    null,
                                                    "Аа123321",
                                                    "123456789а",
                                                    "Position");
        accountDao.insert(admin);
        assertNotEquals(0, admin.getId());
        administratorDao.insert(admin);
        Administrator adminFromDb = administratorDao.getById(admin.getId());
        assertEquals(adminFromDb, admin);
    }

    @Test
    public void testInsertAdminWithoutAccount() throws ServerException {
        exceptionRule.expect(ServerException.class);

        Administrator administrator = new Administrator("NameA",
                                                        "SurnameA",
                                                        null,
                                                        "Аа123321",
                                                        "123456789а",
                                                        "Position");
        administratorDao.insert(administrator);
    }

    @Test
    public void getNonexistentAdmin() throws ServerException {
        exceptionRule.expect(ServerException.class);
        exceptionRule.expectMessage(ErrorCode.ADMINISTRATOR_NOT_FOUND_BY_ACCOUNT_ID.getErrorDescription());

        administratorDao.getById(-1);
    }

    @Test
    public void testInsertSecondPosition() throws ServerException {
        exceptionRule.expect(ServerException.class);

        Administrator admin = new Administrator("NameA",
                "SurnameA",
                null,
                "Аа123321",
                "123456789а",
                "Position");
        accountDao.insert(admin);
        assertNotEquals(0, admin.getId());
        administratorDao.insert(admin);
        admin.setPosition("Position 2");
        administratorDao.insert(admin);
    }

    @Test
    public void testUpdateAdmin() throws ServerException {
        Administrator admin = new Administrator("NameA",
                                                "SurnameA",
                                                null,
                                                "Аа123321",
                                                "123456789а",
                                                "Position");
        accountDao.insert(admin);
        assertNotEquals(0, admin.getId());
        administratorDao.insert(admin);
        admin.setPosition("Position 2");
        administratorDao.update(admin);
        Administrator adminFromDb = administratorDao.getById(admin.getId());
        assertEquals(adminFromDb, admin);
    }
}

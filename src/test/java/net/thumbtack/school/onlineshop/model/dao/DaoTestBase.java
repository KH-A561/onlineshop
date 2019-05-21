package net.thumbtack.school.onlineshop.model.dao;

import net.thumbtack.school.onlineshop.mybatis.dao.*;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DaoTestBase {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    AccountDao accountDao;
    @Autowired
    AdministratorDao administratorDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    ClientDao clientDao;
    @Autowired
    DepositDao depositDao;
    @Autowired
    ProductCategoryDao productCategoryDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    ProductInBasketDao productInBasketDao;
    @Autowired
    ProductInPurchaseDao productInPurchaseDao;
    @Autowired
    SessionDao sessionDao;
    @Autowired
    private DebugDao debugDao;

    @Before()
    public void clearDatabase() throws ServerException {
        debugDao.clearDatabase();
    }

    @Test
    public void testBase() throws ServerException {
        debugDao.clearDatabase();
    }
}

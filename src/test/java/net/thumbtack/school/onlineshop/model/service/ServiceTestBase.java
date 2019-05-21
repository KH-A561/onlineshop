package net.thumbtack.school.onlineshop.model.service;

import net.thumbtack.school.onlineshop.mybatis.dao.DebugDao;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.service.AccountService;
import net.thumbtack.school.onlineshop.spring.service.AdminService;
import net.thumbtack.school.onlineshop.spring.service.BasketService;
import net.thumbtack.school.onlineshop.spring.service.CategoryService;
import net.thumbtack.school.onlineshop.spring.service.ClientService;
import net.thumbtack.school.onlineshop.spring.service.DepositService;
import net.thumbtack.school.onlineshop.spring.service.ProductService;
import net.thumbtack.school.onlineshop.spring.service.PurchaseService;
import net.thumbtack.school.onlineshop.spring.service.SessionService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ServiceTestBase {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    AccountService accountService;
    @Autowired
    AdminService adminService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ClientService clientService;
    @Autowired
    DepositService depositService;
    @Autowired
    ProductService productService;
    @Autowired
    BasketService basketService;
    @Autowired
    PurchaseService purchaseService;
    @Autowired
    SessionService sessionService;
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

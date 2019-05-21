package net.thumbtack.school.onlineshop.model.integration;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.mybatis.dao.DebugDao;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.AddCategoryAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.AddProductAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.RegisterAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.client.DepositMoneyClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.client.ProductClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.client.RegisterClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.AdminDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.CategoryDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.ProductDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.ClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.PurchaseClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ControllerTestBase {
    protected RestTemplate template = new RestTemplate();

    @Autowired
    private DebugDao debugDao;

    @Before()
    public void clearDatabase() throws ServerException {
        debugDao.clearDatabase();
    }

    @Test
    public void test() throws ServerException {
        debugDao.clearDatabase();
    }

    protected ResponseEntity<AdminDtoResponse> registerAdmin(String login) {
        RegisterAdminDtoRequest request = new RegisterAdminDtoRequest();
        request.setFirstName("Иван");
        request.setLastName("Иванов");
        request.setPatronymic("Иванович");
        request.setPosition("Administrator");
        request.setLogin(login);
        request.setPassword("Qwe1.qWe2.qwE3");
        ResponseEntity<AdminDtoResponse> responseEntity =  template.postForEntity("http://localhost:8080/api/admins", request, AdminDtoResponse.class);
        assertEquals(responseEntity.getBody().getFirstName(), request.getFirstName());
        assertEquals(responseEntity.getBody().getLastName(), request.getLastName());
        assertEquals(responseEntity.getBody().getPatronymic(), request.getPatronymic());
        assertEquals(responseEntity.getBody().getPosition(), request.getPosition());
        return responseEntity;
    }

    protected ResponseEntity<ClientDtoResponse> registerClient(String email, String phone, String login) {
        RegisterClientDtoRequest request = new RegisterClientDtoRequest();
        request.setFirstName("Петр");
        request.setLastName("Петров");
        request.setPatronymic("Петрович");
        request.setEmail(email);
        request.setAddress("Red Street 123");
        request.setPhone(phone);
        request.setLogin(login);
        request.setPassword("Wqe1/wQe2/wqE3");
        ResponseEntity<ClientDtoResponse> responseEntity =  template.postForEntity("http://localhost:8080/api/clients", request, ClientDtoResponse.class);
        assertEquals(responseEntity.getBody().getFirstName(), request.getFirstName());
        assertEquals(responseEntity.getBody().getLastName(), request.getLastName());
        assertEquals(responseEntity.getBody().getPatronymic(), request.getPatronymic());
        return responseEntity;
    }

    protected ResponseEntity<CategoryDtoResponse> addCategory(String name, String cookie) {
        AddCategoryAdminDtoRequest requestAddCategory = new AddCategoryAdminDtoRequest(name);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        HttpEntity<?> httpEntity = new HttpEntity<>(requestAddCategory, headers);
        ResponseEntity<CategoryDtoResponse> categoryDtoResponseEntity = template.exchange("http://localhost:8080/api/categories", HttpMethod.POST, httpEntity, CategoryDtoResponse.class);
        assertEquals(requestAddCategory.getName(), categoryDtoResponseEntity.getBody().getName());
        return  categoryDtoResponseEntity;
    }

    protected ResponseEntity<ProductDtoResponse> addProduct(String name, int price, Integer count, List<Integer> categories, String cookie) {
        AddProductAdminDtoRequest requestAddProduct = new AddProductAdminDtoRequest(name, price, count, categories);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        HttpEntity<?> httpEntity = new HttpEntity<>(requestAddProduct, headers);
        ResponseEntity<ProductDtoResponse> productDtoResponseEntity = template.exchange("http://localhost:8080/api/products", HttpMethod.POST, httpEntity, ProductDtoResponse.class);
        assertEquals(requestAddProduct.getName(), productDtoResponseEntity.getBody().getName());
        return productDtoResponseEntity;
    }

    protected ResponseEntity<PurchaseClientDtoResponse> buyProduct(int id, String name, int price, Integer count, String cookie) {
        ProductClientDtoRequest requestBuyProduct = new ProductClientDtoRequest(id, name, price, count);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        HttpEntity<?> httpEntity = new HttpEntity<>(requestBuyProduct, headers);
        ResponseEntity<PurchaseClientDtoResponse> purchaseDtoResponseEntity = template.exchange("http://localhost:8080/api/purchases", HttpMethod.POST, httpEntity, PurchaseClientDtoResponse.class);
        assertEquals(requestBuyProduct.getName(), purchaseDtoResponseEntity.getBody().getName());
        return purchaseDtoResponseEntity;
    }

    protected ResponseEntity<ClientDtoResponse> addMoney(int amount, String cookie) {
        DepositMoneyClientDtoRequest request = new DepositMoneyClientDtoRequest(amount);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        HttpEntity<?> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ClientDtoResponse> responseEntity =  template.exchange("http://localhost:8080/api/deposits", HttpMethod.PUT, httpEntity, ClientDtoResponse.class);
        assertEquals(responseEntity.getBody().getDeposit(), request.getDeposit());
        return responseEntity;
    }
}

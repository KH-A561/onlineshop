package net.thumbtack.school.onlineshop.model.integration;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.AddCategoryAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.AddProductAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.client.ProductClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.AdminDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.CategoryDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.ProductDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.ClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.PurchaseClientDtoResponse;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BasketControllerTest extends ControllerTestBase {
    @Test
    public void addProductToBasket() {
        ResponseEntity<AdminDtoResponse> adminDtoResponseResponseEntity = registerAdmin("asdfqwqwe12");

        AddCategoryAdminDtoRequest requestAddCategory = new AddCategoryAdminDtoRequest("Category 1");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        HttpEntity<?> httpEntity = new HttpEntity<>(requestAddCategory, headers);
        ResponseEntity<CategoryDtoResponse> categoryDtoResponseResponseEntity = template.exchange("http://localhost:8080/api/categories", HttpMethod.POST, httpEntity, CategoryDtoResponse.class);
        assertEquals(requestAddCategory.getName(), categoryDtoResponseResponseEntity.getBody().getName());

        AddProductAdminDtoRequest requestAddProduct = new AddProductAdminDtoRequest("Product 1", 100, 10, Collections.singletonList(categoryDtoResponseResponseEntity.getBody().getId()));
        httpEntity = new HttpEntity<>(requestAddProduct, headers);
        ResponseEntity<ProductDtoResponse> productDtoResponseResponseEntity = template.exchange("http://localhost:8080/api/products", HttpMethod.POST, httpEntity, ProductDtoResponse.class);
        assertEquals(requestAddProduct.getName(), productDtoResponseResponseEntity.getBody().getName());


        ResponseEntity<ClientDtoResponse> responseEntity1 = registerClient("asd@asd.com", "+79991223456", "asdasdasdqwee2");
        ProductClientDtoRequest requestProduct = new ProductClientDtoRequest(productDtoResponseResponseEntity.getBody().getId(), "Product 1", 100, null);
        headers = new HttpHeaders();
        headers.add("Cookie", responseEntity1.getHeaders().getFirst("Set-Cookie"));
        httpEntity = new HttpEntity<>(requestProduct, headers);
        ResponseEntity<List> responseEntity = template.exchange("http://localhost:8080/api/baskets", HttpMethod.POST, httpEntity, List.class);
        assertNotNull(responseEntity.getBody().get(0));

        requestProduct.setCount(15);
        headers = new HttpHeaders();
        headers.add("Cookie", responseEntity1.getHeaders().getFirst("Set-Cookie"));
        httpEntity = new HttpEntity<>(requestProduct, headers);
        ResponseEntity<List> responseEntityEditProduct = template.exchange("http://localhost:8080/api/baskets", HttpMethod.PUT, httpEntity, List.class);
    }
}

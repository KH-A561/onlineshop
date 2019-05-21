package net.thumbtack.school.onlineshop.model.integration;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.AdminDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.CategoryDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.ProductDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.ClientDtoResponse;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class ProductsControllerTest extends ControllerTestBase {
    @Test
    public void getByCategories() {
        ResponseEntity<AdminDtoResponse> adminDtoResponseResponseEntity = registerAdmin("asdasdasdqwe2");
        ResponseEntity<ClientDtoResponse> clientDtoResponseResponseEntity = registerClient("sss@sss.sss", "+79123648511", "qweqwesss2");
        ResponseEntity<CategoryDtoResponse> categoryDtoResponseResponseEntity1 = addCategory("Category 1", adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        ResponseEntity<CategoryDtoResponse> categoryDtoResponseResponseEntity2 = addCategory("Category 2", adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        ResponseEntity<CategoryDtoResponse> categoryDtoResponseResponseEntity3 = addCategory("Category 3", adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));

        ResponseEntity<ProductDtoResponse> productDtoResponseResponseEntity1 = addProduct("Product 1", 100, 10, Arrays.asList(categoryDtoResponseResponseEntity1.getBody().getId(), categoryDtoResponseResponseEntity2.getBody().getId(), categoryDtoResponseResponseEntity3.getBody().getId()), adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        ResponseEntity<ProductDtoResponse> productDtoResponseResponseEntity2 = addProduct("Product 2", 100, 10, null, adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        ResponseEntity<ProductDtoResponse> productDtoResponseResponseEntity3 = addProduct("Product 3", 100, 10, Arrays.asList(categoryDtoResponseResponseEntity1.getBody().getId(), categoryDtoResponseResponseEntity2.getBody().getId()), adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/products")
                .queryParam("category", categoryDtoResponseResponseEntity1.getBody().getId())
                .queryParam("category", categoryDtoResponseResponseEntity2.getBody().getId())
                .queryParam("category", categoryDtoResponseResponseEntity3.getBody().getId())
                .queryParam("order", "category");

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<List> categoryDtoResponseResponseEntity = template.exchange(builder.toUriString(), HttpMethod.GET, entity, List.class);
        assertTrue(categoryDtoResponseResponseEntity.getBody().size() == 5);
    }

    @Test
    public void getByCategoriesEmptyParam() {
        ResponseEntity<AdminDtoResponse> adminDtoResponseResponseEntity = registerAdmin("asdasdasdqwe2");
        ResponseEntity<ClientDtoResponse> clientDtoResponseResponseEntity = registerClient("sss@sss.sss", "+79123648511", "qweqwesss2");
        ResponseEntity<CategoryDtoResponse> categoryDtoResponseResponseEntity1 = addCategory("Category 1", adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        ResponseEntity<CategoryDtoResponse> categoryDtoResponseResponseEntity2 = addCategory("Category 2", adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        ResponseEntity<CategoryDtoResponse> categoryDtoResponseResponseEntity3 = addCategory("Category 3", adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));

        ResponseEntity<ProductDtoResponse> productDtoResponseResponseEntity1 = addProduct("Product 1", 100, 10, Arrays.asList(categoryDtoResponseResponseEntity1.getBody().getId(), categoryDtoResponseResponseEntity2.getBody().getId(), categoryDtoResponseResponseEntity3.getBody().getId()), adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        ResponseEntity<ProductDtoResponse> productDtoResponseResponseEntity2 = addProduct("Product 2", 100, 10, null, adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        ResponseEntity<ProductDtoResponse> productDtoResponseResponseEntity3 = addProduct("Product 3", 100, 10, Arrays.asList(categoryDtoResponseResponseEntity1.getBody().getId(), categoryDtoResponseResponseEntity2.getBody().getId()), adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/products")
                .queryParam("category");

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<List> categoryDtoResponseResponseEntity = template.exchange(builder.toUriString(), HttpMethod.GET, entity, List.class);
        assertTrue(categoryDtoResponseResponseEntity.getBody().size() == 1);
    }
}

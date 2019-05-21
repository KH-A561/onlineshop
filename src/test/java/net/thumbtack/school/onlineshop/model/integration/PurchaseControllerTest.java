package net.thumbtack.school.onlineshop.model.integration;

import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.AdminDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.ProductDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.ClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.PurchaseClientDtoResponse;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class PurchaseControllerTest extends ControllerTestBase {
    @Test
    public void getPurchases() {
        ResponseEntity<AdminDtoResponse> adminDtoResponseResponseEntity = registerAdmin("asdasdasdqwe2");
        ResponseEntity<ClientDtoResponse> clientDtoResponseResponseEntity = registerClient("sss@sss.sss", "+79123648511", "qweqwesss2");

        ResponseEntity<ProductDtoResponse> productDtoResponseResponseEntity1 = addProduct("Product 1", 100, 10, null, adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        ResponseEntity<ProductDtoResponse> productDtoResponseResponseEntity2 = addProduct("Product 2", 100, 10, null, adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        ResponseEntity<ProductDtoResponse> productDtoResponseResponseEntity3 = addProduct("Product 3", 100, 10, null, adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));

        addMoney(1000000, clientDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));

        ResponseEntity<PurchaseClientDtoResponse> purchaseClientDtoResponseResponseEntity1 = buyProduct(productDtoResponseResponseEntity1.getBody().getId(), productDtoResponseResponseEntity1.getBody().getName(), productDtoResponseResponseEntity1.getBody().getPrice(), productDtoResponseResponseEntity1.getBody().getCount(), clientDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        ResponseEntity<PurchaseClientDtoResponse> purchaseClientDtoResponseResponseEntity2 = buyProduct(productDtoResponseResponseEntity2.getBody().getId(), productDtoResponseResponseEntity2.getBody().getName(), productDtoResponseResponseEntity2.getBody().getPrice(), productDtoResponseResponseEntity2.getBody().getCount(), clientDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        ResponseEntity<PurchaseClientDtoResponse> purchaseClientDtoResponseResponseEntity3 = buyProduct(productDtoResponseResponseEntity3.getBody().getId(), productDtoResponseResponseEntity3.getBody().getName(), productDtoResponseResponseEntity3.getBody().getPrice(), productDtoResponseResponseEntity3.getBody().getCount(), clientDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/purchases")
                .queryParam("product", productDtoResponseResponseEntity1.getBody().getId())
                .queryParam("product", productDtoResponseResponseEntity2.getBody().getId())
                .queryParam("order", "profit");
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> categoryDtoResponseResponseEntity = template.exchange(builder.toUriString(), HttpMethod.GET, entity, new ParameterizedTypeReference<List<Product>>(){});
    }
}

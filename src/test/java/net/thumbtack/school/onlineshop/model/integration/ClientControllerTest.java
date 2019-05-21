package net.thumbtack.school.onlineshop.model.integration;

import net.thumbtack.school.onlineshop.mybatis.dao.DebugDao;
import net.thumbtack.school.onlineshop.spring.dto.request.client.EditAccountClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.client.RegisterClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.AdminDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.ClientDtoResponse;
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
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServlet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClientControllerTest extends ControllerTestBase {
    @Test
    public void getClientsAsAdmin() {
        ResponseEntity<ClientDtoResponse> responseEntity1 = registerClient("asd@asd.com", "+79991223456", "asdasdasdqwee2");
        ResponseEntity<ClientDtoResponse> responseEntity2 = registerClient("asf@asd.com", "+79991223457", "asdasdasdqwee3");
        ResponseEntity<ClientDtoResponse> responseEntity3 = registerClient("asg@asd.com", "+79991223458", "asdasdasdqwee4");
        ResponseEntity<AdminDtoResponse> adminDtoResponseResponseEntity = registerAdmin("asdfqwqwe12");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", adminDtoResponseResponseEntity.getHeaders().getFirst("Set-Cookie"));
        ResponseEntity<List> response = template.exchange("http://localhost:8080/api/clients", HttpMethod.GET, new HttpEntity<>(headers), List.class);
        assertNotNull(response.getBody().get(0));
        assertNotNull(response.getBody().get(1));
        assertNotNull(response.getBody().get(2));
    }

    @Test
    public void editAccount() {
        ResponseEntity<ClientDtoResponse> responseEntity1 = registerClient("asd@asd.com", "+79991223456", "asdasdasdqwee2");
        EditAccountClientDtoRequest request = new EditAccountClientDtoRequest("Иван", "Иванов", null, "jjjjj@jjjh.ss", "Red Street 123", "+79991223458", "Wqe1/wQe2/wqE3", "dasdasdqweeeWWWWW123");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", responseEntity1.getHeaders().getFirst("Set-Cookie"));
        HttpEntity<?> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ClientDtoResponse> responseEntity = template.exchange("http://localhost:8080/api/clients", HttpMethod.PUT, httpEntity, ClientDtoResponse.class);

        assertEquals(request.getFirstName(), responseEntity.getBody().getFirstName());
        assertEquals(request.getLastName(), responseEntity.getBody().getLastName());
        assertEquals(request.getAddress(), responseEntity.getBody().getAddress());
        assertEquals(request.getEmail(), responseEntity.getBody().getEmail());
        assertEquals(request.getPhone(), responseEntity.getBody().getPhone());
        assertEquals(request.getAddress(), responseEntity.getBody().getAddress());
        assertEquals(responseEntity.getBody().getDeposit(), 0);
    }
}

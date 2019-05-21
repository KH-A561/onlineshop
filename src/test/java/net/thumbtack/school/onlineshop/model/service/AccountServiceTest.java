package net.thumbtack.school.onlineshop.model.service;

import net.thumbtack.school.onlineshop.model.Account;
import net.thumbtack.school.onlineshop.model.Administrator;
import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Session;
import net.thumbtack.school.onlineshop.mybatis.dao.AdministratorDao;
import net.thumbtack.school.onlineshop.mybatis.dao.ClientDao;
import net.thumbtack.school.onlineshop.mybatis.dao.SessionDao;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.AdminDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.ClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountServiceTest extends ServiceTestBase {
    @Test
    public void testGetAdministrator() throws ServerException {
        Administrator administrator = new Administrator("NameA",
                                            "SurnameA",
                                            null,
                                            "Аа123321",
                                            "123456789а",
                                            "Position");

        SessionDao sessionDaoMock = mock(SessionDao.class);
        when(sessionDaoMock.getByCookie(anyString())).thenReturn(new Session("itscookie", administrator));

        AdministratorDao administratorDaoMock = mock(AdministratorDao.class);
        when(administratorDaoMock.getById(anyInt())).thenReturn(administrator);

        accountService.setSessionDao(sessionDaoMock);
        accountService.setAdministratorDao(administratorDaoMock);

        AdminDtoResponse response = new AdminDtoResponse(administrator.getId(),
                                                         administrator.getFirstName(),
                                                         administrator.getLastName(),
                                                         administrator.getPatronymic(),
                                                         administrator.getPosition());
        String cookieString = ResponseCookie.from("JAVASESSIONID", "itscookie").build().toString();
        assertEquals(ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieString).body(response), accountService.getAccount("itscookie"));
    }

    @Test
    public void testGetClient() throws ServerException {
        Client client = new Client("NameA",
                                    "SurnameA",
                                    null,
                                    "Аа123321",
                                    "123456789а",
                                    "aaa@aaa.com",
                                    "Aaaaa st.",
                                    "+79259876543");

        SessionDao sessionDaoMock = mock(SessionDao.class);
        when(sessionDaoMock.getByCookie(anyString())).thenReturn(new Session("itscookie", client));

        ClientDao clientDaoMock = mock(ClientDao.class);
        when(clientDaoMock.getById(anyInt())).thenReturn(client);

        accountService.setSessionDao(sessionDaoMock);
        accountService.setClientDao(clientDaoMock);

        ClientDtoResponse response = new ClientDtoResponse(client.getId(),
                                                           client.getFirstName(),
                                                           client.getLastName(),
                                                           client.getPatronymic(),
                                                           client.getEmail(),
                                                           client.getAddress(),
                                                           client.getPhone(),
                                                           client.getDeposit().getAmount());
        String cookieString = ResponseCookie.from("JAVASESSIONID", "itscookie").build().toString();
        assertEquals(ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieString).body(response), accountService.getAccount("itscookie"));
    }
}

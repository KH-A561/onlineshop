package net.thumbtack.school.onlineshop.spring.controller;

import com.fasterxml.jackson.annotation.JsonView;
import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.spring.dto.request.client.EditAccountClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.client.RegisterClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.client.ClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.service.AdminService;
import net.thumbtack.school.onlineshop.spring.service.ClientService;
import net.thumbtack.school.onlineshop.spring.view.AsAdmin;
import net.thumbtack.school.onlineshop.spring.view.AsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;
    private final AdminService adminService;

    @Autowired
    public ClientController(ClientService clientService, AdminService adminService) {
        this.clientService = clientService;
        this.adminService = adminService;
    }

    @Transactional(rollbackFor = ServerException.class)
    @JsonView(AsClient.class)
    @RequestMapping(
            method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ClientDtoResponse register(@Valid @RequestBody RegisterClientDtoRequest request, HttpServletResponse response) throws ServerException {
        ClientDtoResponse registerResponse = clientService.register(request);
        response.addCookie(new Cookie("JAVASESSIONID", clientService.login(request.getLogin(), request.getPassword())));
        return registerResponse;
    }

    @JsonView(AsAdmin.class)
    @RequestMapping(
            method = GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ClientDtoResponse> getClientsAsAdmin(@CookieValue(name = "JAVASESSIONID") String cookie) throws ServerException {
        return adminService.getClients(cookie);
    }

    @JsonView(AsClient.class)
    @RequestMapping(
            method = PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ClientDtoResponse editAccount(@CookieValue(name = "JAVASESSIONID") String cookie,
                                         @Valid @RequestBody EditAccountClientDtoRequest request,
                                         HttpServletResponse response) throws ServerException {
        ClientDtoResponse editResponse = clientService.editAccount(cookie, request, request.getNewPassword());
        response.addCookie(new Cookie("JAVASESSIONID", cookie));
        return editResponse;
    }
}

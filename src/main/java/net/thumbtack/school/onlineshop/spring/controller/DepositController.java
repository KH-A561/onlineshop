package net.thumbtack.school.onlineshop.spring.controller;

import net.thumbtack.school.onlineshop.spring.dto.request.client.DepositMoneyClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.client.ClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import net.thumbtack.school.onlineshop.spring.service.ClientService;
import net.thumbtack.school.onlineshop.spring.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {
    private final DepositService depositService;
    private final ClientService clientService;

    @Autowired
    public DepositController(DepositService depositService, ClientService clientService) {
        this.depositService = depositService;
        this.clientService = clientService;
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ClientDtoResponse putMoney(@CookieValue(name = "JAVASESSIONID") String cookie,
                                      @RequestBody @Valid DepositMoneyClientDtoRequest request) throws ServerException {
        return depositService.putMoney(request, cookie);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ClientDtoResponse get(@CookieValue(name = "JAVASESSIONID") String cookie) throws ServerException {
        return clientService.getByCookie(cookie);
    }
}

package net.thumbtack.school.onlineshop.spring.controller;

import net.thumbtack.school.onlineshop.spring.dto.request.client.ProductClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.client.PurchaseClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.service.BasketService;
import net.thumbtack.school.onlineshop.spring.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/api/baskets")
public class BasketController {
    private final BasketService basketService;
    private final ClientService clientService;

    @Autowired
    public BasketController(BasketService basketService, ClientService clientService) {
        this.basketService = basketService;
        this.clientService = clientService;
    }

    @RequestMapping(
            method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PurchaseClientDtoResponse> addProductToBasket(@CookieValue(name = "JAVASESSIONID") String cookie,
                                                              @RequestBody @Valid ProductClientDtoRequest request) throws ServerException {
        return basketService.addProduct(request, cookie);
    }

    @RequestMapping(
            method = DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/{id}"
    )
    public ResponseEntity<?> deleteProductFromBasketById(@CookieValue(name = "JAVASESSIONID") String cookie,
                                                         @PathVariable("id") int id) throws ServerException {
        basketService.deleteProductFromBasketById(id, cookie);
        return ResponseEntity.ok().body(Json.createObjectBuilder().build());
    }

    @RequestMapping(
            method = PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PurchaseClientDtoResponse> editProductCountInBasket(@CookieValue(name = "JAVASESSIONID") String cookie,
                                                                    @RequestBody @Valid ProductClientDtoRequest request)
                                                                    throws ServerException {
        return basketService.editProductCountInBasket(request, cookie);
    }

    @RequestMapping(
            method = GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PurchaseClientDtoResponse> getBasket(@CookieValue(name = "JAVASESSIONID") String cookie) throws ServerException {
        return basketService.getBasket(cookie);
    }
}

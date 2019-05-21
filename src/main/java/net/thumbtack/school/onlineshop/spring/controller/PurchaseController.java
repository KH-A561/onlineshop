package net.thumbtack.school.onlineshop.spring.controller;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.spring.dto.request.client.ProductClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.client.BasketPurchaseClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.PurchaseClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.service.ClientService;
import net.thumbtack.school.onlineshop.spring.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService, ClientService clientService) {
        this.purchaseService = purchaseService;
    }

    @RequestMapping(
            method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private PurchaseClientDtoResponse buyProduct(@CookieValue(name = "JAVASESSIONID") String cookie,
                                                 @RequestBody @Valid ProductClientDtoRequest request)
                                                 throws ServerException {
        return purchaseService.buyProduct(request, cookie);
    }

    @RequestMapping(
            path = "/baskets",
            method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private BasketPurchaseClientDtoResponse buyProductsInBasket(@CookieValue(name = "JAVASESSIONID") String cookie,
                                                                @RequestBody @NotBlank List<ProductClientDtoRequest> request)
                                                                throws ServerException {
        return purchaseService.buyProductsInBasket(request, cookie);
    }

    @RequestMapping(
            method = GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private ResponseEntity<?> getPurchases(@CookieValue(name = "JAVASESSIONID") String cookie,
                                           @RequestParam(name = "category", required = false) List<Integer> categoryIds,
                                           @RequestParam(name = "product", required = false) List<Integer> productIds,
                                           @RequestParam(name = "client", required = false) List<Integer> clientIds,
                                           @RequestParam(name = "order", required = false) String order,
                                           @RequestParam(name = "offset", required = false) Integer offset,
                                           @RequestParam(name = "limit", required = false) Integer limit) throws ServerException {
        return purchaseService.getPurchases(categoryIds, productIds, clientIds, order, offset, limit, cookie);
    }
}

package net.thumbtack.school.onlineshop.spring.controller;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.AddProductAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.EditProductAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.ProductDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import net.thumbtack.school.onlineshop.spring.service.AccountService;
import net.thumbtack.school.onlineshop.spring.service.AdminService;
import net.thumbtack.school.onlineshop.spring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;
import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final AdminService adminService;
    private final AccountService accountService;

    @Autowired
    public ProductController(ProductService productService, AdminService adminService, AccountService accountService) {
        this.productService = productService;
        this.adminService = adminService;
        this.accountService = accountService;
    }

    @RequestMapping(
            method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ProductDtoResponse insert(@CookieValue(name = "JAVASESSIONID") String cookie,
                                     @RequestBody @Valid AddProductAdminDtoRequest request) throws ServerException {
        return productService.insert(request, cookie);
    }

    @RequestMapping(
            method = PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/{id}"
    )
    public ProductDtoResponse update(@CookieValue(name = "JAVASESSIONID") String cookie,
                                     @RequestBody @Valid EditProductAdminDtoRequest request,
                                     @PathVariable("id") int id) throws ServerException {
        return productService.update(request, id, cookie);
    }

    @RequestMapping(
            method = DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/{id}"
    )
    public ResponseEntity<?> delete(@CookieValue(name = "JAVASESSIONID") String cookie,
                                    @PathVariable("id") int id) throws ServerException {
        productService.deleteById(id, cookie);
        return ResponseEntity.ok().body(Json.createObjectBuilder().build());
    }

    @RequestMapping(
            method = GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/{id}"
    )
    public ProductDtoResponse get(@CookieValue(name = "JAVASESSIONID") String cookie,
                                  @PathVariable("id") int id) throws ServerException {
        return productService.getById(id, cookie);
    }

    @RequestMapping(
            method = GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ProductDtoResponse> getByCategories(@CookieValue(name = "JAVASESSIONID") String cookie,
                                                    @RequestParam(name = "category", required = false) List<Category> categories,
                                                    @RequestParam(name = "order", required = false) String order) throws ServerException {
        return productService.getByCategories(categories, order, cookie);
    }
}

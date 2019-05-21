package net.thumbtack.school.onlineshop.spring.controller;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.AddCategoryAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.EditCategoryAdminRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.CategoryDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import net.thumbtack.school.onlineshop.spring.service.AdminService;
import net.thumbtack.school.onlineshop.spring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final AdminService adminService;

    @Autowired
    public CategoryController(CategoryService categoryService, AdminService adminService) {
        this.categoryService = categoryService;
        this.adminService = adminService;
    }

    @RequestMapping(
            method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CategoryDtoResponse insert(@RequestBody @Valid AddCategoryAdminDtoRequest request,
                                      @CookieValue(name = "JAVASESSIONID") String cookie) throws ServerException {
        return categoryService.insert(request, cookie);
    }

    @RequestMapping(
            method = GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    public CategoryDtoResponse getById(@CookieValue(name = "JAVASESSIONID") String cookie,
                                       @PathVariable("id") int id) throws ServerException {
        return categoryService.getById(id, cookie);
    }

    @RequestMapping(
            method = PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    public CategoryDtoResponse update(@CookieValue(name = "JAVASESSIONID") String cookie,
                                      @PathVariable("id") int id,
                                      @RequestBody @Valid EditCategoryAdminRequest request) throws ServerException {
        if ((request.getName().isEmpty() || request.getName() == null) &&
                (request.getParentId() == null || request.getParentId() == 0)) {
            ErrorCode.INVALID_REQUEST_ARGUMENT.setField("name / parent_id");
            ErrorCode.INVALID_REQUEST_ARGUMENT.setErrorDescription("Both arguments cannot be empty in this request");
            throw new ServerException(ErrorCode.INVALID_REQUEST_ARGUMENT);
        }
        return categoryService.update(id, request, cookie);
    }

    @RequestMapping(
            method = DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    public ResponseEntity<?> delete(@CookieValue(name = "JAVASESSIONID") String cookie,
                                    @PathVariable("id") int id) throws ServerException {
        categoryService.deleteById(id, cookie);
        return ResponseEntity.ok().body(Json.createObjectBuilder().build());
    }

    @RequestMapping(
            method = GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<CategoryDtoResponse> getAll(@CookieValue(name = "JAVASESSIONID") String cookie) throws ServerException {
        return categoryService.getAll(cookie);
    }
}

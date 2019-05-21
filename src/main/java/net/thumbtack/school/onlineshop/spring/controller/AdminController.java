package net.thumbtack.school.onlineshop.spring.controller;

import net.thumbtack.school.onlineshop.model.Administrator;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.EditAccountAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.RegisterAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.AdminDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.service.AdminService;
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

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/api/admins")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Transactional(rollbackFor = ServerException.class)
    @RequestMapping(
            method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AdminDtoResponse register(@Valid @RequestBody RegisterAdminDtoRequest request, HttpServletResponse response) throws ServerException {
        AdminDtoResponse registerResponse = adminService.register(request);
        response.addCookie(new Cookie("JAVASESSIONID", adminService.login(request.getLogin(), request.getPassword())));
        return registerResponse;
    }

    @RequestMapping(
            method = PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AdminDtoResponse editAccount(@CookieValue(name = "JAVASESSIONID") String cookie,
                                        @Valid @RequestBody EditAccountAdminDtoRequest request,
                                        HttpServletResponse response) throws ServerException {
        AdminDtoResponse editResponse = adminService.editAccount(cookie, request, request.getNewPassword());
        response.addCookie(new Cookie("JAVASESSIONID", cookie));
        return editResponse;
    }
}

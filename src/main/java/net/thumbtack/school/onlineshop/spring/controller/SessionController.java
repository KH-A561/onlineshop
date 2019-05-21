package net.thumbtack.school.onlineshop.spring.controller;

import net.thumbtack.school.onlineshop.spring.dto.request.LogInDtoRequest;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.json.Json;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @RequestMapping(
            method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> logIn(@Valid @RequestBody LogInDtoRequest request) throws ServerException {
        return sessionService.logIn(request.getLogin(), request.getPassword());
    }

    @RequestMapping(
            method = DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> logOut(@CookieValue(name = "JAVASESSIONID") String cookie) throws ServerException {
        sessionService.logOut(cookie);
        return ResponseEntity.ok().body(Json.createObjectBuilder().build());
    }
}

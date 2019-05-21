package net.thumbtack.school.onlineshop.spring.controller;

import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.service.DebugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/debug")
public class DebugController {
    private final DebugService debugService;

    @Autowired
    public DebugController(DebugService debugService) {
        this.debugService = debugService;
    }

    @RequestMapping(
            method = POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> clearDatabase() throws ServerException {
        debugService.clearDatabase();
        return ResponseEntity.ok().body(Json.createObjectBuilder().build());
    }
}

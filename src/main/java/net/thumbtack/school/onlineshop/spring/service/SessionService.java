package net.thumbtack.school.onlineshop.spring.service;

import net.thumbtack.school.onlineshop.model.Account;
import net.thumbtack.school.onlineshop.model.Administrator;
import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Session;
import net.thumbtack.school.onlineshop.mybatis.dao.AccountDao;
import net.thumbtack.school.onlineshop.mybatis.dao.AdministratorDao;
import net.thumbtack.school.onlineshop.mybatis.dao.ClientDao;
import net.thumbtack.school.onlineshop.mybatis.dao.SessionDao;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.AdminDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.ClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SessionService {
    private final SessionDao sessionDao;
    private final AccountDao accountDao;
    private final AdministratorDao administratorDao;
    private final ClientDao clientDao;

    @Autowired
    public SessionService(SessionDao sessionDao, AccountDao accountDao, AdministratorDao administratorDao, ClientDao clientDao) {
        this.sessionDao = sessionDao;
        this.accountDao = accountDao;
        this.administratorDao = administratorDao;
        this.clientDao = clientDao;
    }

    @Transactional(rollbackFor = ServerException.class)
    public ResponseEntity<?> logIn(String login, String password) throws ServerException {
        Account account = accountDao.getByLogin(login);
        if (!account.getPassword().equals(password)) {
            throw new ServerException(ErrorCode.ACCOUNT_WRONG_PASSWORD);
        }
        String cookie = UUID.randomUUID().toString();
        ResponseEntity<?> responseEntity;
        switch (account.getType()) {
            case CLIENT: {
                Client client = clientDao.getById(account.getId());
                ClientDtoResponse response = new ClientDtoResponse(client.getId(),
                                                                    client.getFirstName(),
                                                                    client.getLastName(),
                                                                    client.getPatronymic(),
                                                                    client.getEmail(),
                                                                    client.getAddress(),
                                                                    client.getPhone(),
                                                                    client.getDeposit().getAmount());
                String cookieString = ResponseCookie.from("JAVASESSIONID", cookie).build().toString();
                responseEntity = ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieString).body(response);
                break;
            }
            case ADMINISTRATOR: {
                Administrator administrator = administratorDao.getById(account.getId());
                AdminDtoResponse response = new AdminDtoResponse(administrator.getId(),
                                                                 administrator.getFirstName(),
                                                                 administrator.getLastName(),
                                                                 administrator.getPatronymic(),
                                                                 administrator.getPosition());
                String cookieString = ResponseCookie.from("JAVASESSIONID", cookie).build().toString();
                responseEntity = ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieString).body(response);
                break;
            }
            default: {
                throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
            }
        }
        sessionDao.insert(new Session(cookie, account));
        return responseEntity;
    }

    @Transactional(rollbackFor = ServerException.class)
    public void logOut(String cookie) throws ServerException {
        sessionDao.delete(new Session(cookie, null));
    }
}

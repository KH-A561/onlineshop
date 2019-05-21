package net.thumbtack.school.onlineshop.spring.service;

import net.thumbtack.school.onlineshop.model.Account;
import net.thumbtack.school.onlineshop.model.Administrator;
import net.thumbtack.school.onlineshop.model.Client;
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

@Service
public class AccountService {
    private ClientDao clientDao;
    private AdministratorDao administratorDao;
    private SessionDao sessionDao;

    @Autowired
    public AccountService(AccountDao accountDao, ClientDao clientDao, AdministratorDao administratorDao, SessionDao sessionDao) {
        this.clientDao = clientDao;
        this.administratorDao = administratorDao;
        this.sessionDao = sessionDao;
    }

    @Transactional(rollbackFor = ServerException.class)
    public ResponseEntity<?> getAccount(String cookie) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
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
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieString).body(response);
            }
            case ADMINISTRATOR: {
                Administrator administrator = administratorDao.getById(account.getId());
                AdminDtoResponse response = new AdminDtoResponse(administrator.getId(),
                        administrator.getFirstName(),
                        administrator.getLastName(),
                        administrator.getPatronymic(),
                        administrator.getPosition());
                String cookieString = ResponseCookie.from("JAVASESSIONID", cookie).build().toString();
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieString).body(response);
            }
            default: {
                throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
            }
        }
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void setAdministratorDao(AdministratorDao administratorDao) {
        this.administratorDao = administratorDao;
    }

    public void setSessionDao(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }
}

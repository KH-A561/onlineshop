package net.thumbtack.school.onlineshop.spring.service;

import net.thumbtack.school.onlineshop.model.Account;
import net.thumbtack.school.onlineshop.model.AccountType;
import net.thumbtack.school.onlineshop.model.Administrator;
import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Session;
import net.thumbtack.school.onlineshop.mybatis.dao.AccountDao;
import net.thumbtack.school.onlineshop.mybatis.dao.AdministratorDao;
import net.thumbtack.school.onlineshop.mybatis.dao.ClientDao;
import net.thumbtack.school.onlineshop.mybatis.dao.SessionDao;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.EditAccountAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.RegisterAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.AdminDtoResponse;
import net.thumbtack.school.onlineshop.spring.dto.response.client.ClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private AdministratorDao administratorDao;
    private ClientDao clientDao;
    private AccountDao accountDao;
    private SessionDao sessionDao;

    @Autowired
    public AdminService(AdministratorDao administratorDao, ClientDao clientDao, AccountDao accountDaoImpl, SessionDao sessionDao) {
        this.administratorDao = administratorDao;
        this.clientDao = clientDao;
        this.accountDao = accountDaoImpl;
        this.sessionDao = sessionDao;
    }

    @Transactional(rollbackFor = ServerException.class)
    public AdminDtoResponse register(RegisterAdminDtoRequest request) throws ServerException {
        Administrator administrator = new Administrator(request.getFirstName(),
                                                        request.getLastName(),
                                                        request.getPatronymic(),
                                                        request.getLogin(),
                                                        request.getPassword(),
                                                        request.getPosition());
        accountDao.insert(administrator);
        administratorDao.insert(administrator);
        return new AdminDtoResponse(administrator.getId(),
                                    administrator.getFirstName(),
                                    administrator.getLastName(),
                                    administrator.getPatronymic(),
                                    administrator.getPosition());
    }

    @Transactional(rollbackFor = ServerException.class)
    public String login(String login, String password) throws ServerException {
        Account account = accountDao.getByLogin(login);
        if (!account.getPassword().equals(password)) {
            throw new ServerException(ErrorCode.ACCOUNT_WRONG_PASSWORD);
        }
        if (!account.getType().equals(AccountType.ADMINISTRATOR)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        UUID cookie = UUID.randomUUID();
        sessionDao.insert(new Session(cookie.toString(), account));
        return cookie.toString();
    }

    @Transactional(rollbackFor = ServerException.class)
    public List<ClientDtoResponse> getClients(String cookie) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
        if (!account.getType().equals(AccountType.ADMINISTRATOR)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        List<Client> clients = clientDao.getClients();
        return  clients.stream().map(c -> new ClientDtoResponse(c.getId(),
                                                                c.getFirstName(),
                                                                c.getLastName(),
                                                                c.getPatronymic(),
                                                                c.getEmail(),
                                                                c.getAddress(),
                                                                c.getPhone(),
                                                                0)).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = ServerException.class)
    public AdminDtoResponse editAccount(String cookie, EditAccountAdminDtoRequest request, String newPassword) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
        if (!account.getType().equals(AccountType.ADMINISTRATOR)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        if (!request.getOldPassword().equals(account.getPassword())) {
            throw new ServerException(ErrorCode.ACCOUNT_WRONG_PASSWORD);
        }
        Administrator administrator = new Administrator(account.getId(),
                request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                account.getLogin(),
                newPassword,
                request.getPosition());
        accountDao.update(administrator);
        administratorDao.update(administrator);
        return new AdminDtoResponse(administrator.getId(),
                                    administrator.getFirstName(),
                                    administrator.getLastName(),
                                    administrator.getPatronymic(),
                                    administrator.getPosition());
    }

    public void setAdministratorDao(AdministratorDao administratorDao) {
        this.administratorDao = administratorDao;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setSessionDao(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }
}

package net.thumbtack.school.onlineshop.spring.service;

import net.thumbtack.school.onlineshop.model.Account;
import net.thumbtack.school.onlineshop.model.AccountType;
import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Deposit;
import net.thumbtack.school.onlineshop.model.Session;
import net.thumbtack.school.onlineshop.mybatis.dao.AccountDao;
import net.thumbtack.school.onlineshop.mybatis.dao.ClientDao;
import net.thumbtack.school.onlineshop.mybatis.dao.DepositDao;
import net.thumbtack.school.onlineshop.mybatis.dao.SessionDao;
import net.thumbtack.school.onlineshop.spring.dto.request.client.EditAccountClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.client.RegisterClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.client.ClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ClientService {
    private final ClientDao clientDao;
    private final AccountDao accountDao;
    private final SessionDao sessionDao;

    @Autowired
    public ClientService(ClientDao clientDao, AccountDao accountDao, SessionDao sessionDao) {
        this.clientDao = clientDao;
        this.accountDao = accountDao;
        this.sessionDao = sessionDao;
    }

    @Transactional(rollbackFor = ServerException.class)
    public ClientDtoResponse register(RegisterClientDtoRequest request) throws ServerException {
        Client client = new Client(request.getFirstName(),
                                    request.getLastName(),
                                    request.getPatronymic(),
                                    request.getLogin(),
                                    request.getPassword(),
                                    request.getEmail(),
                                    request.getAddress(),
                                    request.getPhone());
        if (client.getPhone().contains("-")) {
            client.setPhone(client.getPhone().replaceAll("-", ""));
        }
        Client insertedClient = (Client) accountDao.insert(client);
        clientDao.insert(insertedClient);
        return new ClientDtoResponse(insertedClient.getId(),
                                    insertedClient.getFirstName(),
                                    insertedClient.getLastName(),
                                    insertedClient.getPatronymic(),
                                    insertedClient.getEmail(),
                                    insertedClient.getAddress(),
                                    insertedClient.getPhone(),
                                    insertedClient.getDeposit().getAmount());
    }

    @Transactional(rollbackFor = ServerException.class)
    public String login(String login, String password) throws ServerException {
        Account account = accountDao.getByLogin(login);
        if (!account.getPassword().equals(password)) {
            throw new ServerException(ErrorCode.ACCOUNT_WRONG_PASSWORD);
        }
        if (!account.getType().equals(AccountType.CLIENT)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        UUID cookie = UUID.randomUUID();
        sessionDao.insert(new Session(cookie.toString(), account));
        return cookie.toString();
    }

    @Transactional(rollbackFor = ServerException.class)
    public ClientDtoResponse editAccount(String cookie, EditAccountClientDtoRequest request, String newPassword) throws ServerException {
        Client client = new Client(request.getFirstName(),
                                    request.getLastName(),
                                    request.getPatronymic(),
                                    " ",
                                    request.getOldPassword(),
                                    request.getEmail(),
                                    request.getAddress(),
                                    request.getPhone());
        if (client.getPhone().contains("-")) {
            client.setPhone(client.getPhone().replaceAll("-", ""));
        }
        Account account = sessionDao.getByCookie(cookie).getAccount();
        if (!account.getType().equals(AccountType.CLIENT)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        if (!client.getPassword().equals(account.getPassword())) {
            throw new ServerException(ErrorCode.ACCOUNT_WRONG_PASSWORD);
        }
        client.setId(account.getId());
        client.setLogin(account.getLogin());
        client.setPassword(newPassword);
        accountDao.update(client);
        clientDao.update(client);
        return new ClientDtoResponse(client.getId(),
                                    client.getFirstName(),
                                    client.getLastName(),
                                    client.getPatronymic(),
                                    client.getEmail(),
                                    client.getAddress(),
                                    client.getPhone(),
                                    client.getDeposit().getAmount());
    }

    @Transactional(rollbackFor = ServerException.class)
    public ClientDtoResponse getByCookie(String cookie) throws ServerException {
        Client client = clientDao.getByCookie(cookie);
        return new ClientDtoResponse(client.getId(),
                                    client.getFirstName(),
                                    client.getLastName(),
                                    client.getPatronymic(),
                                    client.getEmail(),
                                    client.getAddress(),
                                    client.getPhone(),
                                    client.getDeposit().getAmount());
    }
}

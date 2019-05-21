package net.thumbtack.school.onlineshop.spring.service;

import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Deposit;
import net.thumbtack.school.onlineshop.mybatis.dao.ClientDao;
import net.thumbtack.school.onlineshop.mybatis.dao.DepositDao;
import net.thumbtack.school.onlineshop.mybatis.dao.SessionDao;
import net.thumbtack.school.onlineshop.spring.dto.request.client.DepositMoneyClientDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.client.ClientDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepositService {
    private final DepositDao depositDao;
    private final ClientDao clientDao;
    private final SessionDao sessionDao;

    @Autowired
    public DepositService(DepositDao depositDao, ClientDao clientDao, SessionDao sessionDao) {
        this.depositDao = depositDao;
        this.clientDao = clientDao;
        this.sessionDao = sessionDao;
    }

    @Transactional(rollbackFor = ServerException.class)
    public ClientDtoResponse putMoney(DepositMoneyClientDtoRequest request, String cookie) throws ServerException {
        Client client = clientDao.getByCookie(cookie);
        depositDao.update(client, new Deposit(request.getDeposit()));
        return new ClientDtoResponse(client.getId(),
                                     client.getFirstName(),
                                     client.getLastName(),
                                     client.getPatronymic(),
                                     client.getEmail(),
                                     client.getAddress(),
                                     client.getPhone(),
                                     request.getDeposit());
    }
}

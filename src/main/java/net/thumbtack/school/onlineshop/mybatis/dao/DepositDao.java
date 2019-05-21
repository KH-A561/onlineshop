package net.thumbtack.school.onlineshop.mybatis.dao;

import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Deposit;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;

public interface DepositDao {
    void insert(Client client, Deposit deposit) throws ServerException;

    void update(Client client, Deposit deposit) throws ServerException;
}

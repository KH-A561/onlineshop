package net.thumbtack.school.onlineshop.mybatis.dao;

import net.thumbtack.school.onlineshop.model.Account;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;

public interface AccountDao {
    Account insert(Account account) throws ServerException;

    Account getByLogin(String login) throws ServerException;

    void update(Account account) throws ServerException;

    void deleteAll() throws ServerException;
}

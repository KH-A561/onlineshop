// REVU net.thumbtack.school.onlineshop.mybatis.daoimpl; no capital letters!
package net.thumbtack.school.onlineshop.mybatis.daoImpl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.onlineshop.model.Account;
import net.thumbtack.school.onlineshop.mybatis.dao.AccountDao;
import net.thumbtack.school.onlineshop.mybatis.mapper.AccountMapper;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.mybatis.cdi.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountDaoImpl implements AccountDao {
    private final AccountMapper accountMapper;

    @Autowired
    public AccountDaoImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Transactional
    @Override
    public Account insert(Account account) throws ServerException {
        try {
            accountMapper.insert(account);
        } catch (DataAccessException e) {
            log.error("Can't insert account {}", e);
            if (e.getClass().equals(DuplicateKeyException.class)) {
                if (e.getLocalizedMessage().contains("'login_UNIQUE'")) {
                    throw new ServerException(ErrorCode.ACCOUNT_LOGIN_ALREADY_EXISTS);
                }
            }
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
        return account;
    }

    @Transactional
    @Override
    public Account getByLogin(String login) throws ServerException {
        try {
            Account account;
            if ((account = accountMapper.getByLogin(login)) == null) {
                throw new ServerException(ErrorCode.ACCOUNT_NOT_FOUND_BY_LOGIN);
            }
            return accountMapper.getByLogin(login);
        } catch (DataAccessException e) {
            log.error("Can't get account by login {}", e);
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void update(Account account) throws ServerException {
        try {
            accountMapper.update(account);
        } catch (DataAccessException e) {
            log.error("Can't update account {}", e);
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteAll() throws ServerException {
        try {
            accountMapper.deleteAll();
        } catch (DataAccessException e) {
            log.error("Can't delete all accounts {}", e);
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }
}

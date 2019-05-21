package net.thumbtack.school.onlineshop.mybatis.daoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Deposit;
import net.thumbtack.school.onlineshop.mybatis.dao.DepositDao;
import net.thumbtack.school.onlineshop.mybatis.mapper.DepositMapper;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Component
@Slf4j
public class DepositDaoImpl implements DepositDao {
    private final DepositMapper depositMapper;

    @Autowired
    public DepositDaoImpl(DepositMapper depositMapper) {
        this.depositMapper = depositMapper;
    }

    public void insert(Client client, Deposit deposit) throws ServerException {
        try {
            depositMapper.insert(client, deposit);
        } catch (DataAccessException e) {
            log.error("Can't insert Deposit {}", e);
            if (e.getCause().getClass().equals(MySQLIntegrityConstraintViolationException.class)) {
                throw new ServerException(ErrorCode.CLIENT_NOT_FOUND_BY_ACCOUNT_ID);
            } else {
                ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
                throw new ServerException(ErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void update(Client client, Deposit deposit) throws ServerException {
        try {
            if (deposit.getAmount() < 0) {
                throw new ServerException(ErrorCode.PURCHASE_INSUFFICIENT_FUNDS);
            }
            if (!depositMapper.update(client, deposit)) {
                throw new ServerException(ErrorCode.TRANSACTION_BLOCKED);
            }
        } catch (DataAccessException e) {
            log.error("Can't update Deposit {}", e);
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }
}

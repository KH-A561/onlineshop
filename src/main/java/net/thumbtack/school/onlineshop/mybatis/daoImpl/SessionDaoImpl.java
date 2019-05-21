package net.thumbtack.school.onlineshop.mybatis.daoImpl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.onlineshop.model.Session;
import net.thumbtack.school.onlineshop.mybatis.dao.SessionDao;
import net.thumbtack.school.onlineshop.mybatis.mapper.SessionMapper;
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
public class SessionDaoImpl implements SessionDao {
    private final SessionMapper sessionMapper;

    @Autowired
    public SessionDaoImpl(SessionMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
    }

    @Override
    public void insert(Session session) throws ServerException {
        try {
            sessionMapper.insert(session);
        } catch (DataAccessException e) {
            log.error("Can't insert Session {}", e);
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void delete(Session session) throws ServerException {
        try {
            sessionMapper.delete(session);
        } catch (DataAccessException e) {
            log.error("Can't delete Session {}", e);
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Session getByCookie(String cookie) throws ServerException {
        try {
            Session session;
            if ((session = sessionMapper.getByCookie(cookie)) == null || session.getAccount() == null) {
                throw new ServerException(ErrorCode.SESSION_NOT_FOUND);
            }
            return session;
        } catch (DataAccessException e) {
            log.error("Can't get Session {}", e);
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.SESSION_NOT_FOUND);
        }
    }
}

package net.thumbtack.school.onlineshop.mybatis.daoImpl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.onlineshop.model.Administrator;
import net.thumbtack.school.onlineshop.mybatis.dao.AdministratorDao;
import net.thumbtack.school.onlineshop.mybatis.mapper.AdministratorMapper;
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
public class AdministratorDaoImpl implements AdministratorDao {
    private final AdministratorMapper administratorMapper;

    @Autowired
    public AdministratorDaoImpl(AdministratorMapper administratorMapper) {
        this.administratorMapper = administratorMapper;
    }

    @Override
    public void insert(Administrator administrator) throws ServerException {
        try {
            administratorMapper.insert(administrator);
        } catch (DataAccessException e) {
            log.error("Can't insert administrator {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Administrator getById(int id) throws ServerException {
        try {
            Administrator administrator;
            if ((administrator = administratorMapper.getById(id)) == null) {
                throw new ServerException(ErrorCode.ADMINISTRATOR_NOT_FOUND_BY_ACCOUNT_ID);
            }
            return administrator;
        } catch (DataAccessException e) {
            log.error("Can't get administrator by id {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void update(Administrator administrator) throws ServerException {
        try {
            administratorMapper.update(administrator);
        } catch (DataAccessException e) {
            log.error("Can't update administrator {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }
}

package net.thumbtack.school.onlineshop.mybatis.daoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.mybatis.dao.CategoryDao;
import net.thumbtack.school.onlineshop.mybatis.mapper.CategoryMapper;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CategoryDaoImpl implements CategoryDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDaoImpl.class);
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryDaoImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Category insert(Category category) throws ServerException {
        try {
            categoryMapper.insert(category);
        } catch (DataAccessException e) {
            log.error("Can't insert Category {}", e);
            if (e.getClass().equals(DuplicateKeyException.class) && e.getCause().getMessage().contains("'name_UNIQUE'")){
                throw new ServerException(ErrorCode.CATEGORY_EXISTING_NAME);
            }
            if (e.getCause().getClass().equals(MySQLIntegrityConstraintViolationException.class)) {
                throw new ServerException(ErrorCode.CATEGORY_PARENT_NOT_FOUND);
            }
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
        return category;
    }

    @Override
    public Category getByIdWithoutSubcategories(int id) throws ServerException {
        try {
            Category category;
            if ((category = categoryMapper.getByIdWithoutSubcategories(id)) == null) {
                throw new ServerException(ErrorCode.CATEGORY_NOT_FOUND);
            }
            return category;
        } catch (DataAccessException e) {
            log.error("Can't get Category by id {}", e);
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void update(Category newCategory) throws ServerException {
        try {
            categoryMapper.update(newCategory);
        } catch (DataAccessException e) {
            log.error("Can't update Category {}", e);
            if (e.getClass().equals(DuplicateKeyException.class) && e.getCause().getMessage().contains("'name_UNIQUE'")) {
                throw new ServerException(ErrorCode.CATEGORY_EXISTING_NAME);
            }
            if (e.getCause().getClass().equals(MySQLIntegrityConstraintViolationException.class)) {
                throw new ServerException(ErrorCode.CATEGORY_PARENT_NOT_FOUND);
            }
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteById(int id) throws ServerException {
        try {
            categoryMapper.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Can't delete Category by id {}", e);
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public List<Category> getAll() throws ServerException {
        try {
           return categoryMapper.getAll();
        } catch (DataAccessException e) {
            log.error("Can't get all Categories {}", e);
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }
}

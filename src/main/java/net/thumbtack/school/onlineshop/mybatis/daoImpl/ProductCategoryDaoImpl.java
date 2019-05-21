package net.thumbtack.school.onlineshop.mybatis.daoImpl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.mybatis.dao.ProductCategoryDao;
import net.thumbtack.school.onlineshop.mybatis.mapper.ProductCategoryMapper;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Slf4j
public class ProductCategoryDaoImpl implements ProductCategoryDao {
    private final ProductCategoryMapper productCategoryMapper;

    @Autowired
    public ProductCategoryDaoImpl(ProductCategoryMapper productCategoryMapper) {
        this.productCategoryMapper = productCategoryMapper;
    }

    @Override
    public void batchInsert(Product product, List<Category> categories) throws ServerException {
        try {
            productCategoryMapper.batchInsert(product, categories);
        } catch (DataAccessException e) {
            log.error("Can't insert Product with Categories {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteAllByProductId(int id) throws ServerException {
        try {
            productCategoryMapper.deleteAllByProductId(id);
        } catch (DataAccessException e) {
            log.error("Can't delete all by Product id {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }
}

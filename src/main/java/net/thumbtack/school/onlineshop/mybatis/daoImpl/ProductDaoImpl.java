package net.thumbtack.school.onlineshop.mybatis.daoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.mybatis.dao.ProductDao;
import net.thumbtack.school.onlineshop.mybatis.mapper.ProductCategoryMapper;
import net.thumbtack.school.onlineshop.mybatis.mapper.ProductMapper;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductDaoImpl implements ProductDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);
    private final ProductMapper productMapper;
    private final ProductCategoryMapper productCategoryMapper;

    @Autowired
    public ProductDaoImpl(ProductMapper productMapper, ProductCategoryMapper productCategoryMapper) {
        this.productMapper = productMapper;
        this.productCategoryMapper = productCategoryMapper;
    }

    @Override
    public Product insert(Product product) throws ServerException {
        try {
            productMapper.insert(product);
            if (product.getCategories() != null && !product.getCategories().isEmpty()) {
                productCategoryMapper.batchInsert(product, product.getCategories());
            }
        } catch (DataAccessException e) {
            log.error("Can't insert Product {}", e.getMessage());
            if (e.getClass().equals(DataIntegrityViolationException.class)) {
                if (e.getLocalizedMessage().contains("`category_id`")) {
                    throw new ServerException(ErrorCode.CATEGORY_NOT_FOUND);
                }
            }
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
        return product;
    }

    @Override
    public void update(Product newProduct) throws ServerException {
        try {
            if (!productMapper.update(newProduct)) {
                throw new ServerException(ErrorCode.TRANSACTION_BLOCKED);
            }
        } catch (DataAccessException e) {
            log.error("Can't update Product {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteById(int id) throws ServerException {
        try {
            productMapper.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Can't delete Product by id {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Product getById(int id) throws ServerException {
        try {
            Product product;
            if ((product = productMapper.getById(id)) == null) {
                throw new ServerException(ErrorCode.PRODUCT_NOT_FOUND);
            }
            return product;
        } catch (DataAccessException e) {
            log.error("Can't get Product by id {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public List<Product> getAll() throws ServerException {
        try {
            return productMapper.getAll();
        } catch (DataAccessException e) {
            log.error("Can't get all Products {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public List<Product> getNotCategorized() throws ServerException {
        try {
            return productMapper.getNotCategorized();
        } catch (DataAccessException e) {
            log.error("Can't get nto categorized Products {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public List<Product> getAllByCategories(List<Category> categories) throws ServerException {
        try {
            return productMapper.getAllByCategories(categories);
        } catch (DataAccessException e) {
            log.error("Can't get Products by categories {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }
}

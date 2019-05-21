package net.thumbtack.school.onlineshop.mybatis.daoImpl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.mybatis.dao.ProductInBasketDao;
import net.thumbtack.school.onlineshop.mybatis.mapper.ProductInBasketMapper;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductInBasketDaoImpl implements ProductInBasketDao {
    private final ProductInBasketMapper productInBasketMapper;

    @Autowired
    public ProductInBasketDaoImpl(ProductInBasketMapper productInBasketMapper) {
        this.productInBasketMapper = productInBasketMapper;
    }

    @Override
    public void insert(Client client, Product product) throws ServerException {
        try {
            productInBasketMapper.insert(client, product, product.getCount());
        } catch (DataAccessException e) {
            log.error("Can't insert Product In Basket {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public List<Product> getAllProductsByClient(Client client) throws ServerException {
        try {
            return productInBasketMapper.getAllByClient(client);
        } catch (DataAccessException e) {
            log.error("Can't get Products From Basket {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteProductFromBasketById(int productId, Client client) throws ServerException {
        try {
            productInBasketMapper.deleteProductFromBasketById(productId, client);
        } catch (DataAccessException e) {
            log.error("Can't delete Product From Basket {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void update(Client client, Product product) throws ServerException {
        try {
            productInBasketMapper.update(client, product, product.getCount());
        } catch (DataAccessException e) {
            log.error("Can't update Product In Basket{}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public List<Product> getAvailableProducts(List<Product> products) throws ServerException {
        try {
            return productInBasketMapper.getAvailableProducts(products);
        } catch (DataAccessException e) {
            log.error("Can't get Products From Basket {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }
}

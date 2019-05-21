package net.thumbtack.school.onlineshop.mybatis.daoImpl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.onlineshop.mybatis.dao.*;
import net.thumbtack.school.onlineshop.mybatis.mapper.*;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class DebugDaoImpl implements DebugDao {
    private final AccountMapper accountMapper;
    private final AdministratorMapper administratorMapper;
    private final CategoryMapper categoryMapper;
    private final ClientMapper clientMapper;
    private final DepositMapper depositMapper;
    private final ProductCategoryMapper productCategoryMapper;
    private final ProductMapper productMapper;
    private final ProductInBasketMapper productInBasketMapper;
    private final ProductInPurchaseMapper productInPurchaseMapper;
    private final SessionMapper sessionMapper;

    @Autowired
    public DebugDaoImpl(AccountMapper accountMapper,
                        AdministratorMapper administratorMapper,
                        CategoryMapper categoryMapper,
                        ClientMapper clientMapper,
                        DepositMapper depositMapper,
                        ProductCategoryMapper productCategoryMapper,
                        ProductMapper productMapper,
                        ProductInBasketMapper productInBasketMapper,
                        ProductInPurchaseMapper productInPurchaseMapper,
                        SessionMapper sessionMapper) {
        this.accountMapper = accountMapper;
        this.administratorMapper = administratorMapper;
        this.categoryMapper = categoryMapper;
        this.clientMapper = clientMapper;
        this.depositMapper = depositMapper;
        this.productCategoryMapper = productCategoryMapper;
        this.productMapper = productMapper;
        this.productInBasketMapper = productInBasketMapper;
        this.productInPurchaseMapper = productInPurchaseMapper;
        this.sessionMapper = sessionMapper;
    }

    @Override
    @Transactional
    public void clearDatabase() throws ServerException {
        try {
            productInBasketMapper.deleteAll();
            productInPurchaseMapper.deleteAll();
            sessionMapper.deleteAll();
            depositMapper.deleteAll();
            clientMapper.deleteAll();
            administratorMapper.deleteAll();
            accountMapper.deleteAll();
            productMapper.deleteAll();
            categoryMapper.deleteAll();
        } catch (DataAccessException e) {
            log.error("Can't clear database {}", e);
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }
}

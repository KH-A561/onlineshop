package net.thumbtack.school.onlineshop.mybatis.daoImpl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.model.Purchase;
import net.thumbtack.school.onlineshop.mybatis.dao.ProductInPurchaseDao;
import net.thumbtack.school.onlineshop.mybatis.mapper.DepositMapper;
import net.thumbtack.school.onlineshop.mybatis.mapper.ProductInBasketMapper;
import net.thumbtack.school.onlineshop.mybatis.mapper.ProductInPurchaseMapper;
import net.thumbtack.school.onlineshop.mybatis.mapper.ProductMapper;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mybatis.spring.SqlSessionUtils.getSqlSession;

@Component
@Slf4j
public class ProductInPurchaseDaoImpl implements ProductInPurchaseDao {
    private final ProductInPurchaseMapper productInPurchaseMapper;
    private final ProductMapper productMapper;
    private final ProductInBasketMapper productInBasketMapper;
    private final DepositMapper depositMapper;

    @Autowired
    public ProductInPurchaseDaoImpl(ProductInPurchaseMapper productInPurchaseMapper,
                                    ProductMapper productMapper,
                                    ProductInBasketMapper productInBasketMapper,
                                    DepositMapper depositMapper) {
        this.productInPurchaseMapper = productInPurchaseMapper;
        this.productMapper = productMapper;
        this.productInBasketMapper = productInBasketMapper;
        this.depositMapper = depositMapper;
    }

    @Override
    public Purchase insert(Purchase purchase) throws ServerException {
        try {
            productInPurchaseMapper.insert(purchase);
            if (!productMapper.subtractCount(purchase.getProduct())) {
                throw new ServerException(ErrorCode.TRANSACTION_BLOCKED);
            }
            purchase.getClient().decreaseDeposit(purchase.getProduct().getCount() * purchase.getProduct().getPrice());
            depositMapper.update(purchase.getClient(), purchase.getClient().getDeposit());
            productInBasketMapper.subtractCount(purchase.getClient(), purchase.getProduct());
            productInBasketMapper.deleteZeroCountProducts();
        } catch (DataAccessException e) {
            log.error("Can't insert Purchase {}", e.getMessage());
            throw e;
        }
        return purchase;
    }

    @Override
    public void batchInsert(List<Purchase> purchases) throws ServerException {
        try {
            productInPurchaseMapper.batchInsert(purchases);
            for (Purchase purchase : purchases) {
                if (!productMapper.subtractCount(purchase.getProduct())) {
                    throw new ServerException(ErrorCode.TRANSACTION_BLOCKED);
                }
                productInBasketMapper.subtractCount(purchase.getClient(), purchase.getProduct());
            }
            productInBasketMapper.deleteZeroCountProducts();
        } catch (DataAccessException e) {
            log.error("Can't insert Purchase {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Purchase> getByCategories(List<Category> categories) {
        try {
            return productInPurchaseMapper.getByCategories(categories);
        } catch (DataAccessException e) {
            log.error("Can't get all Purchases {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Map<Product, Integer> getBoughtProductsWithFullPrices(List<Product> products) {
        try {
            List<Product> boughtProducts;
            if ((boughtProducts = productInPurchaseMapper.getByProducts(products)).isEmpty()) {
                return null;
            } else {
                Map<Integer, Product> map = new HashMap<>();
                for (Product product : boughtProducts) {
                    if (!map.containsKey(product.getId())) {
                        map.put(product.getId(), new Product(product.getId(), product.getPrice() * product.getCount(), product.getCount()));
                    } else {
                        map.get(product.getId()).increaseCount(product.getCount());
                        map.get(product.getId()).increasePrice(product.getPrice() * product.getCount());
                    }
                }
                Map<Product, Integer> result = new HashMap<>();
                for (Integer id : map.keySet()) {
                    int fullPrice;
                    Product productFromDb = productMapper.getById(id);
                    Product product = map.get(id);
                    fullPrice = product.getPrice();
                    product.setName(productFromDb.getName());
                    product.setPrice(product.getPrice());
                    product.setCategories(product.getCategories());
                    result.put(product, fullPrice);
                }
                return result;
            }
        } catch (DataAccessException e) {
            log.error("Can't get all Purchases {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Purchase> getAll(Integer offset, Integer limit) {
        try {
            return productInPurchaseMapper.getAll(offset, limit);
        } catch (DataAccessException e) {
            log.error("Can't get all Purchases {}", e.getMessage());
            throw e;
        }
    }
}

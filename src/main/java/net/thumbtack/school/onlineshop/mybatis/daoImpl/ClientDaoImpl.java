package net.thumbtack.school.onlineshop.mybatis.daoImpl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.mybatis.dao.ClientDao;
import net.thumbtack.school.onlineshop.mybatis.mapper.ClientMapper;
import net.thumbtack.school.onlineshop.mybatis.mapper.DepositMapper;
import net.thumbtack.school.onlineshop.mybatis.mapper.ProductInBasketMapper;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ClientDaoImpl implements ClientDao {
    private final ClientMapper clientMapper;
    private final DepositMapper depositMapper;
    private final ProductInBasketMapper productInBasketMapper;

    @Autowired
    public ClientDaoImpl(ClientMapper clientMapper, DepositMapper depositMapper, ProductInBasketMapper productInBasketMapper) {
        this.clientMapper = clientMapper;
        this.depositMapper = depositMapper;
        this.productInBasketMapper = productInBasketMapper;
    }

    @Override
    public void insert(Client client) throws ServerException {
        try {
            clientMapper.insert(client);
            depositMapper.insert(client, client.getDeposit());

        } catch (DataAccessException e) {
            log.error("Can't insert Client {}", e.getMessage());
            if (e.getClass().equals(DuplicateKeyException.class)) {
                if (e.getLocalizedMessage().contains("'email_UNIQUE'")) {
                    throw new ServerException(ErrorCode.CLIENT_EMAIL_ALREADY_EXISTS);
                }
                if (e.getLocalizedMessage().contains("'phone_number_UNIQUE'")) {
                    throw new ServerException(ErrorCode.CLIENT_PHONE_ALREADY_EXISTS);
                }
            }
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);

        }
    }

    @Override
    public Client getById(int id) throws ServerException {
        try {
            Client client;
            if ((client = clientMapper.getById(id)) == null) {
                throw new ServerException(ErrorCode.CLIENT_NOT_FOUND_BY_ACCOUNT_ID);
            }
            return client;
        } catch (DataAccessException e) {
            log.error("Can't get Client by account id {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public List<Client> getClients() throws ServerException {
        try {
            List<Client> clients;
            if ((clients = clientMapper.getClients()).isEmpty()) {
                throw new ServerException(ErrorCode.NO_CLIENTS_FOUND);
            }
            return clients;
        } catch (DataAccessException e) {
            log.error("Can't get Clients {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void update(Client client) throws ServerException {
        try {
            clientMapper.update(client);
        } catch (DataAccessException e) {
            log.error("Can't update Client {}", e.getMessage());
            if (e.getClass().equals(DuplicateKeyException.class)) {
                if (e.getLocalizedMessage().contains("'email_UNIQUE'")) {
                    throw new ServerException(ErrorCode.CLIENT_EMAIL_ALREADY_EXISTS);
                }
                if (e.getLocalizedMessage().contains("'phone_number_UNIQUE'")) {
                    throw new ServerException(ErrorCode.CLIENT_PHONE_ALREADY_EXISTS);
                }
            } else {
                ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
                throw new ServerException(ErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public Client getByCookie(String cookie) throws ServerException {
        try {
            Client client;
            if ((client = clientMapper.getByCookie(cookie)) == null) {
                throw new ServerException(ErrorCode.CLIENT_NOT_FOUND_BY_ACCOUNT_ID);
            }
            client.setProductBasket(productInBasketMapper.getAllByClient(client));
            return client;
        } catch (DataAccessException e) {
            log.error("Can't get Client by cookie {}", e);
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public List<Client> getClientsWithPurchases(List<Integer> ids) throws ServerException {
        try {
            return clientMapper.getClientsWithPurchases(ids);
        } catch (DataAccessException e) {
            log.error("Can't get Clients {}", e.getMessage());
            ErrorCode.DATABASE_ERROR.setErrorDescription(e.getCause().getMessage());
            throw new ServerException(ErrorCode.DATABASE_ERROR);
        }
    }
}


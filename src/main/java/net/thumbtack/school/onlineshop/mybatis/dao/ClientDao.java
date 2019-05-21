package net.thumbtack.school.onlineshop.mybatis.dao;


import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;

import java.util.List;

public interface ClientDao {
    void insert(Client client) throws ServerException;

    Client getById(int id) throws ServerException;

    List<Client> getClients() throws ServerException;

    void update(Client client) throws ServerException;

    Client getByCookie(String cookie) throws ServerException;

    List<Client> getClientsWithPurchases(List<Integer> ids) throws ServerException;
}

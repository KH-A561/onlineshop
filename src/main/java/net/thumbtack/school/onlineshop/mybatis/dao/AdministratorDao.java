package net.thumbtack.school.onlineshop.mybatis.dao;


import net.thumbtack.school.onlineshop.model.Administrator;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;

public interface AdministratorDao {
    void insert(Administrator administrator) throws ServerException;

    Administrator getById(int id) throws ServerException;

    void update(Administrator administrator) throws ServerException;
}

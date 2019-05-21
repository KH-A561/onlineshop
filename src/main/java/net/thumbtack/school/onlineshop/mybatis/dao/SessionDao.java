package net.thumbtack.school.onlineshop.mybatis.dao;


import net.thumbtack.school.onlineshop.model.Session;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;

public interface SessionDao {
    void insert(Session session) throws ServerException;
    void delete(Session session) throws ServerException;
    Session getByCookie(String cookie) throws ServerException;
}

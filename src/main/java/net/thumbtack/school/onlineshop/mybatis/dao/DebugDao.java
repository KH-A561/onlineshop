package net.thumbtack.school.onlineshop.mybatis.dao;

import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;

public interface DebugDao {
    void clearDatabase() throws ServerException;
}

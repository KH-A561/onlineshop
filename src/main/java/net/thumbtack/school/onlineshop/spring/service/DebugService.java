package net.thumbtack.school.onlineshop.spring.service;

import net.thumbtack.school.onlineshop.mybatis.dao.DebugDao;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebugService {
    private final DebugDao debugDao;

    @Autowired
    public DebugService(DebugDao debugDao) {
        this.debugDao = debugDao;
    }

    public void clearDatabase() throws ServerException {
        debugDao.clearDatabase();
    }
}

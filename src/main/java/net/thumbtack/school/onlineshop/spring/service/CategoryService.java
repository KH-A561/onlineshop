package net.thumbtack.school.onlineshop.spring.service;

import net.thumbtack.school.onlineshop.model.Account;
import net.thumbtack.school.onlineshop.model.AccountType;
import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.mybatis.dao.CategoryDao;
import net.thumbtack.school.onlineshop.mybatis.dao.SessionDao;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.AddCategoryAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.EditCategoryAdminRequest;
import net.thumbtack.school.onlineshop.spring.dto.response.administrator.CategoryDtoResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryDao categoryDao;
    private final SessionDao sessionDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao, SessionDao sessionDao) {
        this.categoryDao = categoryDao;
        this.sessionDao = sessionDao;
    }

    @Transactional(rollbackFor = ServerException.class)
    public CategoryDtoResponse insert(AddCategoryAdminDtoRequest request, String cookie) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
        if (!account.getType().equals(AccountType.ADMINISTRATOR)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        Category category = new Category(0, request.getName(), null, null);
        if (request.getParentId() == null || request.getParentId() == 0) {
            category = categoryDao.insert(category);
            return new CategoryDtoResponse(category.getId(), category.getName(), null, null);
        } else {
            Category parentCategory = categoryDao.getByIdWithoutSubcategories(request.getParentId());
            category.setParentCategory(parentCategory);
            category = categoryDao.insert(category);
            return new CategoryDtoResponse(category.getId(), category.getName(), parentCategory.getId(), parentCategory.getName());
        }
    }

    @Transactional(rollbackFor = ServerException.class)
    public CategoryDtoResponse getById(int id, String cookie) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
        if (!account.getType().equals(AccountType.ADMINISTRATOR)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        Category category = categoryDao.getByIdWithoutSubcategories(id);
        if (category.getParentCategory() != null) {
            return new CategoryDtoResponse(category.getId(), category.getName(), category.getParentCategory().getId(), category.getParentCategory().getName());
        } else {
            return new CategoryDtoResponse(category.getId(), category.getName(), null, null);
        }
    }

    @Transactional(rollbackFor = ServerException.class)
    public CategoryDtoResponse update(int id, EditCategoryAdminRequest request, String cookie) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
        if (!account.getType().equals(AccountType.ADMINISTRATOR)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        Category newCategory;
        Category parentCategory;
        if (!(request.getParentId() == null || request.getParentId() == 0)) {
            newCategory = new Category(request.getParentId(), request.getName(), null, null);
        } else {
            newCategory = new Category(id, request.getName(), new Category(request.getParentId()));
        }
        Category oldCategory = categoryDao.getByIdWithoutSubcategories(id);
        if (newCategory.getName() == null) {
            newCategory.setName(oldCategory.getName());
        }
        CategoryDtoResponse response = new CategoryDtoResponse(newCategory.getId(), newCategory.getName(), null, null);
        if (oldCategory.getParentCategory() == null) {
            if (newCategory.getParentCategory() != null) {
                throw new ServerException(ErrorCode.CATEGORY_FROM_PARENT_TO_CHILD);
            }
        } else {
            if (newCategory.getParentCategory() == null) {
                throw new ServerException(ErrorCode.CATEGORY_FROM_CHILD_TO_PARENT);
            }
            parentCategory = categoryDao.getByIdWithoutSubcategories(newCategory.getParentCategory().getId());
            if (parentCategory.getParentCategory() != null) {
                throw new ServerException(ErrorCode.CATEGORY_CANNOT_ADD_SUBCATEGORY_TO_CHILD);
            }
            response.setParentId(parentCategory.getId());
            response.setParentName(parentCategory.getName());
        }
        categoryDao.update(newCategory);
        return response;
    }

    @Transactional(rollbackFor = ServerException.class)
    public void deleteById(int id, String cookie) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
        if (!account.getType().equals(AccountType.ADMINISTRATOR)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        categoryDao.deleteById(id);
    }

    @Transactional(rollbackFor = ServerException.class)
    public List<CategoryDtoResponse> getAll(String cookie) throws ServerException {
        Account account = sessionDao.getByCookie(cookie).getAccount();
        if (!account.getType().equals(AccountType.ADMINISTRATOR)) {
            throw new ServerException(ErrorCode.ACCOUNT_INCORRECT_TYPE_OF_USER);
        }
        List<CategoryDtoResponse> response = new ArrayList<>();
        for (Category category : categoryDao.getAll()) {
            response.add(new CategoryDtoResponse(category.getId(), category.getName(), null, null));
            category.getSubcategories().forEach(sc -> response.add(new CategoryDtoResponse(sc.getId(),
                                                                                           sc.getName(),
                                                                                           sc.getParentCategory().getId(),
                                                                                           sc.getParentCategory().getName())));
        }
        return response;
    }
}

package net.thumbtack.school.onlineshop.mybatis.mapper;

import net.thumbtack.school.onlineshop.model.Account;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper {
    @Insert("INSERT INTO `account` " +
            "VALUES (#{id}, #{firstName}, #{lastName}, #{patronymic}, #{login}, #{password}, #{type})")
    @Options(useGeneratedKeys = true)
    Integer insert(Account account);

    @Update("UPDATE `account` " +
            "SET `id` = #{id}, `firstName` = #{firstName}, `lastName` = #{lastName}, `patronymic` = #{patronymic}, `password` = #{password} " +
            "WHERE `id` = #{id}")
    void update(Account account);

    @Select("SELECT id, firstName, lastName, patronymic, login, password, type " +
            "FROM `account` WHERE id = #{id}")
    Account getById(int id);

    @Delete("DELETE FROM `account` WHERE `id` = #{id}")
    void deleteById(int id);

    @Select("SELECT id, firstName, lastName, patronymic, login, password, type " +
            "FROM `account` " +
            "WHERE `login` = #{login}")
    Account getByLogin(String login);

    @Delete("DELETE FROM `account`")
    void deleteAll();
}

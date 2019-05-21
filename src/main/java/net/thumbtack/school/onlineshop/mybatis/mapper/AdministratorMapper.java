package net.thumbtack.school.onlineshop.mybatis.mapper;

import net.thumbtack.school.onlineshop.model.Administrator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
public interface AdministratorMapper {
    @Insert("INSERT INTO `administrator` VALUES (#{id}, #{position})")
    void insert(Administrator administrator);

    @Update("UPDATE `administrator` SET `account_id` = #{id}, `position` = #{position} WHERE `account_id` = #{id}")
    void update(Administrator administrator);

    @Select("SELECT id, firstName, lastName, patronymic, login, password, type, position " +
            "FROM `account` " +
            "RIGHT JOIN `administrator` " +
            "ON `administrator`.account_id = `account`.id  " +
            "WHERE `account`.id = #{id}")
    Administrator getById(int id);

    @Delete("DELETE FROM `administrator` WHERE `id` = #{id}")
    void deleteById(int id);

    @Delete("DELETE FROM `administrator`")
    void deleteAll();
}

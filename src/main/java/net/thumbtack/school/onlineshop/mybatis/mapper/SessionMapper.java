package net.thumbtack.school.onlineshop.mybatis.mapper;

import net.thumbtack.school.onlineshop.model.Account;
import net.thumbtack.school.onlineshop.model.Session;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

@Mapper
public interface SessionMapper {
    @Insert("INSERT INTO `session` " +
            "VALUES (#{`session`.cookie}, #{`session`.account.id}) " +
            "ON DUPLICATE KEY UPDATE cookie = #{`session`.cookie}")
    void insert(@Param("`session`") Session session);

    @Select("SELECT cookie, account_id FROM `session` WHERE cookie = #{cookie}")
    @Results({
            @Result(property = "cookie", column = "cookie"),
            @Result(property = "account", column = "account_id", javaType = Account.class,
                    one = @One(select = "net.thumbtack.school.onlineshop.mybatis.mapper.AccountMapper.getById", fetchType = FetchType.LAZY))
    })
    Session getByCookie(String cookie);

    @Delete("DELETE FROM `session` WHERE cookie = #{cookie}")
    void delete(Session session);

    @Delete("DELETE FROM `session`")
    void deleteAll();
}

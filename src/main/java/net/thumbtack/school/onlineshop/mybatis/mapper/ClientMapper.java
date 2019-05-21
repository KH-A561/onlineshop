package net.thumbtack.school.onlineshop.mybatis.mapper;

import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClientMapper {
    @Insert("INSERT INTO `client` VALUES (#{id}, #{email}, #{address}, #{phone})")
    void insert(Client client);

    @Update("UPDATE `client` " +
            "SET `email` = #{email}, `address` = #{address}, `phone` = #{phone} " +
            "WHERE `account_id` = #{id}")
    void update(Client client);

    @Select("SELECT id, firstName, lastName, patronymic, login, password, email, address, phone, type, deposit, version " +
            "FROM `account` " +
            "INNER JOIN `client` ON `client`.account_id = `account`.id " +
            "INNER JOIN `deposit` ON `deposit`.`client_account_id` = `account`.id " +
            "WHERE `account`.id = #{id}")
    Client getById(int id);

    @Select("SELECT id, firstName, lastName, patronymic, login, password, email, address, phone, type, deposit, version " +
            "FROM `session` " +
            "INNER JOIN `account` ON `account`.id = `session`.account_id " +
            "INNER JOIN `client` ON `client`.account_id = `account`.id " +
            "INNER JOIN `deposit` ON `deposit`.`client_account_id` = `account`.id " +
            "WHERE `session`.cookie = #{cookie}")
    Client getByCookie(String cookie);

    @Delete("DELETE FROM `account` WHERE `client_user_id` = #{`client`.id}")
    void delete(Client client);

    @Select("SELECT id, firstName, lastName, patronymic, login, password, email, address, phone, type " +
            "FROM `account` " +
            "INNER JOIN `client` ON `client`.account_id = `account`.id")
    List<Client> getClients();

    @Delete("DELETE FROM `client`")
    void deleteAll();

    @Select("<script>" +
            "SELECT id, firstName, lastName, patronymic, login, password, email, address, phone, type " +
            "FROM `account` " +
            "INNER JOIN `client` ON account_id = id " +
            "WHERE account_id IN" +
            "<foreach item = 'id' collection = 'ids' separator = ',' open = '(' close = ')'>" +
                "#{id}" +
            "</foreach>" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "firstName", column = "firstName"),
            @Result(property = "lastName", column = "lastName"),
            @Result(property = "patronymic", column = "patronymic"),
            @Result(property = "login", column = "login"),
            @Result(property = "password", column = "password"),
            @Result(property = "email", column = "email"),
            @Result(property = "address", column = "address"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "type", column = "type"),
            @Result(property = "purchases", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.onlineshop.mybatis.mapper.net.thumbtack.school.onlineshop.mybatis.mapper.ProductInPurchaseMapper.getByClientId")),
    })
    List<Client> getClientsWithPurchases(@Param("ids") List<Integer> ids);
}

package net.thumbtack.school.onlineshop.mybatis.mapper;

import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Deposit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DepositMapper {
    @Insert("INSERT INTO `deposit` (`client_account_id`, `deposit`) " +
            "VALUES (#{`client`.id}, #{`deposit`.amount})")
    void insert(@Param("`client`") Client client, @Param("`deposit`") Deposit deposit);

    @Update("UPDATE `deposit` " +
            "SET `deposit` = #{`deposit`.amount}, `version` = `version` + 1 " +
            "WHERE `client_account_id` = #{`client`.id} AND `version` = #{`deposit`.version}")
    boolean update(@Param("`client`") Client client, @Param("`deposit`") Deposit deposit);

    @Select("SELECT `deposit`, `version` FROM `deposit` WHERE `client_account_id` = #{`client`.id}")
    Deposit getDeposit(@Param("`client`") Client client);

    @Delete("DELETE FROM `deposit` WHERE `client_account_id` = #{`client`.id}")
    void delete(@Param("`client`") Client client);

    @Delete("DELETE FROM `deposit`")
    void deleteAll();
}

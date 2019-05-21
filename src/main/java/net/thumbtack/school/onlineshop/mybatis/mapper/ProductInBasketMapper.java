package net.thumbtack.school.onlineshop.mybatis.mapper;

import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Product;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProductInBasketMapper {
    @Insert("INSERT INTO `product_in_basket` VALUES (NULL, #{client.id}, #{product.id}, #{count})")
    void insert(@Param("client") Client client, @Param("product") Product product, @Param("count") int count);

    @Delete("DELETE FROM `product_in_basket` WHERE `client_account_id` = #{clientId}")
    void deleteByClientId(int clientId);

    @Select("SELECT `product`.id, `product`.name, `product`.price, `product_in_basket`.count, `product`.version " +
            "FROM `product_in_basket` INNER JOIN `product` " +
            "ON `product`.id = `product_in_basket`.product_id " +
            "WHERE `product_in_basket`.client_account_id = #{`client`.id};")
    List<Product> getAllByClient(@Param("`client`") Client client);

    @Delete("DELETE FROM `product_in_basket` WHERE `product_id` = #{`productId`} AND `client_account_id` = #{`client`.id}")
    void deleteProductFromBasketById(@Param("`productId`") int productId, @Param("`client`") Client client);

    @Update("UPDATE `product_in_basket` " +
            "SET count = #{`newCount`} " +
            "WHERE `product_id` = #{`product`.id} AND `client_account_id` = #{`client`.id}")
    void update(@Param("`client`") Client client, @Param("`product`") Product product, @Param("`newCount`") int newCount);

    @Select({
            "<script>",
            "SELECT `product`.id, `product`.name, `product`.price, `product_in_basket`.count, `product`.version ",
            "FROM `product_in_basket` INNER JOIN `product` ",
            "ON `product`.id = `product_in_basket`.product_id ",
            "WHERE `product_in_basket`.product_id IN  " +
                "<foreach item = 'p' collection = 'products' separator = ',' open = '(' close = ')'>",
                    "#{p.id}",
                "</foreach>",
            "AND `product`.count >= `product_in_basket`.count",
            "</script>"
            })
    List<Product> getAvailableProducts(@Param("products") List<Product> products);

    @Update("UPDATE `product_in_basket` " +
            "SET count = count - #{`product`.count} " +
            "WHERE `product_id` = #{`product`.id} AND `client_account_id` = #{`client`.id}")
    void subtractCount(@Param("`client`") Client client, @Param("`product`") Product product);

    @Delete("DELETE FROM `product_in_basket` WHERE count = 0")
    void deleteZeroCountProducts();

    @Delete("DELETE FROM `product_in_basket`")
    void deleteAll();
}

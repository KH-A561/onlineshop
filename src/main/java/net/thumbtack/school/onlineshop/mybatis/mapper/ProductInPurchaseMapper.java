package net.thumbtack.school.onlineshop.mybatis.mapper;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Client;
import net.thumbtack.school.onlineshop.model.Product;
import net.thumbtack.school.onlineshop.model.Purchase;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProductInPurchaseMapper {
    @Insert("INSERT INTO `product_in_purchase` (`id`, `client_account_id`, `product_id`, `name`, `price`, `count`) " +
            "VALUES (NULL, #{client.id}, #{product.id}, #{name}, #{price}, #{count})")
    @Options(useGeneratedKeys = true)
    Integer insert(Purchase purchase);

    @Insert({
            "<script>",
            "INSERT INTO `product_in_purchase`",
            "(`id`, `client_account_id`, `product_id`, `name`, `price`, `count`)",
            "VALUES" +
                    "<foreach item = 'purchase' collection = 'purchases' separator = ','>",
                        "(NULL, #{purchase.client.id}, #{purchase.product.id}, #{purchase.name}, #{purchase.price}, #{purchase.count})",
                    "</foreach>",
            "</script>"
    })
    void batchInsert(@Param("purchases") List<Purchase> purchases);

    @Delete("DELETE FROM `product_in_purchase`")
    void deleteAll();

    @Select("<script>" +
            "SELECT id, client_account_id, product_id, name, price, count " +
            "FROM `product_in_purchase`" +
            "ORDER BY id" +
                "<if test='offset != 0 and limit != 0'>" +
                "           LIMIT #{offset}, #{limit}" +
                "</if>" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "price", column = "price"),
            @Result(property = "count", column = "count"),
            @Result(property = "client", column = "client_account_id", javaType = Client.class,
            one = @One(select = "net.thumbtack.school.onlineshop.mybatis.mapper.ClientMapper.getById")),
            @Result(property = "product", column = "product_id", javaType = Product.class,
                    one = @One(select = "net.thumbtack.school.onlineshop.mybatis.mapper.ProductMapper.getById"))
    })
    List<Purchase> getAll(@Param("offset") Integer offset,  @Param("limit") Integer limit);

    @Select("SELECT id, product_id, name, price, count " +
            "FROM `product_in_purchase` " +
            "WHERE client_account_id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "price", column = "price"),
            @Result(property = "count", column = "count"),
            @Result(property = "product", column = "product_id", javaType = Product.class,
                    one = @One(select = "net.thumbtack.school.onlineshop.mybatis.mapper.ProductMapper.getById"))
    })
    List<Purchase> getByClientId(int id);

    @Select("<script>" +
                "SELECT DISTINCT id, client_account_id, product_id, name, price, count " +
                "FROM `product_in_purchase` " +
                "INNER JOIN `product_category` " +
                    "ON `product_category`.product_id = `product_in_purchase`.product_id " +
                    "WHERE category_id IN " +
                        "<foreach item = 'category' collection = 'categories' separator = ',' open = '(' close = ')'>" +
                            "#{category.id}" +
                        "</foreach>" +
                "ORDER BY id " +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "price", column = "price"),
            @Result(property = "count", column = "count"),
            @Result(property = "client", column = "client_account_id", javaType = Client.class,
                    one = @One(select = "net.thumbtack.school.onlineshop.mybatis.mapper.ClientMapper.getById")),
            @Result(property = "product", column = "product_id", javaType = Product.class,
                    one = @One(select = "net.thumbtack.school.onlineshop.mybatis.mapper.ProductMapper.getById"))
    })
    List<Purchase> getByCategories(@Param("categories") List<Category> categories);

    /*
        This query would've been so much better if only your server hadn't have 'sql_mode=only_full_group_by' option :(
        - possible improvement:

        SELECT product_id AS id, `product`.name, `product`.price, SUM(`product_in_purchase`.count) AS count,
														  SUM(`product_in_purchase`.price * `product_in_purchase`.count) AS price
        FROM `product_in_purchase`
        INNER JOIN `product` ON `product`.id = product_id
        GROUP BY product_id;
     */
    @Select("<script>" +
                "SELECT product_id, name, price, count " +
                "FROM `product_in_purchase` " +
                "WHERE product_id IN " +
                    "<foreach item = 'p' collection = 'ps' separator = ',' open = '(' close = ')'>" +
                        "#{p.id}" +
                    "</foreach>" +
            "</script>")
    @Results({
            @Result(property = "id", column = "product_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "price", column = "price"),
            @Result(property = "count", column = "count")
    })
    List<Product> getByProducts(@Param("ps") List<Product> products);
}

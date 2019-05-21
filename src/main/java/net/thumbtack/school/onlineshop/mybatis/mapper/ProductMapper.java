package net.thumbtack.school.onlineshop.mybatis.mapper;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Insert("INSERT INTO `product` (id, name, price, count) " +
            "VALUES (#{id}, #{name}, #{price}, #{count})")
    @Options(useGeneratedKeys = true)
    Integer insert(Product product);

    @Update("UPDATE `product` " +
            "SET `id` = #{id}, `name` = #{name}, `price` = #{price}, `count` = #{count}, `version` = `version` + 1 " +
            "WHERE `id` = #{id} AND `version` = #{version}")
    boolean update(Product product);

    @Delete("DELETE FROM `product` WHERE `id` = #{id}")
    void deleteById(int id);

    @Select("SELECT id, name, price, count, version FROM `product` WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "price", column = "price"),
            @Result(property = "count", column = "count"),
            @Result(property = "version", column = "version"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.onlineshop.mybatis.mapper.ProductCategoryMapper.getAllCategoryIdsByProductId",
                                 fetchType = FetchType.LAZY))
    })
    Product getById(int id);

    @Update("UPDATE `product` " +
            "SET count = count - #{product.count}, version = version + 1 " +
            "WHERE id = #{product.id} AND version = #{product.version}")
    boolean subtractCount(@Param("product") Product boughtProduct);

    @Select("SELECT id, name, price, count, version FROM `product` ORDER BY name")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "price", column = "price"),
            @Result(property = "count", column = "count"),
            @Result(property = "version", column = "version"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.onlineshop.mybatis.mapper.ProductCategoryMapper." +
                                          "getAllCategoriesByProductId",
                                 fetchType = FetchType.LAZY))
    })
    List<Product> getAll();

    @Select("SELECT id, name, price, count, version " +
            "FROM `product` " +
            "WHERE NOT EXISTS (" +
                        "SELECT * FROM `product_category` " +
                        "WHERE product_id = `product`.id" +
                      ")" +
            "ORDER BY name")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "price", column = "price"),
            @Result(property = "count", column = "count"),
            @Result(property = "version", column = "version")
    })
    List<Product> getNotCategorized();

    @Select({
            "<script>",
            "SELECT DISTINCT `product`.id, `product`.name, `product`.price, `product`.count, version",
            "FROM `product` INNER JOIN `product_category` ",
            "ON `product`.id = `product_category`.product_id ",
            "WHERE category_id IN  " +
                    "<foreach item = 'c' collection = 'cs' separator = ',' open = '(' close = ')'>",
                        "#{c.id}",
                    "</foreach>",
            "ORDER BY name",
            "</script>"
    })
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "price", column = "price"),
            @Result(property = "count", column = "count"),
            @Result(property = "version", column = "version"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.onlineshop.mybatis.mapper.ProductCategoryMapper." +
                                          "getAllCategoriesByProductId",
                                 fetchType = FetchType.LAZY))
    })
    List<Product> getAllByCategories(@Param("cs") List<Category> categories);

    @Delete("DELETE FROM `product`")
    void deleteAll();
}

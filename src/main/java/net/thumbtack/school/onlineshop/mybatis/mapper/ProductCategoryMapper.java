package net.thumbtack.school.onlineshop.mybatis.mapper;

import net.thumbtack.school.onlineshop.model.Category;
import net.thumbtack.school.onlineshop.model.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProductCategoryMapper {
    @Insert("INSERT INTO `product_has_category` VALUES (NULL, #{`product`.id}, #{`category`.id})")
    void insert(@Param("`product`") Product product, @Param("`category`") Category category);

    @Insert({
            "<script>",
            "INSERT INTO `product_category`",
                "(id, product_id, category_id)",
            "VALUES" +
                    "<foreach item = '`category`' collection = 'categories' separator = ','>",
                        "(NULL, #{`product`.id}, #{`category`.id})",
                    "</foreach>",
            "</script>"
    })
    void batchInsert(@Param("`product`") Product product, @Param("categories") List<Category> categories);

    @Select("SELECT category_id AS id FROM `product_category` WHERE product_id = #{id}")
    List<Category> getAllCategoryIdsByProductId(int id);

    @Delete("DELETE FROM `product_category` WHERE `product_id` = #{productId}")
    void deleteAllByProductId(int productId);

    @Select("SELECT category_id AS id, name " +
            "FROM `product_category` " +
            "INNER JOIN `category` " +
            "ON `category`.id = category_id AND product_id = #{product_id} " +
            "ORDER BY name")
    List<Category> getAllCategoriesByProductId(int product_id);
}

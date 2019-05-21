package net.thumbtack.school.onlineshop.mybatis.mapper;

import net.thumbtack.school.onlineshop.model.Category;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.core.annotation.Order;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Insert("INSERT INTO `category` VALUES (#{id}, #{name}, #{parentCategory.id})")
    @Options(useGeneratedKeys = true)
    Integer insert(Category category);

    @Update("UPDATE `category` SET `id` = #{id}, `name` = #{name}, `parent_id` = #{parentCategory.id} WHERE `id` = #{id}")
    void update(Category category);

    @Select("SELECT id, name, parent_id FROM `category` WHERE id = #{id} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "parentCategory", column = "parent_id", javaType = Category.class,
                    one = @One(select = "net.thumbtack.school.onlineshop.mybatis.mapper.CategoryMapper.getByIdWithoutSubcategories",
                               fetchType = FetchType.LAZY))
    })
    Category getByIdWithoutSubcategories(int id);

    @Select("SELECT id, name FROM `category` WHERE parent_id IS NULL ORDER BY name")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "subcategories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.onlineshop.mybatis.mapper.CategoryMapper.getSubcategoriesById",
                            fetchType = FetchType.LAZY))
    })
    List<Category> getAll();

    @Select("SELECT id, name, parent_id FROM `category` WHERE parent_id = #{id} ORDER BY name")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "parentCategory", column = "parent_id", javaType = Category.class,
                    one = @One(select = "net.thumbtack.school.onlineshop.mybatis.mapper.CategoryMapper.getByIdWithoutSubcategories",
                            fetchType = FetchType.LAZY))
    })
    List<Category> getSubcategoriesById(int id);

    @Delete("DELETE FROM `category` WHERE `id` = #{id}")
    void deleteById(int id);

    @Delete("DELETE FROM `category`")
    void deleteAll();
}

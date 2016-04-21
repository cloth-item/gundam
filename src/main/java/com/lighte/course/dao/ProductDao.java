package com.lighte.course.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lighte.course.meta.Product;

public interface ProductDao {
	/**
	 * 向content表中插入一行数据，并将主键存储到传入的对象中
	 * @param product 包含对应数据的对象，包括产品名称、摘要、图片、细节和价格
	 * @return 受影响的行数；0代表插入失败
	 */
	@Insert({"insert into content(title, abstract, icon, text, price) ",
			"values (#{title}, #{summary}, #{image}, #{detail}, #{price})"})
	@Options(useGeneratedKeys=true, keyProperty="id")
	public int insertProduct(Product product);
	
	/**
	 * 通过产品id，从content表中查询一行数据
	 * @param id 产品id，主键
	 * @return 返回包含数据的对象；null代表查询失败
	 */
	@Results({
		@Result(property="summary", column="abstract"),
		@Result(property="image", column="icon"),
		@Result(property="detail", column="text")
	})
	@Select("select * from content where id=#{id}")
	public Product selectProduct(int id);
	
	/**
	 * 通过产品id，从content表中查询一行数据(部分数据)
	 * @param id 产品id，主键
	 * @return 返回包含数据的对象，包括id，产品名称和图片；null代表查询失败
	 */
	@Results({
		@Result(property="image", column="icon")
	})
	@Select("select id,title,icon from content where id=#{id}")
	public Product selectMiniProduct(int id);
	
	/**
	 * 从content表中查询所有数据
	 * @return 封装成List的数据，其中每个对象包含id，名称，图片，价格；null代表查询失败
	 */
	@Results({
		@Result(property="image", column="icon")
	})
	@Select("select id,title,icon,price from content")
	public List<Product> selectMiniProducts();
	
	/**
	 * 指定id，更新content表的一行数据
	 * @param product 必须包含id的数据对象，包含名称，摘要，图片，细节和价格
	 * @return 受影响的行数，0代表更新失败
	 */
	@Update({"update content set title=#{title}, abstract=#{summary}, icon=#{image}, ",
			"text=#{detail}, price=#{price} where id=#{id}"})
	public int update(Product product);
	
	/**
	 * 指定id，从content表中删除一行数据
	 * @param id 主键
	 * @return 删除结果，成功返回true；false代表删除失败
	 */
	@Delete("delete from content where id=#{id}")
	public boolean delete(int id);
	
}

package com.lighte.course.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.lighte.course.meta.Trx;

public interface TrxDao {
	
	/**
	 * 向trx表插入一行数据，并将主键存储到传入的对象中
	 * @param trx 封装成对象的数据，包括产品id，用户id，购买价格和购买时间
	 * @return 受影响的行数；0代表插入失败
	 */
	@Insert({"insert into trx(contentId, personId, price, time)",
	"values (#{productId}, #{userId}, #{price}, #{time})"})
	@Options(useGeneratedKeys=true, keyProperty="id")
	public int insertTrx(Trx trx);
	
	/**
	 * 指定用户id，从trx表中查询数据
	 * @param userId 用户id
	 * @return 封装成List的数据，每个对象包含id,产品id，用户id，购买价格和购买时间；null代表查询失败
	 */
	@Results({
		@Result(property="productId", column="contentId")
	})
	@Select("select * from trx where personId=#{userId}")
	public List<Trx> selectAllWithUser(int userId);
	
	/**
	 * 指定产品id，从trx表中查询数据(由于每个产品只有一个，所以只会查到一条信息)
	 * @param productId 产品id
	 * @return Trx对象，包含id,产品id，用户id，购买价格和购买时间；null代表查询失败
	 */
	@Select("select * from trx where contentId=#{productId}")
	public Trx selectWithProduct(int productId);

//	@Select({"select content.id as id, content.title as title, trx.price as buyPrice, trx.time as buyTime ",
//	"from trx left join content on trx.contentId=content.id where trx.personId=#{userId}"})
//	public List<Product> selectBuyList(int userId);
}

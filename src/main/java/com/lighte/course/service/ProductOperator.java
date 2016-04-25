package com.lighte.course.service;

import java.util.List;

import com.lighte.course.meta.ProductInfo;

public interface ProductOperator {
	/**
	 * 向content表插入一条记录
	 * @return 主键id的值；如果更插入失败，返回0
	 */
	public int createProduct(String title, String summary, String image, String detail, long price);
	
	/**
	 * 根据id，从content表中查取数据
	 * @return Product对象，包括名称、摘要、图片、细节和价格；如果没找到就返回null
	 */
	public ProductInfo findProduct(int id);
	
	/**
	 * 根据id，先从content中查取数据，再从trx表中查取数据，并合并结果
	 * @return 完整的Product对象，还包括isBuy,isSell,buyTime,buyPrice；如果没找到就返回null
	 */
	public ProductInfo findMoreFilds(int id);
	
	/**
	 * 从content中查找部分列的所有数据
	 * @return Product对象组成List表(数据有精简，不包括detail等)；如果没找到就返回null
	 */
	public List<ProductInfo> findMiniProducts();
	
	/**
	 * 更新content表中的一行信息
	 * @return 受影响的行数；如果更新失败，会返回0
	 */
	public int changeProduct(int id, String title, String summary, String image, String detail, long price);
	
	/**
	 * 删除content表中的一行信息
	 * @return 受影响的行数；如果删除失败，会返回0
	 */
	public boolean deleteProduct(int id);

	/**
	 * 向trx表中插入一条记录
	 * @return 主键id的值；如果更插入失败，返回0
	 */
	public int buy(int productId, int userId, long price, long time);
	
	/**
	 * 根据userId，先从trx中查取数据，再根据结果中的productId从content表中查取数据，并合并结果
	 * @return Product对象组成List表(数据有精简，不包括detail)；如果没找到就返回null
	 */
	public List<ProductInfo> fdUserBuyList(int userId);
}

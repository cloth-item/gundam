package com.lighte.course.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.lighte.course.dao.ProductDao;
import com.lighte.course.dao.TrxDao;
import com.lighte.course.meta.Product;
import com.lighte.course.meta.ProductInfo;
import com.lighte.course.meta.Trx;
import com.lighte.course.service.ProductOperator;

@Component("productOperator")
public class ProductOperatorImpl implements ProductOperator {

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private TrxDao trxDao;
	
	@Override
	public int createProduct(String title, String summary, String image, String detail, long price) {
		Product product = new Product();
		try {
			product.setTitle(title);
			product.setSummary(summary);
			product.setImage(image.getBytes("UTF-8"));
			product.setDetail(detail.getBytes("UTF-8"));
			product.setPrice(price);
		} catch (UnsupportedEncodingException e) {
			// ignore
			System.out.println(e.getMessage());
		}
		
		productDao.insertProduct(product);
		return product.getId();
	}
	
	@Override
	public ProductInfo findProduct(int id) {
		Product product = productDao.selectProduct(id);
		if(product != null) { 
			ProductInfo productInfo = new ProductInfo(product);
			return productInfo;
		}
		
		return null;
	}
	
	@Override
	public ProductInfo findMoreFilds(int id) {
		Product product = productDao.selectProduct(id);
		if(product != null) { 
			ProductInfo productInfo = new ProductInfo(product);
			Trx trx = trxDao.selectWithProduct(id);
			if(trx != null) {
				productInfo.addTrxInfo(trx);
			}
			return productInfo;
		}
		
		return null;
	}

	@Override
	public List<ProductInfo> findMiniProducts() {
		List<Product> productList = productDao.selectMiniProducts();
		if(productList != null) {
			List<ProductInfo> productInfoList = new ArrayList<ProductInfo>();
			for(int i=0; i<productList.size(); i++) {
				Product product = productList.get(i);
				productInfoList.add(new ProductInfo(product));
				Trx trx = trxDao.selectWithProduct(product.getId());
				if(trx != null) {
					productInfoList.get(i).addTrxInfo(trx);
				}
			}
			return productInfoList;
		}
		
		return null;
	}
	
	@Override
	public int changeProduct(int id, String title, String summary, String image, String detail, long price) {
		Product product = new Product();
		try {
			product.setId(id);
			product.setTitle(title);
			product.setSummary(summary);
			product.setImage(image.getBytes("UTF-8"));
			product.setDetail(detail.getBytes("UTF-8"));
			product.setPrice(price);
		} catch (UnsupportedEncodingException e) {
			// ignore
			System.out.println(e.getMessage());
		}
		
		int result = productDao.update(product);
		return result;
	}

	@Override
	public boolean deleteProduct(int id) {
		return productDao.delete(id);
	}
	
	@Override
	public int buy(int productId, int userId, long price, long time) {
		Trx trx = new Trx();
		trx.setProductId(productId);
		trx.setUserId(userId);
		trx.setPrice(price);
		trx.setTime(time);
		
		trxDao.insertTrx(trx);
		return trx.getId();
	}

	@Override
	public List<ProductInfo> fdUserBuyList(int userId) {
		List<Trx> list =  trxDao.selectAllWithUser(userId);
		if(list != null) {
			List<ProductInfo> buyList = new ArrayList<ProductInfo>();
			for(Trx trx : list) {
				Product product = productDao.selectMiniProduct(trx.getProductId());
				ProductInfo productInfo = new ProductInfo(product);
				productInfo.addTrxInfo(trx);
				buyList.add(productInfo);
			}
			return buyList;
		}
		
		return null;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-dao.xml");
		ProductOperator operator = context.getBean("productOperator", ProductOperatorImpl.class);
//		String str = "http://imgsize.ph.126.net/?enlarge=true&imgurl=http://nos.netease.com/edu-image/715A3BB6C88A8969C3DF43CD9615D2EE.jpg?imageView&thumbnail=426y240&quality=100_223x124x1x95.jpg?imageView&thumbnail=426y240&quality=100";
//		String str2 = "我是中文。123123123";
////		byte[] image = str.getBytes();
////		byte[] detail = str2.getBytes("utf-8");
//		System.out.println(operator.changeProduct(52, "语言123123", "athis is num", str, str2, -1034));
		
//		ProductInfo productInfo = operator.findProduct(52);
//		if(productInfo != null) {
//			String image = productInfo.getImage();
//			String detail = productInfo.getDetail();
//			System.out.println(productInfo.getId() + ":" + productInfo.getTitle() + ":" + productInfo.getPrice());
//			System.out.println(image);		
//			System.out.println(detail);
//			System.out.println(productInfo.getSummary());
//		}else {
//			System.out.println("no");
//		}

//		ProductInfo productInfo = operator.findMoreFilds(24);
//		if (productInfo != null) {
//			String image = productInfo.getImage();
//			String detail = productInfo.getDetail();
//			System.out.println(productInfo.getId() + ":" + productInfo.getTitle() + ":" + productInfo.getPrice());
//			System.out.println(image);
//			System.out.println(detail);
//			System.out.println(productInfo.getBuyPrice());
//			System.out.println(productInfo.getBuyTime());
//			System.out.println(productInfo.getIsBuy());
//			System.out.println(productInfo.getIsSell());
//		} else {
//			System.out.println("no");
//		}
		
//		List<ProductInfo> productList = operator.findMiniProducts();
//		for(ProductInfo product : productList) {
//			System.out.println(product.getTitle());
//			System.out.println(product.getPrice());
//			System.out.println(product.getImage());
//			System.out.println(product.getBuyPrice());
//			System.out.println(product.getBuyTime());
//			System.out.println(product.getIsBuy());
//			System.out.println(product.getIsSell());
//		}
		
//		System.out.println(operator.buy(51, 0, 22233322, System.currentTimeMillis()));
		
		List<ProductInfo> productInfoList = operator.fdUserBuyList(0);
		
		for(ProductInfo info : productInfoList) {
			System.out.println(info.getTitle());
			System.out.println(info.getImage());
			System.out.println(info.getBuyPrice());
			System.out.println(info.getBuyTime());
		}
		
		((ConfigurableApplicationContext)context).close();
	}
	
}

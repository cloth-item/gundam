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
import com.lighte.course.meta.Trx;
import com.lighte.course.service.ProductOperator;

@Component("productOperator")
public class ProductOperatorImpl implements ProductOperator {

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private TrxDao trxDao;
	
	@Override
	public int createProduct(String title, String summary, String image, String detail, int price) {
		Product product = new Product();
		product.setTitle(title);
		product.setSummary(summary);
		product.setImage(image);
		product.setDetail(detail);
		product.setPrice(price);
		
		productDao.insertProduct(product);
		product = decodingBlob(product);
		return product.getId();
	}
	
	@Override
	public Product findProduct(int id) {
		Product product = productDao.selectProduct(id);
		if(product != null) { 
			product = decodingBlob(product);
			return product;
		}
		
		return null;
	}
	
	@Override
	public Product findMoreFilds(int id) {
		Product product = productDao.selectProduct(id);
		if(product != null) { 
			product = decodingBlob(product);
			Trx trx = trxDao.selectWithProduct(id);
			if(trx != null) {
				product.getTrxInfo(trx);
			}
			return product;
		}
		
		return null;
	}

	@Override
	public List<Product> findMiniProducts() {
		List<Product> productList = productDao.selectMiniProducts();
		if(productList != null) {
			for(int i=0; i<productList.size(); i++) {
				Product product = productList.get(i);
				product = decodingBlob(product);
				Trx trx = trxDao.selectWithProduct(product.getId());
				if(trx != null) {
					product.getTrxInfo(trx);
				}
			}
			return productList;
		}
		
		return null;
	}
	
	@Override
	public int changeProduct(int id, String title, String summary, String image, String detail, int price) {
		Product product = new Product();
		product.setId(id);
		product.setTitle(title);
		product.setSummary(summary);
		product.setImage(image);
		product.setDetail(detail);
		product.setPrice(price);
		
		int result = productDao.update(product);
		product = decodingBlob(product);
		return result;
	}

	@Override
	public boolean deleteProduct(int id) {
		return productDao.delete(id);
	}
	
	@Override
	public int buy(int productId, int userId, int price, long time) {
		Trx trx = new Trx();
		trx.setProductId(productId);
		trx.setUserId(userId);
		trx.setPrice(price);
		trx.setTime(time);
		
		trxDao.insertTrx(trx);
		
		return trx.getId();
	}

	@Override
	public List<Product> fdUserBuyList(int userId) {
		List<Trx> list =  trxDao.selectAllWithUser(userId);
		if(list != null) {
			List<Product> buyList = new ArrayList<Product>();
			for(Trx trx : list) {
				Product product = productDao.selectMiniProduct(trx.getProductId());
				product = decodingBlob(product);
				product.getTrxInfo(trx);
				buyList.add(product);
			}
			return buyList;
		}
		
		return null;
	}
	
	/**
	 * 解决从数据库中读取blob(转化成String)的乱码问题
	 * @param p
	 * @return
	 */
	private Product decodingBlob(Product p) {
		try {
			if(p != null) {
				String detail = p.getDetail();
				if(detail != null) {
					p.setDetail(new String(detail.getBytes("ISO-8859-1"), "utf-8"));
				}
				String image = p.getImage();
				if(image != null) {
					p.setImage(new String(image.getBytes("ISO-8859-1"), "utf-8"));
				}
			}
		} catch (UnsupportedEncodingException e) {
			// ignore
			System.out.println(e.getMessage());
		}
		return p;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-dao.xml");
		ProductOperator operator = context.getBean("productOperator", ProductOperatorImpl.class);
//		String str = "http://imgsize.ph.126.net/?enlarge=true&imgurl=http://nos.netease.com/edu-image/715A3BB6C88A8969C3DF43CD9615D2EE.jpg?imageView&thumbnail=426y240&quality=100_223x124x1x95.jpg?imageView&thumbnail=426y240&quality=100";
//		String str2 = "我是中文。";
////		byte[] image = str.getBytes();
////		byte[] detail = str2.getBytes("utf-8");
//		System.out.println(operator.createProduct("语言", "athis is num", str, str2, 10));
		
		Product product = operator.findProduct(47);
		if(product != null) {
			String image = product.getImage();
			byte[] bytes = null;
			image = new String(bytes);
			String detail = product.getDetail();
			System.out.println(product.getId() + ":" + product.getTitle() + ":" + product.getPrice());
			System.out.println(image);		
			System.out.println(detail);
			System.out.println(product.getSummary());
		}else {
			System.out.println("no");
		}

//		ProdToView product = operator.fdProdMoreFilds(40);
//		if (product != null) {
//			String image = new String(product.getImage());
//			String detail = new String(product.getDetail());
//			System.out.println(product.getId() + ":" + product.getTitle() + ":" + product.getPrice());
//			System.out.println(image);
//			System.out.println(detail);
//			System.out.println(product.getBuyPrice());
//			System.out.println(product.getBuyTime());
//			System.out.println(product.getIsBuy());
//			System.out.println(product.getIsSell());
//		} else {
//			System.out.println("no");
//		}
		
//		List<ProdToView> productList = operator.findMiniProducts();
//		for(ProdToView product : productList) {
//			System.out.println(product.getTitle());
//			System.out.println(product.getSummary());
//			System.out.println(product.getPrice());
//			System.out.println(product.getDetail());
//			System.out.println(product.getBuyPrice());
//			System.out.println(product.getBuyTime());
//			System.out.println(product.isBuy());
//			System.out.println(product.isSell());
//		}
		
		((ConfigurableApplicationContext)context).close();
	}
	
}

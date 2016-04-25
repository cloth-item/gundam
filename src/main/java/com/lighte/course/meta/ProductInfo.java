package com.lighte.course.meta;

import java.io.UnsupportedEncodingException;

/**
 * 按产品封装相关数据
 * @author lighte
 *
 */
public class ProductInfo {
	
	private int id;
	private String title;
	private String summary;
	private String image;
	private String detail;
	private float price;
	private boolean isBuy;
	private boolean isSell;
	private float buyPrice;
	private long buyTime;
	
	public ProductInfo(Product product) {
		try {
			this.id = product.getId();
			this.title = product.getTitle();
			this.summary = product.getSummary();
			byte[] bytes = product.getImage();
			if(bytes != null){
				this.image = new String(bytes, "UTF-8");
				bytes = product.getDetail();
				if(bytes != null) {
					this.detail = new String(bytes, "UTF-8");
				}
			}
			long longPrice = product.getPrice();
			if(longPrice != 0) {
				this.price = (float) (longPrice/100.0);
			}else {
				this.price = 0;
			}
		} catch (UnsupportedEncodingException e) {
			// ignore
			System.out.println(e.getMessage());
		}
	}
	
	public void addTrxInfo(Trx trx) {
		this.isBuy = true;
		this.isSell = true;
		this.buyPrice = (float) (trx.getPrice()/100.0);
		this.buyTime = trx.getTime();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public boolean getIsBuy() {
		return isBuy;
	}

	public void setIsBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}

	public boolean getIsSell() {
		return isSell;
	}

	public void setIsSell(boolean isSell) {
		this.isSell = isSell;
	}

	public float getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(float buyPrice) {
		this.buyPrice = buyPrice;
	}

	public long getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(long buyTime) {
		this.buyTime = buyTime;
	}
	
}

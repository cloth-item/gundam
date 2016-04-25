package com.lighte.course.meta;

/**
 * 产品数据
 * @author lighte
 *
 */
public class Product {
	
	private int id;
	private String title;
	private String summary;
	private byte[] image;
	private byte[] detail;
	private long price;
	
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
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public byte[] getDetail() {
		return detail;
	}
	public void setDetail(byte[] detail) {
		this.detail = detail;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
}

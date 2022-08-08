package it.polimi.tiw.projects.beans;

import java.util.ArrayList;

public class Product {

	private int productCode;
	
	private String productName;
	
	private String productImage;
	
	private ArrayList<Option> availableOptions;
	
	
	public Product() {
		super();
	}

	public Product(int code, String name, String image) {
		this.productCode=code;
		this.productName=name;
		this.productImage=image;
	}
	
	
	public int getProductCode() {
		return productCode;
	}

	public void setProductCode(int productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}




	public ArrayList<Option> getAvailableOptions() {
		return availableOptions;
	}




	public void setAvailableOptions(ArrayList<Option> availableOptions) {
		this.availableOptions = availableOptions;
	}
	
	
}

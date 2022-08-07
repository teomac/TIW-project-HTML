package it.polimi.tiw.projects.beans;

public class Quote {

	private int quoteID;
	private String clientUsername;
	private String employeeUsername;
	private String productName;
	private double price;
	
	
	public Quote() {
		clientUsername=null;
		employeeUsername=null;
		productName=null;
	}
	
	
	
	public String getClientUsername() {
		return clientUsername;
	}
	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	}
	public String getEmployeeUsername() {
		return employeeUsername;
	}
	public void setEmployeeUsername(String employeeUsername) {
		this.employeeUsername = employeeUsername;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuoteID() {
		return quoteID;
	}

	public void setQuoteID(int quoteID) {
		this.quoteID = quoteID;
	}
	
	
}

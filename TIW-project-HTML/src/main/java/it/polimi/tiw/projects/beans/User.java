package it.polimi.tiw.projects.beans;

public class User {
	
	private String username;
	private String name;
	private String surname;
	private String password;
	private Boolean employee;
	private String email;
	
	

	
	
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname){
		this.surname = surname;
	}
	
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public void setEmployee(Boolean b) {
		employee=b;
	}
	
	public Boolean getEmployee() {
		return employee;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

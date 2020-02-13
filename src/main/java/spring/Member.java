package spring;

import java.sql.Timestamp;
import java.util.Date;

public class Member {
	private long id;
	private String email;
	private String password;
	private String name;
	private Date regTime;
	
	public Member(String email, String password, String name, Date regTime) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.regTime = regTime;
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getRegTime() {
		return regTime;
	}
	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	public void changePassword(String oldPw, String newPw){
		if(!password.equals(oldPw))
			throw new IdPasswordNotMatchingException();
		this.password = newPw;
	}
}

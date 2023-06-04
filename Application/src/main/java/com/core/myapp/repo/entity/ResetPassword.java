package com.core.myapp.repo.entity;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SQLDelete(sql = "update ResetPassword set STATUS = false where id=?")
public class ResetPassword {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String token;
	private Date expirationTime;
	private boolean status = Boolean.TRUE; 
	private static final int EXPIRATION_TIME = 15;

	public ResetPassword() {
		super();
		// TODO Auto-generated constructor stub
	}
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserDetail user;

	public ResetPassword(String token, UserDetail user) {
		super();
		this.token = token;
		this.user = user;
		this.expirationTime = this.getTokenExpirationTime();
	}

	public ResetPassword(String token) {
		super();
		this.token = token;
		this.expirationTime = this.getTokenExpirationTime();
	}
	

	public Date getTokenExpirationTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
		return new Date(calendar.getTime().getTime());
	}
	

}

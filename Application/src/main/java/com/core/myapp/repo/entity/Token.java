package com.core.myapp.repo.entity;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.descriptor.java.BooleanJavaType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SQLDelete(sql = "update Token set STATUS = false where id=?")
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String token;
	private Date expirationTime;
	private boolean status = Boolean.TRUE; 
	private static final int EXPIRATION_TIME = 15;

	public Token() {
		super();
		// TODO Auto-generated constructor stub
	}
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserDetail user;

	public Token(String token, UserDetail user) {
		super();
		this.token = token;
		this.user = user;
		this.expirationTime = this.getTokenExpirationTime();
	}

	public Token(String token) {
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

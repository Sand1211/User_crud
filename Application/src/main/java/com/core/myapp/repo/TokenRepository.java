package com.core.myapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.core.myapp.repo.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

	Token findByToken(String token);

	@Modifying
	@Query(value = "update Token set status= false where token=:token")
	public void updateStatus(@Param("token") String token);

}

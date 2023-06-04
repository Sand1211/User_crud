
package com.core.myapp.repo;

import java.util.List;

import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.core.myapp.repo.entity.UserDetail;

public interface UserRepository extends JpaRepository<UserDetail, Integer> {

	public boolean existsByEmail(String email);

	public boolean existsByUserId(Integer userId);

	public UserDetail findByEmail(String email);

	public boolean existsByEmailAndStatus(String email, boolean b);

	@Modifying
	@Query(value="update UserDetail set status= true where userId=:userId")
	public void activateById(@Param("userId") Integer userId);


}

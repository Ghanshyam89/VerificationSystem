package com.verificationsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.verificationsys.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query("SELECT MAX(u.userId) FROM User u")
    Integer findMaxUserId();
	
	@Query("SELECT MIN(u.userId) FROM User u")
    Integer findMinUserId();
	
	@Query(value = "SELECT * FROM User u ORDER BY u.user_id ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
	List<User> findUsersWithLimitAndOffset(@Param("limit") int limit, @Param("offset") int offset);
}
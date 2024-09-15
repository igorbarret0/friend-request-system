package com.ms_ma_backend_test.ms_ma.repository;

import com.ms_ma_backend_test.ms_ma.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

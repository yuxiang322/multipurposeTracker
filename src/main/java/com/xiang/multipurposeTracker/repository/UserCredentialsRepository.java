package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.model.UserCredentialsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentialsDTO, String> {

    UserCredentialsDTO findByUsernameAndPassword(String username, String password);
}
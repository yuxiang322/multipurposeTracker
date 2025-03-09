package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.UserCredentials;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, String> {

    UserCredentials findByUsername(String username);
    UserCredentials findByUsernameAndPassword(String username, String password);
    UserCredentials findByUserUID(String userUID);
    Boolean existsByUserUID(String userUID);
}
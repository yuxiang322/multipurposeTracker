package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.Template;
import com.xiang.multipurposeTracker.entities.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDetailsRepository extends JpaRepository<UserDetails, String> {
    UserDetails findByUserUID(String userUID);
}

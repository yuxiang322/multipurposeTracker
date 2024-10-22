package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, String> {
    UserDetails findByUserUID(String userUID);
}

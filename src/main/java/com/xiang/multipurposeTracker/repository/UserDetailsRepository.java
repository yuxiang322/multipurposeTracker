package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.model.UserDetailsDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetailsDTO, String> {

}

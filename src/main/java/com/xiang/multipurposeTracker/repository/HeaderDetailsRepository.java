package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.HeaderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeaderDetailsRepository extends JpaRepository<HeaderDetails, Integer> {
    List<HeaderDetails> findHeaderDetailsByTableID(int tableID);
}


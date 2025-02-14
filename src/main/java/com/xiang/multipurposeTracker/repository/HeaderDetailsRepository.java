package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.HeaderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HeaderDetailsRepository extends JpaRepository<HeaderDetails, Integer> {
    List<HeaderDetails> findHeaderDetailsByTableID(int tableID);

    @Modifying
    @Query("DELETE FROM HeaderDetails h WHERE h.tableID = :tableID")
    void deleteByTableID(@Param("tableID") int tableID);
}


package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.TableDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TableDetailsRepository extends JpaRepository<TableDetails, Integer> {
        TableDetails findTableDetailsByTableID(int tableID);

        @Modifying
        @Query("DELETE FROM TableDetails t WHERE t.tableID = :tableID")
        void deleteByTableID(@Param("tableID") int tableID);
}

package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.DTO.TableDetailsDTO;
import com.xiang.multipurposeTracker.entities.TableDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableDetailsRepository extends JpaRepository<TableDetails, Integer> {
        TableDetails findTableDetailsByTableID(int tableID);
}

package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.ShareTable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ShareTableRepository extends JpaRepository<ShareTable, String> {
    ShareTable findByTemplateID(int templateId);

    ShareTable findBySharingCode(String shareCode);
}


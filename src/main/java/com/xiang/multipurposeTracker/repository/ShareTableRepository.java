package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.ShareTable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareTableRepository extends JpaRepository<ShareTable, String> {
    ShareTable findByTemplateID(int templateId);

    ShareTable findBySharingCode(String shareCode);

    @Modifying
    @Query("DELETE FROM ShareTable s WHERE s.templateID = :templateID")
    void deleteByTemplateID(@Param("templateID") int templateID);
}


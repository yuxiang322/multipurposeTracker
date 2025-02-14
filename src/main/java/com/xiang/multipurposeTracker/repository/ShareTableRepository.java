package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.ShareTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ShareTableRepository extends JpaRepository<ShareTable, String>, CustomShareInfoRepository {
    ShareTable findByTemplateId(int templateId);

    @Modifying
    @Transactional
    @Query("UPDATE ShareTable s SET s.TemplateDetails = :shareInfoJson WHERE s.templateID = :templateId ")
    int updateShareInfoJson(@Param("TemplateID") int templateId, @Param("shareInfoJson") String shareInfoJson);
}


package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportStatusRepository extends JpaRepository<ReportStatus, Integer> {

    ReportStatus findByNotificationID(int notificationID);

    Optional<ReportStatus> findByReportID(int reportID);

    void deleteByNotificationID(int notificationID);
}

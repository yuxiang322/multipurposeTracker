package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.ReportStatusDTO;
import com.xiang.multipurposeTracker.entities.ReportStatus;
import com.xiang.multipurposeTracker.repository.ReportStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ReportStatusService {

    @Autowired
    private ReportStatusRepository reportStatusRepository;
    private static final Logger logger = LoggerFactory.getLogger(ReportStatusService.class);

    public ReportStatusDTO getReportStatus(int notificationID) {
        try {
            ReportStatusDTO responseReport = new ReportStatusDTO();

            ReportStatus report = reportStatusRepository.findByNotificationID(notificationID);

            if (report != null) {
                responseReport.setReportID(report.getReportID());
                responseReport.setReportFlag(report.getReportFlag());
                responseReport.setRepeatStartDate(report.getRepeatStartDate() != null && !report.getRepeatStartDate().toString().isEmpty() ? report.getRepeatStartDate().toString() : null);
                responseReport.setRepeatStartTime(report.getRepeatStartTime() != null && !report.getRepeatStartTime().toString().isEmpty() ? report.getRepeatStartTime().toString() : null);
                responseReport.setRepeatIntervalType(report.getRepeatIntervalType());
                responseReport.setRepeatInterval(report.getRepeatInterval());
                return responseReport;
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // updating
    @Transactional
    public Boolean updateReport(ReportStatusDTO reportStatusUpdate) {
        try {
            AtomicBoolean isUpdated = new AtomicBoolean(false);

            Optional<ReportStatus> existingReport = reportStatusRepository.findByReportID(reportStatusUpdate.getReportID());

            existingReport.ifPresent(report -> {
                logger.info("Report Exist....");
                report.setReportFlag(reportStatusUpdate.getReportFlag());
                report.setRepeatInterval(reportStatusUpdate.getRepeatInterval());
                report.setRepeatIntervalType(reportStatusUpdate.getRepeatIntervalType());
                report.setRepeatStartDate(reportStatusUpdate.getRepeatStartDate() != null ? LocalDate.parse(reportStatusUpdate.getRepeatStartDate()) : null);
                report.setRepeatStartTime(reportStatusUpdate.getRepeatStartDate() != null ? LocalTime.parse(reportStatusUpdate.getRepeatStartTime()) : null);

                reportStatusRepository.save(report);
                isUpdated.set(true);
            });

            return isUpdated.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

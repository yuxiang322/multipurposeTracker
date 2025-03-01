package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.ReportStatusDTO;
import com.xiang.multipurposeTracker.entities.ReportStatus;
import com.xiang.multipurposeTracker.repository.ReportStatusRepository;
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

    public ReportStatusDTO getReportStatus(int notificationID) {
        try {
            ReportStatusDTO responseReport = new ReportStatusDTO();

            ReportStatus report = reportStatusRepository.findByNotificationID(notificationID);

            if (report != null) {
                responseReport.setReportID(report.getReportID());
                responseReport.setReportFlag(report.getReportFlag());
                responseReport.setRepeatStartDate(report.getRepeatStartDate().toString());
                responseReport.setRepeatStartTime(report.getRepeatStartTime().toString());
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

                report.setReportFlag(reportStatusUpdate.getReportFlag());
                report.setRepeatInterval(reportStatusUpdate.getRepeatInterval());
                report.setRepeatIntervalType(reportStatusUpdate.getRepeatIntervalType());
                report.setRepeatStartDate(LocalDate.parse(reportStatusUpdate.getRepeatStartDate()));
                report.setRepeatStartTime(LocalTime.parse(reportStatusUpdate.getRepeatStartTime()));

                reportStatusRepository.save(report);
                isUpdated.set(true);
            });

            return isUpdated.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

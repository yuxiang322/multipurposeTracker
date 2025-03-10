package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.NotificationDTO;
import com.xiang.multipurposeTracker.DTO.TemplateDTO;
import com.xiang.multipurposeTracker.entities.*;
import com.xiang.multipurposeTracker.repository.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TemplateService {
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private NotificationsRepository notificationsRepository;
    @Autowired
    private ReportStatusRepository reportStatusRepository;
    @Autowired
    private TemplateTablesRepository templateTablesRepository;
    @Autowired
    private TableDetailsRepository tableDetailsRepository;
    @Autowired
    private HeaderDetailsRepository headerDetailsRepository;
    @Autowired
    private ShareTableRepository shareTableRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TemplateService.class);

    public List<TemplateDTO> getTemplatesByUserUID(String userUID) {
        try {

            if (userUID == null) {
                return new ArrayList<>();
            }

            List<Template> templates = templateRepository.findByUserUID(userUID);
            return templates.stream()
                    .map(template -> new TemplateDTO(
                            template.getTemplateID(),
                            template.getUserUID(),
                            template.getTemplateName(),
                            template.getDateCreated() != null ? template.getDateCreated().format(FORMATTER) : null,
                            template.getTemplateDescription()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve templates.");
        }
    }

    // Create Template
    @Transactional
    public String createTemplate(TemplateDTO templateRequest) {
        try {
            Template template = new Template();
            template.setTemplateName(templateRequest.getTemplateName());
            template.setTemplateDescription(templateRequest.getTemplateDescription());
            template.setUserUID(templateRequest.getUserUID());
            logger.info("TemplateService My user UID empty?: " + template.getUserUID());

            // Convert dateCreated String to LocalDateTime
            LocalDateTime dateCreated = templateRequest.getDateCreatedAsLocalDateTime();
            template.setDateCreated(dateCreated);
            // Save
            Template savedTemplate = templateRepository.save(template);
            String templateIDString = String.valueOf(savedTemplate.getTemplateID());

            // Create Notification
            Notifications notification = new Notifications();
            notification.setTemplateID(savedTemplate.getTemplateID());
            notification.setUserUID(templateRequest.getUserUID());
            notification.setNotificationFlag(false);
            // Save
            Notifications savedNotification = notificationsRepository.save(notification);

            // Create Report Status
            ReportStatus reportStatus = new ReportStatus();
            reportStatus.setNotificationID(savedNotification.getNotificationID());
            reportStatus.setReportFlag(false);
            reportStatusRepository.save(reportStatus);

            return templateIDString;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create template. Please try again later.");
        }
    }

    // Delete Template
    // TABLE DELETION. TemplateTable, HeaderDetails, TableDetails
    @Transactional
    public NotificationDTO deleteTemplate(TemplateDTO templateRequest) {
        try {
            // templateID and userUID
            Template template = templateRepository.findByTemplateIDAndUserUID(
                            templateRequest.getTemplateID(), templateRequest.getUserUID())
                    .orElseThrow(() -> new RuntimeException("Template not found with ID: "
                            + templateRequest.getTemplateID() + " and User ID: " + templateRequest.getUserUID()));

            // Find notification
            Notifications notification = notificationsRepository.findNotificationByTemplateID(template.getTemplateID());

            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setNotificationID(notificationDTO.getNotificationID());
            notificationDTO.setTemplateID(notification.getTemplateID());
            notificationDTO.setSmsFlag(notification.getSmsFlag() != null ? notification.getSmsFlag() : null);
            notificationDTO.setWhatsAppFlag(notification.getWhatsAppFlag() != null ? notification.getWhatsAppFlag() : null);
            notificationDTO.setNotificationFlag(notification.getNotificationFlag() != null ? notification.getNotificationFlag() : null);
            notificationDTO.setRepeatDays(notification.getRepeatDays() != null ? notification.getRepeatDays() : null);
            notificationDTO.setRepeatStartDate(notification.getRepeatStartDate() != null ? notification.getRepeatStartDate().toString() : null);
            notificationDTO.setRepeatStartTime(notification.getRepeatStartTime() != null ? notification.getRepeatStartTime().toString() : null);

            // Delete RepeatStatus
            reportStatusRepository.deleteByNotificationID(notification.getNotificationID());
            // Delete notification
            notificationsRepository.delete(notification);

            // Delete Share
            shareTableRepository.deleteByTemplateID(template.getTemplateID());

            List<TemplateTables> templateTablesList = templateTablesRepository.findTemplateTablesByTemplateID(templateRequest.getTemplateID());

            for(TemplateTables currentTemplateTable : templateTablesList){
                headerDetailsRepository.deleteByTableID(currentTemplateTable.getTableID());
                tableDetailsRepository.deleteByTableID(currentTemplateTable.getTableID());
            }
            // Delete Template Table
            templateTablesRepository.deleteAllInBatch(templateTablesList);
            // Delete template
            templateRepository.delete(template);

            return notificationDTO;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete template. Please try again later.");
        }
    }

    // Save template Details
    @Transactional
    public String saveTemplateDetails(TemplateDTO saveTemplateDetailsDTO){
        try{

            Template templateToUpdate = templateRepository.findByTemplateID(saveTemplateDetailsDTO.getTemplateID());

            templateToUpdate.setTemplateName(saveTemplateDetailsDTO.getTemplateName());
            templateToUpdate.setTemplateDescription(saveTemplateDetailsDTO.getTemplateDescription());

            templateRepository.save(templateToUpdate);

            return "Template details saved.";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

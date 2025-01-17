package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.NotificationDTO;
import com.xiang.multipurposeTracker.DTO.TemplateDTO;
import com.xiang.multipurposeTracker.entities.*;
import com.xiang.multipurposeTracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TemplateService {
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private NotificationsRepository notificationsRepository;
    @Autowired
    private RepeatStatusRepository repeatStatusRepository;
    @Autowired
    private TemplateTablesRepository templateTablesRepository;
    @Autowired
    private TableDetailsRepository tableDetailsRepository;
    @Autowired
    private HeaderDetailsRepository headerDetailsRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public List<TemplateDTO> getTemplatesByUserUID(String userUID) {
        try {
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

            // Convert dateCreated String to LocalDateTime
            LocalDateTime dateCreated = templateRequest.getDateCreatedAsLocalDateTime();
            template.setDateCreated(dateCreated);
            // Save
            Template savedTemplate = templateRepository.save(template);

            // Create Notification
            Notifications notification = new Notifications();
            notification.setTemplateID(savedTemplate.getTemplateID());
            notification.setUserUID(templateRequest.getUserUID());
            notification.setNotificationFlag(false);
            // Save
            Notifications savedNotification = notificationsRepository.save(notification);

            // Create Repeat Status
            RepeatStatus repeatStatus = new RepeatStatus();
            repeatStatus.setNotificationID(savedNotification.getNotificationID());
            repeatStatusRepository.save(repeatStatus);

            return "Template created successfully";
        } catch (Exception e) {
            throw new RuntimeException("Failed to create template. Please try again later.");
        }
    }

    // Delete Template
    // TABLE DELETION. TemplateTable, HeaderDetails, TableDetails
    @Transactional
    public String deleteTemplate(TemplateDTO templateRequest) {
        try {
            // templateID and userUID
            Template template = templateRepository.findByTemplateIDAndUserUID(
                            templateRequest.getTemplateID(), templateRequest.getUserUID())
                    .orElseThrow(() -> new RuntimeException("Template not found with ID: "
                            + templateRequest.getTemplateID() + " and User ID: " + templateRequest.getUserUID()));

            // Find notification
            Notifications notification = notificationsRepository.findNotificationByTemplateID(template.getTemplateID());

            if (notification != null) {
                // Delete RepeatStatus
                repeatStatusRepository.deleteByNotificationID(notification.getNotificationID());

                // Delete notification
                notificationsRepository.delete(notification);
            }

            List<TemplateTables> templateTablesList = templateTablesRepository.findTemplateTablesByTemplateID(templateRequest.getTemplateID());

            for(TemplateTables currentTemplateTable : templateTablesList){
                headerDetailsRepository.deleteByTableID(currentTemplateTable.getTableID());
                tableDetailsRepository.deleteByTableID(currentTemplateTable.getTableID());
            }
            // Delete Template Table
            templateTablesRepository.deleteAllInBatch(templateTablesList);
            // Delete template
            templateRepository.delete(template);

            return "Template and related data deleted successfully";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete template. Please try again later.");
        }
    }

}

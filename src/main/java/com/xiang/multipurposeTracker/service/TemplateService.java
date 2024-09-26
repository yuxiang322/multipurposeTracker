package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.TemplateDTO;
import com.xiang.multipurposeTracker.entities.Template;
import com.xiang.multipurposeTracker.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TemplateService {
    @Autowired
    private TemplateRepository templateRepository;
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
            System.err.println("Error fetching templates: " + e.getMessage()); // test
            throw new RuntimeException("Failed to retrieve templates.");
        }
    }

    // Create Template
    public String createTemplate(TemplateDTO templateRequest) {
        try {
            Template template = new Template();
            template.setTemplateName(templateRequest.getTemplateName());
            template.setTemplateDescription(templateRequest.getTemplateDescription());
            template.setUserUID(templateRequest.getUserUID());

            // Convert dateCreated String to LocalDateTime before setting it
            LocalDateTime dateCreated = templateRequest.getDateCreatedAsLocalDateTime();
            template.setDateCreated(dateCreated);

            // Save the template
            templateRepository.save(template);

            return "Template created successfully";
        } catch (Exception e) {
            System.err.println("Error saving template: " + e.getMessage()); // Log error
            throw new RuntimeException("Failed to create template. Please try again later.");
        }
    }

    // Delete Template
    public String deleteTemplate(TemplateDTO templateRequest) {

        try {
            // Fetch the template by both templateID and userUID
            Template template = templateRepository.findByTemplateIDAndUserUID(
                            templateRequest.getTemplateID(), templateRequest.getUserUID()).orElseThrow(() -> new RuntimeException("Template not found with ID: "
                            + templateRequest.getTemplateID() + " and User ID: " + templateRequest.getUserUID()));

            // Delete the template
            templateRepository.delete(template);

            return "Template deleted successfully";
        } catch (Exception e) {
            System.err.println("Error deleting template: " + e.getMessage()); // test
            throw new RuntimeException("Failed to delete template. Please try again later.");
        }
    }
}

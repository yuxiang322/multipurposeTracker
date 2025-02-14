package com.xiang.multipurposeTracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiang.multipurposeTracker.DTO.CustomShareInfoDTO;
import com.xiang.multipurposeTracker.DTO.ShareTableDTO;
import com.xiang.multipurposeTracker.entities.ShareTable;
import com.xiang.multipurposeTracker.repository.CustomShareInfoRepository;
import com.xiang.multipurposeTracker.repository.ShareTableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ShareTableInformationService {

    @Autowired
    private ShareTableRepository shareTableRepository;

    @Autowired
    private CustomShareInfoRepository customShareInfoRepository;

    private static final Logger logger = LoggerFactory.getLogger(ShareTableInformationService.class);

    @Transactional
    public ShareTableDTO processShareInformation(Integer templateId) {

        try {
            ShareTable existingShareInfo = shareTableRepository.findByTemplateID(templateId);

            ShareTableDTO returnShareInfoExist = new ShareTableDTO();

            if (existingShareInfo != null) {
                returnShareInfoExist.setSharingCode(existingShareInfo.getSharingCode());
                logger.info("Getting existing sharing code.");
            } else {

                String generatedShareCode = generateShareCode(templateId);
                LocalDateTime expirationDate = LocalDateTime.now().plusDays(60);

                ShareTable saveShareInfo = new ShareTable();
                saveShareInfo.setSharingCode(generatedShareCode);
                saveShareInfo.setTemplateID(templateId);
                saveShareInfo.setExpirationDate(expirationDate);

                shareTableRepository.save(saveShareInfo);

                returnShareInfoExist.setSharingCode(generatedShareCode);
                returnShareInfoExist.setTemplateID(templateId);
                returnShareInfoExist.setExpirationDate(expirationDate);
                logger.info("Creating new sharing code.");
            }

            CompletableFuture.runAsync(() -> {
                logger.info("Runasync");
                try {
                    updateShareTable(templateId);
                } catch (Exception e) {
                    logger.error("Error in async task: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }).whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Async task failed with exception: " + ex.getMessage(), ex);
                } else {
                    logger.info("Async task completed successfully.");
                }
            });

            logger.info("Returned ShareCode: " + returnShareInfoExist.getSharingCode());
            return returnShareInfoExist;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateShareCode(int templateId) {
        return UUID.randomUUID().toString() + String.valueOf(templateId);
    }

    private void updateShareTable(int templateId) {

        try {
            CustomShareInfoDTO shareInfo = getShareInfoDetails(templateId);

            if (shareInfo == null) {
                logger.info("Error: Share info is null for templateId: " + templateId);
                return;
            }
            // json
            ObjectMapper shareInfoMapper = new ObjectMapper();
            String shareInfoJson = shareInfoMapper.writeValueAsString(shareInfo);
            logger.info("Generated json: {}", shareInfoJson);

            ShareTable checkExistingShareInfo = shareTableRepository.findByTemplateID(templateId);

            if(checkExistingShareInfo != null){

                checkExistingShareInfo.setTemplateDetails(shareInfoJson);

                shareTableRepository.save(checkExistingShareInfo);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("Exception?" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public CustomShareInfoDTO getShareInfoDetails(int templateId) {
        return customShareInfoRepository.getShareInfoDetails(templateId);
    }

    // import
    // Custom DTO for tempalteDetails
    // Template: 1
    // Array of Table
    // Table 1:
    // Array of HeaderDetails
    // TableDetails
    // Table 2:
    // Array of HeaderDetails
    // TableDetails String of jdata.

    // Custom repo interface extends in sharetablerepo
    // public class implement custom repo and override method of custom repo to return the needed data
    // call the method here using sharetablerepo.

}

package com.xiang.multipurposeTracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiang.multipurposeTracker.DTO.CustomShareInfoDTO;
import com.xiang.multipurposeTracker.DTO.ShareTableDTO;
import com.xiang.multipurposeTracker.entities.ShareTable;
import com.xiang.multipurposeTracker.repository.CustomShareInfoRepository;
import com.xiang.multipurposeTracker.repository.ShareTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ShareTableInformationService {

    @Autowired
    private ShareTableRepository shareTableRepository;

    @Autowired
    private CustomShareInfoRepository customShareInfoRepository;

    public ShareTableDTO processShareInformation(Integer templateId) {

        try {
            ShareTable existingShareInfo = shareTableRepository.findByTemplateID(templateId);

            ShareTableDTO returnShareInfoExist = null;

            if (existingShareInfo != null) {
                returnShareInfoExist = new ShareTableDTO();
                returnShareInfoExist.setSharingCode(existingShareInfo.getSharingCode());
            } else {

                String generatedShareCode = generateShareCode(templateId);
                LocalDateTime expirationDate = LocalDateTime.now().plusDays(60);

                ShareTable saveShareInfo = new ShareTable();
                saveShareInfo.setSharingCode(generatedShareCode);
                saveShareInfo.setTemplateID(templateId);
                saveShareInfo.setExpirationDate(expirationDate);

                shareTableRepository.save(saveShareInfo);

                returnShareInfoExist = new ShareTableDTO(generatedShareCode, templateId, expirationDate);
            }

            CompletableFuture.runAsync(() -> {
                try {
                    updateShareTable(templateId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

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
                System.err.println("Error: Share info is null for templateId: " + templateId);
                return;
            }

            // json
            ObjectMapper shareInfoMapper = new ObjectMapper();
            String shareInfoJson = shareInfoMapper.writeValueAsString(shareInfo);
            System.out.println("Generated json: " + shareInfoJson);

            ShareTable checkExistingShareInfo = shareTableRepository.findByTemplateID(templateId);

            if(checkExistingShareInfo != null){
                ShareTable updateShare = new ShareTable();

                updateShare.setSharingCode(checkExistingShareInfo.getSharingCode());
                updateShare.setTemplateDetails(shareInfoJson);
                shareTableRepository.save(updateShare);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public CustomShareInfoDTO getShareInfoDetails(int templateId) {
        return customShareInfoRepository.getShareInfoDetails(templateId);
    }


}

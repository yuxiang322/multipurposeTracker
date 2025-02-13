package com.xiang.multipurposeTracker.controller;

import com.xiang.multipurposeTracker.DTO.ShareTableDTO;
import com.xiang.multipurposeTracker.DTO.TemplateDTO;
import com.xiang.multipurposeTracker.service.ShareTableInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/shareTable")
public class ShareTableInformationController {

    @Autowired
    private ShareTableInformationService shareTableInformationService;

    // get share info
    @PostMapping("/getShareInformation")
    public ResponseEntity<ShareTableDTO> getShareInfo(@RequestBody TemplateDTO templateShared) {
        try {
            ShareTableDTO shareInfo = shareTableInformationService.processShareInformation(templateShared.getTemplateID());

            return ResponseEntity.ok(shareInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

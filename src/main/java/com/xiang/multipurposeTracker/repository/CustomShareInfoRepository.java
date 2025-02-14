package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.DTO.CustomShareInfoDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomShareInfoRepository {

    CustomShareInfoDTO getShareInfoDetails(int templateId);
}

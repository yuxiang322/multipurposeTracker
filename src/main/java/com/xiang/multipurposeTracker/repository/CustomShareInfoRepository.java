package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.DTO.CustomShareInfoDTO;

public interface CustomShareInfoRepository {

    CustomShareInfoDTO getShareInfoDetails(int templateId);
}

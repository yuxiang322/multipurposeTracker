package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.repository.ReportStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportStatusService {

    @Autowired
    private ReportStatusRepository reportStatusRepository;

}

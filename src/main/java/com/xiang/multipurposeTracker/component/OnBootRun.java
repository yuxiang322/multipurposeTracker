package com.xiang.multipurposeTracker.component;

import com.xiang.multipurposeTracker.service.EmailSchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class OnBootRun implements ApplicationRunner {

    @Autowired
    private EmailSchedulingService emailSchedulingService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // implement apprunner
        // implement restore email -> emailservice
        //emailSchedulingService.restoreEmailScheduling();
    }

}

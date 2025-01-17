package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.RepeatStatusDTO;
import com.xiang.multipurposeTracker.entities.RepeatStatus;
import com.xiang.multipurposeTracker.repository.RepeatStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class RepeatStatusService {

    @Autowired
    private RepeatStatusRepository repeatStatusRepository;

    public RepeatStatusDTO findRepeatStatusByNotificationID(int notificationID){

        RepeatStatus repeatStatus = repeatStatusRepository.findByNotificationID(notificationID);

        RepeatStatusDTO repeatStatusRetrieved = new RepeatStatusDTO();

        if (repeatStatus != null) {
            repeatStatusRetrieved.setNotificationID(repeatStatus.getNotificationID());
            repeatStatusRetrieved.setRepeatID(repeatStatus.getRepeatID());
            // Set the RepeatInterval
            repeatStatusRetrieved.setRepeatIntervalType(repeatStatus.getRepeatIntervalType());
            repeatStatusRetrieved.setRepeatInterval(repeatStatus.getRepeatInterval());
            System.out.println("Repeat Status TYPE: " + repeatStatus.getRepeatIntervalType());

            // Convert DATE to String
            if (repeatStatus.getRepeatStartDate() != null) {
                LocalDate date = repeatStatus.getRepeatStartDate();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String dateString = date.format(dateFormatter);
                repeatStatusRetrieved.setRepeatStartDate(dateString);
            }
            // Convert RepeatStartTime from TIME to String
            if (repeatStatus.getRepeatStartTime() != null) {
                LocalTime time = repeatStatus.getRepeatStartTime();
                String timeString = time.format(DateTimeFormatter.ISO_LOCAL_TIME);
                repeatStatusRetrieved.setRepeatStartTime(timeString);
            }
        }
        return repeatStatusRetrieved;
    }

    public Boolean updateRepeatStatus(RepeatStatusDTO updateRepeatStatus){
        Optional<RepeatStatus> existingRepeatStatus = repeatStatusRepository.findByRepeatID(updateRepeatStatus.getRepeatID());

        if(existingRepeatStatus.isPresent()) {
            RepeatStatus repeatStatusUpdate = existingRepeatStatus.get();

            repeatStatusUpdate.setNotificationID(updateRepeatStatus.getNotificationID());
            // Convert String to LocalDate
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/M/yyyy");
            LocalDate parsedDate = LocalDate.parse(updateRepeatStatus.getRepeatStartDate(), dateFormatter);
            repeatStatusUpdate.setRepeatStartDate(parsedDate);

            // Convert String to LocalTime
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
            LocalTime parsedTime = LocalTime.parse(updateRepeatStatus.getRepeatStartTime(), timeFormatter);
            repeatStatusUpdate.setRepeatStartTime(parsedTime);

            repeatStatusUpdate.setRepeatIntervalType(updateRepeatStatus.getRepeatIntervalType());
            repeatStatusUpdate.setRepeatInterval(updateRepeatStatus.getRepeatInterval());

            repeatStatusRepository.save(repeatStatusUpdate);
            return true;
        }
        return false;
    }
}

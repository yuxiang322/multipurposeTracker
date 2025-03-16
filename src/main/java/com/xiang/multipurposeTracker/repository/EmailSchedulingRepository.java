package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.DTO.EmailSchedulingDTO;
import com.xiang.multipurposeTracker.entities.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailSchedulingRepository extends JpaRepository<Notifications, Integer> {

    @Query("SELECT new com.example.EmailSchedulingDTO(rs.reportFlag, rs.repeatStartDate, rs.repeatStartTime, rs.repeatInterval, n.userUID) " +
            "FROM Notifications n " +
            "INNER JOIN Report_Status rs ON n.NotificationID = rs.NotificationID")
    List<EmailSchedulingDTO> findAllEmailToBeScheduled();
}


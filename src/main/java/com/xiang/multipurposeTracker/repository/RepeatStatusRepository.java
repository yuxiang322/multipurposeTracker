package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.RepeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepeatStatusRepository extends JpaRepository<RepeatStatus, Integer> {

    RepeatStatus findByNotificationID(int notificationID);

    Optional<RepeatStatus> findByRepeatID(int repeatID);

    void deleteByNotificationID(int notificationID);
}

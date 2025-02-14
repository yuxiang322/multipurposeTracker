package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.TemplateTables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateTablesRepository extends JpaRepository<TemplateTables, Integer> {
    List<TemplateTables> findTemplateTablesByTemplateID(int templateID);

    @Modifying
    @Query("DELETE FROM TemplateTables t WHERE t.tableID = :tableID")
    void deleteByTableID(@Param("tableID") int tableID);
}
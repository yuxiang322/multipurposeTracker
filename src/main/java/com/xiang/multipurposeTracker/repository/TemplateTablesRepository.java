package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.TemplateTables;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateTablesRepository extends JpaRepository<TemplateTables, Integer> {
    List<TemplateTables> findTemplateTablesByTemplateID(int templateID);
}
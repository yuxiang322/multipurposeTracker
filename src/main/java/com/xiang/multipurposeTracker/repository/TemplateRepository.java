package com.xiang.multipurposeTracker.repository;

import com.xiang.multipurposeTracker.entities.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Integer> {

    List<Template> findByUserUID(String userUID);

    Optional<Template> findByTemplateIDAndUserUID(int templateID, String userUID);
}



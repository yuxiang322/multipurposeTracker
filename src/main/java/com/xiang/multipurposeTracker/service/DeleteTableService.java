package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.TemplateTablesDTO;
import com.xiang.multipurposeTracker.repository.HeaderDetailsRepository;
import com.xiang.multipurposeTracker.repository.TableDetailsRepository;
import com.xiang.multipurposeTracker.repository.TemplateTablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteTableService {
    @Autowired
    private TemplateTablesRepository templateTablesRepository;
    @Autowired
    private HeaderDetailsRepository headerDetailsRepository;
    @Autowired
    private TableDetailsRepository tableDetailsRepository;

    public String deleteTables(int [] tableIDs){
        try{
            for(int tableID : tableIDs){
                tableDetailsRepository.deleteByTableID(tableID);
                headerDetailsRepository.deleteByTableID(tableID);
                templateTablesRepository.deleteByTableID(tableID);
            }
            return "Deleted";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

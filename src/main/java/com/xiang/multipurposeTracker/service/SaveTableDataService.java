package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.TableDetailsDTO;
import com.xiang.multipurposeTracker.entities.TableDetails;
import com.xiang.multipurposeTracker.repository.TableDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaveTableDataService {

    @Autowired
    private TableDetailsRepository tableDetailsRepository;

    @Transactional
    public String saveTableData(TableDetailsDTO jsonDataUpdate){
        try {

            TableDetails currentTableRecord = tableDetailsRepository.findTableDetailsByTableID(jsonDataUpdate.getTableID());

            currentTableRecord.setJsonData(jsonDataUpdate.getJsonData());

            tableDetailsRepository.save(currentTableRecord);

            return "Data saved";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

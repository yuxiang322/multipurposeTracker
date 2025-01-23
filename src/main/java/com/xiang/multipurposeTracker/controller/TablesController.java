package com.xiang.multipurposeTracker.controller;

import com.xiang.multipurposeTracker.DTO.RetrieveTableDetailsDTO;
import com.xiang.multipurposeTracker.DTO.TableCreationDTO;
import com.xiang.multipurposeTracker.DTO.TableDetailsDTO;
import com.xiang.multipurposeTracker.DTO.TemplateDTO;
import com.xiang.multipurposeTracker.service.DeleteTableService;
import com.xiang.multipurposeTracker.service.RetrieveTableService;
import com.xiang.multipurposeTracker.service.SaveTableDataService;
import com.xiang.multipurposeTracker.service.TableCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/table")
public class TablesController {

    @Autowired
    private TableCreationService tableCreationService;
    @Autowired
    private RetrieveTableService retrieveTableService;
    @Autowired
    private DeleteTableService deleteTableService;
    @Autowired
    private SaveTableDataService saveTableDataService;

    @PostMapping("/create")
    public ResponseEntity<String> tableCreate(@RequestBody TableCreationDTO tableCreationDTO) {
        try {
            // Service
            String addTableDataResponse = tableCreationService.addTable(tableCreationDTO);

            if (addTableDataResponse.equals("Success")) {
                return ResponseEntity.ok("Table added");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(addTableDataResponse);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create table: " + e.getMessage());
        }
    }

    @PostMapping("/get")
    public ResponseEntity<List<RetrieveTableDetailsDTO>> tableGet(@RequestBody TemplateDTO templateObject){
        try{
            List<RetrieveTableDetailsDTO> tableListRetrieved = retrieveTableService.retrieveListOfTable(templateObject.getTemplateID());

            return ResponseEntity.ok(tableListRetrieved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> tableDelete(@RequestBody List<Integer> tableIDs){
        try{
            String deleteTableResult = deleteTableService.deleteTables(tableIDs);

            return ResponseEntity.ok(deleteTableResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete table" + e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<String> tableDataSave(@RequestBody TableDetailsDTO jsonDataUpdate){
        try{
            String saveTableData = saveTableDataService.saveTableData(jsonDataUpdate);

            return ResponseEntity.ok(saveTableData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save data" + e.getMessage());
        }
    }
}

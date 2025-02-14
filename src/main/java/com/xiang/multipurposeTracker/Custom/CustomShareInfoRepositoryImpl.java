package com.xiang.multipurposeTracker.Custom;

import com.xiang.multipurposeTracker.DTO.CustomShareInfoDTO;
import com.xiang.multipurposeTracker.DTO.HeaderDetailsDTO;
import com.xiang.multipurposeTracker.DTO.TableDetailsDTO;
import com.xiang.multipurposeTracker.DTO.TemplateTablesDTO;
import com.xiang.multipurposeTracker.repository.CustomShareInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class CustomShareInfoRepositoryImpl implements CustomShareInfoRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public CustomShareInfoDTO getShareInfoDetails(int templateId) {

        CustomShareInfoDTO shareInfoReturn = new CustomShareInfoDTO();

        String shareInfoSql = "SELECT t.TemplateName, t.Description, tab.TableID AS tab_TableID, tab.TableName, " +
                "head.TableID AS head_TableID, head.HeaderName, head.HeaderFillColour, head.HeaderTextColour, head.TextBold, " +
                "td.TableID AS td_TableID, td.JsonData FROM Template t " +
                "JOIN Template_Tables tab ON t.TemplateID = tab.TemplateID " +
                "JOIN Header_Details head ON tab.TableID = head.TableID " +
                "JOIN Table_Details td ON tab.TableID = td.TableID " +
                "WHERE t.TemplateID = ?";

        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(shareInfoSql, templateId);

            // add first 2 variable
            if (!result.isEmpty()) {
                Map<String, Object> firstRow = result.get(0);
                shareInfoReturn.setTemplateName(firstRow.get("TemplateName") != null ? firstRow.get("TemplateName").toString() : null);
                shareInfoReturn.setTemplateDescription(firstRow.get("Description") != null ? firstRow.get("Description").toString() : null);
            }

            List<TemplateTablesDTO> templateTablesList = new ArrayList<>();
            List<HeaderDetailsDTO> headerDetailsList = new ArrayList<>();
            List<TableDetailsDTO> tableDetailsList = new ArrayList<>();

            // map
            for (Map<String, Object> row : result) {
                TemplateTablesDTO templateTablesTemp = new TemplateTablesDTO();
                HeaderDetailsDTO headerDetailsTemp = new HeaderDetailsDTO();
                TableDetailsDTO tableDetailsTemp = new TableDetailsDTO();
                // Template Table
                templateTablesTemp.setTableID((Integer) row.get("tab_TableID"));
                templateTablesTemp.setTableName(Objects.toString(row.get("TableName"), null));
                // Header Details
                headerDetailsTemp.setTableID((Integer) row.get("head_TableID"));
                headerDetailsTemp.setHeaderName(Objects.toString(row.get("HeaderName"), null));
                headerDetailsTemp.setHeaderFillColour(Objects.toString(row.get("HeaderFillColour"), null));
                headerDetailsTemp.setHeaderTextColour(Objects.toString(row.get("HeaderTextColour"), null));
                headerDetailsTemp.setTextBold(row.get("TextBold") != null && (boolean) row.get("TextBold"));
                // Table Details
                tableDetailsTemp.setTableID((Integer) row.get("td_TableID"));
                tableDetailsTemp.setJsonData(Objects.toString(row.get("JsonData"), null));

                templateTablesList.add(templateTablesTemp);
                headerDetailsList.add(headerDetailsTemp);
                tableDetailsList.add(tableDetailsTemp);
            }

            shareInfoReturn.setTemplateTables(templateTablesList);
            shareInfoReturn.setHeaderDetails(headerDetailsList);
            shareInfoReturn.setTableDetails(tableDetailsList);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return shareInfoReturn;
    }

}

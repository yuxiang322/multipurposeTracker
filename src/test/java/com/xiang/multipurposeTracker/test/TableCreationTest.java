package com.xiang.multipurposeTracker.test;

import com.xiang.multipurposeTracker.DTO.HeaderDetailsDTO;
import com.xiang.multipurposeTracker.repository.HeaderDetailsRepository;
import com.xiang.multipurposeTracker.repository.TableDetailsRepository;
import com.xiang.multipurposeTracker.service.TableCreationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;


public class TableCreationTest {
    @InjectMocks
    private TableCreationService tableCreationService;
    @Mock
    private TableDetailsRepository tableDetailsRepository;
    @Mock
    private HeaderDetailsRepository headerDetailsRepository;

    private List<HeaderDetailsDTO> headerDetailsDTOList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        headerDetailsDTOList = new ArrayList<>();
        headerDetailsDTOList.add(new HeaderDetailsDTO(1, 1, "Head1", "color1", "colour1", true));
        headerDetailsDTOList.add(new HeaderDetailsDTO(1, 1, "Head2", "color2", "colour2", false));
    }

    @Test
    public void testInsertTableDetails() throws Exception {
        tableCreationService.insertTableDetails(1, headerDetailsDTOList);
    }
}

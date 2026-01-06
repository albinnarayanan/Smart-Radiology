package com.smartradiology.project.payload;

import com.smartradiology.project.model.ImagingSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagingScheduleResponse {
    private List<ImagingScheduleDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private int totalPages;
    private boolean lastPage;
}

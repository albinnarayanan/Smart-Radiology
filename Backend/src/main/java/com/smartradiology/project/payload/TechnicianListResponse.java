package com.smartradiology.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnicianListResponse {
    private List<TechnicianDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private int totalPages;
    private boolean lastPage;

}

package com.smartradiology.project.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorListResponse {
    private List<DoctorDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private int totalPages;
    private boolean lastPage;
}

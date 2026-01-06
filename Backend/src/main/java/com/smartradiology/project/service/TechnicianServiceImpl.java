package com.smartradiology.project.service;

import com.smartradiology.project.exception.APIException;
import com.smartradiology.project.exception.ResourceNotFoundException;
import com.smartradiology.project.model.ImagingSchedule;
import com.smartradiology.project.model.RadiologyRequest;
import com.smartradiology.project.model.Report;
import com.smartradiology.project.model.Technician;
import com.smartradiology.project.payload.*;
import com.smartradiology.project.repository.ImagingScheduleRepository;
import com.smartradiology.project.repository.RadiologyRequestRepository;
import com.smartradiology.project.repository.ReportRepository;
import com.smartradiology.project.repository.TechnicianRepository;
import com.smartradiology.project.security.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class TechnicianServiceImpl implements TechnicianService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ImagingScheduleRepository imagingScheduleRepository;

    @Autowired
    private RadiologyRequestRepository radiologyRequestRepository;
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private TechnicianRepository technicianRepository;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Value("${project.image}")
    private String path;

    @Autowired
    private FileService fileService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public ImagingScheduleResponse getAllRequests(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                              ? Sort.by(sortBy).ascending()
                              : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Technician technician = authUtil.loggedInTechnician();
        Page<ImagingSchedule> pageSchedules = imagingScheduleRepository.findByTechnician(technician, pageDetails);
        if(pageSchedules.isEmpty()){
            throw new APIException("No schedules found");
        }
        List<ImagingSchedule> schedules = pageSchedules.getContent();
        List<ImagingScheduleDTO> scheduleDTOs = schedules.stream()
                .map(req->{
                    return modelMapper.map(req,ImagingScheduleDTO.class);
                }).toList();
        ImagingScheduleResponse imagingScheduleResponse = new ImagingScheduleResponse();
        imagingScheduleResponse.setContent(scheduleDTOs);
        imagingScheduleResponse.setPageNumber(pageSchedules.getNumber());
        imagingScheduleResponse.setPageSize(pageSchedules.getSize());
        imagingScheduleResponse.setTotalElements(pageSchedules.getTotalElements());
        imagingScheduleResponse.setTotalPages(pageSchedules.getTotalPages());
        imagingScheduleResponse.setLastPage(pageSchedules.isLast());

        return imagingScheduleResponse;
    }

    @Override
    @Transactional
    public ReportDTO submitReport(Long imagingScheduleId, ReportDTO reportDTO, MultipartFile image) throws IOException {
        ImagingSchedule imagingSchedule = imagingScheduleRepository.findById(imagingScheduleId)
                .orElseThrow(()->new ResourceNotFoundException("Imaging Schedule","imagingScheduleId", imagingScheduleId));

        RadiologyRequest radiologyRequest = radiologyRequestRepository.findByImagingSchedule(imagingSchedule)
                .orElseThrow(()->new ResourceNotFoundException("Imaging Schedule", "imagingSchedule",imagingScheduleId));
        Report report = radiologyRequest.getReport();
        report.setFindings(reportDTO.getFindings());
        report.setGeneratedAt(LocalDateTime.now());
        report.setRadiologyRequest(radiologyRequest);

        String fileName = fileService.uploadImage(path,image);
        report.setImage(fileName);
        Report savedReport = reportRepository.save(report);

        Long doctorId = radiologyRequest.getDoctor().getDoctorId();
        String pattern = "docRadiologyRequests::"+doctorId+"*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        Long technicianId = authUtil.loggedInTechnician().getTechnicianId();
        String techPattern = "techRadiologyRequests::" + technicianId + "*";
        Set<String> techKeys = redisTemplate.keys(techPattern);
        if (techKeys != null && !techKeys.isEmpty()) {
            redisTemplate.delete(techKeys);
        }

        ReportDTO responseDTO = modelMapper.map(savedReport, ReportDTO.class);
        responseDTO.setImage(constructImageUrl(savedReport.getImage()));

        // detach schedule from technician
         Technician technician = imagingSchedule.getTechnician();
         technician.removeSchedule(imagingSchedule); 
         imagingSchedule.setTechnician(null); // owning side update
         imagingScheduleRepository.save(imagingSchedule);

        return responseDTO;


    }
    public String constructImageUrl(String imageName){
        return imageBaseUrl.endsWith("/")?imageBaseUrl+imageName: imageBaseUrl+"/"+imageName;
    }
}

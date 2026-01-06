package com.smartradiology.project.service;

import com.smartradiology.project.exception.APIException;
import com.smartradiology.project.exception.ResourceNotFoundException;
import com.smartradiology.project.model.*;
import com.smartradiology.project.payload.PatientDTO;
import com.smartradiology.project.payload.PatientListResponse;
import com.smartradiology.project.payload.RadiologyRequestDTO;
import com.smartradiology.project.payload.RadiologyRequestResponse;
import com.smartradiology.project.repository.*;
import com.smartradiology.project.security.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private TechnicianRepository technicianRepository;


    @Autowired
    private RadiologyRequestRepository radiologyRequestRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ImagingScheduleRepository imagingScheduleRepository;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Value("${project.image}")
    private String path;

    private static final String PATIENTS_LIST_CACHE = "patientsList";

    @Override
    @CacheEvict(value = PATIENTS_LIST_CACHE, allEntries = true)
    public PatientDTO addPatient(PatientDTO patientDTO) {
        Optional<Patient> existingPatient = patientRepository.findByContactNumber(patientDTO.getContactNumber());
        if(existingPatient.isPresent()) {
            return modelMapper.map(existingPatient.get(),PatientDTO.class);
        }
        Patient patient = modelMapper.map(patientDTO,Patient.class);
        Patient savedPatient = patientRepository.save(patient);
        return modelMapper.map(savedPatient,PatientDTO.class);
    }

    @Override
    @Transactional
    public RadiologyRequestDTO createRadiologyRequest(Long patientId, String imagingType) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(()->new ResourceNotFoundException("Patient","patientId",patientId));
        RadiologyRequest radiologyRequest = new RadiologyRequest();
        Doctor doctor = authUtil.loggedInDoctor();
        radiologyRequest.setPatient(patient);
        radiologyRequest.setDoctor(doctor);
        radiologyRequest.setPriority(patient.getAge()>60);
        radiologyRequest.setRequestedAt(LocalDateTime.now());
        radiologyRequest.setImagingType(imagingType);

        Report report = new Report();
        report.setImage("default.png");
        report.setFindings("Pending");
        report.setGeneratedAt(LocalDateTime.now());
        radiologyRequest.setReport(report);
        report.setRadiologyRequest(radiologyRequest);

        Technician technician = findTechnicianByImagingType(imagingType);
        ImagingSchedule schedule = new ImagingSchedule();
        schedule.setRadiologyRequest(radiologyRequest);
        schedule.setTechnician(technician);
        schedule.setScheduledTime(LocalDateTime.now());
        radiologyRequest.setImagingSchedule(schedule);


        RadiologyRequest saved = radiologyRequestRepository.save(radiologyRequest);
        doctor.addRadiologyRequest(saved);

        return modelMapper.map(saved,RadiologyRequestDTO.class);
    }

    @Override
    public RadiologyRequestResponse getAllRequests(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Doctor doctor = authUtil.loggedInDoctor();

        Page<RadiologyRequest> pageProducts = radiologyRequestRepository.findByDoctor(doctor,pageDetails);
        List<RadiologyRequest> radiologyRequests = pageProducts.getContent();

        List<RadiologyRequestDTO> radiologyRequestDTOs = radiologyRequests.stream()
                .map(req->{
                    Report report = req.getReport();
                    String imageUrl = (report != null && report.getImage() != null)
                            ? constructImageUrl(report.getImage())
                            : "default.png";

                    RadiologyRequestDTO radiologyRequestDTO =  modelMapper.map(req,RadiologyRequestDTO.class);
                    radiologyRequestDTO.setReportImageUrl(imageUrl);
                    return radiologyRequestDTO;
                }).toList();
        return getRequestResponse(radiologyRequestDTOs, pageProducts);
    }

    @Override
    @Cacheable(value = PATIENTS_LIST_CACHE, key = "{#pageNumber, #pageSize, #sortBy, #sortOrder}")
    public PatientListResponse getAllPatients(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy) {
       Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                             ? Sort.by(sortBy).ascending()
                             : Sort.by(sortBy).descending();
       Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
       Page<Patient> patientPage = patientRepository.findAll(pageDetails);
       List<Patient> patients = patientPage.getContent();
       List<PatientDTO> patientDTOList = patients.stream()
               .map(patient->{
                   return modelMapper.map(patient, PatientDTO.class);
               }).toList();
       PatientListResponse patientListResponse = new PatientListResponse();
       patientListResponse.setContent(patientDTOList);
       patientListResponse.setPageNumber(patientPage.getNumber());
       patientListResponse.setPageSize(patientPage.getSize());
       patientListResponse.setTotalPages(patientPage.getTotalPages());
       patientListResponse.setTotalElements(patientPage.getTotalElements());
       patientListResponse.setLastPage(patientPage.isLast());
        return patientListResponse;
    }

    private static RadiologyRequestResponse getRequestResponse(List<RadiologyRequestDTO> radiologyRequestDTOs, Page<RadiologyRequest> pageProducts) {
        RadiologyRequestResponse radiologyRequestResponse = new RadiologyRequestResponse();
        radiologyRequestResponse.setContent(radiologyRequestDTOs);
        radiologyRequestResponse.setPageNumber(pageProducts.getNumber());
        radiologyRequestResponse.setPageSize(pageProducts.getSize());
        radiologyRequestResponse.setTotalElements(pageProducts.getTotalElements());
        radiologyRequestResponse.setTotalPages(pageProducts.getTotalPages());
        radiologyRequestResponse.setLastPage(pageProducts.isLast());
        return radiologyRequestResponse;
    }



    public Technician findTechnicianByImagingType(String imagingType) {
        return switch (imagingType.toLowerCase()) {
            case "mri" -> technicianRepository.findByUsername("mriTech")
                    .orElseThrow(() -> new UsernameNotFoundException("mriTech"));
            case "ctscan" -> technicianRepository.findByUsername("ctTech")
                    .orElseThrow(() -> new UsernameNotFoundException("ctTech"));
            case "xray" -> technicianRepository.findByUsername("xrayTech")
                    .orElseThrow(() -> new UsernameNotFoundException("xrayTech"));
            case "ultrasound" -> technicianRepository.findByUsername("ultrasoundTech")
                    .orElseThrow(() -> new UsernameNotFoundException("ultrasoundTech"));
            default -> throw new IllegalArgumentException("Invalid imaging type: " + imagingType);
        };
    }
    public String constructImageUrl(String imageName){
        return imageBaseUrl.endsWith("/")?imageBaseUrl+imageName: imageBaseUrl+"/"+imageName;
    }

}

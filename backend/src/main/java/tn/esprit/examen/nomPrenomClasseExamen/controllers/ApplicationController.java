package tn.esprit.examen.nomPrenomClasseExamen.controllers;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Application;
import tn.esprit.examen.nomPrenomClasseExamen.entities.JobOffer;
import tn.esprit.examen.nomPrenomClasseExamen.entities.User;
import tn.esprit.examen.nomPrenomClasseExamen.services.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper; // For parsing JSON to Java object
import org.springframework.http.HttpStatus; // For HTTP status codes
import org.springframework.http.ResponseEntity; // For HTTP responses
import org.springframework.web.bind.annotation.*; // For Spring REST annotations
import org.springframework.web.multipart.MultipartFile; // For handling file uploads
import java.io.IOException; // For exception handling
import java.nio.file.Files; // For file operations
import java.nio.file.Path; // For file paths
import java.nio.file.Paths; // For creating file paths
import tn.esprit.examen.nomPrenomClasseExamen.entities.Application; // Your Application entity
import tn.esprit.examen.nomPrenomClasseExamen.services.ApplicationService; // Your service layer
import tn.esprit.examen.nomPrenomClasseExamen.services.JobOfferService;
import tn.esprit.examen.nomPrenomClasseExamen.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final UserService userService;
    private final JobOfferService jobOfferService;

    public ApplicationController(ApplicationService applicationService,UserService userService,JobOfferService jobOfferService) {
        this.applicationService = applicationService;
        this.jobOfferService= jobOfferService;
        this.userService=userService;
    }

    @GetMapping("/GetApplications")
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/GetApplicationById/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        return applicationService.getApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/create/{jobOfferId}/{userId}")
    public ResponseEntity<Application> createApplication(
            @PathVariable Long jobOfferId,
            @PathVariable Long userId,
            @RequestPart("application") String applicationJson,
            @RequestPart("file") MultipartFile file) {
        try {
            // Save file
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get("uploads/" + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            // Configure ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // Deserialize application without jobOffer and user set
            Application application = objectMapper.readValue(applicationJson, Application.class);

            // Set the file path
            application.setCvPath(filePath.toString());

            // Retrieve JobOffer and User entities from database
            JobOffer jobOffer = jobOfferService.getJobOfferById(jobOfferId)
                    .orElseThrow(() -> new RuntimeException("JobOffer not found"));

            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Set associations manually
            application.setJobOffer(jobOffer);
            application.setUser(user);

            // Save application
            Application createdApplication = applicationService.createApplication(userId, jobOfferId, application);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdApplication);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/UpdateApplication/{id}")
    public ResponseEntity<Application> updateApplication(@PathVariable Long id, @RequestBody Application applicationDetails) {
        try {
            Application updatedApplication = applicationService.updateApplication(id, applicationDetails);
            return ResponseEntity.ok(updatedApplication);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/DeleteApplication/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
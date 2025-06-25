package tn.esprit.examen.nomPrenomClasseExamen.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.examen.nomPrenomClasseExamen.entities.JobOffer;
import tn.esprit.examen.nomPrenomClasseExamen.services.JobOfferService;

import org.springframework.http.HttpStatus; // For HTTP status codes
import org.springframework.http.ResponseEntity; // For HTTP responses
import org.springframework.web.bind.annotation.*; // For Spring REST annotations
import org.springframework.web.multipart.MultipartFile; // For handling file uploads
import java.io.IOException; // For exception handling
import java.nio.file.Files; // For file operations
import java.nio.file.Path; // For file paths
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/joboffers")
public class JobOfferController {
    private final JobOfferService jobOfferService;

    public JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }

    @GetMapping("/GetJobOffers")
    public List<JobOffer> getAllJobOffers() {
        return jobOfferService.getAllJobOffers();
    }

    @GetMapping("/GetJobOfferByid/{id}")
    public ResponseEntity<JobOffer> getJobOfferById(@PathVariable Long id) {
        return jobOfferService.getJobOfferById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/CreateJobOffer")
    public ResponseEntity<JobOffer> createJobOffer(
            @RequestPart("jobOffer") String jobOfferJson,
            @RequestPart("image") MultipartFile image) {
        try {
            // Save image to server
            String imageName = image.getOriginalFilename();
            Path imagePath = Paths.get("uploads/images/" + imageName);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, image.getBytes());

            // Configure ObjectMapper to handle Java 8 date/time types
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            // Parse JobOffer object from JSON
            JobOffer jobOffer = objectMapper.readValue(jobOfferJson, JobOffer.class);
            String baseUrl = " http://localhost:8089/PGH/api/images/"; // Replace with your base URL
            jobOffer.setImagePath(baseUrl + imageName);
            // Set image path


            // Save JobOffer
            JobOffer savedJobOffer = jobOfferService.createJobOffer(jobOffer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedJobOffer);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/UpdateJobOffer/{id}")
    public ResponseEntity<JobOffer> updateJobOffer(@PathVariable Long id, @RequestBody JobOffer jobOfferDetails) {
        try {
            JobOffer updatedJobOffer = jobOfferService.updateJobOffer(id, jobOfferDetails);
            return ResponseEntity.ok(updatedJobOffer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/DeleteJobOffer/{id}")
    public ResponseEntity<Void> deleteJobOffer(@PathVariable Long id) {
        jobOfferService.deleteJobOffer(id);
        return ResponseEntity.noContent().build();
    }
}

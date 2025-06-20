package tn.esprit.examen.nomPrenomClasseExamen.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Application;
import tn.esprit.examen.nomPrenomClasseExamen.services.ApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/GetApplications")
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/GetApplicationByid/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        return applicationService.getApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get applications by job offer
    @GetMapping("/GetApplicationByOffer/joboffer/{jobOfferId}")
    public List<Application> getApplicationsByJobOffer(@PathVariable Long jobOfferId) {
        return applicationService.getApplicationsByJobOffer(jobOfferId);
    }

    // Get applications by user
    @GetMapping("/GetApplicationByUser/user/{userId}")
    public List<Application> getApplicationsByUser(@PathVariable Long userId) {
        return applicationService.getApplicationsByUser(userId);
    }

    // Create application with jobOfferId and userId in URL path
    @PostMapping("/CreateApplication/joboffer/{jobOfferId}/user/{userId}")
    public ResponseEntity<Application> createApplication(
            @PathVariable Long jobOfferId,
            @PathVariable Long userId,
            @RequestBody Application application) {
        try {
            Application created = applicationService.createApplication(jobOfferId, userId, application);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/UpdateApplication/{id}")
    public ResponseEntity<Application> updateApplication(@PathVariable Long id, @RequestBody Application applicationDetails) {
        try {
            Application updated = applicationService.updateApplication(id, applicationDetails);
            return ResponseEntity.ok(updated);
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

package tn.esprit.examen.nomPrenomClasseExamen.services;

import org.springframework.stereotype.Service;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Application;
import tn.esprit.examen.nomPrenomClasseExamen.entities.JobOffer;
import tn.esprit.examen.nomPrenomClasseExamen.entities.User;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.ApplicationRepository;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.JobOfferRepository;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobOfferRepository jobOfferRepository;

    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository, JobOfferRepository jobOfferRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobOfferRepository = jobOfferRepository;
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }



    public Application createApplication(Long userId, Long jobOfferId, Application application) {
        User user = userRepository.findById(userId).orElse(null);
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId).orElse(null);

        if (user == null || jobOffer == null) {
            // Handle the case where either user or jobOffer is null.
            // You can log an error or return a default value if needed.
            return null;
        }


        application.setUser(user);
        application.setJobOffer(jobOffer);

        return applicationRepository.save(application);
    }

    public Application updateApplication(Long id, Application applicationDetails) {
        return applicationRepository.findById(id)
                .map(application -> {
                    application.setApplicationDate(applicationDetails.getApplicationDate());
                    application.setCvPath(applicationDetails.getCvPath());
                    application.setJobOffer(applicationDetails.getJobOffer());
                    application.setUser(applicationDetails.getUser());
                    return applicationRepository.save(application);
                })
                .orElseThrow(() -> new RuntimeException("Application not found with id " + id));
    }

    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
}
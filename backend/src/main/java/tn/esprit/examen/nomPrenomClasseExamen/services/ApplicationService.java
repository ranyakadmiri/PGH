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
        private final JobOfferRepository jobOfferRepository;
        private final UserRepository userRepository;

        public ApplicationService(ApplicationRepository applicationRepository,
                                  JobOfferRepository jobOfferRepository,
                                  UserRepository userRepository) {
            this.applicationRepository = applicationRepository;
            this.jobOfferRepository = jobOfferRepository;
            this.userRepository = userRepository;
        }

        public List<Application> getAllApplications() {
            return applicationRepository.findAll();
        }

        public Optional<Application> getApplicationById(Long id) {
            return applicationRepository.findById(id);
        }

        public List<Application> getApplicationsByJobOffer(Long jobOfferId) {
            return applicationRepository.findByJobOfferId(jobOfferId);
        }

        public List<Application> getApplicationsByUser(Long userId) {
            return applicationRepository.findByUserId(userId);
        }

        public Application createApplication(Long jobOfferId, Long userId, Application application) {
            JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                    .orElseThrow(() -> new RuntimeException("JobOffer not found with id " + jobOfferId));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

            application.setJobOffer(jobOffer);
            application.setUser(user);

            return applicationRepository.save(application);
        }

        public Application updateApplication(Long id, Application applicationDetails) {
            Application application = applicationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found with id " + id));

            application.setApplicationDate(applicationDetails.getApplicationDate());
            // Optional: Update jobOffer or user if needed
            return applicationRepository.save(application);
        }

        public void deleteApplication(Long id) {
            applicationRepository.deleteById(id);
        }
    }


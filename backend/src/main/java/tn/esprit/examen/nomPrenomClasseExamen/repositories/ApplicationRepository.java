package tn.esprit.examen.nomPrenomClasseExamen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Application;
import tn.esprit.examen.nomPrenomClasseExamen.entities.JobOffer;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobOfferId(Long jobOfferId);
    List<Application> findByUserId(Long userId);
}

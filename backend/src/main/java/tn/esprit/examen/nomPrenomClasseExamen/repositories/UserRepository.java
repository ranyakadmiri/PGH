package tn.esprit.examen.nomPrenomClasseExamen.repositories;

import tn.esprit.examen.nomPrenomClasseExamen.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {


        Optional<User> findByEmail(String email);
    }

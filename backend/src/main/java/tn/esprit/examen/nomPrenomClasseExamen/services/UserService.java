package tn.esprit.examen.nomPrenomClasseExamen.services;

import tn.esprit.examen.nomPrenomClasseExamen.entities.JobOffer;
import tn.esprit.examen.nomPrenomClasseExamen.entities.User;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
    public Long findUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> user.getId())
                .orElse(null); // Returns null if no user is found
    }
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
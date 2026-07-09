package org.esamepsw.store.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.esamepsw.store.utilities.exceptions.user.UserAlreadyExistsException;
import org.esamepsw.store.entities.User;
import org.esamepsw.store.repositories.UserRepository;
import org.esamepsw.store.utilities.exceptions.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = false)
    public User addUser(User user) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException();
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) throws UserNotFoundException {
        if(userRepository.existsById(id)) {
            return userRepository.findById(id).get();
        }else {
            throw new UserNotFoundException();
        }
    }

}

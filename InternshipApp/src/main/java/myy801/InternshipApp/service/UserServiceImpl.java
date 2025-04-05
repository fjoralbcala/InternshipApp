package myy801.InternshipApp.service;

import myy801.InternshipApp.entity.User;
import myy801.InternshipApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository theUserRepository) {
        this.userRepository = theUserRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(int theId) {
        User result = userRepository.findById(theId);

        if (result != null) {
            return result;
        }
        else {
            throw new RuntimeException("Did not find user with id: " + theId);
        }
    }

    @Override
    public void deleteById(int theId) {
        userRepository.deleteById(theId);
    }
}

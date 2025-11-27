package myy803.TraineeshipApp.TraineeshipApp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import myy803.TraineeshipApp.TraineeshipApp.model.User;
import myy803.TraineeshipApp.TraineeshipApp.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public boolean isUserPresent(User user) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        return userOptional.isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException(
                        String.format("USER_NOT_FOUND %s", username)
                ));

    }

    @Override
    public User findById(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }


}

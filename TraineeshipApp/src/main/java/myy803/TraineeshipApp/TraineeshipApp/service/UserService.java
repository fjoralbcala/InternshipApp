package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.User;

public interface UserService {
    public void saveUser(User user);
    public boolean isUserPresent(User user);
    public User findById(String username);
}

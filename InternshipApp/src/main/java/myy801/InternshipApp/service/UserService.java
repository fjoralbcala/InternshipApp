package myy801.InternshipApp.service;

import myy801.InternshipApp.entity.User;

import java.util.List;

public interface UserService {

    public List<User> findAll();

    public void save(User user);

    public User findById(int theId);

    public void deleteById(int theId);
}

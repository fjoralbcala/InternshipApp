package myy801.InternshipApp.repositories;


import myy801.InternshipApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findById(int theId);
}

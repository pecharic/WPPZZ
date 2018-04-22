package medictonproject.interfaces;

import medictonproject.model.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface UserInterface {
    @Transactional
    Object getUser(UserEntity u);

    UserEntity getUserById(int userId);

    @Transactional
    List<UserEntity> getAllUsers();

    @Transactional
    List<UserEntity> getUnapprovedUsers();

    @Transactional
    List<UserEntity> getApprovedUsers();

    @Transactional
    boolean add(UserEntity u);

    @Transactional
    boolean update(UserEntity u);

    @Transactional
    void delete(UserEntity u);
}

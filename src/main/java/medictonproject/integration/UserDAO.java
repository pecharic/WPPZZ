package medictonproject.integration;


import medictonproject.model.UserEntity;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserDAO implements medictonproject.interfaces.UserInterface {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserDAO.class);

    @PersistenceContext
    private EntityManager em;


    /**
     * Returns user by its email.
     *
     * @param u UserEntity of user
     * @return  user
     */
    @Override
    @Transactional
    public UserEntity getUser(UserEntity u) {
        Query q = em.createNativeQuery("SELECT * FROM swprojekt.user WHERE email = ?1", UserEntity.class).setParameter(1, u.getEmail());
        
        try {
            UserEntity user = (UserEntity)q.getSingleResult();
            return user;
        } catch ( NoResultException e ) {
            logger.info("userDAO", e);
            return null;
        }
    }

    /**
     * Returns user by his ID.
     *
     * @param userId Integer ID of User
     * @return UserEntity object
     */
    @Override
    public UserEntity getUserById(int userId)
    {
        UserEntity u = em.find( UserEntity.class, userId );
        return u;
    }

    /**
     * Returns list of all users.
     *
     * @return list of all UserEntity objects.
     */
    @Override
    @Transactional
    public List<UserEntity> getAllUsers() {
        return em.createNativeQuery("SELECT * FROM user", UserEntity.class).getResultList();
    }

    /**
     * Returns all unapproved users.
     *
     * @return list of unapproved UserEntity objects.
     */
    @Override
    @Transactional
    public List<UserEntity> getUnapprovedUsers() {
        Query q = em.createNativeQuery( "SELECT * FROM user WHERE is_admin = -1", UserEntity.class );
        return q.getResultList();
    }

    /**
     * Returns all approved users
     *
     * @return list of approved UserEntity object
     */
    @Override
    @Transactional
    public List<UserEntity> getApprovedUsers() {
        return em.createNativeQuery("SELECT * FROM user WHERE is_admin != -1", UserEntity.class).getResultList();
    }

    /**
     * Adds new user to the database
     *
     * @param u UserEntity object
     */
    @Override
    @Transactional
    public boolean add(UserEntity u) {
      try {
        em.persist(u);
        return true;
      } catch ( Exception e ) {
          logger.info("userDAO", e);
          return false;
      }
    }


    /**
     * Update user in the database
     *
     * @param u UserEntity object
     */
    @Override
    @Transactional
    public boolean update(UserEntity u) {
        try {
            em.merge( u );
            return true;
        } catch ( Exception e ) {
            logger.info("userDAO", e);
            return false;
        }
    }


    /**
     * Delete user from the database
     *
     * @param u UserEntity object
     */
    @Override
    @Transactional
    public void delete(UserEntity u) {
        em.remove(u);
    }
}

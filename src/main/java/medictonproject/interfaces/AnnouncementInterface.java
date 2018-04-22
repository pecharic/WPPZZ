package medictonproject.interfaces;

import medictonproject.model.AnnouncementEntity;
import medictonproject.model.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface AnnouncementInterface {
    List getMainPageAnnouncements();

    List getLocalAnnouncements();

    AnnouncementEntity getAnnoucmentById(int id);

    List<AnnouncementEntity> getAnnouncementsForUser(UserEntity u);

    @Transactional
    AnnouncementEntity getAnnouncementById(int announcementId);

    @Transactional
    void add(AnnouncementEntity a);

    @Transactional
    void delete(AnnouncementEntity a);

    @Transactional
    void update(AnnouncementEntity a);
}

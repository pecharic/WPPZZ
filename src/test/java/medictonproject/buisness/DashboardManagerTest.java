package medictonproject.buisness;

import medictonproject.integration.AnnouncementDAO;
import medictonproject.integration.DoctorSpecializationDAO;
import medictonproject.integration.UserDAO;
import medictonproject.model.AnnouncementEntity;
import medictonproject.model.Paginator;
import medictonproject.model.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DashboardManagerTest {
    private UserDAO userDAO;
    private UserEntity userEntity;
    private AnnouncementDAO announcementDAO;
    private AnnouncementEntity announcementEntity;
    private DoctorSpecializationDAO doctorSpecializationDAO;
    private DashboardManager dashboardManager;

    private void initializeFields(){
        userDAO = mock(UserDAO.class);
        userEntity = mock(UserEntity.class);
        announcementDAO = mock(AnnouncementDAO.class);
        announcementEntity = mock(AnnouncementEntity.class);
        doctorSpecializationDAO = mock(DoctorSpecializationDAO.class);
        dashboardManager = new DashboardManager(userDAO, announcementDAO, doctorSpecializationDAO);
    }

    @Test
    public void getAnnouncements_findsUser(){
        int page = 0;
        int userId = 0;
        int itemsPerPage = 5;

        initializeFields();

        dashboardManager.getAnnouncements(page, userId, itemsPerPage);

        verify(userDAO).getUserById(userId);
    }

    @Test
    public void getAnnouncements_getsUserAnnouncements(){
        int page = 0;
        int userId = 0;
        int itemsPerPage = 5;

        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        dashboardManager.getAnnouncements(page, userId, itemsPerPage);

        verify(announcementDAO).getAnnouncementsForUser(userEntity);
    }

    @Test
    public void getAnnouncementsOfUserWithNoAnnouncements_returnsEmptyPaginator(){
        int page = 0;
        int userId = 0;
        int itemsPerPage = 5;

        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( announcementDAO.getAnnouncementsForUser(userEntity) ).thenReturn(new ArrayList<>());

        Paginator paginator = dashboardManager.getAnnouncements(page, userId, itemsPerPage);
        int expected = 0;
        int actual = paginator.getTotalItems();

        assertEquals(expected, actual);
    }

    @Test
    public void getAnnouncementById_findsAnnouncement(){
        int announcementId = 0;

        initializeFields();

        dashboardManager.getAnnouncementById(announcementId);

        verify(announcementDAO).getAnnoucmentById(announcementId);
    }

    @Test
    public void addAnnouncement_DaoAddsAnnouncement(){
        initializeFields();

        dashboardManager.addAnnouncement(announcementEntity, new ArrayList<>());

        verify(announcementDAO).add(announcementEntity);
    }

    @Test
    public void addGlobalAnnouncement_DaoAddsAnnouncement(){
        initializeFields();

        dashboardManager.addAnnouncement(announcementEntity);

        verify(announcementDAO).add(announcementEntity);
    }

    @Test
    public void addAnnouncementWithEmptySpecList_doesNotAddAnySpecialization(){
        initializeFields();

        dashboardManager.addAnnouncement(new AnnouncementEntity(), new ArrayList<>());

        verify(doctorSpecializationDAO, times(0)).getSpecialization(anyString());
    }

    @Test
    public void editAnnouncement_DaoUpdatesAnnouncement(){
        initializeFields();

        dashboardManager.editAnnouncement(announcementEntity, new ArrayList<>());

        verify(announcementDAO).update(announcementEntity);
    }

    @Test
    public void editAnnouncementWithEmptySpecList_doesNotAddAnySpecialization(){
        initializeFields();

        dashboardManager.editAnnouncement(new AnnouncementEntity(), new ArrayList<>());

        verify(doctorSpecializationDAO, times(0)).getSpecialization(anyString());
    }

    @Test
    public void deleteAnnouncement_DaoFindsAnnouncement(){
        int announcementId = 0;
        initializeFields();

        dashboardManager.deleteAnnouncement(announcementId);

        verify(announcementDAO).getAnnouncementById(announcementId);
    }

    @Test
    public void deleteAnnouncement_DaoDeletesAnnouncement(){
        int announcementId = 0;
        initializeFields();

        when( announcementDAO.getAnnouncementById(announcementId) ).thenReturn( announcementEntity );

        dashboardManager.deleteAnnouncement(announcementId);

        verify(announcementDAO).delete(announcementEntity);
    }


}
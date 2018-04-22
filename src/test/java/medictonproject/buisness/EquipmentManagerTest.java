package medictonproject.buisness;

import medictonproject.integration.EquipmentDAO;
import medictonproject.integration.UserDAO;
import medictonproject.model.EquipmentEntity;
import medictonproject.model.Paginator;
import medictonproject.model.ProtocolEntity;
import medictonproject.model.UserEntity;
import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class EquipmentManagerTest {
    private EquipmentDAO equipmentDAO;
    private EquipmentEntity equipmentEntity;
    private UserDAO userDAO;
    private UserEntity userEntity;
    private EquipmentManager equipmentManager;

    int userId = 0;
    int page = 0;
    int equipmentId = 0;


    private void initializeFields(){
        equipmentDAO = mock(EquipmentDAO.class);
        equipmentEntity = mock(EquipmentEntity.class);
        userDAO = mock(UserDAO.class);
        userEntity = mock(UserEntity.class);
        equipmentManager = new EquipmentManager(equipmentDAO, userDAO);
    }



    @Test
    public void getEquipment_findsEquipment(){
        initializeFields();

        when( equipmentDAO.find(equipmentId) ).thenReturn( equipmentEntity );
        when( equipmentEntity.getOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId );

        equipmentManager.getEquipment(equipmentId, userId);

        verify(equipmentDAO).find(equipmentId);
    }

    @Test
    public void getEquipmentWithWrongUserId_returnsNull(){
        initializeFields();

        when( equipmentDAO.find(equipmentId) ).thenReturn( equipmentEntity );
        when( equipmentEntity.getOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId + 1 );

        EquipmentEntity result = equipmentManager.getEquipment(equipmentId, userId);

        assertNull(result);
    }

    @Test
    public void getEquipment_returnsMockEquipment(){
        initializeFields();

        when( equipmentDAO.find(equipmentId) ).thenReturn( equipmentEntity );
        when( equipmentEntity.getOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId );

        EquipmentEntity result = equipmentManager.getEquipment(equipmentId, userId);

        assertEquals(equipmentEntity, result);
    }

    @Test
    public void addEquipment_findsUser(){
        initializeFields();

        equipmentManager.addEquipment(equipmentEntity, userId);

        verify(userDAO).getUserById(userId);
    }

    @Test
    public void addEquipment_setsOwner(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        equipmentManager.addEquipment(equipmentEntity, userId);

        verify(equipmentEntity).setOwner(userEntity);
    }

    @Test
    public void cantAddEquipment_returnsFalse(){
        initializeFields();

        when( equipmentDAO.add(equipmentEntity) ).thenReturn( false );

        boolean result = equipmentManager.addEquipment(equipmentEntity, userId);

        assertFalse(result);
    }

    @Test
    public void updateEquipment_findsUser(){
        initializeFields();

        equipmentManager.updateEquipment(equipmentEntity, userId);

        verify(userDAO).getUserById(userId);
    }

    @Test
    public void updateEquipment_setsOwner(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        equipmentManager.updateEquipment(equipmentEntity, userId);

        verify(equipmentEntity).setOwner(userEntity);
    }

    @Test
    public void cantUpdateEquipment_returnsFalse(){
        initializeFields();

        when( equipmentDAO.update(equipmentEntity) ).thenReturn( false );

        boolean result = equipmentManager.addEquipment(equipmentEntity, userId);

        assertFalse(result);
    }

    @Test
    public void getListOfLocalExpiringEquipments(){
        initializeFields();

        Date currentDate = new Date();
        long currentMills = currentDate.getTime();
        long millsIn10Days = currentMills + (long)10 * 24 * 60 * 60 * 1000;
        long millsIn60Days = currentMills + (long)60 * 24 * 60 * 60 * 1000;

        // expired protocol
        ProtocolEntity p1 = new ProtocolEntity();
        p1.setExpires(new Timestamp(currentMills - 1));

        // protocol expires in 10 days
        ProtocolEntity p2 = new ProtocolEntity();
        p2.setExpires(new Timestamp(millsIn10Days));

        // protocol expires in 60 days
        ProtocolEntity p3 = new ProtocolEntity();
        p3.setExpires(new Timestamp(millsIn60Days));


        // equipment with no protocols, should be printed
        EquipmentEntity eq1 = new EquipmentEntity();
        eq1.setBuisnessName("Eq1");
        eq1.setProtocols(new ArrayList<>());

        // equipment with one expired protocol, should be printed
        EquipmentEntity eq2 = new EquipmentEntity();
        eq2.setBuisnessName("Eq2");
        eq2.setProtocols(new ArrayList<ProtocolEntity>(asList( p1 )));

        // equipment with one expired protocol
        // and one protocol which expires in 10 ten days, should be printed
        EquipmentEntity eq3 = new EquipmentEntity();
        eq3.setBuisnessName("Eq3");
        eq3.setProtocols(new ArrayList<ProtocolEntity>(asList( p1, p2 )));

        // equipment with one valid protocol, shouldn't be printed
        EquipmentEntity eq4 = new EquipmentEntity();
        eq4.setBuisnessName("Eq4");
        eq4.setProtocols(new ArrayList<ProtocolEntity>(asList( p3 )));

        // equipment with one valid protocol
        // and one protocol which expires soon, shouldn't be printed
        EquipmentEntity eq5 = new EquipmentEntity();
        eq5.setBuisnessName("Eq5");
        eq5.setProtocols(new ArrayList<ProtocolEntity>(asList( p2, p3 )));

        // equipment with one valid protocol
        // and one expired protocol, shouldn't be printed
        EquipmentEntity eq6 = new EquipmentEntity();
        eq6.setBuisnessName("Eq6");
        eq6.setProtocols(new ArrayList<ProtocolEntity>(asList( p1, p3 )));

        ArrayList<EquipmentEntity> fakeEquipments = new ArrayList<>(
                asList(eq1, eq2, eq3, eq4, eq5, eq6)
        );

        when( userDAO.getUserById(userId) ). thenReturn( userEntity );
        when( userEntity.getEquipments() ). thenReturn( fakeEquipments );

        ArrayList<String> expected = new ArrayList<>(asList( "Eq1", "Eq2", "Eq3" ));
        ArrayList<String> actual = equipmentManager.getListOfLocalExpiringEquipments(userId);

        assertEquals(expected, actual);
    }

    @Test
    public void getListOfLocalExpiringEquipments1(){
        initializeFields();

        // equipment with no protocols, should be printed
        EquipmentEntity eq1 = new EquipmentEntity();
        eq1.setBuisnessName("Eq1");
        eq1.setProtocols(new ArrayList<>());

        ArrayList<EquipmentEntity> fakeEquipments = new ArrayList<>(
                asList(eq1)
        );

        when( userDAO.getUserById(userId) ). thenReturn( userEntity );
        when( userEntity.getEquipments() ). thenReturn( fakeEquipments );

        ArrayList<String> expected = new ArrayList<>( asList("Eq1") );
        ArrayList<String> actual = equipmentManager.getListOfLocalExpiringEquipments(userId);

        assertEquals(expected, actual);
    }

    @Test
    public void getListOfLocalExpiringEquipments2(){
        initializeFields();

        Date currentDate = new Date();
        long currentMills = currentDate.getTime();
        long millsIn10Days = currentMills + (long)10 * 24 * 60 * 60 * 1000;
        long millsIn60Days = currentMills + (long)60 * 24 * 60 * 60 * 1000;

        // expired protocol
        ProtocolEntity p1 = new ProtocolEntity();
        p1.setExpires(new Timestamp(currentMills - 1));

        // equipment with one expired protocol, should be printed
        EquipmentEntity eq2 = new EquipmentEntity();
        eq2.setBuisnessName("Eq2");
        eq2.setProtocols(new ArrayList<ProtocolEntity>(asList( p1 )));

        ArrayList<EquipmentEntity> fakeEquipments = new ArrayList<>(
                asList(eq2)
        );

        when( userDAO.getUserById(userId) ). thenReturn( userEntity );
        when( userEntity.getEquipments() ). thenReturn( fakeEquipments );

        ArrayList<String> expected = new ArrayList<>(asList( "Eq2" ));
        ArrayList<String> actual = equipmentManager.getListOfLocalExpiringEquipments(userId);

        assertEquals(expected, actual);
    }

    @Test
    public void getListOfLocalExpiringEquipments3(){
        initializeFields();

        Date currentDate = new Date();
        long currentMills = currentDate.getTime();
        long millsIn10Days = currentMills + (long)10 * 24 * 60 * 60 * 1000;
        long millsIn60Days = currentMills + (long)60 * 24 * 60 * 60 * 1000;

        // expired protocol
        ProtocolEntity p1 = new ProtocolEntity();
        p1.setExpires(new Timestamp(currentMills - 1));

        // protocol expires in 10 days
        ProtocolEntity p2 = new ProtocolEntity();
        p2.setExpires(new Timestamp(millsIn10Days));

        // equipment with one expired protocol
        // and one protocol which expires in 10 ten days, should be printed
        EquipmentEntity eq3 = new EquipmentEntity();
        eq3.setBuisnessName("Eq3");
        eq3.setProtocols(new ArrayList<ProtocolEntity>(asList( p1, p2 )));

        ArrayList<EquipmentEntity> fakeEquipments = new ArrayList<>(
                asList(eq3)
        );

        when( userDAO.getUserById(userId) ). thenReturn( userEntity );
        when( userEntity.getEquipments() ). thenReturn( fakeEquipments );

        ArrayList<String> expected = new ArrayList<>(asList( "Eq3" ));
        ArrayList<String> actual = equipmentManager.getListOfLocalExpiringEquipments(userId);

        assertEquals(expected, actual);
    }

    @Test
    public void getListOfLocalExpiringEquipments4(){
        initializeFields();

        Date currentDate = new Date();
        long currentMills = currentDate.getTime();
        long millsIn10Days = currentMills + (long)10 * 24 * 60 * 60 * 1000;
        long millsIn60Days = currentMills + (long)60 * 24 * 60 * 60 * 1000;

        // protocol expires in 60 days
        ProtocolEntity p3 = new ProtocolEntity();
        p3.setExpires(new Timestamp(millsIn60Days));

        // equipment with one valid protocol, shouldn't be printed
        EquipmentEntity eq4 = new EquipmentEntity();
        eq4.setBuisnessName("Eq4");
        eq4.setProtocols(new ArrayList<ProtocolEntity>(asList( p3 )));

        ArrayList<EquipmentEntity> fakeEquipments = new ArrayList<>(
                asList(eq4)
        );

        when( userDAO.getUserById(userId) ). thenReturn( userEntity );
        when( userEntity.getEquipments() ). thenReturn( fakeEquipments );

        ArrayList<String> expected = new ArrayList<>();
        ArrayList<String> actual = equipmentManager.getListOfLocalExpiringEquipments(userId);

        assertEquals(expected, actual);
    }

    @Test
    public void getListOfLocalExpiringEquipments5(){
        initializeFields();

        Date currentDate = new Date();
        long currentMills = currentDate.getTime();
        long millsIn10Days = currentMills + (long)10 * 24 * 60 * 60 * 1000;
        long millsIn60Days = currentMills + (long)60 * 24 * 60 * 60 * 1000;

        // protocol expires in 10 days
        ProtocolEntity p2 = new ProtocolEntity();
        p2.setExpires(new Timestamp(millsIn10Days));

        // protocol expires in 60 days
        ProtocolEntity p3 = new ProtocolEntity();
        p3.setExpires(new Timestamp(millsIn60Days));

        // equipment with one valid protocol
        // and one protocol which expires soon, shouldn't be printed
        EquipmentEntity eq5 = new EquipmentEntity();
        eq5.setBuisnessName("Eq5");
        eq5.setProtocols(new ArrayList<ProtocolEntity>(asList( p2, p3 )));

        ArrayList<EquipmentEntity> fakeEquipments = new ArrayList<>(
                asList(eq5)
        );

        when( userDAO.getUserById(userId) ). thenReturn( userEntity );
        when( userEntity.getEquipments() ). thenReturn( fakeEquipments );

        ArrayList<String> expected = new ArrayList<>();
        ArrayList<String> actual = equipmentManager.getListOfLocalExpiringEquipments(userId);

        assertEquals(expected, actual);
    }

    @Test
    public void getListOfLocalExpiringEquipments6(){
        initializeFields();

        Date currentDate = new Date();
        long currentMills = currentDate.getTime();
        long millsIn10Days = currentMills + (long)10 * 24 * 60 * 60 * 1000;
        long millsIn60Days = currentMills + (long)60 * 24 * 60 * 60 * 1000;

        // expired protocol
        ProtocolEntity p1 = new ProtocolEntity();
        p1.setExpires(new Timestamp(currentMills - 1));

        // protocol expires in 10 days
        ProtocolEntity p2 = new ProtocolEntity();
        p2.setExpires(new Timestamp(millsIn10Days));

        // protocol expires in 60 days
        ProtocolEntity p3 = new ProtocolEntity();
        p3.setExpires(new Timestamp(millsIn60Days));

        // equipment with one valid protocol
        // and one expired protocol, shouldn't be printed
        EquipmentEntity eq6 = new EquipmentEntity();
        eq6.setBuisnessName("Eq6");
        eq6.setProtocols(new ArrayList<ProtocolEntity>(asList( p1, p3 )));

        ArrayList<EquipmentEntity> fakeEquipments = new ArrayList<>(
                asList(eq6)
        );

        when( userDAO.getUserById(userId) ). thenReturn( userEntity );
        when( userEntity.getEquipments() ). thenReturn( fakeEquipments );

        ArrayList<String> expected = new ArrayList<>();
        ArrayList<String> actual = equipmentManager.getListOfLocalExpiringEquipments(userId);

        assertEquals(expected, actual);
    }

}
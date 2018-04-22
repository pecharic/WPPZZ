package medictonproject.buisness;

import medictonproject.integration.DocumentDAO;
import medictonproject.integration.EquipmentDAO;
import medictonproject.integration.UserDAO;
import medictonproject.model.EquipmentEntity;
import medictonproject.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

class DocumentManagerTest {

    private EquipmentDAO equipmentDAO;
    private UserDAO userDao;
    private DocumentDAO documentDAO;
    private DocumentManager documentManager;
    private UserEntity userEntity;
    private EquipmentEntity equipmentEntity;
    private List<EquipmentEntity> userEquipment;
    private MultipartFile file;

    int userId = 0;
    int equipmentId = 0;


    private void initializeFields(){
        equipmentDAO = mock(EquipmentDAO.class);
        userDao = mock(UserDAO.class);
        documentDAO = mock(DocumentDAO.class);
       // documentManager = new DocumentManager(equipmentDAO, userDao, documentDAO);
        userEntity = mock(UserEntity.class);
        equipmentEntity = mock(EquipmentEntity.class);
        userEquipment = mock(ArrayList.class);
        file = mock(MultipartFile.class);
    }



}
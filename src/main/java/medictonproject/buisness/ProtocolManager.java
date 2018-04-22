package medictonproject.buisness;

import medictonproject.integration.EquipmentDAO;
import medictonproject.integration.ProtocolDAO;
import medictonproject.integration.UserDAO;
import medictonproject.model.DocumentEntity;
import medictonproject.model.EquipmentEntity;
import medictonproject.model.ProtocolEntity;
import medictonproject.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProtocolManager {
    private ProtocolDAO protocolDAO;
    private EquipmentDAO equipmentDAO;
    private UserDAO userDAO;
    private DocumentManager documentManager;

    @Autowired
    ProtocolManager( ProtocolDAO protocolDAO, EquipmentDAO equipmentDAO, UserDAO userDAO, DocumentManager documentManager){
        this.protocolDAO = protocolDAO;
        this.equipmentDAO = equipmentDAO;
        this.userDAO = userDAO;
        this.documentManager = documentManager;
    }

    public boolean addProtocol(ProtocolEntity protocol, int eqid, int userId,  MultipartFile[] files) throws IOException {
        if( files.length == 0 )
            return false;

        EquipmentEntity eq = equipmentDAO.getEq(eqid);
        UserEntity user    = userDAO.getUserById(userId);
        List<DocumentEntity> documents = new ArrayList<>();

        for( MultipartFile file : files  ){
            DocumentEntity d = documentManager.uploadDocumentwithProtocol(file,user,eq,protocol);
            if( d!= null )
                documents.add(d);
            else{
                documentManager.removeFiles(documents);
                return false;
            }
        }
        protocol.setDocuments(documents);
        protocol.setEquipment(eq);

        if( !protocolDAO.add(protocol)){
            documentManager.removeFiles(documents);
            return false;
        }
        return true;
    }
}

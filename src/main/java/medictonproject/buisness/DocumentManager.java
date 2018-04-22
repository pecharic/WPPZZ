package medictonproject.buisness;

import medictonproject.integration.DocumentDAO;
import medictonproject.integration.EquipmentDAO;
import medictonproject.integration.ProtocolDAO;
import medictonproject.integration.UserDAO;
import medictonproject.model.DocumentEntity;
import medictonproject.model.EquipmentEntity;
import medictonproject.model.ProtocolEntity;
import medictonproject.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class DocumentManager {

  private EquipmentDAO eqDAO;
  private ProtocolDAO protocolDAODao;
  private UserDAO userDao;
  private DocumentDAO docDao;
  @Value("${upload.folder}")
  private String UPLOADED_FOLDER;

  @Autowired
  public DocumentManager(EquipmentDAO eqDAO ,ProtocolDAO protocolDAO, UserDAO userDAO , DocumentDAO docDao){
    this.eqDAO = eqDAO;
    this.protocolDAODao = protocolDAO;
    this.userDao = userDAO;
    this.docDao = docDao;
  }
  
  /**
   * Adds a new file - stores information about the file in database and stores file on the server.
   *
   * @param file - File to be stored
   *
   *
   * @return boolean value depending on success
   */
  public DocumentEntity uploadDocumentwithProtocol(MultipartFile file, UserEntity user, EquipmentEntity eq, ProtocolEntity protocol) throws IOException {
    System.out.println("FOLDER IS " + UPLOADED_FOLDER);
    String pathToFile = UPLOADED_FOLDER + "\\www\\" +
            user.getFirstName() +"\\" +
            eq.getBuisnessName();
    byte[] bytes = file.getBytes();

    File dir = new File( pathToFile );
    dir.mkdirs();

    Path filePath = Paths.get( pathToFile  + "\\" + file.getOriginalFilename() );
    DocumentEntity document = new DocumentEntity();
    document.setName( file.getOriginalFilename() );
    document.setCreated( new Timestamp( new Date().getTime()));
    document.setProtocol(protocol);
    document.setPath( filePath.toString() );
    try{
      Files.write( filePath , bytes);
      return document;
    }catch( Exception e ){
      return null;
    }
  }

  public boolean removeFiles( List<DocumentEntity> docs ){
    for( DocumentEntity doc : docs ){
      File file = new File(doc.getPath());
      if( ! file.delete())
        return false;
    }
    return true;
  }

  public boolean uploadDocument(MultipartFile file , int id , int eqId , int protId) throws IOException
  {
    UserEntity user = userDao.getUserById( id  );
    EquipmentEntity equipment = eqDAO.getEq(eqId);
    ProtocolEntity protocol = protocolDAODao.find( protId );

    List<EquipmentEntity> user_equipment = user.getEquipments();
    if(! user_equipment.contains( equipment )){
      return false;
    }

    if( file.isEmpty() ) {
      return false;
    }
    byte[] bytes = file.getBytes();
    String pathToFile = UPLOADED_FOLDER + "\\www\\" +
            user.getFirstName() +"\\" +
            equipment.getBuisnessName();

    Path path = Paths.get( pathToFile );
    File dir = new File( pathToFile );
    dir.mkdirs();

    Path filePath = Paths.get( pathToFile  + "\\" + file.getOriginalFilename() );

    DocumentEntity document = new DocumentEntity();
    document.setName( file.getOriginalFilename() );
    document.setCreated( new Timestamp( new Date().getTime()));
    document.setPath( pathToFile  + "\\" + file.getOriginalFilename() );
    if ( docDao.add( document ) == false )
      return false;
    Files.write( filePath , bytes);
    return true;
  }
  
}

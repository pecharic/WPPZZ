package medictonproject.controller;

import medictonproject.buisness.*;
import medictonproject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping( "/equipment" )
public class EquipmentController {

  /**
   * instance of EquipmentManager object
   */
  private EquipmentManager equipmentManager;

  /**
   * instance of DocumentManager object
   */
  private DocumentManager documentManager;

  private IntuoManager intuoManager;
  private ProtocolManager protocolManager;

  private MappedEquipmentManager mappedEquipmentManager;
  /**
   * EquipmentController contructor
   */
  @Autowired
  public EquipmentController (EquipmentManager equipmentManager,
                              DocumentManager documentManager,
                              IntuoManager intuoManager,
                              ProtocolManager protocolManager,
                              MappedEquipmentManager mappedEquipmentManager) {
    this.equipmentManager = equipmentManager;
    this.documentManager = documentManager;
    this.intuoManager = intuoManager;
    this.protocolManager = protocolManager;
    this.mappedEquipmentManager = mappedEquipmentManager;
  }

  /**
   * Controller for getting certain ammount of equipments.
   *
   * @param request HttpServletRequest
   * @param page number of page to be returned
   * @param mode mode of sorting (attribute-asc/desc)
   * @return Page of conversations
   */

  @RequestMapping( value = "/getEquipments/{page}/{mode}", method = RequestMethod.GET )
  public Paginator<MappedEquipmentEntity> getSortedEquipments(HttpServletRequest request,
                                                              @PathVariable("page") Integer page,
                                                              @PathVariable("mode") String mode){
    return mappedEquipmentManager.getSortedEquipments( (Integer)request.getAttribute("userId"), page, mode);
  }

  /**
   * Controller for getting certain ammount of equipments.
   *
   * @param request HttpServletRequest
   * @param page number of page to be returned
   * @return Page of conversations
   */

  @RequestMapping( value = "/getEquipments/{page}", method = RequestMethod.GET )
  public Paginator<MappedEquipmentEntity> getAll ( HttpServletRequest request,
                                             @PathVariable("page") Integer page ) {

    return mappedEquipmentManager.getAll( (Integer)request.getAttribute("userId"), page );
  }

  @RequestMapping(value="/newProtocol",  method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public boolean createProtocol(ProtocolEntity protocol,
                                  @RequestParam("eqId") int eqId,
                                  HttpServletRequest req,
                                  @RequestParam("files") MultipartFile[] files) throws IOException {
    return protocolManager.addProtocol(protocol, eqId, (int)req.getAttribute("userId"), files);
  }


  @RequestMapping( value = "/intuo/getEquipments/{page}", method = RequestMethod.GET )
  public Paginator<IntuoEquipmentEntity> getAllFromIntuo (HttpServletRequest request,
                                                                  @PathVariable("page") Integer page ) {
    return intuoManager.getIntuoEquipments( (Integer)request.getAttribute("userId"), page );
  }

  /**
   * Controller which allows user to upload a document.
   *
   * @param file - a representation of an uploaded file received in a multipart request
   * @param eqId - id of equipment
   * @param req - HttpServerRequest object
   * @return boolean value
   */

  @RequestMapping(value = "/postDocument", method = RequestMethod.POST , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public boolean uploadDocument(  @RequestParam("file") MultipartFile file ,
                                  @RequestParam("eqId") int eqId ,
                                  HttpServletRequest req,
                                  @RequestParam("protId") int protId) throws IOException {
    return documentManager.uploadDocument(file, (int)req.getAttribute("userId"), eqId, protId  );
  }

  /**
   * Controller which allows user to add his own equipment.
   *
   * @param equipment - equipment object that holds data of equipment to be inserted
   * @param request - object of type HttpServletRequest
   * @return boolean value
   */

  @RequestMapping(value = "/addEquipment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public boolean addEquipment(EquipmentEntity equipment, HttpServletRequest request) {
    return equipmentManager.addEquipment(equipment, (Integer)request.getAttribute("userId"));
  }

  /**
   * Controller which allows user to update his own equipment.
   *
   * @param equipment - equipment object that holds data of equipment to be updated
   * @param request - object of type HttpServletRequest
   * @return boolean value
   */

  @RequestMapping(value = "/updateEquipment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public boolean updateEquipment(EquipmentEntity equipment, HttpServletRequest request) {
    return equipmentManager.updateEquipment(equipment, (Integer)request.getAttribute("userId"));
  }


  /**
   * Controller which returns equipment of given id.
   *
   * @param eqId - id of equipment which will be returned
   * @param req - object of type HttpServletRequest
   * @return equipment of type EquipmentEntity
   */
  @RequestMapping( value = "/getEquipment/{id}" )
  public EquipmentEntity getEquipment( @PathVariable("id") Integer eqId, HttpServletRequest req ) {
    return equipmentManager.getEquipment( eqId, (Integer)req.getAttribute( "userId" ) );
  }

  /**
   * For a equipment from Intuo get page of protocols.
   *
   * @param eqId ID of equipment
   * @param page number of page to be returned
   * @return A page of protocols.
   */
  @RequestMapping(value = "/getProtocolsForIntuoEquipment/{equipmentId}/{page}")
  public Paginator<IntuoProtocolEntity> getProtocolsForIntuoEquipment(@PathVariable("equipmentId") Integer eqId,
                                                                      @PathVariable("page") Integer page) {
    return intuoManager.getProtocolsForEquipment(eqId, page);
  }


  /**
   * Returning list of names of equipment that needs to be
   * coloured red.
   *
   * @return
   */
  @RequestMapping(value = "/getExpiringEquipments", method = RequestMethod.GET)
  public List<String> getExpiringIntuoEquipments(HttpServletRequest req) {
      return intuoManager.getExpiringIntuoEquipments( (Integer)req.getAttribute( "userId" ) );
  }


  /**
   * Get number of protocols which need revision in
   * 30 or less days
   *
   * @param req Request from a user containing userId
   * @return Number of warned protocols
   */
  @RequestMapping(value = "/getNumberOfProtocolWarnings")
  public Integer getNumberOfWarnings(HttpServletRequest req) {
    return intuoManager.getNumberOfWarnings((Integer)req.getAttribute("userId"));
  }

  @RequestMapping(value = "/**",method = RequestMethod.OPTIONS)
  public ResponseEntity handle() {
    return new ResponseEntity(HttpStatus.OK);
  }

  @RequestMapping(value = "/getLocalExpiring")
  public List<String> getListOfLocalExpiringEquipments (Integer userId){
      return equipmentManager.getListOfLocalExpiringEquipments(userId);
  }


}

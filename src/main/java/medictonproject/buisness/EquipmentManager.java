package medictonproject.buisness;

import medictonproject.integration.EquipmentDAO;
import medictonproject.integration.UserDAO;
import medictonproject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EquipmentManager {
  private EquipmentDAO equipmentDAO;
  private UserDAO userDAO;

  @Autowired
  public EquipmentManager ( EquipmentDAO equipmentDAO, UserDAO userDAO ) {
    this.equipmentDAO = equipmentDAO;
    this.userDAO = userDAO;
  }

  /**
   * Returns user's local equipments.
   *
   * @param userId ID of user
   *
   * @return Page of equipment
   */

  public List<EquipmentEntity> getLocalEquipments ( Integer userId ) {
    return equipmentDAO.getAll(userId);
  }

  /**
   * Adds a new equipment into database.
   *
   * @param equipment object of type EquipmentEntity
   * @param userId ID of user to which equipment belongs
   *
   * @return true for success, false for failure
   */
  public boolean addEquipment(EquipmentEntity equipment, Integer userId) {
    UserEntity user = userDAO.getUserById(userId);
    equipment.setOwner(user);

    return equipmentDAO.add(equipment);
  }
  
  /**
   * Updates information about equipment.
   *
   * @param equipment - object of type EquipmentEntity
   * @param userId ID of user to which equipment belongs
   *
   * @return true for success, false for failure
   */
  public boolean updateEquipment(EquipmentEntity equipment, Integer userId) {
    UserEntity user = userDAO.getUserById(userId);
    equipment.setOwner(user);

    return equipmentDAO.update(equipment);
  }
  
  /**
   * Returns equipment object with specific ID.
   *
   * @param eqId - ID of equipment which is about to be returned
   * @param userId ID of user to which equipment belongs
   *
   * @return Equipment from database
   */
  public EquipmentEntity getEquipment( Integer eqId, Integer userId ) {
    EquipmentEntity eq = equipmentDAO.find( eqId );
    if( eq.getOwner().getUserId() != userId ) {
      //System.out.println("Equip nepatri userovi");
      return null;
    }
    return eq;
  }

  public ArrayList<String> getListOfLocalExpiringEquipments(Integer userId) {
    UserEntity user = userDAO.getUserById(userId);
    ArrayList<String> expiringEquipments = new ArrayList<String>();
    List<EquipmentEntity> equipments = user.getEquipments();
    Date currentDate = new Date();
    long currentMills = currentDate.getTime();
    long deadLineMills = currentMills + (long)30 * 24 * 60 * 60 * 1000;

    for (EquipmentEntity eq : equipments){
        List<ProtocolEntity> protocols = eq.getProtocols();

        int protocolNumber = 0;
        for (ProtocolEntity pr : protocols){
            long timeExpire = pr.getExpires().getTime();

            if (timeExpire <= deadLineMills)
                continue;

            protocolNumber ++;
        }
        if (protocolNumber == 0)
            expiringEquipments.add(eq.getBuisnessName());
    }

    return expiringEquipments;
  }

  /**
     * Scan all the protocols of user's equipment and
     * count protocols which need revision.
     *
     * @param userId ID of user for which you want to get number of warnings
     * @return Number of warnings
     */
//    public Integer getNumberOfWarnings(Integer userId) {
//        List<IntuoProtocolEntity> protocols = getUsersProtocols(userId);
//        Integer numberOfWarnings = 0;
//
//        for(IntuoProtocolEntity protocol : protocols) {
//            if(protocolWarn(protocol) == true)
//                numberOfWarnings += 1;
//        }
//
//        return numberOfWarnings;
//    }

    /**
     * Get all protocols from Intuo belonging to user specified by userId.
     *
     * @param userId ID of user for which you want to get protocols
     * @return all protocols
     */
//    private List<IntuoProtocolEntity> getUsersProtocols(Integer userId) {
//        List<IntuoEquipmentEntity> intuoEquipment = getIntuoEquipments(userId, 1).getItemList();
//
//        List<IntuoProtocolEntity> protocols = new ArrayList<>();
//        for(IntuoEquipmentEntity eq : intuoEquipment) {
//            Integer eqId = Integer.parseInt(eq.getC0_id());
//            List<IntuoProtocolEntity> eqProtocols = this.getProtocolsForEquipment(eqId, 1).getItemList();
//            protocols.addAll(eqProtocols);
//        }
//
//        return protocols;
//    }


    /**
     * Responds whether a protocol should be warned about
     * expiring or not. If next revision is planned in 30 days
     * or less, then protocol is warned.
     *
     * @param protocol Protocol to be checked for warning
     * @return true for expiring protocol, false for not expiring one
     */
//    private boolean protocolWarn(IntuoProtocolEntity protocol) {
//        DateFormat dateFormat = new SimpleDateFormat("y-d-MTH:m:s+H:m");
//        Date date = null;
//
//        try {
//            date = dateFormat.parse(protocol.getC6_dateOfNextInspection());
//        } catch (ParseException e) {
//            //parse exception
//            e.printStackTrace();
//        }
//
//        System.out.println(date.getTime() - 30*86000*1000);
//        if((date.getTime() - 30*86000*1000) < System.currentTimeMillis())
//            return true;
//        return false;
//    }

    @Scheduled(cron = "0 0 2 * * *")
    public void sendEmailToUsersWithExpiringLocalEquipments()
    {
      List<UserEntity> list = userDAO.getAllUsers();
      Date currentDate = new Date();
      long currentTime = currentDate.getTime() / (long)86400000;
      for (UserEntity u : list) {
        List<EquipmentEntity> equipments = u.getEquipments();
        for (EquipmentEntity eq : equipments ) {
          List<ProtocolEntity> protocols = eq.getProtocols();
          for ( ProtocolEntity pro : protocols ) {
             long expires = pro.getExpires().getTime() / (long) 86400000;
             if ( (expires - currentTime) == 30 ) {
             /*  emailSender.sendSimpleMessage(u.getEmail(), "Platnost protocolu",
                                             "K pristroju " + eq.getBuisnessName() + " existuje protocol, ktory vyprsi za 30 dni." );*/
                 break;
             }
          }
        }
      }
    }
}

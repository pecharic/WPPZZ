package medictonproject.buisness;

import medictonproject.integration.IntuoDAO;
import medictonproject.integration.UserDAO;
import medictonproject.integration.XMLParseManager;
import medictonproject.model.*;
import org.jdom.JDOMException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IntuoManager {
    private final XMLParseManager parser;
    private static  String key;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(IntuoManager.class);

    private UserDAO userDAO;
    private IntuoDAO intuoDAO;

    @Autowired
    public IntuoManager(UserDAO userDAO, XMLParseManager parser, IntuoDAO intuoDAO) throws JDOMException, SAXException, ParserConfigurationException, IOException {
        this.parser = parser;
        this.userDAO = userDAO;
        this.intuoDAO = intuoDAO;
        key = intuoDAO.getAuthKey();
    }

    public String getKey() {
        return key;
    }

    @Scheduled(fixedRate=300000)
    public void getAuthKeyInIntervals() {
        //System.out.println("updatol som kluc");
        key = intuoDAO.getAuthKey();
    }

    /**
     * For the ICO of user specified by userId take all the organizations
     * that belong to this ICO and return them.
     *
     * @param userId ID of user for whom you want equipment
     * @return User's organizations
     */
    private ArrayList<IntuoOrganizationEntity> getOrganizationsByICO(Integer userId) {
        String userIco = (userDAO.getUserById(userId)).getIco();
        String strXMLDefinitionOfRestriction = String.format(Constants.view745_strXMLDefinitionOfRestrictionByICO, userIco);
        String responseGetView = intuoDAO.intuoGetView(key, 745, strXMLDefinitionOfRestriction);

        ArrayList<IntuoOrganizationEntity> organizations = parser.GetOrganizations(responseGetView);
//        for ( int i = 0; i < nList.getLength(); i++ ) {
//            System.out.println(nList.item(i).getTextContent());
//        }
        return organizations;
    }

    /**
     * Based on email address parameter gets organizations
     * that belong to user.
     *
     * @param userId ID of user for whom you want equipment
     * @return User's organizations
     */
    private ArrayList<IntuoOrganizationEntity> getUserOrganizationsByEmail(Integer userId) {
        String userEmail = userDAO.getUserById(userId).getEmail();
        String strXMLDefinitionOfRestriction = String.format(Constants.view745_strXMLDefinitionOfRestriction, userEmail);
        String responseGetView = intuoDAO.intuoGetView(key, 745, strXMLDefinitionOfRestriction);

        ArrayList<IntuoOrganizationEntity> organizations = parser.GetOrganizations(responseGetView);
//        for ( int i = 0; i < nList.getLength(); i++ ) {
//            System.out.println(nList.item(i).getTextContent());
//        }
        return organizations;
    }

    /**
     * Get all equipment from Intuo for a user.
     *
     * @param page Page number to be returned
     * @return Page of equipment
     */
    public Paginator<IntuoEquipmentEntity> getIntuoEquipments ( Integer userId, Integer page ) {
        ArrayList<IntuoOrganizationEntity> organizations = getOrganizationsByICO(userId);
        //ArrayList<IntuoOrganizationEntity> organizations = getUserOrganizationsByEmail(userId);

        ArrayList<IntuoEquipmentEntity> equipmentList = new ArrayList<>();

        for (IntuoOrganizationEntity organization : organizations) {
            ArrayList<IntuoEquipmentEntity> eqs = getEquipmentsForOrganization(organization.getC8_adresaId());
            for(IntuoEquipmentEntity eq : eqs) {
                eq.setAddress(organization.getC10_adresaUlice());
            }
            equipmentList.addAll(eqs);
        }

        Paginator<IntuoEquipmentEntity> equipmentPaginator = new Paginator<>(equipmentList, page, Constants.PAGE_SIZE_EQUIPMENT);

        return equipmentPaginator;
    }

    List<IntuoEquipmentEntity> getIntuoEquipments ( Integer userId ) {
        return getIntuoEquipments(userId, 1).getItemList();
    }


    /**
     * Gets one page of equipment for a single organization.
     * @param ID
     * @return Organization's equipment
     */
    private ArrayList<IntuoEquipmentEntity> getEquipmentsForOrganization(String ID) {
        String response = intuoDAO.intuoGetBindingView( key,754, ID );

        ArrayList<IntuoEquipmentEntity> equipmentList = parser.GetEquipmentsForOrganization(response);
       // System.out.println(equipmentList.size());

        return equipmentList;
    }


    /**
     * Get one page of protocols for a concrete equipment.
     * Both equipment and protocols are from Intuo.
     *
     * @param equipmentId ID of equipment for which you want protocols
     * @param page Number of page to be returned
     * @return Page of protocols
     */
    public Paginator<IntuoProtocolEntity> getProtocolsForEquipment(Integer equipmentId, Integer page) {
        String response = intuoDAO.intuoGetBindingView(key, 755, equipmentId.toString());

        ArrayList<IntuoProtocolEntity> protocolList = parser.GetProtocolsForEquipment(response);
        Paginator<IntuoProtocolEntity> protocolPaginator = new Paginator<>(protocolList, page, Constants.PAGE_SIZE_PROTOCOLS);

        return protocolPaginator;
    }


    /**
     * Scan all the protocols of user's equipment and
     * count protocols which need revision.
     *
     * @param userId ID of user for which you want to get number of warnings
     * @return Number of warnings
     */
    public Integer getNumberOfWarnings(Integer userId) {
        List<IntuoProtocolEntity> protocols = getUsersProtocols(userId);
        Integer numberOfWarnings = 0;

        for(IntuoProtocolEntity protocol : protocols) {
            if(protocolWarn(protocol) == true)
                numberOfWarnings += 1;
        }

        return numberOfWarnings;
    }

    /**
     * Get all protocols from Intuo belonging to user specified by userId.
     *
     * @param userId ID of user for which you want to get protocols
     * @return all protocols
     */
    private List<IntuoProtocolEntity> getUsersProtocols(Integer userId) {
        List<IntuoEquipmentEntity> intuoEquipment = getIntuoEquipments(userId, 1).getItemList();

        List<IntuoProtocolEntity> protocols = new ArrayList<>();
        for(IntuoEquipmentEntity eq : intuoEquipment) {
            Integer eqId = Integer.parseInt(eq.getC0_id());
            List<IntuoProtocolEntity> eqProtocols = this.getProtocolsForEquipment(eqId, 1).getItemList();
            protocols.addAll(eqProtocols);
        }

        return protocols;
    }


    /**
     * Responds whether a protocol should be warned about
     * expiring or not. If next revision is planned in 30 days
     * or less, then protocol is warned.
     *
     * @param protocol Protocol to be checked for warning
     * @return true for expiring protocol, false for not expiring one
     */
    private boolean protocolWarn(IntuoProtocolEntity protocol) {
        DateFormat dateFormat = new SimpleDateFormat("y-d-MTH:m:s+H:m");
        Date date = null;

        try {
            date = dateFormat.parse(protocol.getC6_dateOfNextInspection());
        } catch (ParseException e) {
            logger.info("IntuoManager/protocolWarn", e);
        }

        //System.out.println(date.getTime() - 30*86000*1000);
        if((date.getTime() - 30*86000*1000) < System.currentTimeMillis())
            return true;
        return false;
    }


    /**
     * Returning list of names of equipment that needs to be
     * coloured red.
     *
     * @param userId
     * @return Equipments which have at least 1 expiring protocol
     */
    public List<String> getExpiringIntuoEquipments( Integer userId ) {
        List<IntuoProtocolEntity> protocols = getUsersProtocols(userId);
        List<String> redIntuoEquipments = new ArrayList<>();
        Date currentDate = new Date();
        for ( IntuoProtocolEntity pro : protocols ) {
            long currentDays = ( ( ( currentDate.getTime() ) / 1000 ) / 3600 ) / 24;
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+01:00'");
            LocalDateTime expirationDate = LocalDateTime.from(f.parse(pro.getC6_dateOfNextInspection()));
            long expiration = ( expirationDate.getSecond() / 3600 ) / 24;
            if ( ( expiration - currentDays ) <= 30 ) {
                /* ******** NEZABUDNIT DOPLNIT ****/
                //redIntuoEquipments.add(pro.getC1_poradiDleTypu()); - tu musi byt: pro.getPristrojId()
                break;
           }
        }
        return redIntuoEquipments;
    }
}

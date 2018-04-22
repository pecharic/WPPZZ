package medictonproject.buisness;

import medictonproject.model.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MappedEquipmentManager {

    private EquipmentManager equipmentManager;

    private IntuoManager intuoManager;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(IntuoManager.class);

    /**
     * MappedEquipmentManager constructor
     */

    @Autowired
    public MappedEquipmentManager(EquipmentManager equipmentManager, IntuoManager intuoManager) {
        this.equipmentManager = equipmentManager;
        this.intuoManager = intuoManager;
    }


    /**
     * Gets all the equipments (local and intuo), maps to one arrayList
     *
     * @param userId - id of user
     * @return list
     */

    public List<MappedEquipmentEntity> getAllEquipments (Integer userId){
        List<EquipmentEntity> localEquipments = new ArrayList<>();
        localEquipments = equipmentManager.getLocalEquipments(userId);

        List<IntuoEquipmentEntity> intuoEquipments = new ArrayList<>();
        try {
            intuoEquipments = intuoManager.getIntuoEquipments(userId);
        }catch (Exception e){
            logger.info("MappedEquipmentManager/getAllEquipments", e);
        }


        List<MappedEquipmentEntity> allEquipments = new ArrayList<>();

        for (EquipmentEntity equipment : localEquipments){
            MappedEquipmentEntity mappedEquipment = new MappedEquipmentEntity();

            mappedEquipment.setEquipmentId( equipment.getEquipmentId() );
            mappedEquipment.setBuisnessName( equipment.getBuisnessName() );
            mappedEquipment.setSerialNumber( equipment.getSerialNumber() );
            mappedEquipment.setRiskClass( equipment.getRiskClass() );
            mappedEquipment.setProducer( equipment.getProducer() );
            mappedEquipment.setServisContact( "Service contact" );
            mappedEquipment.setStartDate( equipment.getStartDate() );
            mappedEquipment.setType("Local");

            allEquipments.add(mappedEquipment);
        }

        for (IntuoEquipmentEntity equipment : intuoEquipments){
            MappedEquipmentEntity mappedEquipment = new MappedEquipmentEntity();

            mappedEquipment.setEquipmentId( Integer.parseInt(equipment.getC0_id()) );
            mappedEquipment.setBuisnessName( equipment.getC2_equipmentModel() );
            mappedEquipment.setSerialNumber( equipment.getC3_serialNumber() );
            mappedEquipment.setRiskClass( equipment.getC4_equipmentClass() );
            mappedEquipment.setProducer( equipment.getC5_producer() );
            mappedEquipment.setServisContact( "Service contact" );
            mappedEquipment.setStartDate( new java.sql.Date(0) );
            mappedEquipment.setType("Intuo");

            allEquipments.add(mappedEquipment);
        }

        return allEquipments;
    }


    /**
     * Gets all the equipments (local and intuo), maps to one arrayList and passes to paginator
     *
     * @param userId - id of user
     * @param page - page number
     * @return paginator
     */

    public Paginator<MappedEquipmentEntity> getAll (Integer userId,
                                                    Integer page ) {
        List<MappedEquipmentEntity> allEquipments = getAllEquipments(userId);
        return new Paginator<>(allEquipments, page, Constants.PAGE_SIZE_EQUIPMENT);
    }

    /**
     * Gets all the equipments (local and intuo), maps to one arrayList and passes to paginator
     *
     * @param userId - id of user
     * @param page - page number
     * @return paginator
     */

    public Paginator<MappedEquipmentEntity> getSortedEquipments (Integer userId,
                                                                 Integer page,
                                                                 String mode) {
        String attribute = mode.split("-")[0];
        Boolean asc = mode.split("-")[1].equals("asc");

        List<MappedEquipmentEntity> allEquipments = getAllEquipments(userId);

        sortByAttribute(allEquipments, attribute, asc);

        return new Paginator<>(allEquipments, page, Constants.PAGE_SIZE_EQUIPMENT);
    }

    private void sortByAttribute(List<MappedEquipmentEntity> eqList, String attribute, Boolean asc){
        switch (attribute){
            case "id":
                if (asc)
                    eqList.sort( (o1, o2) -> (o1.getEquipmentId() - o2.getEquipmentId()) );
                else
                    eqList.sort( (o1, o2) -> (o2.getEquipmentId() - o1.getEquipmentId()) );
                return;

            case "name":
                if (asc)
                    eqList.sort( (o1, o2) -> o1.getBuisnessName().compareTo(o2.getBuisnessName()) );
                else
                    eqList.sort( (o1, o2) -> o2.getBuisnessName().compareTo(o1.getBuisnessName()) );
                return;

            case "serial":
                if (asc)
                    eqList.sort( (o1, o2) -> o1.getSerialNumber().compareTo(o2.getSerialNumber()) );
                else
                    eqList.sort( (o1, o2) -> o2.getSerialNumber().compareTo(o1.getSerialNumber()) );
                return;

            case "risk":
                if (asc)
                    eqList.sort( (o1, o2) -> o1.getRiskClass().compareTo(o2.getRiskClass()) );
                else
                    eqList.sort( (o1, o2) -> o2.getRiskClass().compareTo(o1.getRiskClass()) );
                return;

            case "producer":
                if (asc)
                    eqList.sort( (o1, o2) -> o1.getProducer().compareTo(o2.getProducer()) );
                else
                    eqList.sort( (o1, o2) -> o2.getProducer().compareTo(o1.getProducer()) );
                return;

            case "service":
                if (asc)
                    eqList.sort( (o1, o2) -> o1.getServisContact().compareTo(o2.getServisContact()) );
                else
                    eqList.sort( (o1, o2) -> o2.getServisContact().compareTo(o1.getServisContact()) );
                return;

            case "startdate":
                if (asc)
                    eqList.sort( (o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()) );
                else
                    eqList.sort( (o1, o2) -> o2.getStartDate().compareTo(o1.getStartDate()) );
                return;

            case "type":
                if (asc)
                    eqList.sort( (o1, o2) -> o1.getType().compareTo(o2.getType()) );
                else
                    eqList.sort( (o1, o2) -> o2.getType().compareTo(o1.getType()) );
                return;
        }
    }

}

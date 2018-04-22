package medictonproject.model;

import java.util.ArrayList;

public class IntuoEquipmentEntity {
    private String c0_id;
    private String c1_equipmentType;
    private String c2_equipmentModel;
    private String c3_serialNumber;
    private String c4_equipmentClass;
    private String c5_producer;
    private String address;

    public IntuoEquipmentEntity() {

    }


    public IntuoEquipmentEntity(ArrayList<String> params) {
        this.c0_id = params.get(0);
    }

    public String getC0_id() {
        return c0_id;
    }

    public void setC0_id(String c0_id) {
        this.c0_id = c0_id;
    }

    public String getC1_equipmentType() {
        return c1_equipmentType;
    }

    public void setC1_equipmentType(String c1_equipmentType) {
        this.c1_equipmentType = c1_equipmentType;
    }

    public String getC2_equipmentModel() {
        return c2_equipmentModel;
    }

    public void setC2_equipmentModel(String c2_equipmentModel) {
        this.c2_equipmentModel = c2_equipmentModel;
    }

    public String getC3_serialNumber() {
        return c3_serialNumber;
    }

    public void setC3_serialNumber(String c3_serialNumber) {
        this.c3_serialNumber = c3_serialNumber;
    }

    public String getC4_equipmentClass() {
        return c4_equipmentClass;
    }

    public void setC4_equipmentClass(String c4_equipmentClass) {
        this.c4_equipmentClass = c4_equipmentClass;
    }

    public String getC5_producer() {
        return c5_producer;
    }

    public void setC5_producer(String c5_producer) {
        this.c5_producer = c5_producer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "IntuoEquipmentEntity{" +
                "c0_id='" + c0_id + '\'' +
                ", c1_equipmentType='" + c1_equipmentType + '\'' +
                ", c2_equipmentModel='" + c2_equipmentModel + '\'' +
                ", c3_serialNumber='" + c3_serialNumber + '\'' +
                ", c4_equipmentClass='" + c4_equipmentClass + '\'' +
                ", c5_producer='" + c5_producer + '\'' +
                '}';
    }
}

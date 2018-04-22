package medictonproject.model;


public class IntuoOrganizationEntity {
    private String c8_adresaId;
    private String c10_adresaUlice;

    public IntuoOrganizationEntity() {
    }

    public IntuoOrganizationEntity(String c8_adresaId, String c10_adresaUlice) {
        this.c8_adresaId = c8_adresaId;
        this.c10_adresaUlice = c10_adresaUlice;
    }

    public String getC8_adresaId() {
        return c8_adresaId;
    }

    public void setC8_adresaId(String c8_adresaId) {
        this.c8_adresaId = c8_adresaId;
    }

    public String getC10_adresaUlice() {
        return c10_adresaUlice;
    }

    public void setC10_adresaUlice(String c10_adresaUlice) {
        this.c10_adresaUlice = c10_adresaUlice;
    }
}

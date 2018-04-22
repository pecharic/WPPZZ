package medictonproject.integration;

import javafx.util.Pair;
import medictonproject.model.IntuoEquipmentEntity;
import medictonproject.model.IntuoOrganizationEntity;
import medictonproject.model.IntuoProtocolEntity;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;

@Service
public class XMLParseManager
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserDAO.class);

    /**
     * Read XML from Intuo WS input stream and put the input
     * into String. Then return it.
     *
     * @param connInput InputStream of IntuoWS
     * @return
     * @throws IOException
     */
    public String read(InputStream connInput) throws IOException {
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(connInput));
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    /**
     * Get single attribute from XML document. For example if XML
     * looks like this:
     * <authKey>
     *     aKey
     * </authKey>
     * then just take what's inside authKey element and return it as String.
     *
     * @param XMLInput XML to be parsed
     * @param attributeName name of the attribute to be parsed
     * @return content of the element named attributeName
     */
    public String GetSingleAtribute(String XMLInput, String attributeName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // documentbuilder can not be created
            logger.info("XMLParser", e);
        }

        try {
            document = builder.parse( new InputSource( new StringReader( XMLInput) ) );
        } catch (SAXException e) {
            // parse error
            e.printStackTrace();
        } catch (IOException e) {
            // IO error
            logger.info("XMLParser", e);
        }

        document.getDocumentElement().normalize();
        NodeList nList = document.getElementsByTagName(attributeName);
        Element node = (Element) nList.item(0);

        return node.getTextContent();
    }


    /**
     * In XML document find all the elements named attributeName,
     * get their values and and return them as NodeList.
     *
     * @param XMLInput XML to be parsed
     * @param attributeName Name of the element which values will be parsed
     *                      out of XMLInput
     * @return Parsed values
     */
    public NodeList GetValuesByAttribute(String XMLInput, String attributeName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.info("XMLParser", e);
        }

        try {
            document = builder.parse( new InputSource( new StringReader(XMLInput) ) );
        } catch (SAXException e) {
            // parse error occured
            e.printStackTrace();
        } catch (IOException e) {
            // IO error occured
            logger.info("XMLParser", e);
        }

        document.getDocumentElement().normalize();
        NodeList nList = document.getElementsByTagName(attributeName);
        /*for ( int i = 0; i < nList.getLength(); i++ ) {
           // System.out.println(nList.item(i).getTextContent());
        }*/

        return nList;
    }


    /**
     * From XML document of equipment belonging to a organization from Intuo
     * parse all the equipment with values and return it.
     *
     * @param XMLInput XML to be parsed
     * @return List of equipment
     */
    public ArrayList<IntuoEquipmentEntity> GetEquipmentsForOrganization(String XMLInput) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // documentBuilder can not be created
            logger.info("XMLParser", e);
        }
        try {
            document = builder.parse( new InputSource( new StringReader(XMLInput) ) );
        } catch (SAXException e) {
            // parse error occured
            e.printStackTrace();
        } catch (IOException e) {
            // IO error occured
            logger.info("XMLParser", e);
        }

        document.getDocumentElement().normalize();
        NodeList tableList = document.getElementsByTagName("Table");

        ArrayList<IntuoEquipmentEntity> equipmentList = new ArrayList<>();

        for ( int i = 0; i < tableList.getLength(); i ++ ) {
            equipmentList.add(GetEquipmentFromTable((Element)tableList.item(i)));
        }

        return equipmentList;
    }


    /**
     * Parse a single equipment from a Table element
     *
     * @param table Table element with equipment's attributes
     * @return Parsed equipment
     */
    private IntuoEquipmentEntity GetEquipmentFromTable(Element table) {
        ArrayList<Pair<String,String>> elementsNames = new ArrayList<>();
        elementsNames.add(new Pair<>("c0", "c0_id"));
        elementsNames.add(new Pair<>("c1", "c1_equipmentType"));
        elementsNames.add(new Pair<>("c2", "c2_equipmentModel"));
        elementsNames.add(new Pair<>("c3", "c3_serialNumber"));
        elementsNames.add(new Pair<>("c4", "c4_equipmentClass"));
        elementsNames.add(new Pair<>("c5", "c5_producer"));

        IntuoEquipmentEntity equipment = new IntuoEquipmentEntity();
        PropertyAccessor myAccessor = PropertyAccessorFactory.forBeanPropertyAccess(equipment);

        for(int i = 0; i < elementsNames.size(); i ++ ) {
            Node node = table.getElementsByTagName(elementsNames.get(i).getKey()).item(0);
            if(node == null)
                myAccessor.setPropertyValue(elementsNames.get(i).getValue(), "");
            else
                myAccessor.setPropertyValue(elementsNames.get(i).getValue(), node.getTextContent());
        }

        return equipment;
    }

    /**
     * From XML document of organizations from Intuo
     * parse all the organizations with certain values and return it.
     *
     * @param XMLInput XML to be parsed
     * @return List of equipment
     */
    public ArrayList<IntuoOrganizationEntity> GetOrganizations(String XMLInput) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // documentBuilder can not be created
            logger.info("XMLParser", e);
        }
        try {
            document = builder.parse( new InputSource( new StringReader(XMLInput) ) );
        } catch (SAXException e) {
            // parse error occured
            e.printStackTrace();
        } catch (IOException e) {
            // IO error occured
            logger.info("XMLParser", e);
        }

        document.getDocumentElement().normalize();
        NodeList tableList = document.getElementsByTagName("Table");

        ArrayList<IntuoOrganizationEntity> organizationsList= new ArrayList<>();

        for ( int i = 0; i < tableList.getLength(); i ++ ) {
            organizationsList.add(GetOrganizationFromTable((Element)tableList.item(i)));
        }

        return organizationsList;
    }

    /**
     * Parse a single organization from a Table element
     *
     * @param table Table element with organization's attributes
     * @return Parsed organization
     */
    private IntuoOrganizationEntity GetOrganizationFromTable(Element table) {
        ArrayList<Pair<String,String>> elementsNames = new ArrayList<>();
        elementsNames.add(new Pair<>("c8", "c8_adresaId"));
        elementsNames.add(new Pair<>("c10", "c10_adresaUlice"));

        IntuoOrganizationEntity organization = new IntuoOrganizationEntity();
        PropertyAccessor myAccessor = PropertyAccessorFactory.forBeanPropertyAccess(organization);

        for(int i = 0; i < elementsNames.size(); i ++ ) {
            Node node = table.getElementsByTagName(elementsNames.get(i).getKey()).item(0);
            if(node == null)
                myAccessor.setPropertyValue(elementsNames.get(i).getValue(), "");
            else
                myAccessor.setPropertyValue(elementsNames.get(i).getValue(), node.getTextContent());
        }

        return organization;
    }


    /**
     * Parses all the protocols for one single equipment out of XML from Intuo
     * and returns them.
     *
     * @param XMLInput XML to be parsed
     * @return all protocols for a single equipment
     */
    public ArrayList<IntuoProtocolEntity> GetProtocolsForEquipment(String XMLInput) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // documentBuilder can not be created
            logger.info("XMLParser", e);
        }
        try {
            document = builder.parse( new InputSource( new StringReader(XMLInput) ) );
        } catch (SAXException e) {
            // parse error occured
            logger.info("XMLParser", e);
        } catch (IOException e) {
            // IO error occured
            logger.info("XMLParser", e);
        }

        document.getDocumentElement().normalize();
        NodeList tableList = document.getElementsByTagName("Table");

        ArrayList<IntuoProtocolEntity> protocolsList = new ArrayList<>();

        for ( int i = 0; i < tableList.getLength(); i ++ ) {
            protocolsList.add(GetProtocolFromTable((Element)tableList.item(i)));
        }

        return protocolsList;
    }


    /**
     * Parse a single protocol from a Table element
     *
     * @param table Table element with equipment's attributes
     * @return Parsed equipment
     */
    private IntuoProtocolEntity GetProtocolFromTable(Element table) {
        ArrayList<Pair<String,String>> elementsNames = new ArrayList<>();
        elementsNames.add(new Pair<>("c0", "c0_id"));
        elementsNames.add(new Pair<>("c1", "c1_equipmentType"));
        elementsNames.add(new Pair<>("c2", "c2_date"));
        elementsNames.add(new Pair<>("c3", "c3_protocolNumber"));
        elementsNames.add(new Pair<>("c4", "c4_protocolType"));
        elementsNames.add(new Pair<>("c5", "c5_subject"));
        elementsNames.add(new Pair<>("c6", "c6_dateOfNextInspection"));
        elementsNames.add(new Pair<>("c7", "c7_doneBy"));

        IntuoProtocolEntity protocols = new IntuoProtocolEntity();
        PropertyAccessor myAccessor = PropertyAccessorFactory.forBeanPropertyAccess(protocols);

        for(int i = 0; i < elementsNames.size(); i ++ ) {
            Node node = table.getElementsByTagName(elementsNames.get(i).getKey()).item(0);
            if(node == null)
                myAccessor.setPropertyValue(elementsNames.get(i).getValue(), "");
            else
                myAccessor.setPropertyValue(elementsNames.get(i).getValue(), node.getTextContent());
        }

        return protocols;
    }
}
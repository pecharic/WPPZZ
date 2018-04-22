package medictonproject.integration;

import medictonproject.model.Constants;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@Service
public class IntuoDAO {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(IntuoDAO.class);

    private final XMLParseManager parser;

    @Autowired
    public IntuoDAO(XMLParseManager parser) {
        this.parser = parser;
    }

    /**
     * Gets authorization token from Intuo WS for future communication
     * with Intuo WS.
     *
     * @return Intuo WS authorization key
     */
    public String getAuthKey() {
        String urlParameters = "strLoginName=" + Constants.intuoUsername +
                               "&strPassword=" + Constants.intuoPassword;
        InputStream inputS = createConnection(Constants.urlLogin, urlParameters, "POST" );

        String key = null;
        try {
            key = parser.GetSingleAtribute(parser.read(inputS), "string");
        } catch (Exception e) {
            logger.info("IntuoDAO getAuthKey", e);
        }

        //System.out.println(key);
        return key;
    }


    /**
     * Function calls Intuo WS method GetView with exact parameters.
     * Parser reads XML and makes string out of that.
     *
     * @param key
     * @param nViewID
     * @param strXMLDefinitionOfRestriction
     * @return XML String
     */
    public String intuoGetView(String key, Integer nViewID, String strXMLDefinitionOfRestriction) {
        //System.out.println(key);
        String param_strAuthKey = null;
        String param_nViewID    = null;
        String param_strXMLDefinitionOfRestriction = null;
        try {
            param_strAuthKey = "strAuthKey=" + URLEncoder.encode(key, StandardCharsets.UTF_8.toString());
            param_nViewID = "nViewID=" + URLEncoder.encode(Integer.toString(nViewID), StandardCharsets.UTF_8.toString());
            param_strXMLDefinitionOfRestriction = "strXMLDefinitionOfRestriction=" + URLEncoder.encode(strXMLDefinitionOfRestriction, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            // unsupported encoding
            logger.info("IntuoDAO intuogetView", e);
        }

        String urlParameters = param_strAuthKey + "&" +
                               param_nViewID    + "&" +
                               param_strXMLDefinitionOfRestriction;

        InputStream inputS = createConnection(Constants.urlGetView, urlParameters, "GET");

        String response = null;
        try {
            response = parser.read(inputS);
        } catch (Exception e) {
            logger.info("IntuoDAO intuoGetView after parser.read(input) method", e);
            e.printStackTrace();
        }

        //System.out.println(response);

        return response;
    }


    /**
     * Function calls Intuo WS method GetView with exact parameters.
     * Parser reads XML and makes string out of that.
     *
     * @param key
     * @param nViewID
     * @param nFromID
     * @return XML String response from Intuo WS
     */
    public String intuoGetBindingView(String key, Integer nViewID, String nFromID) {
        String param_strAuthKey = null;
        String param_nViewID    = null;
        String param_nFromID    = null;
        String param_strXMLDefinitionOfRestriction = "strXMLDefinitionOfRestriction=";
        try {
            param_strAuthKey = "strAuthKey=" + URLEncoder.encode(key, StandardCharsets.UTF_8.toString());
            param_nViewID = "nViewID=" + URLEncoder.encode(Integer.toString(nViewID), StandardCharsets.UTF_8.toString());
            param_nFromID = "nFromID=" + URLEncoder.encode(nFromID, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            logger.info("IntuoDAO intuoGetBindingView", e);
        }


        String urlParameters = param_strAuthKey + "&" +
                               param_nViewID    + "&" +
                               param_nFromID    + "&" +
                               param_strXMLDefinitionOfRestriction;

        //System.out.println(urlParameters);
        InputStream inputS = createConnection(Constants.urlGetBindingView, urlParameters, "GET");

        String response = null;
        try {
            response = parser.read(inputS);
        } catch (IOException e) {
            logger.info("IntuoDAO getBindingView after parser.read(inputS)", e);
        }

        //System.out.println(response);
        return response;
    }

    /**
     * Function opens connection to certain URL, sends parameters to this URL,
     * obtains InputStream ready for reading for this connection and returns it.
     *
     * @param url
     * @param urlParameters
     * @param type
     * @return InputStream of connection
     */
    private InputStream createConnection(String url, String urlParameters, String type) {
        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            // malformed URL
            logger.info("IntuoDAO createConnection", e);
        }

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {
            // chyba pri vytvarani connection
            logger.info("IntuoDAO createConnection after obj.openConnection", e);
        }

        try {
            con.setRequestMethod(type);
        } catch (ProtocolException e) {
            logger.info("IntuoDAO createConnection after setRequestMethod", e);
        }

        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setDoOutput(true);

        DataOutputStream wr = null;
        int responseCode = 0;
        try {
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            responseCode = con.getResponseCode();
        } catch (IOException e) {
            // chyba pri zapise do socketu
            logger.info("IntuoDAO", e);
        }

       // System.out.println("\nSending +" + type + " request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);

        InputStream is = null;
        try {
            is = con.getInputStream();
        } catch (IOException e) {
            // chyba pri ziskavani input streamu
            logger.info("IntuoDAO getAuthKey", e);
        }

        return is;
    }
}

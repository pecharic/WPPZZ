package medictonproject.buisness;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenManager
{
    public TokenManager() {}
    private long EXPIRATIONTIME = 1000 * 60 * 20;
    private String secret = "keyfortoken";
    private String headerString = "Authorization";
    
    public void addAuthentication(HttpServletResponse response, int id) {
        String JWT = Jwts.builder()
                .setSubject(String.valueOf(id))
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration( new Date( new Date().getTime() + EXPIRATIONTIME ))
                .compact();

//        response.addHeader("Access-Control-Allow-Headers", "Authorization, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
//                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
//        response.addHeader("Access-Control-Expose-Headers", "Authorization, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
//                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
//        response.addHeader("Access-Control-Allow-Credentials","true");
//        response.addHeader("Access-Control-Allow-Origin","*");
//        response.addHeader("Access-Control-Allow-Methods","GET,POST,PUT,OPTIONS");

        response.addHeader(headerString, JWT);

    }

    public int authToken(String t) {
        String token = t;
        if (token != null) {
            String id = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            if (id != null) {
                return Integer.parseInt( id );
            }
        }
        else {
            //System.out.println("Nenasiel som to ");
        }
        return -1;
    }
}


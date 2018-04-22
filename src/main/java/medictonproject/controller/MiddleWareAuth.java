package medictonproject.controller;

import medictonproject.buisness.TokenManager;
import medictonproject.integration.UserDAO;
import medictonproject.model.UserEntity;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@WebFilter(urlPatterns = "/*")
public class MiddleWareAuth implements Filter {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MiddleWareAuth.class);
    /**
     * insance of TokenMangaer
     */
    private TokenManager jwt;
    /**
     * insance of UseDao
     */
    private UserDAO userDAO;

    /**
     * Constructor for class
     */
    public MiddleWareAuth(){}
    @Autowired
    public MiddleWareAuth( TokenManager jwt, UserDAO userDAO ) {
        this.jwt = jwt;
        this.userDAO = userDAO;
    }

    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    /**
     * Function for parsing token.
     *
     * @param request ServletRequest
     * @param response ServletResponse
     * @param chain FilterChain
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String xHeader = ((HttpServletRequest)request).getHeader("Authorization");
        HttpServletResponse res = (HttpServletResponse)response;
        String path = ((HttpServletRequest) request).getRequestURI();
        //System.out.println("--> " + path);
        String requestMethod = ((HttpServletRequest) request).getMethod();
        res.addHeader("Access-Control-Allow-Headers", "Authorization, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        res.addHeader("Access-Control-Expose-Headers", "Authorization, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        res.addHeader("Access-Control-Allow-Credentials","true");
        res.addHeader("Access-Control-Allow-Origin","*");
        res.addHeader("Access-Control-Allow-Methods","GET,POST,PUT,OPTIONS");

        if(requestMethod.equals("OPTIONS")) {
            //System.out.println("OK OPTIONS");
            res.setStatus(200);

            return;
        }

        // favicon.ico sa vzdy pri requeste vyvolava
        if( path.equals("/medicton/")  || path.equals("/medicton/index.html") || path.equals("/medicton/bundle.js") || path.contains("/user/login") || path.contains("/user/register")  || requestMethod.contains("OPTIONS") ||
                path.contains("/home/") || path.contains("/faq/getGlobal") || path.contains("/favicon.ico") ) {
            //System.out.println("preskakujem path");
            chain.doFilter(request,response);
            return;
        }

      //  System.out.println("nepreskakujem path");

        int id;
        try {
           // System.out.println(xHeader);
            id = jwt.authToken( xHeader );
            UserEntity user = userDAO.getUserById( id );
            if( id == -1 || user.getIsAdmin() == -1 ) {
                res.setStatus( 501 );
                return;
            }
           // System.out.println("SOM ID " + id );
            if( path.contains( "/admin/" ) ) {
                if( user.getIsAdmin() != 1 ) {
                    res.setStatus( 501 );
                   // System.out.println( "Neni admin" );
                    return;
                }
            }
            request.setAttribute("userId", id );
           // System.out.println("AHAHAH" + id );
            chain.doFilter( request , response);
        } catch( Exception e ) {
            //System.out.println("chyba");
            logger.info("Middleware exception", e);
            res.setStatus(404);
        }
    }
}

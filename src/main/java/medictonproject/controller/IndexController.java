package medictonproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin(origins = "*")
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
//    @RequestMapping("/bundle.js")
//    @ResponseBody
//    public String bundle() {
//        return "bundle.js";
//    }
}

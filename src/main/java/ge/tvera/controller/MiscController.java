package ge.tvera.controller;

import ge.tvera.service.MiscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author ucha
 */
@RequestMapping("/misc")
@Controller
public class MiscController {

    @Autowired
    private MiscService miscService;
//
//    @RequestMapping("/get-languages")
//    @ResponseBody
//    private Response getLanguages() throws Exception {
//        return Response.withSuccess(miscService.getLanguages());
//    }
}

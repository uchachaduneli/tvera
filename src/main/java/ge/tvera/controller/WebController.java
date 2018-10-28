package ge.tvera.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ucha
 */
@Controller
@RequestMapping
public class WebController {

    @RequestMapping("/users")
    public String users() {
        return "users";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/districts")
    public String districts() {
        return "districts";
    }

    @RequestMapping("/streets")
    public String streets() {
        return "streets";
    }

    @RequestMapping("/incasators")
    public String incasators() {
        return "incasators";
    }

    @RequestMapping("/packages")
    public String packages() {
        return "packages";
    }

    @RequestMapping("/abonents")
    public String abonents() {
        return "abonents";
    }

    @RequestMapping("/payments")
    public String payments() {
        return "payments";
    }

    @RequestMapping("/statistics")
    public String statistics() {
        return "statistics";
    }

    @RequestMapping("/monthlybills")
    public String monthlybills() {
        return "monthlybills";
    }

    @RequestMapping("/")
    public String defaultFnc() {
        return "";
    }

}

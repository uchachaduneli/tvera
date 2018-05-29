package ge.tvera.controller;

import ge.tvera.dto.AbonentDTO;
import ge.tvera.misc.Response;
import ge.tvera.service.AbonentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * @author ucha
 */
@RequestMapping("/abonent")
@Controller
public class AbonentController {

    @Autowired
    private AbonentService abonentService;

    @RequestMapping("/get-abonents")
    @ResponseBody
    private Response getAbonents(@RequestParam("start") int start, @RequestParam("limit") int limit,
                                 @RequestBody AbonentDTO request, HttpServletRequest servletRequest) throws Exception {
        return Response.withSuccess(abonentService.getAbonents(start, limit, request));
    }

    @RequestMapping({"/save-abonent"})
    @ResponseBody
    public Response saveAbonent(@RequestBody AbonentDTO request) throws Exception {
        return Response.withSuccess(AbonentDTO.parse(abonentService.saveAbonent(request)));
    }

    @RequestMapping({"/delete-abonent"})
    @ResponseBody
    public Response deleteAbonent(@RequestParam int id) {
        abonentService.deleteAbonent(id);
        return Response.withSuccess(true);
    }

    @RequestMapping({"/get-status-history"})
    @ResponseBody
    public Response getStatusHistory(@RequestParam int id) {
        return Response.withSuccess(abonentService.getStatusHistory(id));
    }
}

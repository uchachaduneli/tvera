package ge.tvera.controller;

import ge.tvera.dto.StreetDTO;
import ge.tvera.misc.Response;
import ge.tvera.service.StreetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author ucha
 */
@RequestMapping("/street")
@Controller
public class StreetController {

    @Autowired
    private StreetService streetService;

    @RequestMapping("/get-streets")
    @ResponseBody
    private Response getSreets(@RequestParam("start") int start, @RequestParam("limit") int limit,
                               @RequestBody StreetDTO request) throws Exception {
        return Response.withSuccess(streetService.getStreets(start, limit, request));
    }

    @RequestMapping("/get-all-streets")
    @ResponseBody
    private Response getAllSreets() throws Exception {
        return Response.withSuccess(streetService.getAllStreets());
    }

    @RequestMapping({"/save-street"})
    @ResponseBody
    public Response saveStreet(@RequestBody StreetDTO request) throws Exception {
        return Response.withSuccess(StreetDTO.parse(streetService.saveStreet(request)));
    }

    @RequestMapping({"/delete-street"})
    @ResponseBody
    public Response deleteStreet(@RequestParam int id) {
        streetService.deleteStreet(id);
        return Response.withSuccess(true);
    }
}

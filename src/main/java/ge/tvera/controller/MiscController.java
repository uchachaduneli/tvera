package ge.tvera.controller;

import ge.tvera.dto.DistrictDTO;
import ge.tvera.dto.IncasatorDTO;
import ge.tvera.misc.Response;
import ge.tvera.service.MiscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author ucha
 */
@RequestMapping("/misc")
@Controller
public class MiscController {

    @Autowired
    private MiscService miscService;

    @RequestMapping("/get-districts")
    @ResponseBody
    private Response getDistricts() throws Exception {
        return Response.withSuccess(miscService.getDistricts());
    }

    @RequestMapping("/get-package-groups")
    @ResponseBody
    private Response getPackageGroups() throws Exception {
        return Response.withSuccess(miscService.getPackageGroups());
    }

    @RequestMapping("/get-package-types")
    @ResponseBody
    private Response getPackageTypes() throws Exception {
        return Response.withSuccess(miscService.getPackageTypes());
    }

    @RequestMapping({"/save-district"})
    @ResponseBody
    public Response saveDistrict(@RequestBody DistrictDTO request) throws Exception {
        return Response.withSuccess(DistrictDTO.parse(miscService.saveDistrict(request)));
    }

    @RequestMapping({"/delete-district"})
    @ResponseBody
    public Response deleteDistrict(@RequestParam int id) {
        miscService.deleteDistrict(id);
        return Response.withSuccess(true);
    }

    @RequestMapping("/get-incasators")
    @ResponseBody
    private Response getIncasators(@RequestParam("start") int start, @RequestParam("limit") int limit,
                                   @RequestBody IncasatorDTO request) throws Exception {
        return Response.withSuccess(miscService.getIncasators(start, limit, request));
    }

    @RequestMapping({"/save-incasator"})
    @ResponseBody
    public Response saveIncasator(@RequestBody IncasatorDTO request) throws Exception {
        return Response.withSuccess(IncasatorDTO.parse(miscService.saveIncasator(request)));
    }

    @RequestMapping({"/delete-incasator"})
    @ResponseBody
    public Response deleteIncasator(@RequestParam int id) {
        miscService.deleteIncasator(id);
        return Response.withSuccess(true);
    }
}

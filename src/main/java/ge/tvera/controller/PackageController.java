package ge.tvera.controller;

import ge.tvera.dto.PackageDTO;
import ge.tvera.misc.Response;
import ge.tvera.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author ucha
 */
@RequestMapping("/package")
@Controller
public class PackageController {

    @Autowired
    private PackageService packageService;

    @RequestMapping("/get-packages")
    @ResponseBody
    private Response getPackages() throws Exception {
        return Response.withSuccess(packageService.getPackages());
    }

    @RequestMapping({"/save-package"})
    @ResponseBody
    public Response savePackage(@RequestBody PackageDTO request) throws Exception {
        return Response.withSuccess(PackageDTO.parse(packageService.savePackage(request)));
    }

    @RequestMapping({"/delete-package"})
    @ResponseBody
    public Response deletePackage(@RequestParam int id) {
        packageService.deletePackage(id);
        return Response.withSuccess(true);
    }
}

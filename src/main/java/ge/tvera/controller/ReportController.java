package ge.tvera.controller;

import ge.tvera.dto.AbonentDTO;
import ge.tvera.misc.Response;
import ge.tvera.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author ucha
 */
@RequestMapping("/abonent")
@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping("/get-count-data")
    @ResponseBody
    private Response getCountrReport(@RequestBody AbonentDTO request) throws Exception {
        return Response.withSuccess(reportService.getAbonents(request));
    }

}

package ge.tvera.controller;

import ge.tvera.dto.PaymentDTO;
import ge.tvera.misc.Response;
import ge.tvera.service.PaymentService;
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
@RequestMapping("/payment")
@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @RequestMapping("/get-payments")
    @ResponseBody
    private Response getPayments(@RequestParam("start") int start, @RequestParam("limit") int limit,
                                 @RequestBody PaymentDTO request, HttpServletRequest servletRequest) throws Exception {
        return Response.withSuccess(paymentService.getPayments(start, limit, request));
    }

    @RequestMapping({"/save-payment"})
    @ResponseBody
    public Response savePayment(@RequestBody PaymentDTO request) throws Exception {
        return Response.withSuccess(PaymentDTO.parse(paymentService.savePayment(request)));
    }

    @RequestMapping({"/delete-payment"})
    @ResponseBody
    public Response deletePayment(@RequestParam int id) {
        paymentService.deletePayment(id);
        return Response.withSuccess(true);
    }
}

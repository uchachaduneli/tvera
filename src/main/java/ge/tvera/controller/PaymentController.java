package ge.tvera.controller;

import ge.tvera.dto.PaymentDTO;
import ge.tvera.misc.Response;
import ge.tvera.service.PaymentService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;


/**
 * @author ucha
 */
@RequestMapping("/payment")
@Controller
public class PaymentController {

  @Autowired
  private PaymentService paymentService;

  @RequestMapping("/download-excell")
  @ResponseBody
  public Response downlodExcell(@RequestBody PaymentDTO request, HttpServletResponse response) throws IOException {
//        String realPath = "C:\\Users\\ucha\\IdeaProjects\\tvera\\src\\main\\webapp\\resources\\excell\\excel.xls"; //leptopi
    String realPath = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\tvera\\resources\\excell\\excel.xls";
//    String realPath = "C:\\Program Files\\Apache Software Foundation\\Apache Tomcat 8.0.27\\webapps\\ROOT\\excel.xls";
    //ექსელის ფაილის შექმნა

    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("FirstSheet");

    HSSFRow rowhead = sheet.createRow((short) 0);
    rowhead.createCell(0).setCellValue("ID");
    rowhead.createCell(1).setCellValue("აბონენტის N");
    rowhead.createCell(2).setCellValue("აბონენტი(ტარიფი)");
    rowhead.createCell(3).setCellValue("უბანი");
    rowhead.createCell(4).setCellValue("ინკასატორი");
    rowhead.createCell(5).setCellValue("თანხა");
    rowhead.createCell(6).setCellValue("ავანსი");
    rowhead.createCell(7).setCellValue("დავალ");
    rowhead.createCell(8).setCellValue("ქვითრის N");
    rowhead.createCell(9).setCellValue("გადახდის თარიღი");
    rowhead.createCell(10).setCellValue("ოპერაციის თარიღი");
    rowhead.createCell(11).setCellValue("ქუჩა");
    rowhead.createCell(12).setCellValue("ბინის N");

    HSSFCellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
    cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

    for (int i = 0; i < rowhead.getPhysicalNumberOfCells(); i++) {
      HSSFCell cell = rowhead.getCell(i);
      cell.setCellStyle(cellStyle);
    }

    SimpleDateFormat dtfrmt = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat dtfrmt1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    HSSFRow row;
    HashMap<String, Object> res = paymentService.getPayments(0, 99999, request);
    List<PaymentDTO> exportList = (List<PaymentDTO>) res.get("list");
    PaymentDTO obj = null;
    for (int i = 1; i <= exportList.size(); i++) {
      obj = exportList.get(i - 1);
      row = sheet.createRow(i);
      row.createCell(0).setCellValue("" + obj.getId());
      row.createCell(1).setCellValue(obj.getAbonent().getId());
      row.createCell(2).setCellValue(obj.getAbonent().getName() + " " + obj.getAbonent().getLastname() + " (" + obj.getAbonent().getBill() + ")");
      row.createCell(3).setCellValue(obj.getAbonent().getDistrict().getName());
      row.createCell(4).setCellValue(obj.getAbonent().getDistrict().getIncasator().getName() + " " + obj.getAbonent().getDistrict().getIncasator().getName());
      row.createCell(5).setCellValue(obj.getAmount());
      row.createCell(6).setCellValue(obj.getAvans());
      row.createCell(7).setCellValue(obj.getDaval());
      row.createCell(8).setCellValue(obj.getCheckNumber());
      row.createCell(9).setCellValue(dtfrmt.format(obj.getPayDate()));
      row.createCell(10).setCellValue(dtfrmt.format(obj.getOperationDate()));
      row.createCell(11).setCellValue(obj.getAbonent().getStreet().getName() + " N" + obj.getAbonent().getStreetNumber());
      row.createCell(12).setCellValue(obj.getAbonent().getRoomNumber());

    }
    row = sheet.createRow(exportList.size() + 1);
    row.createCell(0).setCellValue("");
    row.createCell(1).setCellValue("");
    row.createCell(2).setCellValue("");
    row.createCell(3).setCellValue("");
    row.createCell(4).setCellValue("");
    row.createCell(5).setCellValue("სულ " + res.get("total"));
    row.createCell(6).setCellValue("სულ " + res.get("avansTotal"));
    row.createCell(7).setCellValue("სულ " + res.get("davalTotal"));
    row.createCell(8).setCellValue("");
    row.createCell(9).setCellValue("");
    row.createCell(10).setCellValue("");
    row.createCell(11).setCellValue("");
    row.createCell(12).setCellValue("");

    FileOutputStream fileOut = new FileOutputStream(realPath);
    workbook.write(fileOut);
    fileOut.close();
    System.out.println("Your excel generated!");

    return Response.ok();
  }

  @RequestMapping("/get-payments")
  @ResponseBody
  private Response getPayments(@RequestParam("start") int start, @RequestParam("limit") int limit,
                               @RequestBody PaymentDTO request, HttpServletRequest servletRequest) throws Exception {

    return Response.withSuccess(paymentService.getPayments(start, limit, request));
  }

  @RequestMapping({"/save-payment"})
  @ResponseBody
  public Response savePayment(@RequestBody PaymentDTO request, HttpServletRequest servletRequest) throws Exception {
    request.setUserId((Integer) servletRequest.getSession().getAttribute("userId"));
    return Response.withSuccess(PaymentDTO.parse(paymentService.savePayment(request)));
  }

  @RequestMapping({"/delete-payment"})
  @ResponseBody
  public Response deletePayment(@RequestParam int id) {
    paymentService.deletePayment(id);
    return Response.withSuccess(true);
  }
}

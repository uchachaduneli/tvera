package ge.tvera.controller;

import ge.tvera.dto.MonthlyBillsDTO;
import ge.tvera.misc.Response;
import ge.tvera.service.AbonentService;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;


/**
 * @author ucha
 */
@RequestMapping("/mobthlybills")
@Controller
public class MonthlyBillsController {

  @Autowired
  private AbonentService abonentService;

  @RequestMapping({"/save"})
  @ResponseBody
  public Response saveStreet(@RequestBody MonthlyBillsDTO request, HttpServletRequest servletRequest) throws Exception {

    return Response.withSuccess(MonthlyBillsDTO.parse(abonentService.updateMonthlyBill(request, ((Integer) servletRequest.getSession().getAttribute("userId")))));
  }

  @RequestMapping("/download-excell")
  @ResponseBody
  public Response downlodExcell(@RequestBody MonthlyBillsDTO request, HttpServletResponse response) throws IOException {
//        String realPath = "C:\\Users\\ucha\\IdeaProjects\\tvera\\src\\main\\webapp\\resources\\excell\\excel.xls"; //leptopi
    String realPath = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ROOT\\excel.xls";
//    String realPath = "C:\\Users\\home\\Desktop\\attachments\\excell.xls";
    //ექსელის ფაილის შექმნა

    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("FirstSheet");

    HSSFRow rowhead = sheet.createRow((short) 0);
    rowhead.createCell(0).setCellValue("ID");
    rowhead.createCell(1).setCellValue("აბონენტის N");
    rowhead.createCell(2).setCellValue("აბონენტი(ტარიფი)");
    rowhead.createCell(3).setCellValue("უბანი");
    rowhead.createCell(4).setCellValue("ინკასატორი");
    rowhead.createCell(5).setCellValue("ქუჩა");
    rowhead.createCell(6).setCellValue("თანხა");
    rowhead.createCell(7).setCellValue("ოპერაციის თარიღი");

    HSSFCellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
    cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

    for (int i = 0; i < rowhead.getPhysicalNumberOfCells(); i++) {
      HSSFCell cell = rowhead.getCell(i);
      cell.setCellStyle(cellStyle);
    }

    SimpleDateFormat dtfrmt = new SimpleDateFormat("MM-yyyy");
    DecimalFormat df2 = new DecimalFormat(".##");

    HSSFRow row;
    HashMap<String, Object> res = abonentService.getMonthlyBills(0, 99999, request);
    List<MonthlyBillsDTO> exportList = (List<MonthlyBillsDTO>) res.get("list");
    MonthlyBillsDTO obj = null;
    for (int i = 1; i <= exportList.size(); i++) {
      obj = exportList.get(i - 1);
      row = sheet.createRow(i);
      row.createCell(0).setCellValue("" + obj.getId());
      row.createCell(1).setCellValue(obj.getAbonent().getId());
      row.createCell(2).setCellValue(obj.getAbonent().getName() + " " + obj.getAbonent().getLastname() + " (" + obj.getAbonent().getBill() + ")");
      row.createCell(3).setCellValue(obj.getAbonent().getDistrict().getName());
      row.createCell(4).setCellValue(obj.getAbonent().getDistrict().getIncasator().getName() + " " + obj.getAbonent().getDistrict().getIncasator().getName());
      row.createCell(5).setCellValue(obj.getAbonent().getStreet().getName() + " N" + obj.getAbonent().getStreetNumber() + ", ბინა N " + obj.getAbonent().getRoomNumber());
      row.createCell(6).setCellValue(df2.format(obj.getAmount()));
      row.createCell(7).setCellValue(dtfrmt.format(obj.getOperDate()));

    }
    row = sheet.createRow(exportList.size() + 2);
    row.createCell(0).setCellValue("");
    row.createCell(1).setCellValue("");
    row.createCell(2).setCellValue("");
    row.createCell(3).setCellValue("");
    row.createCell(4).setCellValue("");
    row.createCell(5).setCellValue("");
    row.createCell(6).setCellValue("სულ " + res.get("total"));
    row.createCell(7).setCellValue("");

    FileOutputStream fileOut = new FileOutputStream(realPath);
    workbook.write(fileOut);
    fileOut.close();
    System.out.println("Your excel generated!");

    return Response.ok();
  }

  @RequestMapping("/get-mobthlybills")
  @ResponseBody
  private Response getMonthlyBills(@RequestParam("start") int start, @RequestParam("limit") int limit,
                                   @RequestBody MonthlyBillsDTO request, HttpServletRequest servletRequest) throws Exception {

    return Response.withSuccess(abonentService.getMonthlyBills(start, limit, request));
  }
}

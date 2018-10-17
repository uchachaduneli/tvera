package ge.tvera.controller;

import ge.tvera.dto.AbonentDTO;
import ge.tvera.dto.PackageDTO;
import ge.tvera.misc.Response;
import ge.tvera.request.AbonentPackagesRequest;
import ge.tvera.service.AbonentService;
import ge.tvera.service.PackageService;
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
import java.sql.Date;
import java.util.Calendar;
import java.util.List;


/**
 * @author ucha
 */
@RequestMapping("/abonent")
@Controller
public class AbonentController {

  @Autowired
  private AbonentService abonentService;

  @Autowired
  private PackageService packageService;

  @RequestMapping("/get-abonents")
  @ResponseBody
  private Response getAbonents(@RequestParam("start") int start, @RequestParam("limit") int limit,
                               @RequestBody AbonentDTO request, HttpServletRequest servletRequest) throws Exception {
    return Response.withSuccess(abonentService.getAbonents(start, limit, request));
  }

  @RequestMapping("/get-balance-history")
  @ResponseBody
  private Response getAbonents(@RequestBody AbonentDTO request) throws Exception {
    return Response.withSuccess(abonentService.getAbonentBalanceHistory(request));
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

  @RequestMapping({"/change-abonent-status"})
  @ResponseBody
  public Response changeServiceStatus(@RequestParam int id, @RequestParam(value = "date", required = false) Date date, HttpServletRequest servletRequest) {
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();

    cal1.setTime(date);
    cal2.setTime(new java.util.Date());

    if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
      abonentService.changeServiceStatus(id, date, ((Integer) servletRequest.getSession().getAttribute("userId")));
      return Response.withSuccess(true);
    } else {
      return Response.withError("ცვლილება შესაძლებელია მხოლოდ მიმდინარე თვის ჭრილში, გთხოვთ შეცვალოთ თარიღი");
    }
  }

  @RequestMapping({"/get-status-history"})
  @ResponseBody
  public Response getStatusHistory(@RequestParam int id) {
    return Response.withSuccess(abonentService.getStatusHistory(id));
  }

  @RequestMapping({"/get-abonent-packages"})
  @ResponseBody
  public Response getAbonentPackages(@RequestParam int id) {
    return Response.withSuccess(PackageDTO.parseToList(abonentService.getAbonentPackages(id)));
  }

  @RequestMapping({"/save-abonent-packages"})
  @ResponseBody
  public Response abonentPackagesAction(@RequestBody AbonentPackagesRequest request, HttpServletRequest servletRequest) throws Exception {
    request.setUserId((Integer) servletRequest.getSession().getAttribute("userId"));
    abonentService.abonentPackagesAction(request);
    return Response.ok();
  }

  @RequestMapping("/download-excell")
  @ResponseBody
  public Response downlodExcell(@RequestBody AbonentDTO request, HttpServletResponse response) throws IOException {
//        String realPath = "C:\\Users\\ucha\\IdeaProjects\\tvera\\src\\main\\webapp\\resources\\excell\\excel.xls"; //leptopi
    String realPath = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\tvera\\resources\\excell\\excel.xls";
    //ექსელის ფაილის შექმნა

    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("FirstSheet");

    HSSFRow rowhead = sheet.createRow((short) 0);
    rowhead.createCell(0).setCellValue("ID");
    rowhead.createCell(1).setCellValue("სახელი");
    rowhead.createCell(2).setCellValue("გვარი");
    rowhead.createCell(3).setCellValue("პირადი N");
    rowhead.createCell(4).setCellValue("აბონენტის N");
    rowhead.createCell(5).setCellValue("ქუჩა");
    rowhead.createCell(6).setCellValue("ბინის N");
    rowhead.createCell(7).setCellValue("გადასახადი");
    rowhead.createCell(8).setCellValue("ბალანსი");

    HSSFCellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
    cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

    for (int i = 0; i < rowhead.getPhysicalNumberOfCells(); i++) {
      HSSFCell cell = rowhead.getCell(i);
      cell.setCellStyle(cellStyle);
    }

    HSSFRow row;
    List<AbonentDTO> exportList = (List<AbonentDTO>) abonentService.getAbonents(0, 99999, request).get("list");

    for (int i = 1; i <= exportList.size(); i++) {
      row = sheet.createRow(i);
      row.createCell(0).setCellValue("" + exportList.get(i - 1).getId());
      row.createCell(1).setCellValue(exportList.get(i - 1).getName());
      row.createCell(2).setCellValue(exportList.get(i - 1).getLastname());
      row.createCell(3).setCellValue(exportList.get(i - 1).getPersonalNumber());
      row.createCell(4).setCellValue(exportList.get(i - 1).getId());
      row.createCell(5).setCellValue(exportList.get(i - 1).getStreet().getName() + " " + exportList.get(i - 1).getStreetNumber());
      row.createCell(6).setCellValue(exportList.get(i - 1).getRoomNumber());
      if (exportList.get(i - 1).getBill() == null) row.createCell(6).setCellValue("");
      else row.createCell(7).setCellValue(exportList.get(i - 1).getBill());
      if (exportList.get(i - 1).getBalance() == null) row.createCell(7).setCellValue("");
      else row.createCell(8).setCellValue(exportList.get(i - 1).getBalance() * -1);
    }

    FileOutputStream fileOut = new FileOutputStream(realPath);
    workbook.write(fileOut);
    fileOut.close();
    System.out.println("Your excel generated!");

    return Response.ok();
  }
}

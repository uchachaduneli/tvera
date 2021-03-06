package ge.tvera.service;


import ge.tvera.dao.AbonentDAO;
import ge.tvera.dao.PaymentDAO;
import ge.tvera.dto.PaymentDTO;
import ge.tvera.misc.HasNoBillException;
import ge.tvera.model.Abonent;
import ge.tvera.model.Payment;
import ge.tvera.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * @author ucha
 */
@Service
public class PaymentService {

  @Autowired
  private PaymentDAO paymentDAO;

  @Autowired
  private AbonentDAO abonentDAO;

  public HashMap<String, Object> getPayments(int start, int limit, PaymentDTO srchRequest) {
    return paymentDAO.getPayments(start, limit, srchRequest);
  }

  @Transactional(rollbackFor = Throwable.class)
  public Payment updatePayment(PaymentDTO request) throws Exception {

    if (request.getId() == 0) return null;

    Payment obj = (Payment) paymentDAO.find(Payment.class, request.getId());
    obj.setCheckNumber(request.getCheckNumber());
    obj.setPayDate(new java.sql.Date(request.getPayDate().getTime()));
    obj.setOperationDate(new java.sql.Date(request.getOperationDate().getTime()));
    obj.setUser((Users) paymentDAO.find(Users.class, request.getUserId()));
    obj.setBankPayment(request.getBankPayment());
    paymentDAO.update(obj);
    return obj;
  }

  @Transactional(rollbackFor = Throwable.class)
  public Payment savePayment(PaymentDTO request) throws Exception {
    if (request.getId() != null && request.getId() > 0) return null;

    Abonent abonent = (Abonent) paymentDAO.find(Abonent.class, request.getAbonentId());
    if (abonent == null) {
      throw new Exception("Can't find abonent");
    } else {
      if (!abonentDAO.getAbonentHasPackage(abonent.getId())) {
        throw new HasNoBillException(" გთხოვთ მიაბათ აბონენტზე პაკეტი(ები)");
      }
    }
    Payment obj = new Payment();
    obj.setAmount(request.getAmount());
    obj.setCheckNumber(request.getCheckNumber());
    obj.setPayDate(new java.sql.Date(request.getPayDate().getTime()));
    obj.setOperationDate(new java.sql.Date(request.getOperationDate().getTime()));
    obj.setUser((Users) paymentDAO.find(Users.class, request.getUserId()));
    obj.setAbonent(abonent);
    obj.setBankPayment(request.getBankPayment());

    if (abonent.getBalance() > abonent.getBill()) { //როცა დავალიანება აქვს აბონენტს თვეზე მეტის
      if (request.getAmount() > (abonent.getBalance() - abonent.getBill())) {
        obj.setDaval(abonent.getBalance() - abonent.getBill());
        Double darchenili = request.getAmount() - (abonent.getBalance() - abonent.getBill());
        if (darchenili > abonent.getBill()) {
          obj.setAvans(darchenili - abonent.getBill());
          obj.setMimdinare(abonent.getBill());
        } else {
          if (darchenili <= abonent.getBill()) {
            obj.setMimdinare(darchenili);
          }
        }
      } else {
        obj.setDaval(request.getAmount());
      }
    }

    if (abonent.getBalance() >= 0.0 && abonent.getBalance() <= abonent.getBill()) { //ერთი თვის აქვს გადასახდელი ან უფრო ნაკლები
      if (request.getAmount() > abonent.getBalance()) {
        obj.setMimdinare(abonent.getBalance());
        obj.setAvans(request.getAmount() - abonent.getBalance());
      }
      if (request.getAmount() <= abonent.getBalance()) {
        obj.setMimdinare(request.getAmount());
      }

    }

    if (abonent.getBalance() < 0.0) { // პლიუსშია ისედაც და კიდე მოიტანა მაყუთი
      obj.setAvans(request.getAmount());
    }

    obj = (Payment) paymentDAO.create(obj);
    if (obj != null) {
      abonent.setBalance(abonent.getBalance() - request.getAmount());
      abonent = (Abonent) paymentDAO.update(abonent);
    }
    return obj;
  }

  @Transactional(rollbackFor = Throwable.class)
  public void deletePayment(int id) {
    Payment obj = (Payment) paymentDAO.find(Payment.class, id);
    if (obj != null) {
      Abonent abonent = obj.getAbonent();
      abonent.setBalance(abonent.getBalance() + obj.getAmount());
      paymentDAO.update(abonent);
      paymentDAO.delete(obj);
    }
  }
}

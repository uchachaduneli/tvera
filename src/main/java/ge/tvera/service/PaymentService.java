package ge.tvera.service;


import ge.tvera.dao.PaymentDAO;
import ge.tvera.dto.PaymentDTO;
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

    public HashMap<String, Object> getPayments(int start, int limit, PaymentDTO srchRequest) {
        return paymentDAO.getPayments(start, limit, srchRequest);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Payment savePayment(PaymentDTO request) {

        Payment obj = new Payment();
        obj.setAmount(request.getAmount());
        obj.setCheckNumber(request.getCheckNumber());
        obj.setPayDate(new java.sql.Date(request.getPayDate().getTime()));
        obj.setUser((Users) paymentDAO.find(Users.class, request.getUserId()));
        obj.setAbonent((Abonent) paymentDAO.find(Abonent.class, request.getAbonentId()));

        if (request.getId() != null) {
            obj.setId(request.getId());
            obj = (Payment) paymentDAO.update(obj);
        } else {
            obj = (Payment) paymentDAO.create(obj);
            if (obj != null) {
                Abonent abonent = (Abonent) paymentDAO.find(Abonent.class, request.getAbonentId());
                if (abonent != null) {
                    abonent.setBalance(abonent.getBalance() - request.getAmount());
                    abonent = (Abonent) paymentDAO.update(abonent);
                }
            }
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

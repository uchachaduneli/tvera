package ge.tvera.service;


import ge.tvera.dao.PaymentDAO;
import ge.tvera.dto.PaymentDTO;
import ge.tvera.model.Abonent;
import ge.tvera.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ucha
 */
@Service
public class PaymentService {

    @Autowired
    private PaymentDAO paymentDAO;

    public List<PaymentDTO> getPayments() {
        return PaymentDTO.parseToList(paymentDAO.getAll(Payment.class));
    }

    @Transactional(rollbackFor = Throwable.class)
    public Payment savePayment(PaymentDTO request) {

        Payment obj = new Payment();
        obj.setAmount(request.getAmount());
        obj.setCheckNumber(request.getCheckNumber());
        obj.setAbonent((Abonent) paymentDAO.find(Abonent.class, request.getAbonentId()));

        if (request.getId() != null) {
            obj.setId(request.getId());
            obj = (Payment) paymentDAO.update(obj);
        } else {
            obj = (Payment) paymentDAO.create(obj);
        }
        return obj;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deletePayment(int id) {
        Payment obj = (Payment) paymentDAO.find(Payment.class, id);
        if (obj != null) {
            paymentDAO.delete(obj);
        }
    }
}

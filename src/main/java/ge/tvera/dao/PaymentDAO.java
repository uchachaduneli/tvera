package ge.tvera.dao;


import ge.tvera.dto.PaymentDTO;
import ge.tvera.model.Payment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by ME.
 */

@Repository
public class PaymentDAO extends AbstractDAO {

  @PersistenceContext(unitName = "tvera")
  private EntityManager entityManager;

  @Override
  public EntityManager getEntityManager() {
    return entityManager;
  }

  public HashMap<String, Object> getPayments(int start, int limit, PaymentDTO srchRequest) {

    SimpleDateFormat dtfrmt = new SimpleDateFormat("yyyy-MM-dd");

    String countQuery = "Select count(e.id) From ";
    String totalQuery = "Select sum(e.amount) From ";
    String avansQuery = "Select sum(e.avans) From ";
    String davalQuery = "Select sum(e.daval) From ";

    StringBuilder q = new StringBuilder();
    q.append(Payment.class.getSimpleName()).append(" e Where 1=1 ");

    if (srchRequest.getId() != null && srchRequest.getId() > 0) {
      q.append(" and e.id ='").append(srchRequest.getId()).append("'");
    }
    if (srchRequest.getAbonentId() != null && srchRequest.getAbonentId() > 0) {
      q.append(" and e.abonent.id ='").append(srchRequest.getAbonentId()).append("'");
    }
    if (srchRequest.getCheckNumber() != null) {
      q.append(" and e.checkNumber like '%").append(srchRequest.getCheckNumber()).append("%'");
    }
    if (srchRequest.getName() != null) {
      q.append(" and e.abonent.name like '%").append(srchRequest.getName()).append("%'");
    }
    if (srchRequest.getLastname() != null) {
      q.append(" and e.abonent.lastname like '%").append(srchRequest.getLastname()).append("%'");
    }
    if (srchRequest.getPersonalNumber() != null) {
      q.append(" and e.abonent.personalNumber like '%").append(srchRequest.getPersonalNumber()).append("%'");
    }
    if (srchRequest.getStreetNumber() != null) {
      q.append(" and e.abonent.streetNumber like '%").append(srchRequest.getStreetNumber()).append("%'");
    }
    if (srchRequest.getRoomNumber() != null) {
      q.append(" and e.abonent.roomNumber like '%").append(srchRequest.getRoomNumber()).append("%'");
    }
    if (srchRequest.getDistrictId() != null) {
      q.append(" and e.abonent.district.id ='").append(srchRequest.getDistrictId()).append("'");
    }
    if (srchRequest.getIncasatorId() != null) {
      q.append(" and e.abonent.district.incasator.id ='").append(srchRequest.getIncasatorId()).append("'");
    }
    if (srchRequest.getStreetId() != null) {
      q.append(" and e.abonent.street.id ='").append(srchRequest.getStreetId()).append("'");
    }
    if (srchRequest.getPackageTypeId() != null) {
      q.append(" and e.abonent.packageType.id ='").append(srchRequest.getPackageTypeId()).append("'");
    }
    if (srchRequest.getIsCredit() != null) {
      if (srchRequest.getIsCredit() == 1) {
        q.append(" and e.daval > 0");
      } else {
        q.append(" and e.avans > 0");
      }
    }
    if (srchRequest.getBankPayment() != null) {
      q.append(" and e.bankPayment ='").append(srchRequest.getBankPayment()).append("'");
    }
    if (srchRequest.getCreateDateFrom() != null && srchRequest.getCreateDateTo() != null) {
      Calendar c = Calendar.getInstance();
      c.setTime(srchRequest.getCreateDateTo());
      c.add(Calendar.DATE, 1);
      srchRequest.setCreateDateTo(c.getTime());
      q.append(" and e.payDate between '").append(dtfrmt.format(srchRequest.getCreateDateFrom())).append("' and '")
          .append(dtfrmt.format(srchRequest.getCreateDateTo())).append("'");
    }
    if (srchRequest.getOperationDate() != null && srchRequest.getOperationDateTo() != null) {
//      Calendar c = Calendar.getInstance();
//      c.setTime(srchRequest.getOperationDateTo());
//      c.add(Calendar.MONTH, 1);
//      srchRequest.setOperationDateTo(c.getTime());
      q.append(" and e.operationDate between '").append(dtfrmt.format(srchRequest.getOperationDate())).append("' and '")
          .append(dtfrmt.format(srchRequest.getOperationDateTo())).append("'");
    }

    HashMap<String, Object> resultMap = new HashMap();
    resultMap.put("total", entityManager.createQuery(totalQuery + q.toString()).getSingleResult());
    resultMap.put("avansTotal", entityManager.createQuery(avansQuery + q.toString()).getSingleResult());
    resultMap.put("davalTotal", entityManager.createQuery(davalQuery + q.toString()).getSingleResult());
    resultMap.put("size", entityManager.createQuery(countQuery + q.toString()).getSingleResult());
    resultMap.put("list", PaymentDTO.parseToList(entityManager.createQuery("Select e From " + q.toString() + " order by e.id desc", Payment.class).setFirstResult(start).setMaxResults(limit).getResultList()));
    return resultMap;
  }
}

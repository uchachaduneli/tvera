package ge.tvera.dao;


import ge.tvera.dto.AbonentDTO;
import ge.tvera.model.Abonent;
import ge.tvera.model.Payment;
import ge.tvera.model.StatusHistory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by ME.
 */

@Repository
public class ReportDAO extends AbstractDAO {

  @PersistenceContext(unitName = "tvera")
  private EntityManager entityManager;

  @Override
  public EntityManager getEntityManager() {
    return entityManager;
  }

  public HashMap<String, Object> getCountReport(AbonentDTO srchRequest) {

    HashMap<String, Object> resultMap = new HashMap();

    String statusHistoryQuery = " Select count(h.id) From " + StatusHistory.class.getSimpleName() + " h Where 1=1 ";
    String paymentQuery = " Select sum(p.amount) From " + Payment.class.getSimpleName() + " p Where 1=1 ";
    String abonentBalanceSumQuery = Abonent.class.getSimpleName() + " a Where 1=1 ";

    StringBuilder q = new StringBuilder();
    q.append("Select count(e.id) From ").append(Abonent.class.getSimpleName()).append(" e Where 1=1 ");

    if (srchRequest.getId() != null && srchRequest.getId() > 0) {
      q.append(" and e.id ='").append(srchRequest.getId()).append("'");
      statusHistoryQuery += " and e.abonent.id ='" + srchRequest.getId() + "'";
      abonentBalanceSumQuery += " and a.id ='" + srchRequest.getId() + "'";
    }
    if (srchRequest.getDistrictId() != null) {
      q.append(" and e.district.id ='").append(srchRequest.getDistrictId()).append("'");
      statusHistoryQuery += " and e.abonent.district.id ='" + srchRequest.getDistrictId() + "'";
      abonentBalanceSumQuery += " and a.district.id ='" + srchRequest.getDistrictId() + "'";
    }
    if (srchRequest.getStreetId() != null) {
      q.append(" and e.street.id ='").append(srchRequest.getStreetId()).append("'");
      statusHistoryQuery += " and e.abonent.street.id ='" + srchRequest.getStreetId() + "'";
      abonentBalanceSumQuery += " and a.street.id ='" + srchRequest.getStreetId() + "'";
    }
    if (srchRequest.getIncasatorId() != null) {
      q.append(" and e.district.incasator.id ='").append(srchRequest.getIncasatorId()).append("'");
      statusHistoryQuery += " and e.abonent.district.incasator.id ='" + srchRequest.getIncasatorId() + "'";
      abonentBalanceSumQuery += " and a.district.incasator.id ='" + srchRequest.getIncasatorId() + "'";
    }

    resultMap.put("activesCount", entityManager.createQuery(q.toString() + " and e.status.id='1'").getSingleResult());
    resultMap.put("inactivesCount", entityManager.createQuery(q.toString() + " and e.status.id='2'").getSingleResult());

    if (srchRequest.getBillDateFrom() != null && srchRequest.getBillDateTo() != null) {

      Calendar c = Calendar.getInstance();
      c.setTime(srchRequest.getBillDateTo());
      c.add(Calendar.DATE, 1);
      srchRequest.setBillDateTo(c.getTime());

      statusHistoryQuery += " and h.disableDate between '" + new java.sql.Date(srchRequest.getBillDateFrom().getTime()) + "' and '"
          + new java.sql.Date(srchRequest.getBillDateTo().getTime()) + "'";

      resultMap.put("wasInactivesCount", entityManager.createQuery(statusHistoryQuery).getSingleResult());

      paymentQuery += " and p.payDate between '" + new java.sql.Date(srchRequest.getBillDateFrom().getTime()) + "' and '"
          + new java.sql.Date(srchRequest.getBillDateTo().getTime()) + "'";

    }

    resultMap.put("receivedBankAmount", entityManager.createQuery(paymentQuery + " and p.bankPayment = '1'").getSingleResult());
    resultMap.put("receivedCashAmount", entityManager.createQuery(paymentQuery + " and p.bankPayment = '2'").getSingleResult());

    if (srchRequest.getIncasatorId() != null) {
      resultMap.put("incasatorSumAmount", entityManager.createQuery(paymentQuery + " and p.abonent.district.incasator.id = '" + srchRequest.getIncasatorId() + "'").getSingleResult());
    }

    resultMap.put("abonentAvansSum", (Double) entityManager.createQuery(" Select sum(a.balance) From " + abonentBalanceSumQuery + " and a.balance < 0").getSingleResult() * -1);
    resultMap.put("abonentDavalSum", (Double) entityManager.createQuery(" Select sum(a.balance) From " + abonentBalanceSumQuery + " and a.balance > 0").getSingleResult() * -1);

    resultMap.put("abonentAvansCount", entityManager.createQuery(" Select count(a.id) From " + abonentBalanceSumQuery + " and a.balance < 0").getSingleResult());
    resultMap.put("abonentDavalCount", entityManager.createQuery(" Select count(a.id) From " + abonentBalanceSumQuery + " and a.balance > 0").getSingleResult());

    return resultMap;
  }

}

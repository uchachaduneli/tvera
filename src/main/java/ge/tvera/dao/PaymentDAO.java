package ge.tvera.dao;


import ge.tvera.dto.PaymentDTO;
import ge.tvera.model.Payment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        StringBuilder q = new StringBuilder();
        q.append("Select e From ").append(Payment.class.getSimpleName()).append(" e Where 1=1 ");

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
        if (srchRequest.getCreateDateFrom() != null && srchRequest.getCreateDateTo() != null) {
            q.append(" and e.createDate between '").append(srchRequest.getCreateDateFrom()).append("' and '").append(srchRequest.getCreateDateTo()).append("'");
        }

//        TypedQuery<Payment> query = entityManager.createQuery(q.toString(), Payment.class);
//        return query.setFirstResult(start).setMaxResults(limit).getResultList();

        HashMap<String, Object> resultMap = new HashMap();
        resultMap.put("size", entityManager.createQuery(q.toString(), Payment.class).getResultList().size());
        resultMap.put("list", PaymentDTO.parseToList(entityManager.createQuery(q.toString() + " order by e.id desc", Payment.class).setFirstResult(start).setMaxResults(limit).getResultList()));
        return resultMap;
    }
}

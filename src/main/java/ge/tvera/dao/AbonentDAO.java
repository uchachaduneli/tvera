package ge.tvera.dao;


import ge.tvera.dto.AbonentDTO;
import ge.tvera.model.Abonent;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by ME.
 */

@Repository
public class AbonentDAO extends AbstractDAO {

    @PersistenceContext(unitName = "tvera")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public List<Abonent> getAbonents(int start, int limit, AbonentDTO srchRequest) {
        StringBuilder q = new StringBuilder();
        q.append("Select e From ").append(Abonent.class.getSimpleName()).append(" e Where 1=1 ");

        if (srchRequest.getId() != null && srchRequest.getId() > 0) {
            q.append(" and e.id ='").append(srchRequest.getId()).append("'");
        }
        if (srchRequest.getName() != null) {
            q.append(" and e.name like '%").append(srchRequest.getName()).append("%'");
        }
        if (srchRequest.getLastname() != null) {
            q.append(" and e.lastname like '%").append(srchRequest.getLastname()).append("%'");
        }
        if (srchRequest.getAbonentNumber() != null) {
            q.append(" and e.abonentNumber like '%").append(srchRequest.getAbonentNumber()).append("%'");
        }
        if (srchRequest.getPersonalNumber() != null) {
            q.append(" and e.personalNumber like '%").append(srchRequest.getPersonalNumber()).append("%'");
        }
        if (srchRequest.getDeviceNumber() != null) {
            q.append(" and e.deviceNumber like '%").append(srchRequest.getDeviceNumber()).append("%'");
        }
        if (srchRequest.getDistrictId() != null) {
            q.append(" and e.street.district.id ='").append(srchRequest.getDistrictId()).append("'");
        }
        if (srchRequest.getStreetId() != null) {
            q.append(" and e.street.id ='").append(srchRequest.getStreetId()).append("'");
        }
        if (srchRequest.getIncasatorId() != null) {
            q.append(" and e.street.incasator.id ='").append(srchRequest.getIncasatorId()).append("'");
        }
        if (srchRequest.getBillDate() != null && srchRequest.getBillDateTo() != null) {
            q.append(" and e.billDate between '").append(srchRequest.getBillDate()).append("' and '").append(srchRequest.getBillDateTo()).append("'");
        }

        TypedQuery<Abonent> query = entityManager.createQuery(q.toString(), Abonent.class);
        return query.setFirstResult(start).setMaxResults(limit).getResultList();
    }
}

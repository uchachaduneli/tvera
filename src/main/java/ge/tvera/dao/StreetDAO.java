package ge.tvera.dao;


import ge.tvera.dto.StreetDTO;
import ge.tvera.model.Street;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by ME.
 */

@Repository
public class StreetDAO extends AbstractDAO {

    @PersistenceContext(unitName = "tvera")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public List<Street> getStreets(int start, int limit, StreetDTO srchRequest) {
        StringBuilder q = new StringBuilder();
        q.append("Select e From ").append(Street.class.getSimpleName()).append(" e Where 1=1 ");

        if (srchRequest.getId() != null && srchRequest.getId() > 0) {
            q.append(" and e.id ='").append(srchRequest.getId()).append("'");
        }

        if (srchRequest.getName() != null) {
            q.append(" and e.name like '%").append(srchRequest.getName()).append("%'");
        }

        if (srchRequest.getDistrictId() != null && srchRequest.getDistrictId() > 0) {
            q.append(" and e.district.id ='").append(srchRequest.getDistrictId()).append("'");
        }

        if (srchRequest.getIncasatorId() != null && srchRequest.getIncasatorId() > 0) {
            q.append(" and e.district.incasator.id ='").append(srchRequest.getIncasatorId()).append("'");
        }

        TypedQuery<Street> query = entityManager.createQuery(q.toString(), Street.class);
        return query.setFirstResult(start).setMaxResults(limit).getResultList();
    }
}

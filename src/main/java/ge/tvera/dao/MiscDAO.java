package ge.tvera.dao;


import ge.tvera.dto.IncasatorDTO;
import ge.tvera.model.Incasator;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;

/**
 * Created by ME.
 */

@Repository
public class MiscDAO extends AbstractDAO {

    @PersistenceContext(unitName = "tvera")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public HashMap<String, Object> getIncasators(int start, int limit, IncasatorDTO srchRequest) {
        StringBuilder q = new StringBuilder();
        q.append("Select e From ").append(Incasator.class.getSimpleName()).append(" e Where 1=1 ");
        if (srchRequest.getId() != null && srchRequest.getId() > 0) {
            q.append(" and e.id ='").append(srchRequest.getId()).append("'");
        }
        if (srchRequest.getName() != null) {
            q.append(" and e.name like '%").append(srchRequest.getName()).append("%'");
        }
        if (srchRequest.getLastname() != null) {
            q.append(" and e.lastname like '%").append(srchRequest.getLastname()).append("%'");
        }
        HashMap<String, Object> resultMap = new HashMap();
        resultMap.put("size", entityManager.createQuery(q.toString(), Incasator.class).getResultList().size());
        resultMap.put("list", IncasatorDTO.parseToList(entityManager.createQuery(q.toString(), Incasator.class).setFirstResult(start).setMaxResults(limit).getResultList()));
        return resultMap;
    }
}

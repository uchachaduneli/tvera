package ge.tvera.dao;


import ge.tvera.dto.StreetDTO;
import ge.tvera.model.Street;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;

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

    public HashMap<String, Object> getStreets(int start, int limit, StreetDTO srchRequest) {
        StringBuilder q = new StringBuilder();
        q.append("Select e From ").append(Street.class.getSimpleName()).append(" e Where 1=1 ");

        if (srchRequest.getId() != null && srchRequest.getId() > 0) {
            q.append(" and e.id ='").append(srchRequest.getId()).append("'");
        }

        if (srchRequest.getName() != null) {
            q.append(" and e.name like '%").append(srchRequest.getName()).append("%'");
        }

        HashMap<String, Object> resultMap = new HashMap();
        resultMap.put("size", entityManager.createQuery(q.toString(), Street.class).getResultList().size());
        resultMap.put("list", StreetDTO.parseToList(entityManager.createQuery(q.toString(), Street.class).setFirstResult(start).setMaxResults(limit).getResultList()));
        return resultMap;
    }
}

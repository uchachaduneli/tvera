package ge.tvera.dao;


import ge.tvera.dto.PackageDTO;
import ge.tvera.model.Package;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ME.
 */

@Repository
public class PackageDAO extends AbstractDAO {

  @PersistenceContext(unitName = "tvera")
  private EntityManager entityManager;

  @Override
  public EntityManager getEntityManager() {
    return entityManager;
  }

  public List<Package> getPackagesByTypeId(Integer typeId) {
    StringBuilder q = new StringBuilder();
    q.append("Select e From ").append(Package.class.getSimpleName()).append(" e Where e.type.id='").append(typeId).append("'");
    TypedQuery<Package> query = entityManager.createQuery(q.toString(), Package.class);
    return query.getResultList();
  }

  public HashMap<String, Object> getGroupedPackages() {
    List<Integer> arr = new ArrayList<>();
    arr.add(1);
    arr.add(6);
    HashMap<String, Object> resultMap = new HashMap();
    resultMap.put("distrPackages", PackageDTO.parseToList(
        entityManager.createQuery(" Select e From " + Package.class.getSimpleName() + " e where e.group.id in :list", Package.class)
            .setParameter("list", arr)
            .getResultList()));
    resultMap.put("oneTimepackages", PackageDTO.parseToList(
        entityManager.createQuery(" Select e From " + Package.class.getSimpleName() + " e where e.group.id not in :list", Package.class)
            .setParameter("list", arr)
            .getResultList()));
    return resultMap;
  }
}

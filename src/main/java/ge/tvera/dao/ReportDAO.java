package ge.tvera.dao;


import ge.tvera.dto.AbonentDTO;
import ge.tvera.dto.BalanceHistoryDTO;
import ge.tvera.model.*;
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

        StringBuilder q = new StringBuilder();
        q.append("Select count(e.id) From ").append(Abonent.class.getSimpleName()).append(" e Where 1=1 ");

        if (srchRequest.getId() != null && srchRequest.getId() > 0) {
            q.append(" and e.id ='").append(srchRequest.getId()).append("'");
            statusHistoryQuery += " and e.abonent.id ='" + srchRequest.getId() + "'";
        }
        if (srchRequest.getDistrictId() != null) {
            q.append(" and e.district.id ='").append(srchRequest.getDistrictId()).append("'");
            statusHistoryQuery += " and e.abonent.district.id ='" + srchRequest.getDistrictId() + "'";
        }
        if (srchRequest.getStreetId() != null) {
            q.append(" and e.street.id ='").append(srchRequest.getStreetId()).append("'");
            statusHistoryQuery += " and e.abonent.street.id ='" + srchRequest.getStreetId() + "'";
        }
        if (srchRequest.getIncasatorId() != null) {
            q.append(" and e.district.incasator.id ='").append(srchRequest.getIncasatorId()).append("'");
            statusHistoryQuery += " and e.abonent.district.incasator.id ='" + srchRequest.getIncasatorId() + "'";
        }

        resultMap.put("activesCount", entityManager.createQuery(q.toString() + " and e.status.id=1", Abonent.class).getResultList().size());
        resultMap.put("inactivesCount", entityManager.createQuery(q.toString() + " and e.status.id=2", Abonent.class).getResultList().size());

        if (srchRequest.getBillDateFrom() != null && srchRequest.getBillDateTo() != null) {
            statusHistoryQuery += " and h.disableDate between '" + new java.sql.Date(srchRequest.getBillDateFrom().getTime()) + "' and '"
                    + new java.sql.Date(srchRequest.getBillDateTo().getTime()) + "'";
            გათიშული იყო
        }

        return resultMap;
    }

    public List<BalanceHistoryDTO> getAbonentBalanceHistory(AbonentDTO srchRequest) {
        StringBuilder q = new StringBuilder();
        q.append("Select e From ").append(BalanceHistory.class.getSimpleName()).append(" e Where e.abonent.balance > e.abonent.bill ");

        if (srchRequest.getId() != null && srchRequest.getId() > 0) {
            q.append(" and e.abonent.id ='").append(srchRequest.getId()).append("'");
        }
        if (srchRequest.getName() != null) {
            q.append(" and e.abonent.name like '%").append(srchRequest.getName()).append("%'");
        }
        if (srchRequest.getLastname() != null) {
            q.append(" and e.abonent.lastname like '%").append(srchRequest.getLastname()).append("%'");
        }
        if (srchRequest.getPersonalNumber() != null) {
            q.append(" and e.abonent.personalNumber ='").append(srchRequest.getPersonalNumber()).append("'");
        }
        if (srchRequest.getRoomNumber() != null) {
            q.append(" and e.abonent.roomNumber ='").append(srchRequest.getRoomNumber()).append("'");
        }
        if (srchRequest.getDistrictId() != null) {
            q.append(" and e.abonent.district.id ='").append(srchRequest.getDistrictId()).append("'");
        }
        if (srchRequest.getStatusId() != null) {
            q.append(" and e.abonent.status.id ='").append(srchRequest.getStatusId()).append("'");
        }
        if (srchRequest.getStreetId() != null) {
            q.append(" and e.abonent.street.id ='").append(srchRequest.getStreetId()).append("'");
        }
        if (srchRequest.getIncasatorId() != null) {
            q.append(" and e.abonent.district.incasator.id ='").append(srchRequest.getIncasatorId()).append("'");
        }
        if (srchRequest.getBillDateFrom() != null && srchRequest.getBillDateTo() != null) {
            q.append(" and e.abonent.billDate between '").append(new java.sql.Date(srchRequest.getBillDateFrom().getTime())).append("' and '")
                    .append(new java.sql.Date(srchRequest.getBillDateTo().getTime())).append("'");
        }

        TypedQuery<BalanceHistory> query = entityManager.createQuery(q.toString(), BalanceHistory.class);
        return BalanceHistoryDTO.parseToList(query.getResultList());
    }

    public List<StatusHistory> getStatusHistory(int id) {
        StringBuilder q = new StringBuilder();
        q.append("Select e From ").append(StatusHistory.class.getSimpleName()).append(" e Where e.abonent.id='").append(id).append("'");
        TypedQuery<StatusHistory> query = entityManager.createQuery(q.toString(), StatusHistory.class);
        return query.getResultList();
    }

    public List<Package> getAbonentPackages(int id) {
        StringBuilder q = new StringBuilder();
        q.append("Select e From ").append(AbonentPackages.class.getSimpleName()).append(" e Where e.abonent.id='").append(id).append("'");
        TypedQuery<AbonentPackages> query = entityManager.createQuery(q.toString(), AbonentPackages.class);

        List<Package> packages = new ArrayList<>();
        for (AbonentPackages abPacks : query.getResultList()) {
            packages.add(abPacks.getPackages());
        }
        return packages;
    }

    public List<Package> getAbonentPackagesByIdList(List<Integer> ids) {
        StringBuilder q = new StringBuilder();
        q.append("Select e From ").append(Package.class.getSimpleName()).append(" e Where e.id in :idList");
        TypedQuery<Package> query = entityManager.createQuery(q.toString(), Package.class);
        query.setParameter("idList", ids);
        return query.getResultList();
    }

}

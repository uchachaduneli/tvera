package ge.tvera.dao;


import ge.tvera.dto.AbonentDTO;
import ge.tvera.dto.BalanceHistoryDTO;
import ge.tvera.dto.MonthlyBillsDTO;
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
public class AbonentDAO extends AbstractDAO {

    @PersistenceContext(unitName = "tvera")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public HashMap<String, Object> getAbonents(int start, int limit, AbonentDTO srchRequest) {

        String countQuery = "Select count(e.id) From ";
        String avansQuery = "select sum(case when e.balance <= e.bill*-1 then e.balance + e.bill else 0 end) From abonent e " +
                " join district d on d.id=e.district_id " +
                " join incasator i on i.id=d.incasator_id Where 1=1 ";

        StringBuilder q = new StringBuilder();
        q.append(" e Where 1=1 ");

        if (srchRequest.getId() != null && srchRequest.getId() > 0) {
            q.append(" and e.id ='").append(srchRequest.getId()).append("'");
            avansQuery += " and e.id =" + srchRequest.getId();
        }
        if (srchRequest.getName() != null) {
            q.append(" and e.name like '%").append(srchRequest.getName()).append("%'");
            avansQuery += " and e.name like '%" + srchRequest.getName() + "%'";
        }
        if (srchRequest.getLastname() != null) {
            q.append(" and e.lastname like '%").append(srchRequest.getLastname()).append("%'");
            avansQuery += " and e.lastname like '%" + srchRequest.getLastname() + "%'";
        }
        if (srchRequest.getStreetNumber() != null) {
            q.append(" and e.streetNumber like '%").append(srchRequest.getStreetNumber()).append("%'");
            avansQuery += " and e.street_number like '%" + srchRequest.getStreetNumber() + "%'";
        }
        if (srchRequest.getPersonalNumber() != null) {
            q.append(" and e.personalNumber ='").append(srchRequest.getPersonalNumber()).append("'");
            avansQuery += " and e.personal_number ='" + srchRequest.getPersonalNumber() + "'";
        }
        if (srchRequest.getRoomNumber() != null) {
            q.append(" and e.roomNumber ='").append(srchRequest.getRoomNumber()).append("'");
            avansQuery += " and e.room_number ='" + srchRequest.getRoomNumber() + "'";
        }
        if (srchRequest.getDeviceNumber() != null) {
            q.append(" and e.deviceNumber ='").append(srchRequest.getDeviceNumber()).append("'");
            avansQuery += " and e.device_number ='" + srchRequest.getDeviceNumber() + "'";
        }
        if (srchRequest.getDistrictId() != null) {
            q.append(" and e.district.id ='").append(srchRequest.getDistrictId()).append("'");
            avansQuery += " and e.district_id ='" + srchRequest.getDistrictId() + "'";
        }
        if (srchRequest.getStatusId() != null) {
            q.append(" and e.status.id ='").append(srchRequest.getStatusId()).append("'");
            avansQuery += "and e.status_id='" + srchRequest.getStatusId() + "'";
        }
        if (srchRequest.getStreetId() != null) {
            q.append(" and e.street.id ='").append(srchRequest.getStreetId()).append("'");
            avansQuery += " and e.street_id ='" + srchRequest.getStreetId() + "'";
        }
        if (srchRequest.getIncasatorId() != null) {
            q.append(" and e.district.incasator.id ='").append(srchRequest.getIncasatorId()).append("'");
            avansQuery += " and i.id='" + srchRequest.getIncasatorId() + "'";
        }
        if (srchRequest.getPackageTypeId() != null) {
            q.append(" and e.packageType.id ='").append(srchRequest.getPackageTypeId()).append("'");
            avansQuery += " and e.package_type_id='" + srchRequest.getPackageTypeId() + "'";
        }
        if (srchRequest.getHasBill() != null) {
            if (srchRequest.getHasBill() == 1) {
                q.append(" and e.balance > 0");
            }
            if (srchRequest.getHasBill() == 2) {
                q.append(" and e.balance < 0");
                avansQuery += " and e.balance < 0";
            }
        }
        if (srchRequest.getBillDateFrom() != null && srchRequest.getBillDateTo() != null) {
            q.append(" and e.billDate between '").append(new java.sql.Date(srchRequest.getBillDateFrom().getTime())).append("' and '")
                    .append(new java.sql.Date(srchRequest.getBillDateTo().getTime())).append("'");
            avansQuery += " and e.billDate between '" + new java.sql.Date(srchRequest.getBillDateFrom().getTime()) + "' and '" +
                    new java.sql.Date(srchRequest.getBillDateTo().getTime()) + "'";

        }
        String orderByStr = " order by status_id asc, id desc";
        if (srchRequest.getForTickets() != null && srchRequest.getForTickets() == 1) {
            orderByStr = " order by e.street.id asc, e.streetNumber asc";
        }

//        TypedQuery<Abonent> query = entityManager.createQuery(q.toString(), Abonent.class);
        HashMap<String, Object> resultMap = new HashMap();
        resultMap.put("size", entityManager.createQuery(countQuery + Abonent.class.getSimpleName() + q.toString()).getSingleResult());
        resultMap.put("avansTotal", entityManager.createNativeQuery(avansQuery).getSingleResult());
        resultMap.put("list", AbonentDTO.parseToList(entityManager.createQuery("Select e From " + Abonent.class.getSimpleName() + q.toString() + orderByStr, Abonent.class).setFirstResult(start).setMaxResults(limit).getResultList()));
        return resultMap;
    }

    public HashMap<String, Object> getMonthlyBills(int start, int limit, MonthlyBillsDTO srchRequest) {

        String countQuery = "Select count(e.id) From ";
        String totalQuery = "Select sum(e.amount) From ";

        StringBuilder q = new StringBuilder();
        q.append(MonthlyBills.class.getSimpleName()).append(" e Where 1=1 ");

        if (srchRequest.getAbonent() != null) {
            if (srchRequest.getAbonent().getId() != null && srchRequest.getAbonent().getId() > 0) {
                q.append(" and e.abonent.id ='").append(srchRequest.getAbonent().getId()).append("'");
            }
            if (srchRequest.getAbonent().getName() != null) {
                q.append(" and e.abonent.name like '%").append(srchRequest.getAbonent().getName()).append("%'");
            }
            if (srchRequest.getAbonent().getLastname() != null) {
                q.append(" and e.abonent.lastname like '%").append(srchRequest.getAbonent().getLastname()).append("%'");
            }
            if (srchRequest.getAbonent().getPersonalNumber() != null) {
                q.append(" and e.abonent.personalNumber ='").append(srchRequest.getAbonent().getPersonalNumber()).append("'");
            }
            if (srchRequest.getAbonent().getDistrictId() != null) {
                q.append(" and e.abonent.district.id ='").append(srchRequest.getAbonent().getDistrictId()).append("'");
            }
            if (srchRequest.getAbonent().getStreetId() != null) {
                q.append(" and e.abonent.street.id ='").append(srchRequest.getAbonent().getStreetId()).append("'");
            }
            if (srchRequest.getAbonent().getIncasatorId() != null) {
                q.append(" and e.abonent.district.incasator.id ='").append(srchRequest.getAbonent().getIncasatorId()).append("'");
            }
        }
        if (srchRequest.getOperDate() != null && srchRequest.getOperDateTo() != null) {
            q.append(" and e.operDate between '").append(new java.sql.Date(srchRequest.getOperDate().getTime())).append("' and '")
                    .append(new java.sql.Date(srchRequest.getOperDateTo().getTime())).append("'");
        }

        HashMap<String, Object> resultMap = new HashMap();
        resultMap.put("total", entityManager.createQuery(totalQuery + q.toString()).getSingleResult());
        resultMap.put("size", entityManager.createQuery(countQuery + q.toString()).getSingleResult());
        resultMap.put("list", MonthlyBillsDTO.parseToList(entityManager.createQuery("Select e From " + q.toString() + " order by e.id desc", MonthlyBills.class).setFirstResult(start).setMaxResults(limit).getResultList()));
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

    public StatusHistory getStatusLastHistory(int id) {
        StringBuilder q = new StringBuilder();
        q.append("Select e From ").append(StatusHistory.class.getSimpleName())
                .append(" e Where e.id=(select max(u.id) from "
                        + StatusHistory.class.getSimpleName() + " u where u.abonent.id='" + id + "')");
        List<StatusHistory> res = entityManager.createQuery(q.toString(), StatusHistory.class).getResultList();
        return (res != null && !res.isEmpty()) ? res.get(0) : null;
    }

    public void deleteAbonentPackages(Integer abonentId) {
        entityManager.createQuery("UPDATE " + AbonentPackages.class.getSimpleName() + " e set e.deleted=2 WHERE e.deleted=1 AND e.abonent.id=" + abonentId).executeUpdate();
    }

    public List<Package> getAbonentPackages(int id) {
        StringBuilder q = new StringBuilder();
        q.append("Select e From ").append(AbonentPackages.class.getSimpleName()).append(" e Where e.deleted=1 and e.abonent.id='").append(id).append("'");
        TypedQuery<AbonentPackages> query = entityManager.createQuery(q.toString(), AbonentPackages.class);

        List<Package> packages = new ArrayList<>();
        Package pack = null;
        for (AbonentPackages abPacks : query.getResultList()) {
            pack = abPacks.getPackages();
            pack.setJuridicalPrice(abPacks.getJuridicalPrice());
            pack.setPersonalPrice(abPacks.getPhisicalPrice());
            packages.add(pack);
        }
        return packages;
    }

    public boolean getAbonentHasPackage(int id) {
        StringBuilder q = new StringBuilder();
        q.append("Select count(e.id) From ");
        q.append(AbonentPackages.class.getSimpleName()).append(" e Where e.deleted=1 and e.abonent.id='").append(id).append("'");
        return (Long) entityManager.createQuery(q.toString()).getSingleResult() > 0 ? true : false;
    }

}

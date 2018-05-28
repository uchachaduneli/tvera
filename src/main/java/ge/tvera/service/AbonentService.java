package ge.tvera.service;


import ge.tvera.dao.AbonentDAO;
import ge.tvera.dto.AbonentDTO;
import ge.tvera.dto.AbonentPackageDTO;
import ge.tvera.model.Abonent;
import ge.tvera.model.AbonentPackages;
import ge.tvera.model.Package;
import ge.tvera.model.Street;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ucha
 */
@Service
public class AbonentService {

    @Autowired
    private AbonentDAO abonentDAO;

    public List<AbonentDTO> getAbonents() {
        return AbonentDTO.parseToList(abonentDAO.getAll(Abonent.class));
    }

    @Transactional(rollbackFor = Throwable.class)
    public Abonent saveAbonent(AbonentDTO request) {

        Abonent obj = new Abonent();
        obj.setName(request.getName());
        obj.setLastname(request.getLastname());
        obj.setAbonentNumber(request.getAbonentNumber());
        obj.setDeviceNumber(request.getDeviceNumber());
        obj.setPersonalNumber(request.getPersonalNumber());
        obj.setComment(request.getComment());
        obj.setBillDate(new java.sql.Date(request.getBillDate().getTime()));
        obj.setStreet((Street) abonentDAO.find(Street.class, request.getStreetId()));

        if (request.getId() != null) {
            Abonent tmp = (Abonent) abonentDAO.find(Abonent.class, request.getId());
            obj.setBalance(tmp.getBalance());
            obj.setBill(tmp.getBill());
            obj.setId(request.getId());
            obj = (Abonent) abonentDAO.update(obj);
        } else {
            obj = (Abonent) abonentDAO.create(obj);
        }
        return obj;
    }

    @Transactional(rollbackFor = Throwable.class)
    public AbonentPackages saveAbonentPackages(AbonentPackageDTO request) {

        AbonentPackages obj = new AbonentPackages();
        obj.setPointsCount(request.getPointsCount());
        obj.setAbonent((Abonent) abonentDAO.find(Abonent.class, request.getAbonentId()));
        obj.setPackages((Package) abonentDAO.find(Package.class, request.getPackageId()));

        if (request.getId() != null) {
            obj.setId(request.getId());
            obj = (AbonentPackages) abonentDAO.update(obj);
        } else {
            obj = (AbonentPackages) abonentDAO.create(obj);
        }
        return obj;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteAbonent(int id) {
        Abonent obj = (Abonent) abonentDAO.find(Abonent.class, id);
        if (obj != null) {
            abonentDAO.delete(obj);
        }
    }
}

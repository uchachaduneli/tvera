package ge.tvera.service;


import ge.tvera.dao.AbonentDAO;
import ge.tvera.dao.PackageDAO;
import ge.tvera.dto.AbonentDTO;
import ge.tvera.dto.AbonentPackageDTO;
import ge.tvera.dto.PackageDTO;
import ge.tvera.dto.StatusHistoryDTO;
import ge.tvera.model.*;
import ge.tvera.model.Package;
import ge.tvera.request.AbonentPackagesRequest;
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

    @Autowired
    private PackageDAO packageDAO;

    public List<AbonentDTO> getAbonents(int start, int limit, AbonentDTO srchRequest) {
        return AbonentDTO.parseToList(abonentDAO.getAbonents(start, limit, srchRequest));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void abonentPackagesAction(AbonentPackagesRequest request) {
        Abonent abonent = (Abonent) abonentDAO.find(Abonent.class, request.getAbonendId());
        Users user = (Users) abonentDAO.find(Users.class, request.getUserId());

        boolean isJuridical = abonent.getJuridicalOrPhisical() == AbonentDTO.JURIDICAL;

        Double bill = 0.0;

        AbonentPackages abonentPack = null;

        List<Package> packages = packageDAO.getPackagesByTypeId(request.getPackageTypeId());
        if (request.getServicePointsNumber() == null || request.getServicePointsNumber() == 0 || request.getServicePointsNumber() == 1) {
            if (isJuridical) {
                for (Package pack : packages) {
                    if (pack.getGroup().getId() == PackageDTO.DISTRIBUTION || pack.getGroup().getId() == PackageDTO.INSTALLATION) {
                        bill += pack.getJuridicalPrice();
                    }
                    abonentPack = new AbonentPackages(abonent, pack, pack.getJuridicalPrice(), null, user);
                    abonentDAO.create(abonentPack);
                }
            } else {
                for (Package pack : packages) {
                    if (pack.getGroup().getId() == PackageDTO.DISTRIBUTION || pack.getGroup().getId() == PackageDTO.INSTALLATION) {
                        bill += pack.getPersonalPrice();
                    }
                    abonentPack = new AbonentPackages(abonent, pack, null, pack.getPersonalPrice(), user);
                    abonentDAO.create(abonentPack);
                }
            }
        } else {
            if (isJuridical) {
                for (Package pack : packages) {
                    if (pack.getGroup().getId() == PackageDTO.DISTRIBUTION
                            || pack.getGroup().getId() == PackageDTO.INSTALLATION
                            || pack.getGroup().getId() == PackageDTO.EXTERNAL_POINT) {
                        bill += pack.getJuridicalPrice();
                    }
                    abonentPack = new AbonentPackages(abonent, pack, pack.getJuridicalPrice(), null, user);
                    abonentDAO.create(abonentPack);
                }
            } else {
                for (Package pack : packages) {
                    if (pack.getGroup().getId() == PackageDTO.DISTRIBUTION
                            || pack.getGroup().getId() == PackageDTO.INSTALLATION
                            || pack.getGroup().getId() == PackageDTO.EXTERNAL_POINT) {
                        bill += pack.getPersonalPrice();
                    }
                    abonentPack = new AbonentPackages(abonent, pack, null, pack.getPersonalPrice(), user);
                    abonentDAO.create(abonentPack);
                }
            }
        }

        abonent.setPackageType((PackageType) abonentDAO.find(PackageType.class, request.getPackageTypeId()));
        abonent.setBill(bill);
        abonent.setBalance(bill);
        abonent.setServicePointsNumber(request.getServicePointsNumber());
        abonent = (Abonent) abonentDAO.update(abonent);
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
        obj.setJuridicalOrPhisical(request.getJuridicalOrPhisical());

        if (request.getId() != null) {
            Abonent tmp = (Abonent) abonentDAO.find(Abonent.class, request.getId());
            obj.setBalance(tmp.getBalance());
            obj.setBill(tmp.getBill());
            obj.setId(request.getId());
            obj.setStatus(tmp.getStatus());
            obj.setPackageType(tmp.getPackageType());
            obj.setServicePointsNumber(tmp.getServicePointsNumber());
            obj = (Abonent) abonentDAO.update(obj);
        } else {
            obj = (Abonent) abonentDAO.create(obj);
        }
        return obj;
    }

    @Transactional(rollbackFor = Throwable.class)
    public AbonentPackages saveAbonentPackages(AbonentPackageDTO request) {

        AbonentPackages obj = new AbonentPackages();
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

    public List<StatusHistoryDTO> getStatusHistory(int id) {
        return StatusHistoryDTO.parseToList(abonentDAO.getStatusHistory(id));
    }

    public List<AbonentPackageDTO> getAbonentPackages(int id) {
        return AbonentPackageDTO.parseToList(abonentDAO.getAbonentPackages(id));
    }
}

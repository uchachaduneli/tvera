package ge.tvera.service;


import ge.tvera.dao.AbonentDAO;
import ge.tvera.dao.PackageDAO;
import ge.tvera.dto.*;
import ge.tvera.model.*;
import ge.tvera.model.Package;
import ge.tvera.request.AbonentPackagesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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

    public HashMap<String, Object> getAbonents(int start, int limit, AbonentDTO srchRequest) {
        return abonentDAO.getAbonents(start, limit, srchRequest);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void abonentPackagesAction(AbonentPackagesRequest request) {
        Abonent abonent = (Abonent) abonentDAO.find(Abonent.class, request.getAbonendId());
        Users user = (Users) abonentDAO.find(Users.class, request.getUserId());

        boolean isJuridical = abonent.getJuridicalOrPhisical() == AbonentDTO.JURIDICAL;

        Double billSum = abonent == null || abonent.getBill() == null ? 0.0 : abonent.getBill();
        Double balance = abonent == null || abonent.getBalance() == null ? 0.0 : abonent.getBalance();

        Integer servicePointsNumber = request.getAbonentPackages().isEmpty() ? 0 : 1;

        List<Package> abonentExistingPackages = abonentDAO.getAbonentPackages(abonent.getId());

        AbonentPackages abonentPack = null;

        if (abonentExistingPackages.isEmpty()) {
            //ახალი აბონენტია და პირველი მიბმაა პაკეტების
            if (!request.getAbonentPackages().isEmpty()) {
                request.setPackageTypeId(request.getAbonentPackages().get(0).getType().getId());
            }
            for (PackageDTO pack : request.getAbonentPackages()) {
                switch (pack.getGroup().getId()) {
                    case PackageDTO.DISTRIBUTION:
                        if (isJuridical) {
                            billSum += pack.getJuridicalPrice();
                            balance += pack.getJuridicalPrice();
                        } else {
                            billSum += pack.getPersonalPrice();
                            balance += pack.getPersonalPrice();
                        }
                        break;
                    case PackageDTO.INSTALLATION:
                        if (isJuridical) {
                            balance += pack.getJuridicalPrice();
                        } else {
                            balance += pack.getPersonalPrice();
                        }
                        break;
                    case PackageDTO.DISTRIBUTION_ON_EXTRA_POINT:
                        if (pack.getExternalPointCount() == null || pack.getExternalPointCount() == 0) {
                            pack.setExternalPointCount(1);
                        }
                        if (isJuridical) {
                            billSum += pack.getJuridicalPrice() * pack.getExternalPointCount();
                            balance += pack.getJuridicalPrice() * pack.getExternalPointCount();
                        } else {
                            billSum += pack.getPersonalPrice() * pack.getExternalPointCount();
                            balance += pack.getPersonalPrice() * pack.getExternalPointCount();
                        }
                        break;
                    case PackageDTO.EXTRA_POINT_INSTALLATION:
                        if (pack.getExternalPointCount() == null || pack.getExternalPointCount() == 0) {
                            pack.setExternalPointCount(1);
                        }
                        if (isJuridical) {
                            balance += pack.getJuridicalPrice() * pack.getExternalPointCount();
                        } else {
                            balance += pack.getPersonalPrice() * pack.getExternalPointCount();
                        }
                        servicePointsNumber += pack.getExternalPointCount();
                        break;
                    default:
                        break;
                }
                abonentPack = new AbonentPackages(abonent, abonentDAO.getEntityManager().find(Package.class, pack.getId()),
                        isJuridical ? pack.getJuridicalPrice() : null, isJuridical ? null : pack.getPersonalPrice(), user);
                abonentDAO.create(abonentPack);
            }
        } else {
            //აბონენტის არსებული პაკეტების ედიტია, რედაქტირდება მარტო წერტილების დამატება თუ მოხდა მაგათი რაოდენობის მხედვით ედიტდება ბალანსები და სააბონენტოს თანხა
            for (PackageDTO pack : request.getAbonentPackages()) {
                switch (pack.getGroup().getId()) {
                    case PackageDTO.DISTRIBUTION_ON_EXTRA_POINT:
                        if (abonent.getServicePointsNumber() != pack.getExternalPointCount() && pack.getExternalPointCount() != null) {
                            if (abonent.getServicePointsNumber() > pack.getExternalPointCount()) {// თუ წერტილების რაოდენობა შემცირდა
                                if (isJuridical) {
                                    billSum -= pack.getJuridicalPrice() * (abonent.getServicePointsNumber() - pack.getExternalPointCount());
                                    balance -= pack.getJuridicalPrice() * (abonent.getServicePointsNumber() - pack.getExternalPointCount());
                                } else {
                                    billSum -= pack.getPersonalPrice() * (abonent.getServicePointsNumber() - pack.getExternalPointCount());
                                    balance -= pack.getPersonalPrice() * (abonent.getServicePointsNumber() - pack.getExternalPointCount());
                                }
                                servicePointsNumber = pack.getExternalPointCount();
                            } else { // თუ წერტილების რაოდენობა გაიზარდა
                                if (isJuridical) {
                                    billSum += pack.getJuridicalPrice() * (pack.getExternalPointCount() - abonent.getServicePointsNumber());
                                    balance += pack.getJuridicalPrice() * (pack.getExternalPointCount() - abonent.getServicePointsNumber());
                                } else {
                                    billSum += pack.getPersonalPrice() * (pack.getExternalPointCount() - abonent.getServicePointsNumber());
                                    balance += pack.getPersonalPrice() * (pack.getExternalPointCount() - abonent.getServicePointsNumber());
                                }
                                servicePointsNumber = pack.getExternalPointCount();
                            }
                        }
                        break;
                    case PackageDTO.EXTRA_POINT_INSTALLATION:
                        if (abonent.getServicePointsNumber() != pack.getExternalPointCount() && pack.getExternalPointCount() != null) {
                            if (abonent.getServicePointsNumber() > pack.getExternalPointCount()) {// თუ წერტილების რაოდენობა შემცირდა
                                if (isJuridical) {
                                    balance -= pack.getJuridicalPrice() * (abonent.getServicePointsNumber() - pack.getExternalPointCount());
                                } else {
                                    balance -= pack.getPersonalPrice() * (abonent.getServicePointsNumber() - pack.getExternalPointCount());
                                }
                                servicePointsNumber = pack.getExternalPointCount();
                            } else { // თუ წერტილების რაოდენობა გაიზარდა
                                if (isJuridical) {
                                    balance += pack.getJuridicalPrice() * (pack.getExternalPointCount() - abonent.getServicePointsNumber());
                                } else {
                                    balance += pack.getPersonalPrice() * (pack.getExternalPointCount() - abonent.getServicePointsNumber());
                                }
                                servicePointsNumber = pack.getExternalPointCount();
                            }

                        }
                        break;
                    default:
                        break;
                }
            }
            /* თუ ახალი პაკეტებიც მიაბეს რო დაემატოს ბაზაში
            List<PackageDTO> newOnes = request.getAbonentPackages();
            newOnes.removeAll(abonentExistingPackages);

            if (!newOnes.isEmpty()) {
                for (PackageDTO pack : newOnes) {
                    abonentPack = new AbonentPackages(abonent, abonentDAO.getEntityManager().find(Package.class, pack.getId()),
                            isJuridical ? pack.getJuridicalPrice() : null, isJuridical ? null : pack.getPersonalPrice(), user);
                    abonentDAO.create(abonentPack);
                }
            }
            */
        }

        abonent.setPackageType((PackageType) abonentDAO.find(PackageType.class, request.getPackageTypeId()));
        abonent.setBill(billSum);
        abonent.setBalance(balance);
        abonent.setServicePointsNumber(servicePointsNumber);
        abonent = (Abonent) abonentDAO.update(abonent);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Abonent saveAbonent(AbonentDTO request) {

        Abonent obj = new Abonent();
        obj.setName(request.getName());
        obj.setLastname(request.getLastname());
        obj.setDeviceNumber(request.getDeviceNumber());
        obj.setPersonalNumber(request.getPersonalNumber());
        obj.setComment(request.getComment());
        obj.setBillDate(new java.sql.Date(request.getBillDate().getTime()));
        obj.setStreet((Street) abonentDAO.find(Street.class, request.getStreetId()));
        obj.setDistrict((District) abonentDAO.find(District.class, request.getDistrictId()));
        obj.setJuridicalOrPhisical(request.getJuridicalOrPhisical());
        obj.setStreetNumber(request.getStreetNumber());
        obj.setRoomNumber(request.getRoomNumber());
        obj.setFloor(request.getFloor());

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
            obj.setBalance(0.0);
            obj.setStatus((Status) abonentDAO.find(Status.class, StatusDTO.STATUS_ACTIVE));
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

    public List<Package> getAbonentPackages(int id) {
        return abonentDAO.getAbonentPackages(id);
    }
}

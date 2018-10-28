package ge.tvera.service;


import ge.tvera.dao.AbonentDAO;
import ge.tvera.dao.PackageDAO;
import ge.tvera.dto.*;
import ge.tvera.model.*;
import ge.tvera.model.Package;
import ge.tvera.request.AbonentPackagesRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * @author ucha
 */
@Service
public class AbonentService {

  final static Logger logger = Logger.getLogger(AbonentService.class);

  @Autowired
  private AbonentDAO abonentDAO;

  @Autowired
  private PackageDAO packageDAO;

  public HashMap<String, Object> getAbonents(int start, int limit, AbonentDTO srchRequest) {
    return abonentDAO.getAbonents(start, limit, srchRequest);
  }

  public HashMap<String, Object> getMonthlyBills(int start, int limit, MonthlyBillsDTO srchRequest) {
    return abonentDAO.getMonthlyBills(start, limit, srchRequest);
  }

  public HashMap<String, Object> calculateBills(List<PackageDTO> list, boolean isJuridical) {
    HashMap<String, Object> resultMap = new HashMap();

    Double billSum = 0.0;
    Double balance = 0.0;
    Double instalationBill = 0.0;
    Double restoreBill = 0.0;

    for (PackageDTO pack : list) {
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
            instalationBill += pack.getJuridicalPrice();
          } else {
            balance += pack.getPersonalPrice();
            instalationBill += pack.getPersonalPrice();
          }
          break;
        case PackageDTO.RESTORATION:
          if (isJuridical) {
            balance += pack.getJuridicalPrice();
            restoreBill += pack.getJuridicalPrice();
          } else {
            balance += pack.getPersonalPrice();
            restoreBill += pack.getPersonalPrice();
          }
          break;
        case PackageDTO.PORTIREBA:
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
            instalationBill += pack.getJuridicalPrice() * pack.getExternalPointCount();
          } else {
            balance += pack.getPersonalPrice() * pack.getExternalPointCount();
            instalationBill += pack.getPersonalPrice() * pack.getExternalPointCount();
          }
          break;
        default:
          break;
      }
    }
    resultMap.put("billSum", billSum);
    resultMap.put("balance", balance);
    resultMap.put("instalationBill", instalationBill);
    resultMap.put("restoreBill", restoreBill);
    return resultMap;
  }

  public List<BalanceHistoryDTO> getAbonentBalanceHistory(AbonentDTO srchRequest) {
    return abonentDAO.getAbonentBalanceHistory(srchRequest);
  }

  @Transactional(rollbackFor = Throwable.class)
  public void abonentPackagesAction(AbonentPackagesRequest request) {

    Abonent abonent = (Abonent) abonentDAO.find(Abonent.class, request.getAbonendId());
    Users user = (Users) abonentDAO.find(Users.class, request.getUserId());

    boolean isJuridical = abonent.getJuridicalOrPhisical() == AbonentDTO.JURIDICAL;
    Integer servicePointsNumber = request.getAbonentPackages().isEmpty() ? 0 : 1;

    List<Package> abonentExistingPackages = abonentDAO.getAbonentPackages(abonent.getId());
    Double billSum = (abonent == null || abonent.getBill() == null) ? 0.0 : abonent.getBill();
    Double balance = (abonent == null || abonent.getBalance() == null) ? 0.0 : abonent.getBalance();
    Double instalationBill = (abonent == null || abonent.getInstallationBill() == null) ? 0.0 : abonent.getInstallationBill();
    Double restoreBill = (abonent == null || abonent.getRestoreBill() == null) ? 0.0 : abonent.getRestoreBill();

    HashMap<String, Object> resultMap = null;
    AbonentPackages abonentPack = null;

    if (!abonentExistingPackages.isEmpty()) {
      // არსებული აბონენტია კალკულაცია კეთდება ბაზიდან წამოღებული აბონენტის პაკეტების ჯამების ცალკე და რექუესთიდან მოსულის ცალკე და მეტია თუ ნაკლები ირკვევა

      abonentDAO.deleteAbonentPackages(abonent.getId());// არსებული პაკეტების დადისეიბლება ბაზაში deleted=2

      for (PackageDTO pack : request.getAbonentPackages()) {// ხელახლა ინსერტი შესაძლო ცვლილიებებიანა
        abonentPack = new AbonentPackages(abonent, abonentDAO.getEntityManager().find(Package.class, pack.getId()),
            isJuridical ? pack.getJuridicalPrice() : null, isJuridical ? null : pack.getPersonalPrice(), user, pack.getExternalPointCount());
        abonentDAO.create(abonentPack);
      }

      // ახლად მოსულების კალკულაცია
      resultMap = calculateBills(request.getAbonentPackages(), abonent.getJuridicalOrPhisical() == AbonentDTO.JURIDICAL);

      Double newBillSum = (Double) resultMap.get("billSum");
      Double newBalance = (Double) resultMap.get("balance");
      Double newInstalationBill = (Double) resultMap.get("instalationBill");
      Double newRestoreBill = (Double) resultMap.get("restoreBill");

      Double existBillSum = 0.0;
      Double existBalance = 0.0;
      Double existInstalationBill = 0.0;
      Double existRestoreBill = 0.0;
      //უკვე არსებულების კალკულაცია
      resultMap = calculateBills(PackageDTO.parseToList(abonentExistingPackages), abonent.getJuridicalOrPhisical() == AbonentDTO.JURIDICAL);

      existBillSum = (Double) resultMap.get("billSum");
      existBalance = (Double) resultMap.get("balance");
      existInstalationBill = (Double) resultMap.get("instalationBill");
      existRestoreBill = (Double) resultMap.get("restoreBill");

      if (existBillSum != null && newBillSum != null && existBillSum != newBillSum) {
        if (existBillSum > newBillSum) {
          billSum -= Math.abs((existBillSum - newBillSum));
        } else {
          billSum += Math.abs((existBillSum - newBillSum));
        }
      }
      if (existBalance != null && newBalance != null && existBalance != newBalance) {
        if (existBalance > newBalance) {
          balance -= Math.abs((existBalance - newBalance));
        } else {
          balance += Math.abs((existBalance - newBalance));
        }
      }
      if (existInstalationBill != null && newInstalationBill != null && existInstalationBill != newInstalationBill) {
        if (existInstalationBill > newInstalationBill) {
          instalationBill -= Math.abs((existInstalationBill - newInstalationBill));
        } else {
          instalationBill += Math.abs((existInstalationBill - newInstalationBill));
        }
      }
      if (existRestoreBill != null && newRestoreBill != null && existRestoreBill != newRestoreBill) {
        if (existRestoreBill > newRestoreBill) {
          restoreBill -= Math.abs((existRestoreBill - newRestoreBill));
        } else {
          restoreBill += Math.abs((existRestoreBill - newRestoreBill));
        }
      }

    } else {
      //ახალი აბონენტია და პირველი მიბმაა პაკეტების
      resultMap = calculateBills(request.getAbonentPackages(), abonent.getJuridicalOrPhisical() == AbonentDTO.JURIDICAL);

      for (PackageDTO pack : request.getAbonentPackages()) {
        abonentPack = new AbonentPackages(abonent, abonentDAO.getEntityManager().find(Package.class, pack.getId()),
            isJuridical ? pack.getJuridicalPrice() : null, isJuridical ? null : pack.getPersonalPrice(), user, pack.getExternalPointCount());
        abonentDAO.create(abonentPack);
      }
      billSum = (Double) resultMap.get("billSum");
      balance = (Double) resultMap.get("balance");
      instalationBill = (Double) resultMap.get("instalationBill");
      restoreBill = (Double) resultMap.get("restoreBill");

    }
    //ფინალური სეივი ბაზაში
    if (!request.getAbonentPackages().isEmpty()) {
      abonent.setPackageType((PackageType) abonentDAO.find(PackageType.class,
          request.getPackageTypeId() != null ? request.getPackageTypeId() : request.getAbonentPackages().get(0).getType().getId()));
      abonent.setBill(billSum);
      abonent.setBalance(balance);
      abonent.setServicePointsNumber(servicePointsNumber);
      abonent.setInstallationBill(instalationBill);
      abonent.setRestoreBill(restoreBill);
      abonent = (Abonent) abonentDAO.update(abonent);
    }
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
    obj.setPhone(request.getPhone());

    if (request.getId() != null) {
      Abonent tmp = (Abonent) abonentDAO.find(Abonent.class, request.getId());
      obj.setBalance(tmp.getBalance());
      obj.setCollectedBill(tmp.getCollectedBill());
      obj.setStartPay(tmp.getStartPay());
      obj.setBill(tmp.getBill());
      obj.setInstallationBill(tmp.getInstallationBill());
      obj.setRestoreBill(tmp.getRestoreBill());
      obj.setId(request.getId());
      obj.setStatus(tmp.getStatus());
      obj.setPackageType(tmp.getPackageType());
      obj.setServicePointsNumber(tmp.getServicePointsNumber());
      obj = (Abonent) abonentDAO.update(obj);
    } else {
      obj.setBalance(0.0);
      obj.setCollectedBill(0.0);
      obj.setStatus((Status) abonentDAO.find(Status.class, StatusDTO.STATUS_ACTIVE));
      obj = (Abonent) abonentDAO.create(obj);
    }
    return obj;
  }

//  @Transactional(rollbackFor = Throwable.class)
//  public AbonentPackages saveAbonentPackages(AbonentPackageDTO request) {
//
//    AbonentPackages obj = new AbonentPackages();
//    obj.setAbonent((Abonent) abonentDAO.find(Abonent.class, request.getAbonentId()));
//    obj.setPackages((Package) abonentDAO.find(Package.class, request.getPackageId()));
//    obj.setPointsNumber(request.getPointsNumber());
//    if (request.getId() != null) {
//      obj.setId(request.getId());
//      obj = (AbonentPackages) abonentDAO.update(obj);
//    } else {
//      obj = (AbonentPackages) abonentDAO.create(obj);
//    }
//    return obj;
//  }

  @Transactional(rollbackFor = Throwable.class)
  public void deleteAbonent(int id) {
    Abonent obj = (Abonent) abonentDAO.find(Abonent.class, id);
    if (obj != null) {
      abonentDAO.delete(obj);
    }
  }

  @Transactional(rollbackFor = Throwable.class)
  public void changeStatusHistoryDate(int statHistoryId, Date disableDate, int userId) {

    Users user = (Users) abonentDAO.find(Users.class, userId);

    StatusHistory stHistory = (StatusHistory) abonentDAO.find(StatusHistory.class, statHistoryId);

    logger.warn("*********  Change Status History Date By " + user.getUserDesc()
        + " ************ Old Date: " + stHistory.getDisableDate() + "  Changed To: " + disableDate);

    if (stHistory != null && stHistory.getDisableDate() != null) {
      Abonent obj = (Abonent) abonentDAO.find(Abonent.class, stHistory.getAbonent().getId());
      int daysCountBetween = daysBetween(stHistory.getDisableDate(), disableDate);
      int daysCountBeforeToday = daysBetween(new java.util.Date(), disableDate);
      boolean oldDate = stHistory.getDisableDate().after(disableDate);
      Calendar tmpCal = Calendar.getInstance();
      tmpCal.setTime(disableDate);
      int daysCountInMonth = tmpCal.getActualMaximum(Calendar.DAY_OF_MONTH);

      if (stHistory.getStatus().getId() == StatusDTO.STATUS_ACTIVE) {
        if (oldDate) {
          // ძველი თარიღით თიშავს ანუ ბალანსიდან უნდა მოაკლდეს გათიშვის თარიღის მერე რა დღეებისაც დაერიცხა
          if (obj.getBill() != null) {
            Double dailyBill = obj.getBill() / daysCountInMonth;
            obj.setBalance(obj.getBalance() + (dailyBill * daysCountBetween));
            obj.setCollectedBill(obj.getCollectedBill() + (dailyBill * daysCountBetween));
          }
        } else {
          // მომავლის თარიღით თიშავს ანუ ბალანსს უნდა დაემატოს დღეიდან გათიშვის თარიღამდე რამდენი დღისაც იქნება
          if (obj.getBill() != null) {
            Double dailyBill = obj.getBill() / daysCountInMonth;
            obj.setBalance(obj.getBalance() - (dailyBill * daysCountBetween));
            obj.setCollectedBill(obj.getCollectedBill() - (dailyBill * daysCountBetween));
          }
        }
        abonentDAO.update(obj);
      } else {
        if (stHistory.getStatus().getId() == StatusDTO.STATUS_DISABLED) {
          if (oldDate) {
            // ძველი თარიღით ააქტიურებს ანუ ბალანსიდან უნდა მოაკლდეს გათიშვის თარიღის მერე რა დღეებისაც დაერიცხა
            if (obj.getBill() != null) {
              obj.setStartPay(null);
              Double dailyBill = obj.getBill() / daysCountInMonth;
              obj.setBalance(obj.getBalance() - (dailyBill * daysCountBetween));
              obj.setCollectedBill(obj.getCollectedBill() - (dailyBill * daysCountBetween));
            }
          } else {
            // მომავლის თარიღით რაც არ დაერიცხა უნდა დაუმატოს გადასახდელში
            if (obj.getBill() != null) {
              obj.setStartPay(null);
              Double dailyBill = obj.getBill() / daysCountInMonth;
              obj.setBalance(obj.getBalance() + (dailyBill * daysCountBetween));
              obj.setCollectedBill(obj.getCollectedBill() + (dailyBill * daysCountBetween));
            }
          }
          abonentDAO.update(obj);
        }
      }

      stHistory.setUser(user);
      stHistory.setDisableDate(disableDate);
      abonentDAO.update(stHistory);
    }
  }

  @Transactional(rollbackFor = Throwable.class)
  public void changeServiceStatus(int id, Date disableDate, int userId) {
    StatusHistory stHistory = new StatusHistory();
    Abonent obj = (Abonent) abonentDAO.find(Abonent.class, id);
    int daysCountBetween = daysBetween(new java.util.Date(), disableDate);
    boolean oldDate = new java.util.Date().after(disableDate);
    boolean isToday = new java.util.Date().compareTo(disableDate) == 0;
    int daysCountInMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

    if (obj.getStatus().getId() == StatusDTO.STATUS_ACTIVE) {
      if (!isToday) {
        if (oldDate) {
          // ძველი თარიღით თიშავს ანუ ბალანსიდან უნდა მოაკლდეს გათიშვის თარიღის მერე რა დღეებისაც დაერიცხა
          if (obj.getBill() != null) {
            Double dailyBill = obj.getBill() / daysCountInMonth;
            obj.setBalance(obj.getBalance() - (dailyBill * daysCountBetween));
            obj.setCollectedBill(obj.getCollectedBill() - (dailyBill * daysCountBetween));
          }
        } else {
          // მომავლის თარიღით თიშავს ანუ ბალანსს უნდა დაემატოს დღეიდან გათიშვის თარიღამდე რამდენი დღისაც იქნება
          if (obj.getBill() != null) {
            Double dailyBill = obj.getBill() / daysCountInMonth;
            obj.setBalance(obj.getBalance() + (dailyBill * daysCountBetween + 1));
            obj.setCollectedBill(obj.getCollectedBill() + (dailyBill * daysCountBetween + 1));
          }
        }
      }
      obj.setStatus((Status) abonentDAO.find(Status.class, StatusDTO.STATUS_DISABLED));
      abonentDAO.update(obj);
    } else {
      if (obj.getStatus().getId() == StatusDTO.STATUS_DISABLED) {

        if (!isToday) {
          if (oldDate) {
            // ძველი თარიღით ააქტიურებს ანუ ბალანსიდან უნდა მოაკლდეს გათიშვის თარიღის მერე რა დღეებისაც დაერიცხა
            if (obj.getBill() != null) {
              obj.setStartPay(null);
              Double dailyBill = obj.getBill() / daysCountInMonth;
              obj.setBalance(obj.getBalance() + (dailyBill * daysCountBetween));
              obj.setCollectedBill(obj.getCollectedBill() + (dailyBill * daysCountBetween));
            }
          } else {
            // მომავლის თარიღით ააქტიურებს ანუ ბალანსს უნდა დაემატოს დღეიდან გათიშვის თარიღამდე რამდენი დღისაც იქნება
            obj.setStartPay(disableDate);
            //სტატუსი ქვევით უაქტიურდება
          }
        }

        obj.setStatus((Status) abonentDAO.find(Status.class, StatusDTO.STATUS_ACTIVE));
        abonentDAO.update(obj);
      }
    }
    stHistory.setStatus(obj.getStatus());
    stHistory.setAbonent(obj);
    stHistory.setDisableDate(disableDate);
    stHistory.setUser((Users) abonentDAO.find(Users.class, userId));
    abonentDAO.create(stHistory);
  }

  public List<StatusHistoryDTO> getStatusHistory(int id) {
    return StatusHistoryDTO.parseToList(abonentDAO.getStatusHistory(id));
  }

  public List<Package> getAbonentPackages(int id) {
    return abonentDAO.getAbonentPackages(id);
  }

  public int daysBetween(java.util.Date d1, java.util.Date d2) {
    return Math.abs((int) (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
  }
}

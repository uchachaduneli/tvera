package ge.tvera.service;


import ge.tvera.dao.MiscDAO;
import ge.tvera.dto.DistrictDTO;
import ge.tvera.dto.IncasatorDTO;
import ge.tvera.dto.PackageGroupDTO;
import ge.tvera.dto.PackageTypeDTO;
import ge.tvera.model.District;
import ge.tvera.model.Incasator;
import ge.tvera.model.PackageGroup;
import ge.tvera.model.PackageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author ucha
 */
@Service
public class MiscService {

    @Autowired
    private MiscDAO miscDAO;

    public List<PackageTypeDTO> getPackageTypes() {
        return PackageTypeDTO.parseToList(miscDAO.getAll(PackageType.class));
    }

    public List<PackageGroupDTO> getPackageGroups() {
        return PackageGroupDTO.parseToList(miscDAO.getAll(PackageGroup.class));
    }

    public List<DistrictDTO> getDistricts() {
        return DistrictDTO.parseToList(miscDAO.getAll(District.class));
    }

    @Transactional(rollbackFor = Throwable.class)
    public District saveDistrict(DistrictDTO request) {

        District obj = new District();
        obj.setName(request.getName());
        obj.setIncasator((Incasator) miscDAO.find(Incasator.class, request.getIncasatorId()));

        if (request.getId() != null) {
            obj.setId(request.getId());
            obj = (District) miscDAO.update(obj);
        } else {
            obj = (District) miscDAO.create(obj);
        }
        return obj;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteDistrict(int id) {
        District obj = (District) miscDAO.find(District.class, id);
        if (obj != null) {
            miscDAO.delete(obj);
        }
    }

    public HashMap<String, Object> getIncasators(int start, int limit, IncasatorDTO srchRequest) {
        return miscDAO.getIncasators(start, limit, srchRequest);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Incasator saveIncasator(IncasatorDTO request) {

        Incasator obj = new Incasator();
        obj.setName(request.getName());
        obj.setLastname(request.getLastname());

        if (request.getId() != null) {
            obj.setId(request.getId());
            obj = (Incasator) miscDAO.update(obj);
        } else {
            obj = (Incasator) miscDAO.create(obj);
        }
        return obj;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteIncasator(int id) {
        Incasator obj = (Incasator) miscDAO.find(Incasator.class, id);
        if (obj != null) {
            miscDAO.delete(obj);
        }
    }
}

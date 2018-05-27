package ge.tvera.service;


import ge.tvera.dao.MiscDAO;
import ge.tvera.dto.DistrictDTO;
import ge.tvera.dto.PackageGroupDTO;
import ge.tvera.dto.PackageTypeDTO;
import ge.tvera.model.District;
import ge.tvera.model.PackageGroup;
import ge.tvera.model.PackageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

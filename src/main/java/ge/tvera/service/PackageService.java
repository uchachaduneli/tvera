package ge.tvera.service;


import ge.tvera.dao.PackageDAO;
import ge.tvera.dao.ParamValuePair;
import ge.tvera.dto.PackageDTO;
import ge.tvera.model.Package;
import ge.tvera.model.PackageGroup;
import ge.tvera.model.PackageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author ucha
 */
@Service
public class PackageService {

    @Autowired
    private PackageDAO packageDAO;
    List<ParamValuePair> orderBy = new ArrayList<>();
    ParamValuePair pr = new ParamValuePair("group.id", "ASC");

    public List<Package> getPackages() {
        return packageDAO.getAllByParamValue(Package.class, null, orderBy);
    }

    public HashMap<String, Object> getGroupedPackages() {
        return packageDAO.getGroupedPackages();
    }

    @Transactional(rollbackFor = Throwable.class)
    public Package savePackage(PackageDTO request) {

        Package obj = new Package();
        obj.setName(request.getName());
        obj.setJuridicalPrice(request.getJuridicalPrice());
        obj.setPersonalPrice(request.getPersonalPrice());
        obj.setScheduler(request.getScheduler());
        obj.setGroup((PackageGroup) packageDAO.find(PackageGroup.class, request.getGroupId()));
        obj.setType((PackageType) packageDAO.find(PackageType.class, request.getTypeId()));

        if (request.getId() != null) {
            obj.setId(request.getId());
            obj = (Package) packageDAO.update(obj);
        } else {
            obj = (Package) packageDAO.create(obj);
        }
        return obj;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deletePackage(int id) {
        Package obj = (Package) packageDAO.find(Package.class, id);
        if (obj != null) {
            packageDAO.delete(obj);
        }
    }
}

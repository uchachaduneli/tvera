package ge.tvera.service;


import ge.tvera.dao.StreetDAO;
import ge.tvera.dto.StreetDTO;
import ge.tvera.model.Street;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author ucha
 */
@Service
public class StreetService {

    @Autowired
    private StreetDAO streetDAO;

    public List<StreetDTO> getAllStreets() {
        return StreetDTO.parseToList(streetDAO.getAll(Street.class));
    }

    public HashMap<String, Object> getStreets(int start, int limit, StreetDTO srchRequest) {
        return streetDAO.getStreets(start, limit, srchRequest);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Street saveStreet(StreetDTO request) {

        Street obj = new Street();
        obj.setName(request.getName());

        if (request.getId() != null) {
            obj.setId(request.getId());
            obj = (Street) streetDAO.update(obj);
        } else {
            obj = (Street) streetDAO.create(obj);
        }
        return obj;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteStreet(int id) {
        Street obj = (Street) streetDAO.find(Street.class, id);
        if (obj != null) {
            streetDAO.delete(obj);
        }
    }
}

package ge.tvera.service;


import ge.tvera.dao.ReportDAO;
import ge.tvera.dto.AbonentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author ucha
 */
@Service
public class ReportService {
    @Autowired
    private ReportDAO reportDAO;

    public HashMap<String, Object> getAbonents(AbonentDTO srchRequest) {
        return reportDAO.getCountReport(srchRequest);
    }
}

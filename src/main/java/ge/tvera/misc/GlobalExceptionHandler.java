package ge.tvera.misc;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static transient Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) throws Exception {
        logger.error("********** Exception Occured !!! *************", exception);
//        response.setStatus(400); //bad request
        Response rs = new Response();
        rs.setData(exception);
        rs.setErrorCode(700);
        if (exception instanceof org.springframework.dao.DataIntegrityViolationException) {
            if (exception.getCause() != null && exception.getCause().getCause() != null) {
                rs.setMessage("ოპერაცია არ სრულდება (" + exception.getCause().getCause().getLocalizedMessage() + ")");
            }
            return rs;
        } else if (exception instanceof org.springframework.dao.InvalidDataAccessApiUsageException) {
            rs.setMessage("ოპერაცია არ სრულდება");
            return rs;
        } else {
            rs.setMessage("ოპერაცია არ სრულდება");
            return rs;
        }
//        return "asdasd";
    }
}

package ge.tvera.misc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ge.tvera.misc.JsonDateDeSerializeSupport.DATE_FORMAT;

/**
 * @author ucha
 */
public class JsonDateSerializeSupport extends JsonSerializer<Date> {

    @Override
    public void serialize(Date t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        jg.writeString(t != null ? dateFormat.format(t) : "");
    }

}

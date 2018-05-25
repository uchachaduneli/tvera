package ge.tvera.misc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ucha
 */
public class JsonDateTimeSerializeSupport extends JsonSerializer<Date> {

    @Override
    public void serialize(Date t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        jg.writeString(t != null ? dateFormat.format(t) : "");
    }

}

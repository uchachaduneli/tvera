
package ge.tvera.misc;

import com.google.gson.Gson;

/**
 * @author ucha
 */
public abstract class JsonSerializable {

    public String serialize() {
        Gson g = new Gson();
        return g.toJson(this, getClass());
    }
}
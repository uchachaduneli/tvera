/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ge.tvera.utils;

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

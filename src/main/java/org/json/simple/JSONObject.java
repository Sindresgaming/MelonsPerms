/*
 * $Id: JSONObject.java,v 1.1 2006/04/15 14:10:48 platform Exp $
 * Created on 2006-4-10
 */
package org.json.simple;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 * <p>
 * Written by Devon "Turqmelon" - http://turqmelon.com
 * <p>
 * This code is licensed under Creative Commons Attributation-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * <p>
 * You are free to:
 * SHARE - copy and redistribute the material in any medium or format
 * ADAPT - remix, transform, and build upon the material
 * <p>
 * The licensor cannot revoke these freedoms as long as you follow the license terms.
 * <p>
 * Under the following terms:
 * ATTRIBUTION - You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 * <p>
 * NONCOMMERCIAL - You may not use the material for commercial purposes.
 * <p>
 * SHAREALIKE - If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
 * <p>
 * Full License: http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 ******************************************************************************/

/*******************************************************************************
 * Written by Devon "Turqmelon" - http://turqmelon.com
 *
 * This code is licensed under Creative Commons Attributation-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 *
 * You are free to:
 * SHARE - copy and redistribute the material in any medium or format
 * ADAPT - remix, transform, and build upon the material
 *
 * The licensor cannot revoke these freedoms as long as you follow the license terms.
 *
 * Under the following terms:
 * ATTRIBUTION - You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 *
 * NONCOMMERCIAL - You may not use the material for commercial purposes.
 *
 * SHAREALIKE - If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
 *
 * Full License: http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 ******************************************************************************/

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
 *
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public class JSONObject extends HashMap implements Map, JSONAware, JSONStreamAware {

    private static final long serialVersionUID = -503443796854799292L;


    public JSONObject() {
        super();
    }

    /**
     * Allows creation of a JSONObject from a Map. After that, both the
     * generated JSONObject and the Map can be modified independently.
     *
     * @param map
     */
    public JSONObject(Map map) {
        super(map);
    }


    /**
     * Encode a map into JSON text and write it to out.
     * If this map is also a JSONAware or JSONStreamAware, JSONAware or JSONStreamAware specific behaviours will be ignored at this top level.
     *
     * @see org.json.simple.JSONValue#writeJSONString(Object, Writer)
     *
     * @param map
     * @param out
     */
    public static void writeJSONString(Map map, Writer out) throws IOException {
        if (map == null) {
            out.write("null");
            return;
        }

        boolean first = true;
        Iterator iter = map.entrySet().iterator();

        out.write('{');
        while (iter.hasNext()) {
            if (first)
                first = false;
            else
                out.write(',');
            Map.Entry entry = (Map.Entry) iter.next();
            out.write('\"');
            out.write(escape(String.valueOf(entry.getKey())));
            out.write('\"');
            out.write(':');
            JSONValue.writeJSONString(entry.getValue(), out);
        }
        out.write('}');
    }

    public void writeJSONString(Writer out) throws IOException {
        writeJSONString(this, out);
    }

    /**
     * Convert a map to JSON text. The result is a JSON object.
     * If this map is also a JSONAware, JSONAware specific behaviours will be omitted at this top level.
     *
     * @see org.json.simple.JSONValue#toJSONString(Object)
     *
     * @param map
     * @return JSON text, or "null" if map is null.
     */
    public static String toJSONString(Map map) {
        if (map == null)
            return "null";

        StringBuffer sb = new StringBuffer();
        boolean first = true;
        Iterator iter = map.entrySet().iterator();

        sb.append('{');
        while (iter.hasNext()) {
            if (first)
                first = false;
            else
                sb.append(',');

            Map.Entry entry = (Map.Entry) iter.next();
            toJSONString(String.valueOf(entry.getKey()), entry.getValue(), sb);
        }
        sb.append('}');
        return sb.toString();
    }

    public String toJSONString() {
        return toJSONString(this);
    }

    private static String toJSONString(String key, Object value, StringBuffer sb) {
        sb.append('\"');
        if (key == null)
            sb.append("null");
        else
            JSONValue.escape(key, sb);
        sb.append('\"').append(':');

        sb.append(JSONValue.toJSONString(value));

        return sb.toString();
    }

    public String toString() {
        return toJSONString();
    }

    public static String toString(String key, Object value) {
        StringBuffer sb = new StringBuffer();
        toJSONString(key, value, sb);
        return sb.toString();
    }

    /**
     * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters (U+0000 through U+001F).
     * It's the same as JSONValue.escape() only for compatibility here.
     *
     * @see org.json.simple.JSONValue#escape(String)
     *
     * @param s
     * @return
     */
    public static String escape(String s) {
        return JSONValue.escape(s);
    }
}

/*
 * $Id: JSONArray.java,v 1.1 2006/04/15 14:10:48 platform Exp $
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A JSON array. JSONObject supports java.util.List interface.
 *
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public class JSONArray extends ArrayList implements List, JSONAware, JSONStreamAware {
    private static final long serialVersionUID = 3957988303675231981L;

    /**
     * Encode a list into JSON text and write it to out. 
     * If this list is also a JSONStreamAware or a JSONAware, JSONStreamAware and JSONAware specific behaviours will be ignored at this top level.
     *
     * @see org.json.simple.JSONValue#writeJSONString(Object, Writer)
     *
     * @param list
     * @param out
     */
    public static void writeJSONString(List list, Writer out) throws IOException {
        if (list == null) {
            out.write("null");
            return;
        }

        boolean first = true;
        Iterator iter = list.iterator();

        out.write('[');
        while (iter.hasNext()) {
            if (first)
                first = false;
            else
                out.write(',');

            Object value = iter.next();
            if (value == null) {
                out.write("null");
                continue;
            }

            JSONValue.writeJSONString(value, out);
        }
        out.write(']');
    }

    public void writeJSONString(Writer out) throws IOException {
        writeJSONString(this, out);
    }

    /**
     * Convert a list to JSON text. The result is a JSON array.
     * If this list is also a JSONAware, JSONAware specific behaviours will be omitted at this top level.
     *
     * @see org.json.simple.JSONValue#toJSONString(Object)
     *
     * @param list
     * @return JSON text, or "null" if list is null.
     */
    public static String toJSONString(List list) {
        if (list == null)
            return "null";

        boolean first = true;
        StringBuffer sb = new StringBuffer();
        Iterator iter = list.iterator();

        sb.append('[');
        while (iter.hasNext()) {
            if (first)
                first = false;
            else
                sb.append(',');

            Object value = iter.next();
            if (value == null) {
                sb.append("null");
                continue;
            }
            sb.append(JSONValue.toJSONString(value));
        }
        sb.append(']');
        return sb.toString();
    }

    public String toJSONString() {
        return toJSONString(this);
    }

    public String toString() {
        return toJSONString();
    }


}

package org.json.simple.example;

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

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This example will try to mimic the JSON representation you can find
 * in JSON page on Wikipedia[1].
 *
 *    {
 *	     "firstName": "John",
 *	     "lastName": "Smith",
 *	     "age": 25,
 *	     "address":
 *         {
 *	         "streetAddress": "21 2nd Street",
 *	         "city": "New York",
 *	         "state": "NY",
 *	         "postalCode": "10021"
 *         },
 *	     "phoneNumber":
 *	     [
 *             {
 *	           "type": "home",
 *	           "number": "212 555-1234"
 *             },
 *             {
 *	           "type": "fax",
 *	           "number": "646 555-4567"
 *             }
 *	     ]
 *     }
 *
 * [1]http://en.wikipedia.org/wiki/JSON
 *
 * @author Piero Ottuzzi <piero.ottuzzi@brucalipto.org>
 *
 */

public class Wikipedia {
    public static void main(String[] args) {
        new Wikipedia();
    }

    public Wikipedia() {
        Map addressMap = new LinkedHashMap();
        addressMap.put("streetAddress", "21 2nd Street");
        addressMap.put("city", "New York");
        addressMap.put("state", "NY");
        addressMap.put("postalCode", "10021");

        Map phone1 = new LinkedHashMap();
        phone1.put("type", "home");
        phone1.put("number", "212 555-1234");

        Map phone2 = new LinkedHashMap();
        phone2.put("type", "fax");
        phone2.put("number", "646 555-4567");

        JSONArray phones = new JSONArray();
        phones.add(phone1);
        phones.add(phone2);

        Map userMap = new LinkedHashMap();
        userMap.put("firstName", "John");
        userMap.put("lastName", "Smith");
        userMap.put("age", new Integer(25));
        userMap.put("address", addressMap);
        userMap.put("phoneNumber", phones);

        System.out.println(JSONValue.toJSONString(userMap));
    }

}

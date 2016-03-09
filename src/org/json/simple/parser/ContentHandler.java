package org.json.simple.parser;

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

/**
 * A simplified and stoppable SAX-like content handler for stream processing of JSON text. 
 *
 * @see org.xml.sax.ContentHandler
 * @see org.json.simple.parser.JSONParser#parse(java.io.Reader, ContentHandler, boolean)
 *
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public interface ContentHandler {
    /**
     * Receive notification of the beginning of JSON processing.
     * The parser will invoke this method only once.
     *
     * @throws ParseException
     * 			- JSONParser will stop and throw the same exception to the caller when receiving this exception.
     */
    void startJSON() throws ParseException, IOException;

    /**
     * Receive notification of the end of JSON processing.
     *
     * @throws ParseException
     */
    void endJSON() throws ParseException, IOException;

    /**
     * Receive notification of the beginning of a JSON object.
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException
     *          - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     * @see #endJSON
     */
    boolean startObject() throws ParseException, IOException;

    /**
     * Receive notification of the end of a JSON object.
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException
     *
     * @see #startObject
     */
    boolean endObject() throws ParseException, IOException;

    /**
     * Receive notification of the beginning of a JSON object entry.
     *
     * @param key - Key of a JSON object entry.
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException
     *
     * @see #endObjectEntry
     */
    boolean startObjectEntry(String key) throws ParseException, IOException;

    /**
     * Receive notification of the end of the value of previous object entry.
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException
     *
     * @see #startObjectEntry
     */
    boolean endObjectEntry() throws ParseException, IOException;

    /**
     * Receive notification of the beginning of a JSON array.
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException
     *
     * @see #endArray
     */
    boolean startArray() throws ParseException, IOException;

    /**
     * Receive notification of the end of a JSON array.
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException
     *
     * @see #startArray
     */
    boolean endArray() throws ParseException, IOException;

    /**
     * Receive notification of the JSON primitive values:
     * 	java.lang.String,
     * 	java.lang.Number,
     * 	java.lang.Boolean
     * 	null
     *
     * @param value - Instance of the following:
     * 			java.lang.String,
     * 			java.lang.Number,
     * 			java.lang.Boolean
     * 			null
     *
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException
     */
    boolean primitive(Object value) throws ParseException, IOException;

}

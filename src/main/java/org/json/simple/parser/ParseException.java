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

/**
 * ParseException explains why and where the error occurs in source JSON text.
 *
 * @author FangYidong<fangyidong@yahoo.com.cn>
 *
 */
public class ParseException extends Exception {
    private static final long serialVersionUID = -7880698968187728548L;

    public static final int ERROR_UNEXPECTED_CHAR = 0;
    public static final int ERROR_UNEXPECTED_TOKEN = 1;
    public static final int ERROR_UNEXPECTED_EXCEPTION = 2;

    private int errorType;
    private Object unexpectedObject;
    private int position;

    public ParseException(int errorType) {
        this(-1, errorType, null);
    }

    public ParseException(int errorType, Object unexpectedObject) {
        this(-1, errorType, unexpectedObject);
    }

    public ParseException(int position, int errorType, Object unexpectedObject) {
        this.position = position;
        this.errorType = errorType;
        this.unexpectedObject = unexpectedObject;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    /**
     * @see org.json.simple.parser.JSONParser#getPosition()
     *
     * @return The character position (starting with 0) of the input where the error occurs.
     */
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @see org.json.simple.parser.Yytoken
     *
     * @return One of the following base on the value of errorType:
     * 		   	ERROR_UNEXPECTED_CHAR		java.lang.Character
     * 			ERROR_UNEXPECTED_TOKEN		org.json.simple.parser.Yytoken
     * 			ERROR_UNEXPECTED_EXCEPTION	java.lang.Exception
     */
    public Object getUnexpectedObject() {
        return unexpectedObject;
    }

    public void setUnexpectedObject(Object unexpectedObject) {
        this.unexpectedObject = unexpectedObject;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        switch (errorType) {
            case ERROR_UNEXPECTED_CHAR:
                sb.append("Unexpected character (").append(unexpectedObject).append(") at position ").append(position).append(".");
                break;
            case ERROR_UNEXPECTED_TOKEN:
                sb.append("Unexpected token ").append(unexpectedObject).append(" at position ").append(position).append(".");
                break;
            case ERROR_UNEXPECTED_EXCEPTION:
                sb.append("Unexpected exception at position ").append(position).append(": ").append(unexpectedObject);
                break;
            default:
                sb.append("Unkown error at position ").append(position).append(".");
                break;
        }
        return sb.toString();
    }
}

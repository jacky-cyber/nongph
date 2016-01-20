package cn.globalph.openadmin.web.rulebuilder;

import cn.globalph.common.exception.TranslatableException;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public class MVELTranslationException extends TranslatableException {

    private static final long serialVersionUID = 1L;

    public static final int SPECIFIED_FIELD_NOT_FOUND = 100;
    public static final int NO_FIELD_FOUND_IN_RULE = 101;
    public static final int INCOMPATIBLE_DATE_VALUE = 102;
    public static final int UNRECOGNIZABLE_RULE = 103;
    public static final int OPERATOR_NOT_FOUND = 104;
    public static final int INCOMPATIBLE_DECIMAL_VALUE = 105;
    public static final int INCOMPATIBLE_INTEGER_VALUE = 106;
    public static final int INCOMPATIBLE_RULE = 107;

    public MVELTranslationException(int code, String message) {
        super(code, message);
    }
}

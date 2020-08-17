import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class LegalCharSetUtil {
    //+的位置填充特征值
    private static final String EIGENVALUE_CHAR = "\\+";
    //_的位置使用随机数填充
    private static final String RANDOM_CHAR = "_";

    private static final String CHAR_SET = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void check(String pattern) {
        blankCheck(pattern);
        String s = pattern.replaceAll(RANDOM_CHAR, "").replaceAll(EIGENVALUE_CHAR,"");
        blankCheck(pattern);
        repeatCheck(s);
        String sort = sort(s);
        if (!CHAR_SET.contains(sort)) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, "illegal char sequence");
        }
        if (sort.charAt(0) != '1') {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, "illegal char sequence");
        }
    }

    private static void blankCheck(String str) {
        if (StringUtils.isBlank(str)) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, "illegal char sequence");
        }
    }

    private static void repeatCheck(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (str.lastIndexOf(c) != i) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, "illegal char sequence");
            }
        }
    }

    private static String sort(String str) {
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}

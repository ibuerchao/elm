import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CodeUtil {

    private static final SecureRandom random = new SecureRandom();

    //+的位置填充特征值
    private static final char EIGENVALUE_CHAR = '+';
    //_的位置使用随机数填充
    private static final char RANDOM_CHAR = '_';

    private static final Map<String, Rule> cache = new ConcurrentHashMap<>();

    private static int s = 34567;

    public static String getCode(String key, String pattern, String... eigenvalue) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(pattern)) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, "illegal char sequence");
        }
        Rule rule = getRuleFromCache(pattern, eigenvalue);
        long seq = getNextSeq(key, pattern);
        if (seq > rule.getMax()) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, "exceeds the maximum allowable value");
        }
        String str = StringUtils.leftPad(String.valueOf(seq), rule.getCount(), '0');
        StringBuilder sb = new StringBuilder();
        int[] r = rule.getRule();

        for (int i = 0, j = 0; i < r.length; i++) {
            if (r[i] == -1) {
                sb.append(random.nextInt(10));
            } else if (r[i] == -2) {
                sb.append(eigenvalue[j]);
                j++;
            } else {
                sb.append(str.charAt(r[i]));
            }
        }
        return sb.toString();
    }

    private static long getNextSeq(String key, String pattern) {
        return s++;
    }

    private static Rule getRuleFromCache(String pattern, String... eigenvalue) {
        Rule rule = cache.get(pattern);
        if (rule == null) {
            LegalCharSetUtil.check(pattern);
            rule = resolvePattern(pattern);
            if (rule.getNum() != eigenvalue.length) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, "illegal char sequence");
            }
            cache.put(pattern, rule);
        }
        return rule;
    }

    private static Rule resolvePattern(String pattern) {
        int[] rule = new int[pattern.length()];
        int count = 0;
        int num = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == RANDOM_CHAR) {
                rule[i] = -1;
            } else if (c == EIGENVALUE_CHAR) {
                rule[i] = -2;
                num++;
            } else {
                count++;
                if (c >= '1' && c <= '9') {
                    rule[i] = c - '1';
                } else {
                    rule[i] = c - '1' - 7;
                }
            }
        }
        return new Rule(rule, count, num);
    }

    public static long decode(String value, String pattern, String... eigenvalue) {
        if (StringUtils.isBlank(pattern)) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, "illegal char sequence");
        }
        Rule rule = getRuleFromCache(pattern, eigenvalue);

        int[] result = new int[rule.getCount()];
        int[] r = rule.getRule();
        for (int i = 0, j = 0; i < r.length; i++) {
            if (r[i] == -2) {
                if (i == 0) {
                    value = "_" + value.substring(eigenvalue[j].length());
                } else if (i == r.length - 1) {
                    value = value.substring(0, i) + "_";
                } else {
                    String s1 = value.substring(0, i);
                    String s2 = eigenvalue[j].replaceAll(eigenvalue[j], "_");
                    String s3 = value.substring(i + eigenvalue[j].length());
                    value = s1 + s2 + s3;
                }
                j++;
            }
        }
        for (int i = 0; i < r.length; i++) {
            if (r[i] != -1 && r[i] != -2) {
                result[r[i]] = value.charAt(i) - '0';
            }
        }

        return getSeq(result);
    }

    private static long getSeq(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i : array) {
            sb.append(i);
        }
        return NumberUtils.toLong(sb.toString());
    }
}

package sample.sys;

/**
 * @author wutl
 * @Title:
 * @Package
 * @Description:
 * @date 2021-2-23 9:44
 */
public class SysUtil {

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if("".equals(obj) || "null".equals(obj)){
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}

package example;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class MdcSgin {
    public static void main(String[] args) {
        Map<String, String> params = new HashMap<>(16);
        params.put("app_id", "xp-trip");
        String format = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println("timestamp = " + format);
        params.put("timestamp", format);
        String replace = UUID.randomUUID().toString().replace("-", "");
        System.out.println("state = " + replace);
        params.put("state", replace);
        String signStr = sign(params, "MtnsXaNNuqKPrwDZ");
        System.out.println("签名生成完成, sign= " + signStr);
        //System.out.println(UUIDUtil.generate());
    }

    public static String sign(Map<String, String> params, String appSecret) {
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuilder query = new StringBuilder();
        query.append(appSecret);
        for (String key : keys) {
            String value = params.get(key);
            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
                query.append(key).append(value);
            }
        }
        query.append(appSecret);
        String forSignStr2 = query.toString();
        return SecureUtil.md5(forSignStr2).toUpperCase();
    }
}

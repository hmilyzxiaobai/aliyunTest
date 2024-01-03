package example;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import org.springframework.util.StringUtils;

import java.util.*;

public class TestRead {

    public static void main(String[] args) {
        Map<String, String> params = new HashMap<>(16);
        params.put("app_id", "xp-alm");
        String format = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println("timestamp = " + format);
        params.put("timestamp", format);
        String replace = UUID.randomUUID().toString().replace("-", "");
        System.out.println("state = " + replace);
        params.put("state", replace);
        String signStr = sign(params, "bjSHZFC0Cd2VT8TL");
        params.put("sign", signStr);
        params.put("Content-Type","multipart/form-data");
        //for --> 文件夹
        JSONObject body = new JSONObject();
        body.putIfAbsent("appId","01844fbccd69844fbccd8a4290eb0001");
        body.putIfAbsent("appCode","vvip");
        //body.putIfAbsent("sourceBucket","xp-static-prod");
        body.putIfAbsent("objectAcl",1);

        String body1 = HttpRequest.post("https://xp-org-open-api-cn.xiaopeng.com/web/xpFile/batchUpload")
                .addHeaders(params)
                .body(body.toString())
                .execute()
                .body();
        //log.info(body1);
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

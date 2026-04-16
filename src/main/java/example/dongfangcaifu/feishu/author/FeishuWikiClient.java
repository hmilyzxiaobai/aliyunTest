package example.dongfangcaifu.feishu.author;

import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeishuWikiClient {
    private static final String BASE_URL = "https://open.feishu.cn/open-apis";
    private final String appId;
    private final String appSecret;
    private String tenantAccessToken;
    private long tokenExpireTime;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FeishuWikiClient(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 刷新访问令牌
     */
    private synchronized void refreshAccessToken() throws IOException, ParseException {
        if (tenantAccessToken != null && System.currentTimeMillis() < tokenExpireTime - 300000) {
            return;
        }

        String url = BASE_URL + "/auth/v3/tenant_access_token/internal";
        HttpPost httpPost = new HttpPost(url);

        Map<String, String> body = new HashMap<>();
        body.put("app_id", appId);
        body.put("app_secret", appSecret);



        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(body), ContentType.parse("UTF-8")));
        httpPost.setHeader("Content-Type", "application/json");

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            Map<String, Object> resp = objectMapper.readValue(result, Map.class);

            if ((Integer) resp.get("code") != 0) {
                throw new IOException("获取token失败: " + resp.get("msg"));
            }

            this.tenantAccessToken = (String) resp.get("tenant_access_token");
            this.tokenExpireTime = System.currentTimeMillis() + ((Integer) resp.get("expire") * 1000L);
        }
    }

    /**
     * 获取 Wiki 节点列表
     */
    public List<WikiNode> getWikiNodes(String spaceId) throws IOException, ParseException {
        refreshAccessToken();

        String url = BASE_URL + "/wiki/v2/spaces/" + spaceId + "/nodes?page_size=50";
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Bearer " + tenantAccessToken);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            Map<String, Object> resp = objectMapper.readValue(result, Map.class);

            if ((Integer) resp.get("code") != 0) {
                throw new IOException("获取节点失败: " + resp.get("msg"));
            }

            // 解析节点列表
            List<WikiNode> nodes = new ArrayList<>();
            Map<String, Object> data = (Map<String, Object>) resp.get("data");
            List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");

            for (Map<String, Object> item : items) {
                WikiNode node = new WikiNode();
                node.setNodeId((String) item.get("node_id"));
                node.setTitle((String) item.get("title"));
                node.setObjType((String) item.get("obj_type"));
                nodes.add(node);
            }

            return nodes;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文档内容
     */
    public String getDocumentContent(String nodeId) throws IOException, ParseException {
        refreshAccessToken();

        String url = BASE_URL + "/docx/v1/documents/" + nodeId + "/raw_content";
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Bearer " + tenantAccessToken);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            Map<String, Object> resp = objectMapper.readValue(result, Map.class);

            if ((Integer) resp.get("code") != 0) {
                throw new IOException("获取文档失败: " + resp.get("msg"));
            }

            Map<String, Object> data = (Map<String, Object>) resp.get("data");
            return (String) data.get("content");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // WikiNode 类
    @Data
    public static class WikiNode {
        private String nodeId;
        private String title;
        private String objType;  // doc, sheet, bitable 等
    }
}


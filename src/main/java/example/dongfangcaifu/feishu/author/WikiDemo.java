package example.dongfangcaifu.feishu.author;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.List;

// 使用示例
public class WikiDemo {
    public static void main(String[] args) {
        String appId = "cli_a9570c9e557b9bd7";
        String appSecret = "0IwPiyk7sj3mK6saLtvIScsZGbcW6wSd";

        // 从您的链接中提取 spaceId
        // https://scn5c04kkozg.feishu.cn/wiki/LyDowtLOWiSKapkiL8GcoLe5nhg
        // spaceId 通常是路径中的一部分，这里需要确认实际的 space_id
        String spaceId = "LyDowtLOWiSKapkiL8GcoLe5nhg";  // 示例，可能需要调整

        FeishuWikiClient client = new FeishuWikiClient(appId, appSecret);

        try {
            // 获取 Wiki 下的所有节点
            List<FeishuWikiClient.WikiNode> nodes = client.getWikiNodes(spaceId);

            for (FeishuWikiClient.WikiNode node : nodes) {
                System.out.println("节点: " + node.getTitle() + " (类型: " + node.getObjType() + ")");

                // 如果是文档类型，读取内容
                if ("doc".equals(node.getObjType())) {
                    String content = client.getDocumentContent(node.getNodeId());
                    System.out.println("内容预览: " + content.substring(0, Math.min(200, content.length())));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}

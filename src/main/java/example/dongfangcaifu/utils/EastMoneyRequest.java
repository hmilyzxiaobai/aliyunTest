package example.dongfangcaifu.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EastMoneyRequest {
    public static void main(String[] args) {
        try {
            // 您的原始URL
            String urlStr = "https://41.push2.eastmoney.com/api/qt/clist/get?cb=jQuery35104660061245692523_1761750620605&pn=1&pz=1&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&dect=1&wbp2u=|0|0|0|web&fid=f26&fs=b:BK0707&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f11,f62,f128,f136,f115,f152&_=1761750620605";

            // 创建URL对象
            URL url = new URL(urlStr);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法
            connection.setRequestMethod("GET");

            // 设置请求头，模拟浏览器访问
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Host", "41.push2.eastmoney.com");
            connection.setRequestProperty("Connection", "keep-alive");

            // 设置超时时间
            connection.setConnectTimeout(10000); // 10秒
            connection.setReadTimeout(10000);    // 10秒

            // 获取响应码
            int responseCode = connection.getResponseCode();
            System.out.println("响应码: " + responseCode);

            // 读取响应内容
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "UTF-8")
                );

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // 输出原始响应
                System.out.println("原始响应: " + response.toString());

                // 处理JSONP响应（提取JSON部分）
                String jsonResponse = extractJsonFromJsonp(response.toString());
                System.out.println("提取的JSON: " + jsonResponse);

            } else {
                System.out.println("请求失败，响应码: " + responseCode);
            }

            // 关闭连接
            connection.disconnect();

        } catch (Exception e) {
            System.err.println("发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 从JSONP响应中提取JSON数据
    public static String extractJsonFromJsonp(String jsonpResponse) {
        try {
            // 查找第一个左括号和最后一个右括号
            int startIndex = jsonpResponse.indexOf("(");
            int endIndex = jsonpResponse.lastIndexOf(")");

            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                return jsonpResponse.substring(startIndex + 1, endIndex);
            }
            return jsonpResponse;
        } catch (Exception e) {
            return jsonpResponse;
        }
    }
}
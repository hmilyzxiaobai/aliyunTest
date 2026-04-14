package example.jikongju;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class CurlToJavaRequest {
    public static void main(String[] args) {
        try {
            // Define the URL
            String urlString = "https://gzwy.gov.cn/inc/nc/class/course/list?id=ca43ef7a9dd1444fba36bc385f928010&_t=1732780394133";
            URL url = new URL(urlString);

            // Open the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method (GET request)
            connection.setRequestMethod("GET");

            // Set the request headers (similar to the curl headers)
            connection.setRequestProperty("Accept", "application/json, text/plain, */*");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Cookie", "SESSION=YjZjNDZmZGEtNWIzMC00MmJkLWI1MmEtMGNjZGFlYTM1OGYy; authorization_token=eyJTRVNTSU9OSUQiOiJhNTQ5ZjNkZjczY2U0YTFhOTUwOWFkYjg5ZGE2N2M3OCIsInR5cCI6IkpXVCIsImFsZyI6IkhTMjU2In0.eyJpc3MiOiJXZW5KIiwiZXhwIjoxNzMyODIzNDcwfQ.ePF_l8RA8CRnnEyd20bqr3hm7uu9hHZa9YMiwqLRmOE");
            connection.setRequestProperty("Referer", "https://gzwy.gov.cn/page.html");
            connection.setRequestProperty("Sec-Fetch-Dest", "empty");
            connection.setRequestProperty("Sec-Fetch-Mode", "cors");
            connection.setRequestProperty("Sec-Fetch-Site", "same-origin");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
            connection.setRequestProperty("authorization_token", "eyJTRVNTSU9OSUQiOiJhNTQ5ZjNkZjczY2U0YTFhOTUwOWFkYjg5ZGE2N2M3OCIsInR5cCI6IkpXVCIsImFsZyI6IkhTMjU2In0.eyJpc3MiOiJXZW5KIiwiZXhwIjoxNzMyODIzNDcwfQ.ePF_l8RA8CRnnEyd20bqr3hm7uu9hHZa9YMiwqLRmOE");
            connection.setRequestProperty("client", "pc");
            connection.setRequestProperty("sec-ch-ua", "\"Google Chrome\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"");
            connection.setRequestProperty("sec-ch-ua-mobile", "?0");
            connection.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");

            // Connect to the server
            connection.connect();

            // Get the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read the response if successful
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Output the response content
                System.out.println("Response Body: " + response.toString());
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

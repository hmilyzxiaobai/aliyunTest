package example.dongfangcaifu.service.email;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailExtractor {
    public static void mainTest(String[] args) {
        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("当前时间的小时数是: " + hourOfDay);


        String input = "?utf-8?B?5bygIOejiuejig==?= <Zlldream@hotmail.com>";

        // 定义正则表达式
        String regex = "<([^>]+)>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            // 获取邮箱地址
            String email = matcher.group(1);
            System.out.println("提取到的邮箱地址: " + email);
        } else {
            System.out.println("未找到邮箱地址");
        }
    }
    public static void main(String[] args){
        try {
            String urlString =
                    //        String.format("https://70.push2.eastmoney.com/api/qt/clist/get?cb=%S&pn=%S&pz=%S&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&wbp2u=|0|0|0|web&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f11,f62,f128,f136,f115,f152&_=1714361372410",uuid,page,size);
                    "https://www.miit.gov.cn/zwgk/zcwj/wjfb/tz/art/2024/art_1487cd32993340d5a44ea90091553358.html";
           // String encode = URLEncoder.encode(urlString, "utf-8");
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法为GET
            connection.setRequestMethod("POST");
            // 获取响应内容
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // 输出响应内容
            // System.out.println("响应内容：");
            String dataAll = response.toString();
            System.out.println(dataAll);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


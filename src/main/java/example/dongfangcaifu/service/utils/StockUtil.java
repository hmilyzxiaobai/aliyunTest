package example.dongfangcaifu.service.utils;

public class StockUtil {

    /**
     * 判断股票是沪市还是深市
     * @param code 6位股票代码
     * @return "沪市" 或 "深市"，异常返回 "未知"
     */
    public static String getStockMarket(String code) {
        if (code == null || code.length() != 6) {
            return "未知";
        }

        // 沪市：60开头、68开头（科创板）
        if (code.startsWith("60") || code.startsWith("68")) {
            return "沪A";
        }

        // 深市：00开头、30开头（创业板）
        if (code.startsWith("00") || code.startsWith("30")) {
            return "深A";
        }

        return "未知";
    }


}
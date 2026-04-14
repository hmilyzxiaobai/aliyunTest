package example.dongfangcaifu.service.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// 定义对应的Java类
class StockData {
    @JsonProperty("code")
    private String code;

    @JsonProperty("market")
    private int market;

    @JsonProperty("decimal")
    private int decimal;

    @JsonProperty("prePrice")
    private double prePrice;

    @JsonProperty("details")
    private List<String> details;

    // 构造方法、getter和setter
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getMarket() { return market; }
    public void setMarket(int market) { this.market = market; }

    public int getDecimal() { return decimal; }
    public void setDecimal(int decimal) { this.decimal = decimal; }

    public double getPrePrice() { return prePrice; }
    public void setPrePrice(double prePrice) { this.prePrice = prePrice; }

    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }
}


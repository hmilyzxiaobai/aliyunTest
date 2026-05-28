package example.dongfangcaifu.httpUtils;

import lombok.Getter;

@Getter
public enum HttpRefererEnum {
    CODE("CODE","https://quote.eastmoney.com/center/gridlist.html"),
    HUDATA("找沪深一个股的历史数据","https://data.eastmoney.com/zjlx/"),
    PLATE("板块","https://quote.eastmoney.com/center/gridlist.html"),
    PLATE_CAPITAL_DETIAL_KLINE("板块资金详情","https://data.eastmoney.com/bkzj/"),
    // SHENDATA("assistant","系统回复"),
   // HUCAPITAL(),
   // SHENCAPITAL()

    ;
    private final String type;
    private final String comment;
    HttpRefererEnum(String type, String comment) {
        this.type=type;
        this.comment=comment;
    }


}

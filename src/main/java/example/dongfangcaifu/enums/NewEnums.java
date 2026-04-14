package example.dongfangcaifu.enums;

import org.checkerframework.common.value.qual.EnumVal;


public enum NewEnums {
    MIIT("https://www.miit.gov.cn/api-gateway/jpaas-publish-server/front/page/build/unit?webId=8d828e408d90447786ddbe128d495e9e&pageId=ca517c97303b40cf80bd668b35f6148f&parseType=buildstatic&pageType=column&tagId=%E5%8F%B3%E4%BE%A7%E5%86%85%E5%AE%B9&tplSetId=209741b2109044b5b7695700b2bec37e&paramJson=%7B%22pageNo%22%3A2%2C%22pageSize%22%3A%2224%22%7D","工信部")

    ;



    private final String url;
    private final String group;


    NewEnums(String url, String group) {
        this.url=url;
        this.group=group;
    }
    public String getUrl(){
        return url;
    }
    public String getGroup(){
        return group;
    }
}

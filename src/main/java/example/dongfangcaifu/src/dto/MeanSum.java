package example.dongfangcaifu.src.dto;

import lombok.Data;
//sumThirty,float sumTwenty,float sumTen,float sumFive
@Data
public class MeanSum {
    Float sumThirty;
    Float sumTwenty;
    Float sumTen;
    Float sumFive;

    public MeanSum(Float sumThirty,
    Float sumTwenty,
    Float sumTen,
    Float sumFive){
        this.sumFive=sumFive;
        this.sumThirty=sumThirty;
        this.sumTwenty=sumTwenty;
        this.sumTen=sumTen;
    }
}

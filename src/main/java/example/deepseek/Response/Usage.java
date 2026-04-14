package example.deepseek.Response;


import lombok.Data;

@Data
public class Usage {
    Integer promptTokens;
    Integer completionTokens;
    Integer totalTokens;
    Integer promptCacheHitTokens;
    Integer prompt_cache_missTokens;
    PromptTokensDetails promptTokensDetails;
}

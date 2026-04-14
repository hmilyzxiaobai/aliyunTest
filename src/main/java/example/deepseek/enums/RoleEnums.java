package example.deepseek.enums;

import lombok.Getter;

@Getter
public enum RoleEnums {
    USER("user","用户"),
    SYSTEM("system","系统"),
    ASSISTANT("assistant","系统回复")

    ;
    private final String type;
    private final String comment;


    RoleEnums(String type, String comment) {
        this.type=type;
        this.comment=comment;
    }

}

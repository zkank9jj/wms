package cn.wolfcode.wms.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter@Getter@ToString
public class Permission extends BaseDomain {
    private String name;
    private String expression;
}
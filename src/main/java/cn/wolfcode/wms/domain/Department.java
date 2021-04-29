package cn.wolfcode.wms.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter@Getter
public class Department extends BaseDomain {
    private String name;
    private String sn;
}
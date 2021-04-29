package cn.wolfcode.wms.domain;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class Depot extends BaseDomain {
    private String name; //仓库名称
    private String location; //仓库地址
}
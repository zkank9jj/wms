package cn.wolfcode.wms.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter@Getter
public class SystemMenu extends BaseDomain {
    private String name;
    private String url;
    private String sn;

    //one方对象
    private SystemMenu parent;
}
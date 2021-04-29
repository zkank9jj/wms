package cn.wolfcode.wms.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter@Getter@ToString
public class Role extends BaseDomain {
    private String name;
    private String sn;

    //many2many
    private List<Permission> permissions = new ArrayList<>();
    //many2many
    private List<SystemMenu> menus = new ArrayList<>();
}
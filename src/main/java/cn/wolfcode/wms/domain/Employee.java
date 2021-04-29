package cn.wolfcode.wms.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter@Getter@ToString
public class Employee extends BaseDomain {
    private String name;
    private String password;
    private String email;
    private Integer age;
    private boolean admin;
    //many2one
    private Department dept;
    //many2many
    private List<Role> roles = new ArrayList<>();
}
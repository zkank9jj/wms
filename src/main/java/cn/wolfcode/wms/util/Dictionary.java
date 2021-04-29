package cn.wolfcode.wms.util;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Dictionary {

    public static final Map<String, String> ORDER_MAP = new LinkedHashMap<>();
    public static final Map<String, String> SALE_MAP = new LinkedHashMap<>();

    static {
        //订货报表的分组类型
        ORDER_MAP.put("e.name", "订货员");
        ORDER_MAP.put("p.name", "商品名称");
        ORDER_MAP.put("p.brandName", "商品品牌");
        ORDER_MAP.put("s.name", "供应商");
        ORDER_MAP.put("DATE_FORMAT(bill.vdate, '%Y-%m')", "订货月份");
        ORDER_MAP.put("DATE_FORMAT(bill.vdate, '%Y-%m-%d')", "订货日期");
        //销售报表的分组类型
        SALE_MAP.put("e.name", "销售员");
        SALE_MAP.put("p.name", "商品名称");
        SALE_MAP.put("p.brandName", "商品品牌");
        SALE_MAP.put("c.name", "客户");
        SALE_MAP.put("DATE_FORMAT(sa.vdate, '%Y-%m')", "订货月份");
        SALE_MAP.put("DATE_FORMAT(sa.vdate, '%Y-%m-%d')", "订货日期");
    }
}

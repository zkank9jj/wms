package cn.wolfcode.wms.domain;

import cn.wolfcode.wms.util.JSONUtil;
import cn.wolfcode.wms.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.io.FilenameUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Setter@Getter
public class Product extends BaseDomain {
    private String name; //商品名称
    private String sn; //商品编码
    private BigDecimal costPrice; //成本价
    private BigDecimal salePrice; //销售价
    private String imagePath; //图片路径
    private String intro; //备注

    //冗余数据
    private Long brandId; //品牌的ID
    private String brandName; //品牌的名称

    public String getImagePath() {
        return StringUtil.empty2Null(imagePath);
    }

    public String getSmallImagePath() {
        if (!StringUtil.hasLength(imagePath)) {
            return null;
        }
        // /upload/1e0fceec-6336-4ee8-9546-254fb7d42ca3.jpg
        // /upload/1e0fceec-6336-4ee8-9546-254fb7d42ca3_small.jpg
        //拿到文件的拓展名
        String ext = FilenameUtils.getExtension(imagePath);
        String path = imagePath.substring(0, imagePath.lastIndexOf("."));
        return path+"_small."+ext;
    }

    //自定义属性转成JSON
    public String getJsonString() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("costPrice", costPrice);
        map.put("salePrice", salePrice);
        map.put("brandName", brandName);
        return JSONUtil.toJSONString(map);
    }
}
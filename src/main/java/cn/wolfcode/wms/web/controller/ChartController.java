package cn.wolfcode.wms.web.controller;

import cn.wolfcode.wms.query.OrderChartQueryObject;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.query.SaleChartQueryObject;
import cn.wolfcode.wms.service.IBrandService;
import cn.wolfcode.wms.service.IChartService;
import cn.wolfcode.wms.service.IClientService;
import cn.wolfcode.wms.service.ISupplierService;
import cn.wolfcode.wms.util.Dictionary;
import cn.wolfcode.wms.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("chart")
public class ChartController {
    @Autowired
    private IBrandService brandService;
    @Autowired
    private ISupplierService supplierService;
    @Autowired
    private IClientService clientService;
    @Autowired
    private IChartService chartService;

    @RequestMapping("order")
    public String list(@ModelAttribute("qo") OrderChartQueryObject qo, Model model) {
        model.addAttribute("brands", brandService.list());
        model.addAttribute("suppliers", supplierService.list());
        model.addAttribute("orderMap", Dictionary.ORDER_MAP);
        model.addAttribute("list", chartService.queryOrderChart(qo));
        return "chart/order";
    }

    @RequestMapping("sale")
    public String list(@ModelAttribute("qo") SaleChartQueryObject qo, Model model) {
        model.addAttribute("brands", brandService.list());
        model.addAttribute("clients", clientService.list());
        model.addAttribute("saleMap", Dictionary.SALE_MAP);
        model.addAttribute("list", chartService.selectSaleChart(qo));
        return "chart/sale";
    }

    @RequestMapping("saleByBar")
    public String saleByBar(@ModelAttribute("qo") SaleChartQueryObject qo, Model model)
        throws Exception {
        //往网页中共享3个数据:分组类型,x坐标,y坐标
        model.addAttribute("groupType", Dictionary.SALE_MAP.get(qo.getGroupType()));
        List<Map<String, Object>> list = chartService.selectSaleChart(qo);
        //x:["逍遥", "星星", "王尼玛"]
        List<Object> x = new ArrayList<>();
        List<Object> y = new ArrayList<>();
        for (Map<String, Object> map : list) {
            x.add(map.get("groupType"));
            y.add(map.get("totalAmount"));
        }
        model.addAttribute("x", JSONUtil.toJSONString(x));
        model.addAttribute("y", JSONUtil.toJSONString(y));
        return "chart/saleByBar";

    }

    @RequestMapping("saleByPie")
    public String saleByPie(@ModelAttribute("qo") SaleChartQueryObject qo, Model model)
        throws Exception {
        //往网页中共享3个数据:分组类型,x坐标,显示的数据[{name:xxx,value:xxx},...]
        model.addAttribute("groupType", Dictionary.SALE_MAP.get(qo.getGroupType()));
        List<Map<String, Object>> list = chartService.selectSaleChart(qo);
        //x:["逍遥", "星星", "王尼玛"]
        List<Object> x = new ArrayList<>();
        //[{name:xxx,value:xxx},...]
        List<Map<String, Object>> datas = new ArrayList<>();
        for (Map<String, Object> map : list) {
            x.add(map.get("groupType"));
            //{name:x,value:x}
            Map<String, Object> data = new HashMap<>();
            data.put("name", map.get("groupType"));
            data.put("value", map.get("totalAmount"));
            datas.add(data);
        }
        model.addAttribute("x", JSONUtil.toJSONString(x));
        model.addAttribute("datas", JSONUtil.toJSONString(datas));
        return "chart/saleByPie";

    }
}

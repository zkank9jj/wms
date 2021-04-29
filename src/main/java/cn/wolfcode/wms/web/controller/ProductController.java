package cn.wolfcode.wms.web.controller;

import cn.wolfcode.wms.domain.Product;
import cn.wolfcode.wms.query.ProductQueryObject;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IBrandService;
import cn.wolfcode.wms.service.IProductService;
import cn.wolfcode.wms.util.JSONResult;
import cn.wolfcode.wms.util.StringUtil;
import cn.wolfcode.wms.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.util.Optional;

@Controller
@RequestMapping("product")
public class ProductController {
    @Autowired
    private IProductService productService;
    @Autowired
    private IBrandService brandService;
    @Autowired
    private ServletContext ctx;

    @RequestMapping("list")
    public String list(@ModelAttribute("qo") ProductQueryObject qo, Model model) {
        model.addAttribute("brands", brandService.list());
        model.addAttribute("result", productService.query(qo));
        return "product/list";
    }

    @RequestMapping("listView")
    public String listView(@ModelAttribute("qo") ProductQueryObject qo, Model model) {
        qo.setPageSize(15); //故意写死
        model.addAttribute("brands", brandService.list());
        model.addAttribute("result", productService.query(qo));
        return "product/listView";
    }

    @RequestMapping("input")
    public String input(Long id, Model model) {
        model.addAttribute("brands", brandService.list());
        if (Optional.ofNullable(id).isPresent()) {
            model.addAttribute("entity", productService.get(id)); //有ID
        }
        return "product/input";
    }

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(Product entity, MultipartFile pic) {
        //处理图片
        if (pic != null && pic.getContentType().startsWith("image") && pic.getSize() > 0) {
            //是否需要删除旧的图片
            if (StringUtil.hasLength(entity.getImagePath())) {
                UploadUtil.deleteFile(ctx, entity.getImagePath());
            }
            //符合上传的图片要求
            String path = UploadUtil.upload(pic, ctx.getRealPath("/upload"));
            entity.setImagePath(path);
        }
        productService.saveOrUpdate(entity);
        return new JSONResult();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id) {
        String imagePath = productService.get(id).getImagePath();
        if (Optional.ofNullable(id).isPresent()) {
            productService.delete(id); //有ID
        }
        //数据删除完成后再删除图片
        if (StringUtil.hasLength(imagePath)) {
            UploadUtil.deleteFile(ctx, imagePath);
        }
        return new JSONResult();
    }
}

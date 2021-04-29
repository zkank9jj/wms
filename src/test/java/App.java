import cn.wolfcode.wms.domain.Department;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IDepartmentService;
import cn.wolfcode.wms.service.IEmployeeService;
import cn.wolfcode.wms.util.PageResult;
import com.alibaba.druid.filter.config.ConfigTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class App {
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IEmployeeService employeeService;

    @Test
    public void testDruid() throws Exception {
        //lCzd9geWAuAuJtLhpaG/J+d28H8KiMFAWopxXkYpMNdUai6Xe/LsPqMQeg5MIrmvtMa+hzycdRhWs29ZUPU1IQ==
        System.out.println(ConfigTools.encrypt("admin"));
        System.out.println(ConfigTools.decrypt("lCzd9geWAuAuJtLhpaG/J+d28H8KiMFAWopxXkYpMNdUai6Xe/LsPqMQeg5MIrmvtMa+hzycdRhWs29ZUPU1IQ=="));
    }

    @Test
    public void testQuery() {
        QueryObject qo = new QueryObject();
        qo.setCurrentPage(4);
        PageResult result = employeeService.query(qo);
        System.out.println(result);
    }

    @Test
    public void testSaveOrUpdate() {
        System.out.println(departmentService.getClass());
        Department d = new Department();
        d.setId(1L);
        d.setName("总经办x");
        d.setSn("BOSSx");
        departmentService.saveOrUpdate(d);
    }

    @Test
    public void testDelete() {
        departmentService.delete(2L);
    }

    @Test
    public void testGet() {
        System.out.println(departmentService.get(1L));
    }

    @Test
    public void testList() {
        departmentService.list().forEach(System.out::println);
    }
}

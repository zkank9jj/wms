<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>WMS-演示版</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link href="/style/basic_layout.css" rel="stylesheet" type="text/css">
    <link href="/style/common_style.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/js/jquery/jquery.js"></script>
    <script type="text/javascript" src="/js/plugins/artDialog/jquery.artDialog.js?skin=blue"></script>
    <script type="text/javascript" src="/js/plugins/artDialog/iframeTools.js"></script>
    <script type="text/javascript" src="/js/plugins/form/jQueryForm.js"></script>
    <script type="text/javascript" src="/js/plugins/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="/js/commonAll.js"></script>
    <script type="text/javascript" >
        $(function () {
            //把当前的表单做成ajax表单
            $("#editForm").ajaxForm(function (data) {
                if (data.success) {
                    showDialog("操作成功", function () {
                        location.href = "/orderBill/list.do";
                    });
                }
            });

            //给日历显示框添加日历选择效果
            $(".Wdate").click(function () {
                WdatePicker({
                    readOnly: true, //日历只读
                    maxDate: new Date() //最大时间
                });
            });

            //点击添加明显事件
            $(".appendRow").click(function () {
                //把第一行数据做克隆,同步克隆事件
                var cp = $("#edit_table_body tr:first").clone();
                //清空克隆体中的数据
                cp.find("input").val("");
                cp.find("span").html("");
                cp.appendTo("#edit_table_body");
            });

            //#edit_table_body中的元素动态事件绑定
            $("#edit_table_body")
                //单价或数量发生变动,重新计算小计
            .on("blur", "input[tag='costPrice'],input[tag='number']", function () {
                var tr = $(this).closest("tr"); //拿到当前的行
                var costPrice = tr.find("input[tag='costPrice']").val();
                var num = tr.find("input[tag='number']").val();
                if (costPrice && num) { //单价和数据都有值
                    var amount = costPrice * num;
                    tr.find("span[tag='amount']").html(amount.toFixed(2));
                }
            })
            //点击删除链接,删除当前的行
            .on("click", ".removeItem",function () {
                //判断当前是否最后1行
                if ($("#edit_table_body tr").size() == 1) {
                    //仅剩下最后1行,清空数据即可
                    $(this).closest("tr").find("input").val("");
                    $(this).closest("tr").find("span").html("");
                    return;
                }

                $(this).closest("tr").remove();
            })
            //点击放大镜,弹出窗口(选择商品)
            .on("click", ".searchproduct", function () {
                var tr = $(this).closest("tr"); //当前放大镜所在的行
                //弹出子窗口(选择商品)
                $.dialog.open("/product/listView.do", {
                    title: "选择商品", //标题
                    width: "85%", //宽度
                    height: "85%", //高度
                    lock: true, //锁定模式
                    resize: false, //不允许重置
                    //给子窗口的关闭绑定一个事件
                    close: function () {
                        //拿到共享过来的数据,直接就是JSON对象
                        var product = $.dialog.data("obj");
                        $.dialog.removeData("obj")
                        if (product) { //有选择商品回填数据
                            tr.find("input[tag='name']").val(product.name);
                            tr.find("input[tag='pid']").val(product.id);
                            tr.find("input[tag='costPrice']").val(product.costPrice);
                            tr.find("input[tag='number']").val(1);
                            tr.find("span[tag='brand']").html(product.brandName);
                            tr.find("span[tag='amount']").html(product.costPrice.toFixed(2));
                        }
                    }
                });
            });
            
            //点击确定按钮,重新编排明显的索引
            $(".btn_submit").click(function () {
                //判断至少要有1个明显
                if (!$("#edit_table_body tr input[tag='pid']").val()) {
                    showDialog("至少要有1个明显");
                    return;
                }

                //拿到所有的明显
                $.each($("#edit_table_body tr"), function (index, ele) {
                    $(ele).find("input[tag='pid']").prop("name", "items["+index+"].product.id");
                    $(ele).find("input[tag='costPrice']").prop("name", "items["+index+"].costPrice");
                    $(ele).find("input[tag='number']").prop("name", "items["+index+"].number");
                    $(ele).find("input[tag='remark']").prop("name", "items["+index+"].remark");
                });
                //提交表单
                $("#editForm").submit();
            });
        });
    </script>
</head>
<body>
<form id="editForm" action="/orderBill/saveOrUpdate.do" method="post" >
    <input type="hidden" name="id" value="${entity.id}">
    <div id="container">
        <div id="nav_links">
            <span style="color: #1A5CC6;">采购单编辑</span>
            <div id="page_close">
                <a>
                    <img src="/images/common/page_close.png" width="20" height="20" style="vertical-align: text-top;"/>
                </a>
            </div>
        </div>
        <div class="ui_content">
            <table cellspacing="0" cellpadding="0" width="100%" align="left" border="0">
                <tr>
                    <td class="ui_text_rt" width="140">采购单编码</td>
                    <td class="ui_text_lt">
                        <input name="sn" value="${entity.sn}" class="ui_input_txt02"/>
                    </td>
                </tr>
                <tr>
                    <td class="ui_text_rt" width="140">供应商</td>
                    <td class="ui_text_lt">
                        <select class="ui_select03 supplier" name="supplier.id">
                            <c:forEach var="s" items="${suppliers}">
                                <option value="${s.id}">${s.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <script type="text/javascript">
                    $(".supplier option[value='']").prop("selected", true);
                </script>
                <tr>
                    <td class="ui_text_rt" width="140">业务时间</td>
                    <td class="ui_text_lt">
                        <fmt:formatDate var="vdate" value="${entity.vdate}" pattern="yyyy-MM-dd"/>
                        <input name="vdate" value="${vdate}" class="ui_input_txt02 Wdate"/>
                    </td>
                </tr>
                <tr>
                    <td class="ui_text_rt" width="140">明细</td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input type="button" value="添加明细" class="ui_input_btn01 appendRow"/>
                        <table class="edit_table" cellspacing="0" cellpadding="0" border="0">
                            <thead>
                            <tr>
                                <th width="170">货品</th>
                                <th width="100">品牌</th>
                                <th width="80">价格</th>
                                <th width="80">数量</th>
                                <th width="100">金额小计</th>
                                <th width="180">备注</th>
                                <th width="120"></th>
                            </tr>
                            </thead>
                            <tbody id="edit_table_body">
                                <c:choose>
                                    <c:when test="${empty entity.items}">
                                        <!--新增时的一条明细样板-->
                                        <tr>
                                            <td>
                                                <input disabled readonly class="ui_input_txt01" tag="name"/>
                                                <img src="/images/common/search.png" class="searchproduct"/>
                                                <input type="hidden" tag="pid"/>
                                            </td>
                                            <td><span tag="brand"></span></td>
                                            <td><input type="number" tag="costPrice" class="ui_input_txt01"/></td>
                                            <td><input type="number" tag="number" class="ui_input_txt01"/></td>
                                            <td><span tag="amount"></span></td>
                                            <td><input tag="remark" class="ui_input_txt01"/></td>
                                            <td>
                                                <a href="javascript:;" class="removeItem">删除明细</a>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <!--编辑操作的商品明细回显-->
                                        <c:forEach var="item" items="${entity.items}">
                                            <tr>
                                                <td>
                                                    <input readonly class="ui_input_txt01" tag="name" value="${item.product.name}"/>
                                                    <img src="/images/common/search.png" class="searchproduct"/>
                                                    <input type="hidden" tag="pid" value="${item.product.id}"/>
                                                </td>
                                                <td><span tag="brand">${item.product.brandName}</span></td>
                                                <td><input type="number" tag="costPrice" class="ui_input_txt01" value="${item.costPrice}"/></td>
                                                <td><input type="number" tag="number" class="ui_input_txt01" value="${item.number}"/></td>
                                                <td><span tag="amount">${item.amount}</span></td>
                                                <td><input tag="remark" class="ui_input_txt01" value="${item.remark}"/></td>
                                                <td>
                                                    <a href="javascript:;" class="removeItem">删除明细</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td class="ui_text_lt">
                        &nbsp;<input type="button" value="确定保存" class="ui_input_btn01 btn_submit"/>
                        &nbsp;<input id="cancelbutton" type="button" value="重置" class="ui_input_btn01"/>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</form>
</body>
</html>

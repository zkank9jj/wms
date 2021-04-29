//禁用数组参数加[]的设置
$.ajaxSettings.traditional = true;

/** table鼠标悬停换色* */
$(function () {
    // 如果鼠标移到行上时，执行函数
    $(".table tr").mouseover(function () {
        $(this).css({
            background: "#CDDAEB"
        });
        $(this).children('td').each(function (index, ele) {
            $(ele).css({
                color: "#1D1E21"
            });
        });
    }).mouseout(function () {
        $(this).css({
            background: "#FFF"
        });
        $(this).children('td').each(function (index, ele) {
            $(ele).css({
                color: "#909090"
            });
        });
    });
});

$(function () {
    //给新增按钮绑定点击事件,跳转到编辑界面
    $(".btn_input").click(function () {
        var url = $(this).data("url");
        location.href = url;
    });

    //翻页和提交表单操作
    $(".btn_page").click(function () {
        //拿到页码
        var pageNo = $(this).data("page") ||
            $("input[name='currentPage']").val();
        //设置给当前页的文本框
        $("input[name='currentPage']").val(pageNo);
        //提交表单
        $("#searchForm").submit();
    });

    //改变页面容量
    $(".pageSize").change(function () {
        $("input[name='currentPage']").val(1);
        $("#searchForm").submit();
    });

    //点击删除按钮弹出对话框
    $(".btn_delete").click(function () {
        var url = $(this).data("url");
        showDialog("亲,确定要删除吗?", function () {
            //发送ajax请求
            $.get(url, function (data) {
                //象征性的成功了
                if (data.success) {
                    showDialog("删除成功", function () {
                        location.reload();//刷新界面
                    });
                }
            }, "json");
        }, true);
    });

    //批量删除按钮
    $(".batch_delete").click(function () {
        var url = $(this).data("url");
        showDialog("确定要批量删除吗?", function () {
            //最少要选择1条数据
            if ($(".acb:checked").size() == 0) {
                showDialog("至少选中1条数据");
                return;
            }
            //拿到选择的id
            var ids = $.map($(".acb:checked"), function (ele) {
                return $(ele).data("eid");
            });
            //发送ajax请求
            $.post(url, {ids: ids}, function (data) {
                //象征性返回
                if (data.success) {
                    showDialog("批量删除成功", function () {
                        location.reload();
                    });
                } else {
                    showDialog("批量删除失败:"+data.msg);
                }
            });
        }, true);
    });
    
    //全选或全不选
    $("#all").click(function () {
        $(".acb").prop("checked", this.checked);
    });
    
    $(".acb").click(function () {
        if ($(".acb:checked").size() == $(".acb").size()) {
            $("#all").prop("checked", true);
        } else {
            $("#all").prop("checked", false);
        }
    });
});

//弹出对话框
function showDialog(content, ok, cancel) {
    $.dialog({
        title: "温馨提示",
        icon: "face-smile",
        content: content,
        ok: ok || true,
        cancel: cancel
    });
}



























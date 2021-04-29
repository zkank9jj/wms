<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="/style/basic_layout.css" rel="stylesheet" type="text/css">
<link href="/style/common_style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/plugins/artDialog/jquery.artDialog.js?skin=blue"></script>
<script type="text/javascript" src="/js/plugins/artDialog/iframeTools.js"></script>
<script type="text/javascript" src="/js/plugins/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/commonAll.js"></script>
<script type="text/javascript">
    $(function () {
        //给日历显示框添加日历选择效果
        $(".Wdate[name='beginDate']").click(function () {
            WdatePicker({
                readOnly: true, //日历只读
                maxDate: new Date() //最大时间
            });
        });

        $(".Wdate[name='endDate']").click(function () {
            WdatePicker({
                readOnly: true, //日历只读
                minDate: $(".Wdate[name='beginDate']").val(), //最小时间
                maxDate: new Date() //最大时间
            });
        });
        
        //点击图形报表按钮,触发点击事件
		$(".chart").click(function () {
			//把表单中的参数序列化
            var args = $("#searchForm").serialize();
            var url = $(this).data("url")+"?"+args;
            //弹出子窗口
            $.dialog.open(url, {
                title: "报表", //标题
                width: "85%", //宽度
                height: "85%", //高度
                lock: true, //锁定模式
                resize: false //不允许重置
            });
        });
    });
</script>
<title>WMS-演示版</title>
<style>
	.alt td{ background:black !important;}
</style>
</head>
<body>
	<form id="searchForm" action="/chart/sale.do" method="post">
		<div id="container">
			<div class="ui_content">
				<div class="ui_text_indent">
					<div id="box_border">
						<div id="box_top">搜索</div>
						<div id="box_center">
							业务时间
							<fmt:formatDate var="beginDate" value="${qo.beginDate}" pattern="yyyy-MM-dd"/>
							<fmt:formatDate var="endDate" value="${qo.endDate}" pattern="yyyy-MM-dd"/>
							<input type="text" class="ui_input_txt01 Wdate" name="beginDate" value="${beginDate}"/> ~
							<input type="text" class="ui_input_txt01 Wdate" name="endDate" value="${endDate}"/>
							货品名称/编码
							<input type="text" class="ui_input_txt01" name="keyword" value="${qo.keyword}"/>
							客户
							<select class="ui_select01 client" name="clientId">
								<option value="-1">全部客户</option>
								<c:forEach var="c" items="${clients}">
									<option value="${c.id}">${c.name}</option>
								</c:forEach>
							</select>
							品牌
							<select class="ui_select01 brand" name="brandId">
								<option value="-1">全部品牌</option>
								<c:forEach var="b" items="${brands}">
									<option value="${b.id}">${b.name}</option>
								</c:forEach>
							</select>
							类型
							<select class="ui_select01 groupType" name="groupType">
								<c:forEach var="entry" items="${saleMap}">
									<option value="${entry.key}">${entry.value}</option>
								</c:forEach>
							</select>
							<script type="text/javascript">
								$(".client option[value='${qo.clientId}']").prop("selected", true);
								$(".brand option[value='${qo.brandId}']").prop("selected", true);
								$(".groupType option[value=\"${qo.groupType}\"]").prop("selected", true);
							</script>
						</div>
						<div id="box_bottom">
							<input type="button" value="查询" class="ui_input_btn01 btn_page"/>
							<input value="柱状图" class="left2right chart" data-url="/chart/saleByBar.do" type="button">
							<input value="饼状图" class="left2right chart" data-url="/chart/saleByPie.do" type="button">
						</div>
					</div>
				</div>
			</div>
			<div class="ui_content">
				<div class="ui_tb">
					<table class="table" cellspacing="0" cellpadding="0" width="100%" align="center" border="0">
						<tr>
							<th>分组类型</th>
							<th>订货总数</th>
							<th>订货总额</th>
							<th>利润</th>
						</tr>
						<c:forEach var="entity" items="${list}" varStatus="num">
							<tr>
								<td>${entity.groupType}</td>
								<td>${entity.totalNumber}</td>
								<td>${entity.totalAmount}</td>
								<td>${entity.profit}</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</form>
</body>
</html>

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
    });
</script>
<title>WMS-演示版</title>
<style>
	.alt td{ background:black !important;}
</style>
</head>
<body>
	<form id="searchForm" action="/chart/order.do" method="post">
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
							供应商
							<select class="ui_select01 supplier" name="supplierId">
								<option value="-1">全部供应商</option>
								<c:forEach var="s" items="${suppliers}">
									<option value="${s.id}">${s.name}</option>
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
								<c:forEach var="entry" items="${orderMap}">
									<option value="${entry.key}">${entry.value}</option>
								</c:forEach>
							</select>
							<script type="text/javascript">
								$(".supplier option[value='${qo.supplierId}']").prop("selected", true);
								$(".brand option[value='${qo.brandId}']").prop("selected", true);
								$(".groupType option[value=\"${qo.groupType}\"]").prop("selected", true);
							</script>
						</div>
						<div id="box_bottom">
							<input type="button" value="查询" class="ui_input_btn01 btn_page"/>
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
						</tr>
						<c:forEach var="entity" items="${list}" varStatus="num">
							<tr>
								<td>${entity.groupType}</td>
								<td>${entity.totalNumber}</td>
								<td>${entity.totalAmount}</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</form>
</body>
</html>

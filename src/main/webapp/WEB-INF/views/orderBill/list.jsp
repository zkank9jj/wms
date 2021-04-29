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

        //点击审核按钮,发送审核请求
		$(".btn_audit").click(function () {
		    var url = $(this).data("url");
			showDialog("确定要审核吗?", function () {
				$.get(url, function () {
					showDialog("审核成功!", function () {
						location.reload();
                    });
                });
            }, true);
        });
    });
</script>
<title>WMS-演示版</title>
<style>
	.alt td{ background:black !important;}
</style>
</head>
<body>
	<form id="searchForm" action="/orderBill/list.do" method="post">
		<div id="container">
			<div class="ui_content">
				<div class="ui_text_indent">
					<div id="box_border">
						<div id="box_top">搜索</div>
						<div id="box_center">
							业务时间
							<fmt:formatDate var="beginDate" value="${qo.beginDate}" pattern="yyyy-MM-dd"/>
							<fmt:formatDate var="endDate" value="${qo.endDate}" pattern="yyyy-MM-dd"/>
							<input type="text" class="ui_input_txt02 Wdate" name="beginDate" value="${beginDate}"/> ~
							<input type="text" class="ui_input_txt02 Wdate" name="endDate" value="${endDate}"/>
							供应商
							<select class="ui_select01 supplier" name="supplierId">
								<option value="-1">全部供应商</option>
								<c:forEach var="s" items="${suppliers}">
									<option value="${s.id}">${s.name}</option>
								</c:forEach>
							</select>
							状态
							<select class="ui_select01 status" name="status">
								<option value="-1">全部</option>
								<option value="1">未审核</option>
								<option value="2">已审核</option>
							</select>
							<script type="text/javascript">
								$(".supplier option[value='${qo.supplierId}']").prop("selected", true);
								$(".status option[value='${qo.status}']").prop("selected", true);
							</script>
						</div>
						<div id="box_bottom">
							<input type="button" value="查询" class="ui_input_btn01 btn_page"/>
							<input type="button" value="新增" class="ui_input_btn01 btn_input"
								data-url="/orderBill/input.do"/>
						</div>
					</div>
				</div>
			</div>
			<div class="ui_content">
				<div class="ui_tb">
					<table class="table" cellspacing="0" cellpadding="0" width="100%" align="center" border="0">
						<tr>
							<th width="30"><input type="checkbox" id="all" /></th>
							<th>采购单号</th>
							<th>业务时间</th>
							<th>供应商</th>
							<th>总数量</th>
							<th>总金额</th>
							<th>录入人</th>
							<th>审核人</th>
							<th>状态</th>
							<th></th>
						</tr>
						<c:forEach var="entity" items="${result.data}" varStatus="num">
							<tr>
								<td><input type="checkbox" class="acb"/></td>
								<td>${entity.sn}</td>
								<td><fmt:formatDate value="${entity.vdate}" pattern="yyyy-MM-dd"/></td>
								<td>${entity.supplier.name}</td>
								<td>${entity.totalNumber}</td>
								<td>${entity.totalAmount}</td>
								<td>${entity.inputUser.name}</td>
								<td>${entity.auditor.name}</td>
								<c:choose>
									<c:when test="${entity.status == 1}">
										<%--未审核显示的界面--%>
										<td>
											<span style="color: green;">待审核</span>
										</td>
										<td>
											<a href="/orderBill/input.do?id=${entity.id}">编辑</a>
											<a href="javascript:" class="btn_delete"
											   data-url="/orderBill/delete.do?id=${entity.id}">删除</a>
											<a href="javascript:" class="btn_audit"
											   data-url="/orderBill/audit.do?id=${entity.id}">审核</a>
										</td>
									</c:when>
									<c:otherwise>
										<td>
											<span style="color: red;">已审核</span>
										</td>
										<td>
											<a href="/orderBill/billView.do?id=${entity.id}">查看</a>
										</td>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
					</table>
				</div>
				<%--包含分页界面--%>
				<%@ include file="/WEB-INF/views/common/page.jsp" %>
			</div>
		</div>
	</form>
</body>
</html>

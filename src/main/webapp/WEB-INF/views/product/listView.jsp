<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="/style/basic_layout.css" rel="stylesheet" type="text/css">
<link href="/style/common_style.css" rel="stylesheet" type="text/css">
<link href="/js/plugins/fancybox/jquery.fancybox.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/plugins/artDialog/jquery.artDialog.js?skin=blue"></script>
<script type="text/javascript" src="/js/plugins/artDialog/iframeTools.js"></script>
<script type="text/javascript" src="/js/commonAll.js"></script>
<script type="text/javascript">
	$(function () {
		//点击选中按钮时,回传数据,关闭当前的窗口
        $(".selected").click(function () {
			//设置回显的数据
            $.dialog.data("obj", $(this).data("obj"));
            //关闭当前的窗口
            $.dialog.close();
        });
    });
</script>
<title>WMS-演示版</title>
<style>
	.alt td{ background:black !important;}
</style>
</head>
<body>
	<form id="searchForm" action="/product/listView.do" method="post">
		<div id="container">
			<div class="ui_content">
				<div class="ui_text_indent">
					<div id="box_border">
						<div id="box_top">搜索</div>
						<div id="box_center">
							商品名称/编码
							<input type="text" class="ui_input_txt02" name="keyword" value="${qo.keyword}"/>
							商品品牌
							<select class="ui_select01 brandId" name="brandId">
								<option value="-1">全部品牌</option>
								<c:forEach var="b" items="${brands}">
									<option value="${b.id}">${b.name}</option>
								</c:forEach>
							</select>
							<script type="text/javascript">
								$(".brandId option[value='${qo.brandId}']").prop("selected", true);
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
							<th width="30"><input type="checkbox" id="all" /></th>
							<th>编号</th>
							<th>商品图片</th>
							<th>商品名称</th>
							<th>商品编码</th>
							<th>商品品牌</th>
							<th>成本价</th>
							<th>零售价</th>
							<th></th>
						</tr>
						<c:forEach var="entity" items="${result.data}" varStatus="num">
							<tr>
								<td><input type="checkbox" class="acb" data-eid="${entity.id}"/></td>
								<td>${num.count}</td>
								<td>
									<img src="${entity.smallImagePath}" class="list_img_min"/>
								</td>
								<td>${entity.name}</td>
								<td>${entity.sn}</td>
								<td>${entity.brandName}</td>
								<td>${entity.costPrice}</td>
								<td>${entity.salePrice}</td>
								<td>
									<input type="button" class="left2right selected"
										   data-obj='${entity.jsonString}'
										   value="选中"/>
								</td>
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

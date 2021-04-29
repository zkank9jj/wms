<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="/style/basic_layout.css" rel="stylesheet" type="text/css">
<link href="/style/common_style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/plugins/artDialog/jquery.artDialog.js?skin=blue"></script>
<script type="text/javascript" src="/js/commonAll.js"></script>
<title>WMS-演示版</title>
<style>
	.alt td{ background:black !important;}
</style>
</head>
<body>
	<form id="searchForm" action="/productStock/list.do" method="post">
		<div id="container">
			<div class="ui_content">
				<div class="ui_text_indent">
					<div id="box_border">
						<div id="box_top">搜索</div>
						<div id="box_center">
							商品名称/编码
							<input type="text" class="ui_input_txt02" name="keyword" value="${qo.keyword}"/>
							所属仓库
							<select class="ui_select01 depot" name="depotId">
								<option value="-1">全部仓库</option>
								<c:forEach var="d" items="${depots}">
									<option value="${d.id}">${d.name}</option>
								</c:forEach>
							</select>
							所属品牌
							<select class="ui_select01 brand" name="brandId">
								<option value="-1">全部品牌 </option>
								<c:forEach var="b" items="${brands}">
									<option value="${b.id}">${b.name}</option>
								</c:forEach>
							</select>
							库存阈值
							<input type="number" name="warnNum" value="${qo.warnNum}" class="ui_input_txt01"/>
							<script type="text/javascript">
								$(".depot option[value='${qo.depotId}']").prop("selected", true);
								$(".brand option[value='${qo.brandId}']").prop("selected", true);
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
							<th>仓库名称</th>
							<th>商品编码</th>
							<th>商品名称</th>
							<th>商品品牌</th>
							<th>库存价格</th>
							<th>库存数量</th>
							<th>总价值</th>
						</tr>
						<c:forEach var="entity" items="${result.data}">
							<tr>
								<td>${entity.depot.name}</td>
								<td>${entity.product.sn}</td>
								<td>${entity.product.name}</td>
								<td>${entity.product.brandName}</td>
								<td>${entity.price}</td>
								<td>${entity.storeNumber}</td>
								<td>${entity.amount}</td>
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

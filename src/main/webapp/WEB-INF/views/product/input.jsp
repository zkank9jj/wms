<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>WMS-演示版</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="/style/basic_layout.css" rel="stylesheet" type="text/css">
<link href="/style/common_style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/plugins/artDialog/jquery.artDialog.js?skin=blue"></script>
<script type="text/javascript" src="/js/plugins/form/jQueryForm.js"></script>
<script type="text/javascript" src="/js/commonAll.js"></script>
<script type="text/javascript">
	$(function () {
		//把当前页面中的表单变成ajax提交,有submit按钮,直接使用ajaxForm比较好
        $("#editForm").ajaxForm(function (data) {
            if (data.success) {
                showDialog("操作成功",function () {
                    location.href = "/product/list.do";
                });
            }
        });
    });
</script>
</head>
<body>
<form id="editForm" action="/product/saveOrUpdate.do" method="post" enctype="multipart/form-data">
	<input type="hidden" name="id" value="${entity.id}">
	<div id="container">
		<div id="nav_links">
			<span style="color: #1A5CC6;">品牌编辑</span>
			<div id="page_close">
				<a>
					<img src="/images/common/page_close.png" width="20" height="20" style="vertical-align: text-top;"/>
				</a>
			</div>
		</div>
		<div class="ui_content">
			<table cellspacing="0" cellpadding="0" width="100%" align="left" border="0">
				<tr>
					<td class="ui_text_rt" width="140">品牌名称</td>
					<td class="ui_text_lt">
						<input name="name" value="${entity.name}" class="ui_input_txt02"/>
					</td>
				</tr>
				<tr>
					<td class="ui_text_rt" width="140">品牌编码</td>
					<td class="ui_text_lt">
						<input name="sn" value="${entity.sn}" class="ui_input_txt02"/>
					</td>
				</tr>
				<tr>
					<td class="ui_text_rt" width="140">成本价</td>
					<td class="ui_text_lt">
						<input name="costPrice" value="${entity.costPrice}" class="ui_input_txt02"/>
					</td>
				</tr>
				<tr>
					<td class="ui_text_rt" width="140">零售价</td>
					<td class="ui_text_lt">
						<input name="salePrice" value="${entity.salePrice}" class="ui_input_txt02"/>
					</td>
				</tr>
				<tr>
					<td class="ui_text_rt" width="140">商品品牌</td>
					<td class="ui_text_lt">
						<select name="brandId" class="ui_select03 brandId">
							<c:forEach var="b" items="${brands}">
								<option value="${b.id}">${b.name}</option>
							</c:forEach>
						</select>
					</td>
					<script>
						$(".brandId option[value='${entity.brandId}']").prop("selected", true);
					</script>
				</tr>
				<tr>
					<td class="ui_text_rt" width="140">商品图片</td>
					<td class="ui_text_lt">
						<input type="file" name="pic" class="ui_file"/>
						<c:if test="${not empty entity.imagePath}">
							<img src="${entity.imagePath}" class="list_img"/>
							<input type="hidden" name="imagePath" value="${entity.imagePath}"/>
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="ui_text_rt" width="140">商品简介</td>
					<td class="ui_text_lt">
						<textarea name="intro" class="ui_input_txtarea">${entity.intro}</textarea>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td class="ui_text_lt">
						&nbsp;<input type="submit" value="确定保存" class="ui_input_btn01"/>
						&nbsp;<input id="cancelbutton" type="button" value="重置" class="ui_input_btn01"/>
					</td>
				</tr>
			</table>
		</div>
	</div>
</form>
</body>
</html>
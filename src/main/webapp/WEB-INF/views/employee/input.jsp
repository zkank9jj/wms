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
<script type="text/javascript" src="/js/plugins/validation/jquery.validate.min.js"></script>
<script type="text/javascript" src="/js/plugins/validation/messages_cn.js"></script>
<script type="text/javascript" src="/js/plugins/artDialog/jquery.artDialog.js?skin=blue"></script>
<script type="text/javascript" src="/js/plugins/form/jQueryForm.js"></script>
<script type="text/javascript" src="/js/commonAll.js"></script>
<script type="text/javascript">
	$(function () {
        //使用表单检验插件validate
        $("#editForm").validate({
            //配置校验的规则
            rules: {
                name: {
                    required: true, //必填
                    rangelength: [2, 10] //字符长度
                },
                password: {
                    required: true,
                    rangelength: [1, 6]
                },
                repassword: {
                    equalTo: "#password"
                },
                email: {
                    required: true,
                    email: true //符合邮箱的格式
                },
                age: {
                    required: true,
                    digits: true,
                    range: [16, 65]
                }
            }
        });

        //把当前的表单做成ajax表单
        $("#editForm").ajaxForm(function (data) {
            if (data.success) {
                showDialog("操作成功", function () {
                    location.href = "/employee/list.do";
                }

                );
            }
        });

        //点击提交表单
        $(".btn_submit").click(function () {
			$(".selected_roles option").prop("selected", true);
			$("#editForm").submit();
        });

        //列表移动
		$("#select").click(function () {
			$(".all_roles option:selected").appendTo(".selected_roles");
        });
        $("#deselect").click(function () {
            $(".selected_roles option:selected").appendTo(".all_roles");
        });
        $("#selectAll").click(function () {
			$(".all_roles option").appendTo(".selected_roles");
        });
        $("#deselectAll").click(function () {
            $(".selected_roles option").appendTo(".all_roles");
        });

        //去除重复的选项
		var roleIds = $.map($(".selected_roles option"), function (ele) {
			return ele.value;
        });

		$.each($(".all_roles option"), function (index, ele) {
			if ($.inArray(ele.value, roleIds) != -1) {
                $(ele).remove();
            }
        });

		//当选择超级管理员时,去掉角色选择
		var roleTr = null;
		$(".admin").click(function () {
		    if (this.checked) {
                roleTr = $(".role").detach();
            } else {
               $(this).closest("tr").after(roleTr);
            }
        });

		<%--默认就是超级管理员,就直接开始就删除角色框--%>
		<c:if test="${entity.admin}">
			roleTr = $(".role").detach();
		</c:if>
    });
</script>
</head>
<body>
<form id="editForm" action="/employee/saveOrUpdate.do" method="post" >
	<input type="hidden" name="id" value="${entity.id}">
	<div id="container">
		<div id="nav_links">
			<span style="color: #1A5CC6;">用户编辑</span>
			<div id="page_close">
				<a>
					<img src="/images/common/page_close.png" width="20" height="20" style="vertical-align: text-top;"/>
				</a>
			</div>
		</div>
		<div class="ui_content">
			<table cellspacing="0" cellpadding="0" width="100%" align="left" border="0">
				<tr>
					<td class="ui_text_rt" width="140">用户名</td>
					<td class="ui_text_lt">
						<input name="name" value="${entity.name}" class="ui_input_txt02"/>
					</td>
				</tr>
				<c:if test="${empty entity}">
					<tr>
						<td class="ui_text_rt" width="140">密码</td>
						<td class="ui_text_lt">
							<input type="password" name="password" id="password" class="ui_input_txt02"/>
						</td>
					</tr>
					<tr>
						<td class="ui_text_rt" width="140">验证密码</td>
						<td class="ui_text_lt">
							<input name="repassword" type="password" class="ui_input_txt02" />
						</td>
					</tr>
				</c:if>
				<tr>
					<td class="ui_text_rt" width="140">Email</td>
					<td class="ui_text_lt">
						<input name="email" value="${entity.email}" class="ui_input_txt02"/>
					</td>
				</tr>
				<tr>
					<td class="ui_text_rt" width="140">年龄</td>
					<td class="ui_text_lt">
						<input name="age" value="${entity.age}" class="ui_input_txt02"/>
					</td>
				</tr>
				<tr>
					<td class="ui_text_rt" width="140">所属部门</td>
					<td class="ui_text_lt">
						<select name="dept.id" class="ui_select03 dept">
							<c:forEach var="dept" items="${depts}">
								<option value="${dept.id}">${dept.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td class="ui_text_rt" width="140">超级管理员</td>
					<td class="ui_text_lt">
						<input type="checkbox" name="admin" class="ui_checkbox01 admin"/>
					</td>
				</tr>
				<script type="text/javascript">
					//回显列表和超级管理员
					$(".dept option[value='${entity.dept.id}']").prop("selected", true);
					$(".admin").prop("checked", ${entity.admin ? entity.admin : false});
				</script>
				<tr class="role">
					<td class="ui_text_rt" width="140">角色</td>
					<td class="ui_text_lt">
						<table>
							<tr>
								<td>
									<select multiple="true" class="ui_multiselect01 all_roles">
										<c:forEach var="r" items="${roles}">
											<option value="${r.id}">${r.name}</option>
										</c:forEach>
									</select>
								</td>
								<td align="center">
									<input type="button" id="select" value="-->" class="left2right"/><br/>
									<input type="button" id="selectAll" value="==>" class="left2right"/><br/>
									<input type="button" id="deselect" value="<--" class="left2right"/><br/>
									<input type="button" id="deselectAll" value="<==" class="left2right"/>
								</td>
								<td>
									<select name="roleIds" multiple="true" class="ui_multiselect01 selected_roles">
										<c:forEach var="r" items="${entity.roles}">
											<option value="${r.id}">${r.name}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
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
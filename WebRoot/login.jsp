<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
<head>

<title>系统</title>

<script type="text/javascript" src="resources/js/jquery-easyui-1.3.6/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/jquery-easyui-1.3.6/jquery.easyui.min.js"></script>
<script type="text/javascript" src="resources/js/jquery-easyui-1.3.6/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="resources/js/jquery-easyui-1.3.6/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="resources/js/jquery-easyui-1.3.6/themes/icon.css">
<script type="text/javascript" src="resources/js/lyUtils.js"></script>

<script type="text/javascript">
	var loginDialog;
	var loginForm;

	$(function() {
		//登录弹出的对话框
		loginDialog = $('#loginDialog').dialog({
			title : '登录系统',
			width : 250,
			closable : true,
			modal : true,
			inline : true,
			buttons : [ {
				text : '登录',
				iconCls : 'icon-help',
				handler : function() {
					if ($('#loginForm').form('validate')) {
						$('#loginForm').submit();
					}
				}
			} ]
		});

		/*添加回车提交功能*/
		$('#loginForm input').bind('keyup', function(event) {
			if (event.keyCode == '13') {
				if ($('#loginForm').form('validate')) {
					$('#loginForm').submit();
				}
			}
		});
	});
	
	function clearTips() {
		$('#stip').html('');
	}
	
</script>

</head>

<body class="easyui-layout">
	<div data-options="region:'center',border:false">
		<div id="loginDialog">
			<form id="loginForm" action="login.do" method="post" style="">
				<table style="text-align: center;width: 100%">
					<tr>
						<th>用户名</th>
						<td><input id="name" name="name" class="easyui-validatebox" data-options="required:true" onfocus="javascript:clearTips();"/></td>
					</tr>
					<tr>
						<th>密&nbsp;码</th>
						<td><input id="password" name="password" class="easyui-validatebox" type="password" data-options="required:true" /></td>
					</tr>
					<tr>
						<td colspan="2" height="25px" align="left" >
							<div id="stip" style="height: 10px; font-size: 15px; color: red;">${error}</div>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>

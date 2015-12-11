<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:include page="../common/header.jsp" />

<script type="text/javascript">
	$(function() {
		/*添加学科属性的属性*/
		addPropertyProperty = function() {
			$('#addDialog').dialog({
				title : '添加属性',
				modal : true,
				inline : true,
				width : 400,
				height : 220,
				buttons : [ {
					text : '添加',
					handler : function() {
						$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍候....'
						}); // 显示进度条
						$('#addForm').form('submit', {
							url : 'propertyPropertyAdd',
							onSubmit : function() {
								var isValid = $(this).form('validate');
								if (!isValid) {
									$.messager.progress('close'); // 如果表单是无效的则隐藏进度条
								}
								return isValid; // 返回false终止表单提交
							},
							success : function(result) {
								$.messager.progress('close'); // 如果提交成功则隐藏进度条
								$('#addDialog').dialog('close');
								var r = $.parseJSON(result);
								if (r.success) {
									$('#treegrid').treegrid('reload');
								}
								$.messager.show({
									title : '提示',
									msg : r.msg
								});
							}
						});
					}
				} ],
				onOpen : function() {
					$('#addForm').form('reset');
				}
			});
		};

		/*修改学科属性的属性或属性值*/
		updatePropertyProperty = function() {
			var selectedData = $('#treegrid').treegrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			if(selectedData.level == 1){
				$('#updateDialog').dialog({
					title : '修改属性',
					modal : true,
					inline : true,
					width : 400,
					height : 220,
					buttons : [ {
						text : '修改',
						handler : function() {
							$.messager.progress({
								title : '提示',
								text : '数据处理中，请稍候....'
							}); // 显示进度条
							$('#updateForm').form('submit', {
								url : 'propertyPropertyUpdate',
								onSubmit : function() {
									var isValid = $(this).form('validate');
									if (!isValid) {
										$.messager.progress('close'); // 如果表单是无效的则隐藏进度条
									}
									return isValid; // 返回false终止表单提交
								},
								success : function(result) {
									$.messager.progress('close'); // 如果提交成功则隐藏进度条
									$('#updateDialog').dialog('close');
									var r = $.parseJSON(result);
									if (r.success) {
										$('#treegrid').treegrid('reload');
									}
									$.messager.show({
										title : '提示',
										msg : r.msg
									});
								}
							});
						}
					} ],
					onOpen : function() {
						$('#updateForm').form('clear');
						$.post('propertyPropertyEdit', {
							id : selectedData.id
						}, function(result) {
							$('#updateForm').form('load', result);
						}, 'JSON');
					}
				});
			}else{
				$('#updateValueDialog').dialog({
					title : '修改属性值',
					modal : true,
					inline : true,
					width : 400,
					height : 280,
					buttons : [ {
						text : '修改',
						handler : function() {
							$.messager.progress({
								title : '提示',
								text : '数据处理中，请稍候....'
							}); // 显示进度条
							$('#updateValueForm').form('submit', {
								url : 'propertyPropertyValueUpdate',
								onSubmit : function() {
									var isValid = $(this).form('validate');
									if (!isValid) {
										$.messager.progress('close'); // 如果表单是无效的则隐藏进度条
									}
									return isValid; // 返回false终止表单提交
								},
								success : function(result) {
									$.messager.progress('close'); // 如果提交成功则隐藏进度条
									$('#updateValueDialog').dialog('close');
									var r = $.parseJSON(result);
									if (r.success) {
										$('#treegrid').treegrid('reload');
									}
									$.messager.show({
										title : '提示',
										msg : r.msg
									});
								}
							});
						}
					} ],
					onOpen : function() {
						$('#updateValueForm').form('clear');
						$('#updatePropertySelect').combobox({
							valueField: 'id',
							textField: 'name',
							url: 'getPropertyPropertyList?id='+${id}
						});
						$.post('propertyPropertyValueEdit', {
							id : selectedData.id
						}, function(result) {
							$('#updateValueForm').form('load', result);
						}, 'JSON');
					}
				});
			}
		};

		/*删除学科属性的属性*/
		deletePropertyProperty = function() {
			var selectedData = $('#treegrid').treegrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			$.messager.confirm('询问', '您是否要删除当前数据？', function(b) {
				if (b) {
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					});
					$.post('propertyPropertyTreeDelete', {
						id : selectedData.id
					}, function(result) {
						if (result.success) {
							$('#treegrid').treegrid('clearSelections');
							$('#treegrid').treegrid('reload');
						}
						$.messager.progress('close');
						$.messager.show({
							title : '提示',
							msg : result.msg
						});
					}, 'JSON');
				}
			});
		};
		
		
		/*添加学科属性的属性值*/
		addPropertyPropertyValue = function() {
			$('#addValueDialog').dialog({
				title : '添加属性值',
				modal : true,
				inline : true,
				width : 400,
				height : 280,
				buttons : [ {
					text : '添加',
					handler : function() {
						$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍候....'
						}); // 显示进度条
						$('#addValueForm').form('submit', {
							url : 'propertyPropertyValueAdd',
							onSubmit : function() {
								var isValid = $(this).form('validate');
								if (!isValid) {
									$.messager.progress('close'); // 如果表单是无效的则隐藏进度条
								}
								return isValid; // 返回false终止表单提交
							},
							success : function(result) {
								$.messager.progress('close'); // 如果提交成功则隐藏进度条
								$('#addValueDialog').dialog('close');
								var r = $.parseJSON(result);
								if (r.success) {
									$('#treegrid').treegrid('reload');
								}
								$.messager.show({
									title : '提示',
									msg : r.msg
								});
							}
						});
					}
				} ],
				onOpen : function() {
					$('#addValueForm').form('reset');
					$('#addPropertySelect').combobox({
						valueField: 'id',
						textField: 'name',
						url: 'getPropertyPropertyList?id='+${id}
					});
				}
			});
		};

	});
</script>

<!-- 主页面内容start -->
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false">
		<table id="treegrid" class="easyui-treegrid" title="属性列表" 
			data-options="
				url : 'propertyPropertyTreeGrid?pid=' + ${id},
				rownumbers: true,
				fitColumns: true,
				border : false,
				fit : true,
				singleSelect : true,
				idField: 'id',
				treeField: 'name',
				toolbar : [ {
					text : '新增属性',
					iconCls : 'icon-add',
					handler : function() {
						addPropertyProperty();
					}
				}, '-', {
					text : '新增属性值',
					iconCls : 'icon-add',
					handler : function() {
						addPropertyPropertyValue();
					}
				}, '-', {
					text : '修改',
					iconCls : 'icon-edit',
					handler : function() {
						updatePropertyProperty();
					}
				}, '-', {
					text : '删除',
					iconCls : 'icon-remove',
					handler : function() {
						deletePropertyProperty();
					}
				} ]
			">
		<thead>
			<tr>
				<th data-options="field:'id',hidden:true,width:180">编号</th>
				<th data-options="field:'name',width:180">名称</th>
				<th data-options="field:'comment',width:180">说明</th>
				<th data-options="field:'level',width:180,hidden:true">级别</th>
			</tr>
		</thead>
	</table>
	</div>
</div>
<!-- 主页面内容end -->

<!-- 新增属性对话框start -->
<div id="addDialog">
	<fieldset style="margin-top: 5px">
		<legend>
			<b><i>属性基本信息</i></b>
		</legend>
		<form id="addForm" method="post">
			<table>
				<tr>
					<td>属性名称</td>
					<td><input type="hidden" id="parentId" name="parentId" value="${id}"/>
						<input class="easyui-validatebox" name="name"
						data-options="required:true" style="width: 180px;" /></td>
				</tr>
				<tr>
					<td>说明</td>
					<td><textarea name="comment" rows="2" cols="30" class="textarea easyui-validatebox"></textarea></td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
<!-- 新增属性对话框end -->

<!-- 更新属性对话框start -->
<div id="updateDialog">
	<fieldset style="margin-top: 5px">
		<legend>
			<b><i>属性基本信息</i></b>
		</legend>
		<form id="updateForm" method="post">
			<table>
				<tr>
					<td>属性名称</td>
					<td><input type="hidden" id="id" name="id" /> <input
						class="easyui-validatebox" name="name"
						data-options="required:true" style="width: 180px;" /></td>
				</tr>
				<tr>
					<td>说明</td>
					<td><textarea name="comment" rows="2" cols="30" class="textarea easyui-validatebox"></textarea></td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
<!-- 更新属性对话框end -->

<!-- 新增属性值对话框start -->
<div id="addValueDialog">
	<fieldset style="margin-top: 5px">
		<legend>
			<b><i>属性值基本信息</i></b>
		</legend>
		<form id="addValueForm" method="post">
			<table>
				<tr>
					<td>属性值名称</td>
					<td><input class="easyui-validatebox" name="name"
						data-options="required:true" style="width: 180px;" /></td>
				</tr>
				<tr>
					<td>所属属性</td>
					<td>
						<input id="addPropertySelect" name="parentId" class="easyui-combobox" data-options="required:true,editable:false,validType:'comboVry[\'请选择...\']',panelHeight:'auto',panelWidth:100,width:100" />
					</td>
				</tr>
				<tr>
					<td>附件</td>
					<td><input type="file"></td>
				</tr>
				<tr>
					<td>说明</td>
					<td><textarea name="comment" rows="2" cols="30" class="textarea easyui-validatebox"></textarea></td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
<!-- 新增属性值对话框end -->

<!-- 更新属性值对话框start -->
<div id="updateValueDialog">
	<fieldset style="margin-top: 5px">
		<legend>
			<b><i>属性值基本信息</i></b>
		</legend>
		<form id="updateValueForm" method="post">
			<table>
				<tr>
					<td>属性值名称</td>
					<td><input type="hidden" id="id" name="id" /> 
					<input class="easyui-validatebox" name="name"
						data-options="required:true" style="width: 180px;" /></td>
				</tr>
				<tr>
					<td>所属属性</td>
					<td>
						<input id="updatePropertySelect" name="parentId" class="easyui-combobox" data-options="required:true,editable:false,validType:'comboVry[\'请选择...\']',panelHeight:'auto',panelWidth:100,width:100"/>
					</td>
				</tr>
				<tr>
					<td>附件</td>
					<td><input type="file"></td>
				</tr>
				<tr>
					<td>说明</td>
					<td><textarea name="comment" rows="2" cols="30" class="textarea easyui-validatebox"></textarea></td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
<!-- 更新属性值对话框end -->

<jsp:include page="../common/footer.jsp" />

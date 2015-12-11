<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:include page="../common/header.jsp" />

<script type="text/javascript">
	$(function() {
		var datagrid;
		datagrid = $('#datagrid').datagrid({
			title : '学科属性值列表',
			url : 'subjectPropertyValueList?id=' + ${id},
			columns : [ [ {
				field : 'id',
				title : '编号',
				width : 100,
				hidden : true
			}, {
				field : 'name',
				title : '属性值名称',
				width : 150,
			}, {
				field : 'comment',
				title : '备注',
				width : 150,
			}, {
				field : 'sort',
				title : '排序',
				width : 60,
			}, ] ],
			toolbar : [ {
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					addSubjectPropertyValue();
				}
			}, '-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					updateSubjectPropertyValue();
				}
			}, '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					deleteSubjectPropertyValue();
				}
			}, '-', {
				text : '属性',
				iconCls : 'page_gear',
				handler : function() {
					getPropertyProperty();
				}
			} ],
			//加载数据成功后触发的事件
			onLoadSuccess : function(data) {
				if (data.rows.length <= 0) {
					var body = $(this).data().datagrid.dc.body2;
					body.find('table tbody').append('<tr><td width="' + body.width() + '" style="height: 25px; text-align: center;">没有数据！</td></tr>');
				}
			},
			onDblClickRow : function(){
				getPropertyProperty();
			},
			fitColumns : true,
			striped : true,
			idField : 'id',
			pagination : true,
			rownumbers : true,
			border : false,
			fit : true,
			singleSelect : true,
			pageSize : 20,
			pageList : [ 20, 40, 60, 80, 100 ]
		});

		/*添加学科属性值*/
		addSubjectPropertyValue = function() {
			$('#addDialog').dialog({
				title : '添加属性值',
				modal : true,
				inline : true,
				width : 400,
				height : 250,
				buttons : [ {
					text : '添加',
					handler : function() {
						$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍候....'
						}); // 显示进度条
						$('#addForm').form('submit', {
							url : 'subjectPropertyValueAdd',
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
									$('#datagrid').datagrid('reload');
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

		/*修改学科属性值*/
		updateSubjectPropertyValue = function() {
			var selectedData = $('#datagrid').datagrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			$('#updateDialog').dialog({
				title : '修改属性值',
				modal : true,
				inline : true,
				width : 400,
				height : 250,
				buttons : [ {
					text : '修改',
					handler : function() {
						$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍候....'
						}); // 显示进度条
						$('#updateForm').form('submit', {
							url : 'subjectPropertyValueUpdate',
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
									$('#datagrid').datagrid('reload');
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
					$.post('subjectPropertyValueEdit', {
						id : selectedData.id
					}, function(result) {
						$('#updateForm').form('load', result);
					}, 'JSON');
				}
			});
		};

		/*删除学科属性值*/
		deleteSubjectPropertyValue = function() {
			var selectedData = $('#datagrid').datagrid('getSelected');
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
					$.post('subjectPropertyValueDelete', {
						id : selectedData.id
					}, function(result) {
						if (result.success) {
							$('#datagrid').datagrid('clearSelections');
							$('#datagrid').datagrid('reload');
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
		
		/*学科属性的属性列表*/
		getPropertyProperty = function(){
			var selectedData = $('#datagrid').datagrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			if (window.parent.$('#centerTabs').tabs('exists', selectedData.name)){//如果tab已经存在,则选中并刷新该tab  
				window.parent.$('#centerTabs').tabs('select', selectedData.name);
		    } else {
				window.parent.$('#centerTabs').tabs('add', {
					title: selectedData.name,
					closable: true,
					content: '<iframe scrolling="no" frameborder="0"  src="propertyProperty?id='+selectedData.id+'" style="width:100%;height:99.5%;"></iframe>', // 新内容的URL
					tools : [ {
						iconCls : 'icon-mini-refresh',
						handler : function() {
							var selectedTab = window.parent.$('#centerTabs').tabs('getSelected');
							selectedTab.panel('refresh');
						}
					} ]
				});
		    }
		};
		

	});
</script>

<!-- 主页面内容start -->
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false">
		<table id="datagrid"></table>
	</div>
</div>
<!-- 主页面内容end -->

<!-- 新增对话框start -->
<div id="addDialog">
	<fieldset style="margin-top: 5px">
		<legend>
			<b><i>属性值基本信息</i></b>
		</legend>
		<form id="addForm" method="post">
			<table>
				<tr>
					<td>属性值名称</td>
					<td><input type="hidden" id="parentId" name="parentId"
						value="${id}" /> <input class="easyui-validatebox" name="name"
						data-options="required:true" style="width: 180px;" /></td>
				</tr>
				<tr>
					<td>排序</td>
					<td><input type="text" name="sort" class="easyui-numberbox"
						data-options="min:1,max:100,required:true" style="width: 100px;" /></td>
				</tr>
				<tr>
					<td>备注</td>
					<td><textarea name="comment" rows="2" cols="30" class="textarea easyui-validatebox"></textarea></td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
<!-- 新增对话框end -->

<!-- 更新对话框start -->
<div id="updateDialog">
	<fieldset style="margin-top: 5px">
		<legend>
			<b><i>属性值基本信息</i></b>
		</legend>
		<form id="updateForm" method="post">
			<table>
				<tr>
					<td>属性名称</td>
					<td><input type="hidden" id="parentId" name="parentId"
						value="${id}" /><input type="hidden" id="id" name="id" /> <input
						class="easyui-validatebox" name="name"
						data-options="required:true" style="width: 180px;" /></td>
				</tr>
				<tr>
					<td>排序</td>
					<td><input class="easyui-numberbox" name="sort"
						data-options="min:1,max:100,required:true" /></td>
				</tr>
				<tr>
					<td>备注</td>
					<td><textarea name="comment" rows="2" cols="30" class="textarea easyui-validatebox"></textarea></td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
<!-- 更新对话框end -->

<jsp:include page="../common/footer.jsp" />

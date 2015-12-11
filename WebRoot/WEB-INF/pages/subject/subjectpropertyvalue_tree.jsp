<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:include page="../common/header.jsp" />

<script type="text/javascript">
	$(function() {
		/*添加学科属性值*/
		addSubjectPropertyValue = function() {
			var selectedData = $('#treegrid').treegrid('getSelected');
			var id = '';
			if(selectedData){
				id = selectedData.id;
			}
			$('#addDialog').dialog({
				title : '添加属性值',
				modal : true,
				inline : true,
				width : 360,
				height : 200,
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
					$('#addComboTree').combotree({    
					    url: 'subjectPropertyValueComboTree?pid=' + ${id},
					    parentField : 'pid',
						textFiled : 'text',
						idFiled : 'id',
					    required : true,
					    panelWidth : 180,
					    width : 180,
					    value : id
					});  
				}
			});
		};

		/*修改学科属性值*/
		updateSubjectPropertyValue = function() {
			var selectedData = $('#treegrid').treegrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			if(selectedData._parentId == 0){
				$.messager.alert('警告', '该根节点不能修改！', 'warning');
				return;
			}
			$('#updateDialog').dialog({
				title : '修改属性值',
				modal : true,
				inline : true,
				width : 360,
				height : 200,
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
					$.post('subjectPropertyValueEdit', {
						id : selectedData.id
					}, function(result) {
						$('#updateForm').form('load', result);
						$('#updateComboTree').combotree({    
						    url: 'subjectPropertyValueComboTree?pid=' + ${id} + '&id=' + selectedData.id,
						    parentField : 'pid',
							textFiled : 'text',
							idFiled : 'id',
						    required : true,
						    panelWidth : 180,
						    width : 180,
						    value : result.parentId
						});  
					}, 'JSON');
				}
			});
		};

		/*删除学科属性值*/
		deleteSubjectPropertyValue = function() {
			var selectedData = $('#treegrid').treegrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			if(selectedData._parentId == 0){
				$.messager.alert('警告', '该根节点不能删除！', 'warning');
				return;
			}
			$.messager.confirm('询问', '您是否要删除当前数据？', function(b) {
				if (b) {
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					});
					$.post('subjectPropertyValueTreeDelete', {
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
		
		
		/*学科属性的属性列表*/
		getPropertyProperty = function(){
			var selectedData = $('#treegrid').treegrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			if(selectedData._parentId == 0){
				$.messager.alert('警告', '该根节点不能操作！', 'warning');
				return;
			}
			if (window.parent.$('#centerTabs').tabs('exists', selectedData.name)){//如果tab已经存在,则选中并刷新该tab  
				window.parent.$('#centerTabs').tabs('close', selectedData.name);
			}
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
		};

	});
</script>

<!-- 主页面内容start -->
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false">
		<table id="treegrid" class="easyui-treegrid" title="学科属性值列表" 
			data-options="
				url : 'subjectPropertyValueTreeGrid?pid=' + ${id},
				rownumbers: true,
				fitColumns: true,
				border : false,
				fit : true,
				singleSelect : true,
				idField: 'id',
				treeField: 'name',
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
				onDblClickRow : function(){
					getPropertyProperty();
				}
			">
		<thead>
			<tr>
				<th data-options="field:'id',hidden:true,width:180">编号</th>
				<th data-options="field:'name',width:180">属性值名称</th>
				<th data-options="field:'sort',width:80">排序</th>
			</tr>
		</thead>
	</table>
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
					<td><input class="easyui-validatebox" name="name"
						data-options="required:true" style="width: 180px;" /></td>
				</tr>
				<tr>
					<td>排序</td>
					<td><input type="text" name="sort" class="easyui-numberbox"
						data-options="min:1,max:1000,required:true" style="width: 100px;" /></td>
				</tr>
				<tr>
					<td>父节点</td>
					<td><input id="addComboTree" name="parentId" /></td>
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
					<td><input type="hidden" id="id" name="id" /> <input
						class="easyui-validatebox" name="name"
						data-options="required:true" style="width: 180px;" /></td>
				</tr>
				<tr>
					<td>排序</td>
					<td><input class="easyui-numberbox" name="sort"
						data-options="min:1,max:1000,required:true" /></td>
				</tr>
				<tr>
					<td>父节点</td>
					<td><input id="updateComboTree" name="parentId" /></td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
<!-- 更新对话框end -->

<jsp:include page="../common/footer.jsp" />

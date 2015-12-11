<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:include page="../common/header.jsp"/>

<script type="text/javascript">

	$(function(){
		var datagrid;
		datagrid = $('#datagrid').datagrid({
			title : '学科属性列表',
			url : 'subjectPropertyList.do?id='+${id},
			columns : [ [ {
				field : 'id',
				title : '编号',
				width : 100,
				hidden : true
			}, {
				field : 'name',
				title : '属性名称',
				width : 150,
			}, {
				field : 'type',
				title : '属性类型',
				width : 60,
			}, {
				field : 'status',
				title : '状态',
				width : 60,
			}, {
				field : 'sort',
				title : '排序',
				width : 60,
			}, {
				field : 'comment',
				title : '备注',
				width : 150,
			}, ] ],
			toolbar : [ {
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					addSubjectProperty();
				}
			},'-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					updateSubjectProperty();
				}
			}, '-', {
				text : '复制到',
				iconCls : 'page_copy',
				handler : function() {
					copySubjectProperty();
				}
			}],
			//加载数据成功后触发的事件
			onLoadSuccess : function(data) {
				if (data.rows.length <= 0) {
					var body = $(this).data().datagrid.dc.body2;
					body.find('table tbody').append('<tr><td width="' + body.width() + '" style="height: 25px; text-align: center;">没有数据！</td></tr>');
				}
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
			pageList : [20,40,60,80,100]
		});
		
		
		/*添加学科属性*/
		addSubjectProperty = function() {
			$('#addDialog').dialog({
				title : '添加属性',
				modal : true,
				inline : true,
				width : 380,
				height : 280,
				buttons : [ {
					text : '添加',
					handler : function() {
						$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍候....'
						}); // 显示进度条
						$('#addForm').form('submit', {
							url : 'subjectPropertyAdd',
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
									$('#datagrid').datagrid('load', ly.serializeObjct($('#searchForm')));
									window.parent.$('#subjectMenuTree').tree('reload');
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
		
		
		/*修改学科属性*/
		updateSubjectProperty = function() {
			var selectedData = $('#datagrid').datagrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			$('#updateDialog').dialog({
				title : '修改属性',
				modal : true,
				inline : true,
				width : 380,
				height : 300,
				buttons : [ {
					text : '修改',
					handler : function() {
						$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍候....'
						}); // 显示进度条
						$('#updateForm').form('submit', {
							url : 'subjectPropertyUpdate',
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
									$('#datagrid').datagrid('load',ly.serializeObjct($('#searchForm')));
									window.parent.$('#subjectMenuTree').tree('reload');
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
					$.post('subjectPropertyEdit', {
						id : selectedData.id
					}, function(result) {
						$('#updateForm').form('load',result);
					}, 'JSON');
				}
			});
		};
		
		
		//复制学科属性到某节点
		copySubjectProperty = function(){
			var selectedData = $('#datagrid').datagrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			$('#copyDialog').dialog({
				title : '复制属性',
				modal : true,
				inline : true,
				width : 300,
				height : 400,
				top : 100,
				buttons : [ {
					text : '复制',
					handler : function() {
						$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍候....'
						}); // 显示进度条
						$('#copyForm').form('submit', {
							url : 'subjectPropertyCopy',
							onSubmit : function() {
								var isValid = $(this).form('validate');
								if (!isValid) {
									$.messager.progress('close'); // 如果表单是无效的则隐藏进度条
								}
								var selectedNode = $('#copyToTree').tree('getSelected');
								if(!selectedNode){
									$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
									$.messager.progress('close');
									return false;
								}
								$('#copyToId').val(selectedNode.id);
								return isValid; // 返回false终止表单提交
							},
							success : function(result) {
								$.messager.progress('close'); // 如果提交成功则隐藏进度条
								$('#copyDialog').dialog('close');
								window.parent.$('#subjectMenuTree').tree('reload');
								var r = $.parseJSON(result);
								$.messager.show({
									title : '提示',
									msg : r.msg
								});
							}
						});
					}
				} ],
				onOpen : function() {
					$('#copyToTree').tree({
						url : 'subjectPropertyCopyToTree',
						parentField : 'pid',
						textFiled : 'text',
						idFiled : 'id',
						onSelect : function(node){
							if(!$('#copyToTree').tree('isLeaf',node.target)){
								$.messager.show({
									title : '提示',
									msg : '请选择正确的节点！'
								});
								$('#copyToTree').find('.tree-node-selected').removeClass('tree-node-selected');
								return;
							}
							$.post('isExistSubjectPropertyName', {
								name : selectedData.name,
								pid : node.id
							}, function(result) {
								if(!result.success){
									$.messager.show({
										title : '提示',
										msg : '该节点下已存在相同名称的属性！'
									});
									$('#copyToTree').find('.tree-node-selected').removeClass('tree-node-selected');
								}
							}, 'JSON');
						}
					});
					$('#subjectPropertyId').val(selectedData.id);
				}
			});
		};
		
		
		//启用状态选择项
		$('#statusSelect').combobox({
			editable : false,
			panelHeight : 'auto',
			panelWidth : 60,
			width : 60,
			onSelect : function(record){
				$('#datagrid').datagrid('load',ly.serializeObjct($('#searchForm')));
			} 
		});
		
	});

</script>

<!-- 主页面内容start -->
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north'" class="datagrid-toolbar">
		<fieldset>
			<legend>查询条件</legend>
			<form id="searchForm" method="post">
				启用状态
				<select id="statusSelect" name="status">
					<option selected="selected">全部</option>
					<option>启用</option>
					<option>停用</option>
				</select>
			</form>
		</fieldset>
	</div>
	<div data-options="region:'center'">
		<table id="datagrid"></table>
	</div>
</div>
<!-- 主页面内容end -->

<!-- 新增对话框start -->
<div id="addDialog">
	<fieldset style="margin-top: 5px">
		<legend>
			<b><i>属性基本信息</i></b>
		</legend>
		<form id="addForm" method="post">
			<table>
				<tr>
					<td>属性名称</td>
					<td>
						<input type="hidden" id="parentId" name="parentId" value="${id}"/>
						<input class="easyui-validatebox" name="name" data-options="required:true,validType:'isExistName[\'isExistSubjectPropertyName\',${id}]',delay:1000" style="width: 200px;"/>
					</td>
				</tr>
				<tr>
					<td>属性类型</td>
					<td>
						<select id="addTypeSelect" name="type" class="easyui-combobox" data-options="required:true,editable:false,validType:'comboVry[\'请选择...\']',panelHeight:'auto',panelWidth:100,width:100">
							<option selected="selected">请选择...</option>
							<option>文本</option>
							<option>单选</option>
							<option>复选</option>
							<option>下拉</option>
							<option>树形</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>排序</td>
					<td><input type="text" name="sort" class="easyui-numberbox" data-options="min:1,max:100,required:true" style="width: 100px;"/></td>
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
			<b><i>属性基本信息</i></b>
		</legend>
		<form id="updateForm" method="post">
			<table>
				<tr>
					<td>属性名称</td>
					<td>
						<input type="hidden" id="id" name="id"/>
						<input class="easyui-validatebox" name="name" data-options="required:true,validType:'isExistNameUpdate[\'isExistSubjectPropertyName\',${id},\'#id\']',delay:1000" style="width: 200px;"/>
					</td>
				</tr>
				<tr>
					<td>属性类型</td>
					<td>
						<select id="updateTypeSelect" name="type" class="easyui-combobox" data-options="editable:false,panelHeight:'auto',panelWidth:100,width:100">
							<option>文本</option>
							<option>单选</option>
							<option>复选</option>
							<option>下拉</option>
							<option>树形</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>状态</td>
					<td>
						<select id="updateStatus" name="status" class="easyui-combobox" data-options="editable:false,panelHeight:'auto',panelWidth:100,width:100">
							<option>启用</option>
							<option>停用</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>排序</td>
					<td><input class="easyui-numberbox" name="sort" data-options="min:1,max:100,required:true" /></td>
				</tr>
				<tr>
					<td>备注</td>
					<td><textarea name="comment" rows="2" cols="30" class="textarea easyui-validatebox" ></textarea></td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
<!-- 更新对话框end -->

<!-- 复制到某节点对话框start -->
<div id="copyDialog">
	<fieldset>
		<legend>
			<b><i>选择复制到：</i></b>
		</legend>
		<form id="copyForm" method="post">
			<table>
				<tr>
					<td>
						<ul id="copyToTree"></ul>
						<input id="subjectPropertyId" name="subjectPropertyId" type="hidden" />
						<input id="parentId" name="parentId" type="hidden" value="${id}"/>
						<input id="copyToId" name="copyToId" type="hidden" />
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
<!-- 复制到某节点对话框end -->

<jsp:include page="../common/footer.jsp"/>

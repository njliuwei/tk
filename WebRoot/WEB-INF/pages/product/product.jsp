<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:include page="../common/header.jsp"/>

<script type="text/javascript">

	$(function(){
		var datagrid;
		datagrid = $('#datagrid').datagrid({
			title : '产品库列表',
			url : 'productList?subject=${subject}',
			columns : [ [ {
				field : 'id',
				title : '编号',
				width : 100,
				hidden : true
			}, {
				field : 'name',
				title : '产品名称',
				width : 250,
			}, {
				field : 'qstCount',
				title : '试题数',
				width : 60,
			}, {
				field : 'status',
				title : '发布状态',
				width : 60,
			}, {
				field : 'creator',
				title : '创建人',
				width : 80,
			}, {
				field : 'created',
				title : '创建时间',
				width : 100,
			}, {
				field : 'lastmodifier',
				title : '最后修改人',
				width : 80,
			}, {
				field : 'lastmodified',
				title : '最后修改时间',
				width : 100,
			}, ] ],
			toolbar : [ {
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					addProduct();
				}
			},'-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					updateProduct();
				}
			},'-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					deleteProduct();
				}
			},'-', {
				text : '浏览',
				iconCls : 'application_view_detail',
				handler : function() {
					viewProduct();
				}
			},'-', {
				text : '组卷',
				iconCls : 'page_add',
				handler : function() {
					generatePaper();
				}
			},'-', {
				text : '生成授权',
				iconCls : 'application_key',
				handler : function() {
					generateKey();
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
		
		
		/*添加产品*/
		addProduct = function() {
			$('#addDialog').dialog({
				title : '添加产品',
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
							url : 'productAdd',
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
		
		
		/*修改产品*/
		updateProduct = function() {
			var selectedData = $('#datagrid').datagrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			$('#updateDialog').dialog({
				title : '修改产品',
				modal : true,
				inline : true,
				width : 380,
				height : 280,
				buttons : [ {
					text : '修改',
					handler : function() {
						$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍候....'
						}); // 显示进度条
						$('#updateForm').form('submit', {
							url : 'productUpdate',
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
					$.post('productEdit', {
						id : selectedData.id
					}, function(result) {
						$('#updateForm').form('load',result);
					}, 'JSON');
				}
			});
		};
		
		
		/*删除产品*/
		deleteProduct = function() {
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
					$.post('productDelete', {
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
		
		/*浏览产品*/
		viewProduct = function(){
			var selectedData = $('#datagrid').datagrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			$('#view').window({
				title : '产品浏览',
			    width : 1000,    
			    height : 600,    
			    modal : true,
			    content : '<iframe scrolling="no" frameborder="0"  src="productView?subject=${subject}&productId=' + selectedData.id + '" style="width:100%;height:99%;"></iframe>',
			    onClose : function(){
			    	$('#datagrid').datagrid('reload');
			    }
			});  
		};
		
		/*组卷*/
		generatePaper = function(){
			var selectedData = $('#datagrid').datagrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			$('#paper').window({
				title : '组卷',
			    width : 1000,
			    height : 600,    
			    modal : true,
			    collapsible : false,
			    minimizable : false,
				maximizable : false,
			    content : '<iframe scrolling="no" frameborder="0"  src="paperGenerate?subject=${subject}&productId=' + selectedData.id +'&productName=' + selectedData.name + '" style="width:100%;height:99%;"></iframe>'
			});  
		};
		
		/*授权*/
		generateKey = function(){
			var selectedData = $('#datagrid').datagrid('getSelected');
			var rowIndex = $('#datagrid').datagrid('getRowIndex',selectedData);
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			$('#grantDialog').dialog({
				title : '授权产品',
				modal : true,
				inline : true,
				width : 280,
				height : 250,
				buttons : [ {
					text : '授权',
					handler : function() {
						$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍候....'
						}); // 显示进度条
						$('#grantForm').form('submit', {
							url : 'keyGenerate',
							onSubmit : function() {
								var isValid = $(this).form('validate');
								if (!isValid) {
									$.messager.progress('close'); // 如果表单是无效的则隐藏进度条
								}
								return isValid; // 返回false终止表单提交
							},
							success : function(result) {
								$.messager.progress('close'); // 如果提交成功则隐藏进度条
								$('#grantDialog').dialog('close');
								var r = $.parseJSON(result);
								if (r.success) {
									var downUrl = 'productTxtDownload?filepath=' + r.obj;
					                window.location = downUrl;
					                //更改产品的发布状态
					                selectedData.status = '已发布';
									$('#datagrid').datagrid('refreshRow',rowIndex);
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
					$('#grantForm').form('reset');
					$('#productId').val(selectedData.id);
					$('#productName').val(selectedData.name);
				}
			});
		};
		
		
		/*获得知识点树*/
		getZsdTree = function(flag){
			$('#zsdDialog').dialog({
				title : '知识点',
				modal : true,
				inline : true,
				width : 380,
				height : 280,
				buttons : [ {
					text : '确定',
					handler : function() {
						var checknodes = $('#zsdTree').tree('getChecked', ['checked','indeterminate']);
						var ids = [];
						var names = [];
						if (checknodes && checknodes.length > 0) {
							for (var i = 0; i < checknodes.length; i++) {
								ids.push(checknodes[i].id);
								names.push(checknodes[i].text);
							}
						}
						if(flag == 0){
							$('#addZsdIds').val(ids);
							$('#addZsdNames').val(names);
						}else{
							$('#updateZsdIds').val(ids);
							$('#updateZsdNames').val(names);
						}
						$('#zsdDialog').dialog('close');
					}
				} ],
				onOpen : function() {
					var ids;
					if(flag == 0){
						ids = $('#addZsdIds').val();
					}else{
						ids = $('#updateZsdIds').val();
					}
					$('#zsdTree').tree({
						url : 'zsdTree?subject=${subject}&id='+ids,
						parentField : 'pid',
						textFiled : 'text',
						idFiled : 'id',
						checkbox : true
					});
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
					<option>已发布</option>
					<option>未发布</option>
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
			<b><i>产品基本信息</i></b>
		</legend>
		<form id="addForm" method="post">
			<table>
				<tr>
					<td>产品名称</td>
					<td>
						<input type="hidden" id="subject" name="subject" value="${subject}"/>
						<input class="easyui-validatebox" name="name" data-options="required:true,validType:'isExistName[\'isExistProductName\',\'\']',delay:1000" style="width: 200px;"/>
					</td>
				</tr>
				<tr>
					<td>教材体系</td>
					<td>
						<input class="easyui-validatebox" name="propversion" />
					</td>
				</tr>
				<tr>
					<td>知识点</td>
					<td>
						<input type="hidden" id="addZsdIds" name="zsd"/>
						<input class="easyui-validatebox" id="addZsdNames" onclick="getZsdTree(0);"/>
					</td>
				</tr>
				<tr>
					<td>题型</td>
					<td><input class="easyui-validatebox" name="tx" /></td>
				</tr>
				<tr>
					<td>难度</td>
					<td><input class="easyui-validatebox" name="nd" /></td>
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
			<b><i>产品基本信息</i></b>
		</legend>
		<form id="updateForm" method="post">
			<table>
				<tr>
					<td>产品名称</td>
					<td>
						<input type="hidden" id="id" name="id"/>
						<input type="hidden" id="subject" name="subject"/>
						<input class="easyui-validatebox" name="name" data-options="required:true,validType:'isExistNameUpdate[\'isExistProductName\',\'\',\'#id\']',delay:1000" style="width: 200px;"/>
					</td>
				</tr>
				<tr>
					<td>教材体系</td>
					<td>
						<input class="easyui-validatebox" name="propversion" />
					</td>
				</tr>
				<tr>
					<td>知识点</td>
					<td>
						<input type="hidden" id="updateZsdIds" name="zsd"/>
						<input class="easyui-validatebox" id="updateZsdNames" onclick="getZsdTree(1);"/>
					</td>
				</tr>
				<tr>
					<td>题型</td>
					<td><input class="easyui-validatebox" name="tx" /></td>
				</tr>
				<tr>
					<td>难度</td>
					<td><input class="easyui-validatebox" name="nd" /></td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
<!-- 更新对话框end -->

<!-- 授权对话框start -->
<div id="grantDialog">
	<fieldset style="margin-top: 5px">
		<legend>
			<b><i>授权基本信息</i></b>
		</legend>
		<form id="grantForm" method="post">
			<table>
				<tr>
					<td>产品名称</td>
					<td>
						<input type="hidden" id="productId" name="productId"/>
						<input  id="productName" name="productName" readonly="readonly" />
					</td>
				</tr>
				<tr>
					<td>使用者</td>
					<td>
						<input class="easyui-validatebox" name="userName" data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td>使用IP</td>
					<td>
						<input class="easyui-validatebox" name="userIP" data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td>失效日期</td>
					<td><input class="easyui-datebox" name="dueDate" data-options="required:true,editable:false"/></td>
				</tr>
				<tr>
					<td>是否会员</td>
					<td>
						<select id="memberSelect" name="isMember">
							<option>否</option>
							<option>是</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
<!-- 授权对话框end -->

<!-- 知识点选择框 -->
<div id="zsdDialog">
	<ul id="zsdTree"></ul> 
</div>

<!-- 浏览产品的弹出框 -->
<div id="view"></div> 
<!-- 组卷的弹出框 -->
<div id="paper"></div>

<jsp:include page="../common/footer.jsp"/>

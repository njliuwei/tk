<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:include page="../common/header.jsp"/>

<script type="text/javascript">

	$(function(){
		var datagrid;
		datagrid = $('#datagrid').datagrid({
			title : '试卷库列表',
			url : 'paperList?subject=${subject}',
			columns : [ [ {
				field : 'id',
				title : '编号',
				width : 100,
				hidden : true
			}, {
				field : 'name',
				title : '试卷名称',
				width : 250,
			}, {
				field : 'productName',
				title : '产品库名称',
				width : 250,
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
					addPaper();
				}
			},'-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					updatePaper();
				}
			},'-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					deletePaper();
				}
			},'-', {
				text : '浏览',
				iconCls : 'page_white_text',
				handler : function() {
					viewPaper();
				}
			},'-', {
				text : '导出',
				iconCls : 'page_white_go',
				handler : function() {
					exportPaper();
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
		
		
		/*添加试卷*/
		addPaper = function() {
			$('#addPaper').window({
				title : '添加试卷',
			    width : 1000,
			    height : 600,    
			    modal : true,
			    collapsible : false,
			    minimizable : false,
				maximizable : false,
			    content : '<iframe scrolling="no" frameborder="0"  src="paperAdd?subject=${subject}&productId=-1" style="width:100%;height:99%;"></iframe>',
			    onClose : function(){
			    	$('#datagrid').datagrid('load',ly.serializeObjct($('#searchForm')));
			    }
			});  
		};
		
		
		/*删除试卷*/
		deletePaper = function() {
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
					$.post('paperDelete', {
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
		
		/*浏览试卷*/
		viewPaper = function(){
			var selectedData = $('#datagrid').datagrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			$('#viewPaper').window({
				title : '试卷浏览',
			    width : 1000,    
			    height : 600,    
			    modal : true,
			    collapsible : false,
			    minimizable : false,
				maximizable : false,
			    content : '<iframe scrolling="no" frameborder="0"  src="paperView?id=' + selectedData.id + '" style="width:100%;height:99%;"></iframe>'
			});  
		};
		
		/*导出试卷*/
		exportPaper = function(){
			var selectedData = $('#datagrid').datagrid('getSelected');
			if (!selectedData) {
				$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
				return;
			}
			$.post('exportPaper', {
				id : selectedData.id,
			}, function(result) {
				$.messager.show({
					title : '提示',
					msg : result.msg
				}); 
				if (result.success) {
					var downUrl = 'paperDownload?filepath=' + result.obj;
	                window.location = downUrl; 
				}
			}, 'JSON');
		};
		
		//根据条件查询
		searchData = function() {
			datagrid.datagrid('load', ly.serializeObjct($('#searchForm')));
			$('#datagrid').datagrid('clearSelections');
		};

		//清除查询条件
		clearCondition = function() {
			$('#searchForm').form('clear');
			datagrid.datagrid('load', {});
		};
		
	});

</script>

<!-- 主页面内容start -->
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north'" class="datagrid-toolbar">
		<fieldset>
			<legend>查询条件</legend>
			<form id="searchForm" method="post">
				试卷名称<input type="text" name="searchPaperName" />
				<a id="btn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchData();">查询</a> 
				<a id="btn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="clearCondition();">清空</a>
			</form>
		</fieldset>
	</div>
	<div data-options="region:'center'">
		<table id="datagrid"></table>
	</div>
</div>
<!-- 主页面内容end -->

<!-- 新增试卷的弹出框 -->
<div id="addPaper"></div>

<!-- 浏览试卷的弹出框 -->
<div id="viewPaper"></div>

<jsp:include page="../common/footer.jsp"/>

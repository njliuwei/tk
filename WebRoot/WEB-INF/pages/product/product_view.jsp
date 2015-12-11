<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/header.jsp" />

<script type="text/javascript">
	$(function(){
		var datagrid;
		$('#zsdTree').tree({
			url : 'getProductZsdTree?productId=${productId}',
			parentField : 'pid',
			textFiled : 'text',
			idFiled : 'id',
			onClick : function(node) {
				$('#datagrid').datagrid('load',{zsd:node.id});
			}
		});
		
		datagrid = $('#datagrid').datagrid({
			url : 'productZsdQuestionList?productId=${productId}&subject=${subject}',
			columns : [ [ {
				field : 'id',
				title : '编号',
				width : 100,
				hidden : true
			}, {
				field : 't',
				title : '题目内容',
				width : 300,
			}, {
				title : '操作',
				field : 'action',
				align : 'center',
				formatter : function(value, row, index) {
					var str = '<a class="buttonly" href="javascript:deleteQst('+row.id+');">删除</a>';
					return str;
				},
				width : 60,
			}, ] ],
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
			nowrap : false
		});
		
		//删除浏览试题
		deleteQst = function(qstId){
			$.messager.confirm('询问', '您是否要删除当前数据？', function(b) {
				if (b) {
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					});
					$.post('productViewQstDelete', {
						qstId : qstId,
						productId : ${productId}
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
		
		//题型选择项
		$('#txSelect').combobox({
			editable : false,
			panelHeight : 'auto',
			panelWidth : 60,
			width : 60,
			onSelect : function(record){
				$('#datagrid').datagrid('load',ly.serializeObjct($('#searchForm')));
			} 
		});
		
		//难度选择项
		$('#ndSelect').combobox({
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

<div data-options="region:'west'" style="width:240px;">
	<div>
		<ul id="zsdTree"></ul>
	</div>
</div>
<div data-options="region:'center',border:false">
	<!-- 主页面内容start -->
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north'" class="datagrid-toolbar">
			<fieldset>
				<legend>查询条件</legend>
				<form id="searchForm" method="post">
					题型
					<select id="txSelect" name="tx">
						<option selected="selected">全部</option>
						<option>填空题</option>
						<option>选择题</option>
						<option>计算题</option>
					</select>
					&nbsp;&nbsp;&nbsp;
					难度
					<select id="ndSelect" name="nd">
						<option selected="selected">全部</option>
						<option>易</option>
						<option>中</option>
						<option>难</option>
					</select>
				</form>
			</fieldset>
		</div>
		<div data-options="region:'center'">
			<table id="datagrid"></table>
		</div>
	</div>
	<!-- 主页面内容end -->
</div>

<jsp:include page="../common/footer.jsp" />

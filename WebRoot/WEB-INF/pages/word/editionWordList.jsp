<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:include page="../common/header.jsp"/>
<style>
	input{width:200px}
	select{width:206px}
</style>
<script type="text/javascript">
	$(function() {
		var wordgrid;
		wordgrid = $('#wordgrid').datagrid({
			title : '教材版本词库列表',
			url : 'editionWordList.do?textbookId=<%=request.getParameter("textbookId")%>',
			columns : [[
				{
					field : 'id',
					title : '编号',
					width : 100,
					hidden : true,
				},{
					field : 'wordName',
					title : '字词名称',
					width : 100
				},{
					field : 'textbookName',
					title : '教材版本名称',
					width : 100
				},]
			],
			toolbar : [ {
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					addWord();
				}
			}, '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					delWord();
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
			singleSelect: true,
			selectOnCheck: true,
			checkOnSelect: true,
			pageSize : 50,
			pageList : [50,100,150,200,250,300],
			rownumbers : true,
			rowspan : false,
			border : false,
			nowrap : false,
			fit : true,
			
		});
		
	});
	
	function addWord() {
		$('#addDialog').dialog({
			title : '添加字词',
			modal : true,
			inline : true,
			width : 600,
			height : 600,
			buttons : [ {
				text : '添加',
				handler : function() {
					var selectedData = $('#selectwordgrid').datagrid('getSelected');
					if (!selectedData) {
						$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
						return;
					}
					var wordId = selectedData.id;
					var type = $("#wordlibtype").val();
					var textbookId = '<%=request.getParameter("textbookId")%>';
					var jctxId = '<%=request.getParameter("jctxId")%>';
					type = type =='word' ? '1' : (type == 'classical_word' ? '2' : (type == 'english_word' ? '3' : 4));
					var _btn = this;
					$(_btn).linkbutton("disable");
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					}); // 显示进度条
					$.ajax({
						type: "POST",
						url: "addEditionWord.do?wordId=" +wordId + "&type=" + type + "&textbookId=" + textbookId + "&jctxId=" + jctxId,
						dataType: 'json',  
			            contentType:'application/json;charset=UTF-8', //contentType很重要  
						success : function(result) {
							$.messager.progress('close'); // 如果提交成功则隐藏进度条
							$(_btn).linkbutton("enable");
							if (result.success) {
								$('#selectwordgrid').datagrid('reload',ly.serializeObjct($('#selectqueryForm')));
								$('#selectwordgrid').datagrid('clearSelections');
								$('#wordgrid').datagrid('reload', ly.serializeObjct($('#queryForm')));
							}
							$.messager.show({
								title : '提示',
								msg : result.msg
							});
						}
					});
				}
			} ],
			onOpen : function() {
				var subject = '<%=request.getParameter("subject")%>';
				var table = 'word';
				var jctxId = '<%=request.getParameter("jctxId")%>';
				$("#wordlibtype").empty();
				if(subject == 'yw') {
					$("#wordlibtype").append("<option value='word'>现代字词库</option>");
					$("#wordlibtype").append("<option value='classical_word'>文言文字词库</option>");
				} else {
					$("#wordlibtype").append("<option value='english_word'>英文字词库</option>");
					$("#wordlibtype").append("<option value='people_and_place'>英文人名地名库</option>");
					table = 'english_word';
				}
				var selectwordgrid;
				selectwordgrid = $('#selectwordgrid').datagrid({
					title : '字词列表',
					url : 'wordList.do?jctxId=' + jctxId,
					queryParams : {
						wordlibtype:table
					},
					columns : [[
						{
							field : 'id',
							title : '编号',
							width : 100,
							hidden : true,
						},{
							field : 'name',
							title : '字词名称',
							width : 100
						},]
					],
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
					pageSize : 50,
					pageList : [50,100,150,200,250,300],
					rownumbers : true,
					rowspan : false,
					singleSelect : true,
					border : false,
					nowrap : false,
					fit : true,
					
				});
				
			}
		});
	};
		
		
	
	function delWord() {
		var selectedData = $('#wordgrid').datagrid('getSelected');
		if (!selectedData) {
			$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
			return;
		}
		$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
			if (r){    
	        	$.post('deleteEditionWord.do', {
					id : selectedData.id
				}, function(result) {
					if (result.success) {
						$('#wordgrid').datagrid('reload',ly.serializeObjct($('#queryForm')));
						$('#wordgrid').datagrid('clearSelections');
					}
					$.messager.show({
						title : '提示',
						msg : result.msg
					});
				}, 'JSON');    
		    }    
		});  
	}
	
	function queryWord() {
		$('#wordgrid').datagrid('reload',ly.serializeObjct($('#queryForm')));
	}
	
	function selectqueryWord() {
		$('#selectwordgrid').datagrid('reload',ly.serializeObjct($('#selectqueryForm')));
	}
	
</script>

<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north'" class="datagrid-toolbar">
		<fieldset>
			<legend>查询条件</legend>
			<form id="queryForm" method="post">
				<label for='wordName' style='margin-left:20px'>名称：</label><input type='text' name='wordName'/>
				<input type='button' style='margin-left:20px;width:50px' value='查询' onclick='queryWord()'/>
			</form>
		</fieldset>
	</div>
	<div data-options="region:'center'">
		<table id="wordgrid"></table>
	</div>
</div>

<div id="addDialog">
	<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north'" class="datagrid-toolbar">
		<fieldset>
			<legend>查询条件</legend>
			<form id="selectqueryForm" method="post">
				<label for='wordlibtype'>字词库：</label><select style='width:100px' name='wordlibtype' id='wordlibtype'></select>
				<label for='name' style='margin-left:20px'>字词名称：</label><input type='text' style='width:100px' name='name'/>
				<input type='button' style='margin-left:20px;width:50px' value='查询' onclick='selectqueryWord()'/>
			</form>
		</fieldset>
	</div>
	<div data-options="region:'center'">
		<table id="selectwordgrid"></table>
	</div>
	</div>
</div>

<jsp:include page="../common/footer.jsp"/>

<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:include page="../common/header.jsp"/>
<style>
	input{width:200px}
	select{width:206px}
	a{
		color : blue;
	}
	a:link{
		text-decoration:none;
	}
	a:hover{
		text-decoration:underline;
	} 
	.soundmark{
		margin-left:10px;
		color : #66B3FF;
	}
	.property{
		margin-left : 10px;
	}
	.explain{
		margin-left : 10px;
	}
	.createdtime{
		margin-left : 10px;
	}
	.container{
		margin-bottom : 10px;
		margin-top : 10px;
		background-color : #F8F8FF;
		padding-bottom : 10px;
	}
	.datagrid-row-selected{
		color : #000;
		background : #fff;
	}
	.datagrid-row-over{
		color : #000;
		background : #fff;
	}
	.update_view{
		padding-left:10px;
	}
</style>
<script type="text/javascript">
	$(function() {
		var wordgrid;
		wordgrid = $('#wordgrid').datagrid({
			title : '名称列表',
			url : 'placeNameList.do',
			columns : [[
				/*{
					field : 'id',
					title : '编号',
					width : 100,
					hidden : true,
				},{
					field : 'name',
					title : '英文名称',
					width : 100
				},{
					field : 'cname',
					title : '中文名称',
					width : 100
				},{
					field : 'type',
					title : '类型',
					width : 100
				}*/{
					field : 'explain',
					title : '字词',
					width : 100,
					formatter : function(value, row, index) {
						var result = '<div class="container"><br/><span style="margin-left:10px;margin-right:10px;font-size:30px;">' + row.name + '&nbsp;&nbsp;' + row.cname + '</span><a href="#" onclick="delWord('+row.id + ')">删除</a><br/>';
						result += '<span class="createdtime">创建时间:'+row.created +'</span><br/></div>';
						return result;
					},
					styler: function(value,row,index){
						return  'border-bottom-color:transparent';
					}
				},]
			],
			toolbar : [/* {
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					addWord();
				}
			},'-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					updateWord();
				}
			}, '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					delWord();
				}
			}, '-',*/ {
				text : '导入',
				iconCls : 'icon-import',
				handler : function() {
					uploadWord();
				}
			}, '-', {
				text : '下载模板',
				iconCls : 'icon-download',
				handler : function() {
    				var filename = encodeURI(encodeURI("resources/template/英文人名地名导入模板.xls"));
					window.open("download.do?filename=" + filename);
				}
			}],
			//加载数据成功后触发的事件
			onLoadSuccess : function(data) {
				if (data.rows.length <= 0) {
					var body = $(this).data().datagrid.dc.body2;
					body.find('table tbody').append('<tr><td width="' + body.width() + '" style="height: 25px; text-align: center;">没有数据！</td></tr>');
				}
			},
			onDblClickRow : function(index,data){
				updateWord(data.id);
			},
			fitColumns : true,
			striped : false,
			idField : 'id',
			pagination : true,
			pageSize : 50,
			pageList : [50,100,150,200,250,300],
			rownumbers : false,
			rowspan : false,
			singleSelect : true,
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
			width : 380,
			height : 200,
			buttons : [ {
				text : '添加',
				handler : function() {
					var _btn = this;
					$(_btn).linkbutton("disable");
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					}); // 显示进度条
					$('#addForm').form('submit', {
						url : 'addPlaceName',
						onSubmit : function() {
							var isValid = $(this).form('validate');
							if (!isValid) {
								$.messager.progress('close'); // 如果表单是无效的则隐藏进度条
							}
							return isValid; // 返回false终止表单提交
						},
						success : function(result) {
							$.messager.progress('close'); // 如果提交成功则隐藏进度条
							$(_btn).linkbutton("enable");
							var r = $.parseJSON(result);
							if (r.success) {
								$('#addDialog').dialog('close');
								$('#wordgrid').datagrid('reload', ly.serializeObjct($('#queryForm')));
								$('#wordgrid').datagrid('clearSelections');
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
	function updateWord(id) {
		$('#updateDialog').dialog({
			title : '修改属性',
			modal : true,
			inline : true,
			width : 800,
			height : 500,
			onOpen : function() {
				$.post('getPlaceNameInfo.do', {
					id : id
				}, function(result) {
					if (result.success) {
						var obj = result.obj.list[0];
						$("#updateDialog #lname").html(obj.name);
						$("#updateDialog #rname").html(obj.name);
						$("#updateDialog #creator").html(obj.creator);
						$("#updateDialog #created").html(obj.created);
						$("#updateDialog #lastmodified").html(obj.lastModified);
						$("#updateDialog #cname").html(obj.cname);
						$("#yxcontents").html('');
						$("#jctxContents").html('');
						
						$.each(result.obj.sps,function(index,ele) {
							$("#jctxContents").append('<label style="color:#808080">' + ele.name + ':</label>');
							$.each(result.obj.ws,function(index,element) {
								if(ele.name == element.name) {
									$("#jctxContents").append('<span>' + element.type + '</span>;');
								}
							});
							$("#jctxContents").append('<br/>');
						});
						
					}
				}, 'JSON');  
			}
		});
	};
	
	function delWord(id) {
		$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
			if (r){    
	        	$.post('delPlaceName.do', {
					id : id
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
	
	function updateBasic() {
		$('#updBasicDialog').dialog({
			title : '基本信息修改',
			modal : true,
			inline : true,
			width : 380,
			height : 210,
			buttons : [ {
				text : '确定',
				handler : function() {
					var _btn = this;
					$(_btn).linkbutton("disable");
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					}); // 显示进度条
					$('#basicForm').form('submit', {
						url : 'updatePlaceName',
						onSubmit : function() {
							
						},
						success : function(result) {
							$.messager.progress('close'); // 如果提交成功则隐藏进度条
							$(_btn).linkbutton("enable");
							var r = $.parseJSON(result);
							if (r.success) {
								$('#updBasicDialog').dialog('close');
								$('#wordgrid').datagrid('reload',ly.serializeObjct($('#queryForm')));
								$('#wordgrid').datagrid('clearSelections');
								$('#basiccontents #rname').html($("#basicForm input[name='name']").val());
								$('#basiccontents #cname').html($("#basicForm input[name='cname']").val());
								$('#basiccontents #type').html($("#basicForm input[name='type']").val());
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
				$('#basicForm').form('clear');
				var selectedData = $('#wordgrid').datagrid('getSelected');
				$('#basicForm').form('load','getPlaceNameBasic?id=' + selectedData.id);
			}
		});
	}
	
	function queryWord() {
		$('#wordgrid').datagrid('reload',ly.serializeObjct($('#queryForm')));
		$('#wordgrid').datagrid('clearSelections');
	}
	
	function uploadWord() {
		$('#uploadDialog').dialog({
			title : '上传文件',
			modal : true,
			inline : true,
			width : 380,
			height : 120,
			buttons : [ {
				text : '确定',
				handler : function() {
					var _btn = this;
					$(_btn).linkbutton("disable");
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					}); // 显示进度条
					$('#uploadForm').form('submit', {
						url : 'uploadPlaceName',
						onSubmit : function() {
							var filename = $('#uploadForm input[type="file"]').val();
							var isValid = /\.(xls|xlsx|XLS|XLSX)$/.test(filename);
							if (!isValid) {
								$.messager.progress('close'); // 如果表单是无效的则隐藏进度条
								$.messager.alert('警告', '请选择excel文件！', 'warning');
							}
							return isValid; // 返回false终止表单提交
						},
						success : function(result) {
							$.messager.progress('close'); // 如果提交成功则隐藏进度条
							$(_btn).linkbutton("enable");
							var r = $.parseJSON(result);
							if (r.success) {
								$('#uploadDialog').dialog('close');
								$('#wordgrid').datagrid('reload',ly.serializeObjct($('#queryForm')));
								$('#wordgrid').datagrid('clearSelections');
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
				$('#uploadForm').form('clear');
			}
		});
	}
</script>

<div class="easyui-layout" data-options="fit:true">
<div data-options="region:'north'" class="datagrid-toolbar">
		<fieldset>
			<legend>查询条件</legend>
			<form id="queryForm" method="post">
				<label for='name'>英文名称：</label><input type='text' name='name'/>
				<label for='cname' style='margin-left:20px'>中文名称：</label><input type='text' name='cname'/>
				<input type='button' style='margin-left:20px;width:50px' value='查询' onclick='queryWord()'/>
			</form>
		</fieldset>
	</div>
	<div data-options="region:'center'">
		<table id="wordgrid"></table>
	</div>
</div>

<div id="addDialog">
	<fieldset style="margin-top: 5px;padding-left:40px">
		<legend>
			<b><i>基本信息</i></b>
		</legend>
		<form id="addForm" method="post">
			<table>
				<tr>
					<td>英文名称</td>
					<td>
						<input class="easyui-validatebox" name="name" data-options="required:true,validType:['length[1,200]']"/>
					</td>
				</tr>
				<tr>
					<td>类型</td>
					<td>
						<select name="type" class="easyui-validatebox" data-options="required:true">
							<option selected="selected" value=''>--请选择--</option>
							<option>人名</option>
							<option>地名</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>中文名称</td>
					<td>
						<input class="easyui-validatebox" name="cname" data-options="required:true,validType:['length[1,200]','Hanzi']"/>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>

<div id="updateDialog">
	<div class="easyui-layout" data-options="fit:true">   
    	<div data-options="region:'west'" style="width:300px">
    		<div class="easyui-layout" data-options="fit:true">   
	            <div data-options="region:'north'" style="height:180px;line-height:30px;">
	            	<label class='update_view'></label><span style='font-size:30px;' id='lname'></span><br/>
	            	<label class='update_view'>入库人：</label><span id='creator'></span><br/>
	            	<label class='update_view'>创建时间：</label><span id='created'></span><br/>
	            	<label class='update_view'>最后修改时间：</label><span id='lastmodified'></span><br/>
	            </div>   
	            <div data-options="region:'center'" id="jctxContents">
				</div>   
	        </div>   
    	</div>   
	    <div data-options="region:'center'">   
	        <div class="easyui-layout" data-options="fit:true">   
	            <div id='basiccontents' data-options="region:'center'" style="line-height:20px;">
	            	<div style="background-color:#F8F8FF;height:30px;">
	            		<span style="display:inline-block;padding-top:5px;">基本属性</span>
	            		<span style="display:inline-block;float:right;"><a id="btn" href="#" class="easyui-linkbutton" onclick="updateBasic()" data-options="iconCls:'icon-edit'">编辑</a></span>  
	            	</div>
	            	<label class='update_view'>英文名称：</label><span id='rname'></span><br/>
	            	<label class='update_view'>中文名称：</label><span id='cname'></span><br/>
	            </div>   
	        </div>  
	    </div>   
	</div>  
</div>
<div id="uploadDialog">
	<form id="uploadForm" method="post" style="position: relative;padding:15px"enctype="multipart/form-data">
	   <label>选择文件：</label>
       <input type="text" readOnly="readOnly" name="txt" style="position:absolute;width:170px;background-color:Silver"/>
       <button style="position: absolute;top:15px;left:256px;width:52px;height:26px;">浏览</button>
       <input type="file" name="file" style='opacity:0;background:green;width:230px' onchange="txt.value=this.value" data-options="required:true,validType:'length[1,5000]'"/>
	</form>
</div>

<div id="updBasicDialog">
	<fieldset style="margin-top: 5px;padding-left:40px">
		<legend>
			<b><i>基本信息</i></b>
		</legend>
		<form id="basicForm" method="post">
			<table>
				<tr>
					<td>英文名称</td>
					<td>
						<input class="easyui-validatebox" disabled='disabled' name="name" data-options="required:true,validType:['length[1,500]']"/>
						<input type='hidden' name='id'/>
					</td>
				</tr>
				<tr>
					<td>类型</td>
					<td>
						<select name="type" class="easyui-validatebox" data-options="required:true">
							<option selected="selected" value=''>--请选择--</option>
							<option>人名</option>
							<option>地名</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>中文名称</td>
					<td>
						<input type="text" id='comparison' name="cname" class="easyui-validatebox" data-options="required:true,validType:['length[1,500]']"/>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
<jsp:include page="../common/footer.jsp"/>

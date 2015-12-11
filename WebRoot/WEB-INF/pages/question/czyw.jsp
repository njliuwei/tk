<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:include page="../common/header.jsp"/>
<script type="text/javascript">
	$(function() {
		var czsxQstDatagrid;
		czsxQstDatagrid = $('#czsxQstDatagrid').datagrid({
			title : '题目列表',
			url : 'czywList.do?code=<%=request.getParameter("code")%>&subject=<%=request.getParameter("subject")%>',
			columns : [ [ {
				field : 'id',
				title : '编号323',
				width : 100,
				hidden : true
			}, {
				field : 't',
				title : '题目',
				width : 200,
				formatter : function(value, row, index) {
					return '<br/>IID：' + row.id + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;知识点名称 ：' + row.zsdmc + '<br/>题型：'+ row.tx + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;难度:' + row.nd + '<br/>' + row.t + '<br/><br/>';
				} 
			}, ] ],
			//加载数据成功后触发的事件
			onLoadSuccess : function(data) {
				if (data.rows.length <= 0) {
					var body = $(this).data().datagrid.dc.body2;
					body.find('table tbody').append('<tr><td width="' + body.width() + '" style="height: 25px; text-align: center;">没有数据！</td></tr>');
				}
			},
			onDblClickRow : function(index,row) {
				$('#questionViewWin').window({
					onOpen : function() {
						$('#questionInfo').empty().append(row.t);
						$.ajax({
						   type: "POST",
						   url: "getQuestionAdditionInfo.do",
						   data: "id=" + row.id + "&subject=<%=request.getParameter("subject")%>",
						   success: function(data){
						   		//加载试题标引内容
								if(data && data.props) {
									$("#questionIndexForm div:gt(0)").remove();
									var zclx = '<%=request.getParameter("zclx")%>';
									var propSels = data.propSels;
									var question = data.question;
									for(var i = 0; i < data.props.length; i++) {
										var prop = data.props[i];
										var value = question[prop.col] ? question[prop.col] : '';
										var newdiv = "<div style='margin:5px'><label style='display:inline-block;width:80px;text-align:right;' for='"+prop.col+"'>"+prop.name+"：</label>";
										if(prop.type === '下拉' || prop.type === '单选') {
											newdiv += "<select name='"+prop.col+"' id='"+prop.col+"' style='width:130px;' value='" + value + "'>";
											newdiv += "<option value=''>--请选择--</option>";
											for(var m = 0; m < propSels.length; m++) {
												var sel = propSels[m];
												if(sel.col === prop.col) {
													newdiv += "<option value='" + sel.id + "'";
													if(sel.id == value) {
														newdiv += "selected='true'";
													}
													newdiv += ">" + sel.name + "</option>";
												}
											}
											newdiv += "</select>";
										}
										
										if(prop.type === '复选') {
											var multiValues = value.split(",");
											for(var m = 0; m < propSels.length; m++) {
												var sel = propSels[m];
												if(sel.col === prop.col) {
													newdiv += "<input type='checkbox' name='"+prop.col+"' value='" + sel.id + "'";
													for(var n = 0; n < multiValues.length; n++) {
														if(sel.id == multiValues[n]) {
															newdiv += "checked='checked'";
														}
													}
													newdiv += "/>" + sel.name;
												}
											}
										}
										
										if(prop.type === '树形') {
											newdiv += "<input name='"+prop.col+"' id='"+prop.col+"' type='hidden' value='"+value +"'/>";
											newdiv += "<input disabled='disabled' name='"+prop.col+"mc' id='"+prop.col+"mc' type='text' style='width:130px;' ";
											newdiv += "readonly='readonly' ";
											var names = [];
											var multiValues = value.split(",");
											for(var m = 0; m < propSels.length; m++) {
												var sel = propSels[m];
												if(sel.col == prop.col) {
													for(var n = 0; n < multiValues.length; n++) {
														if(sel.id == multiValues[n]) {
															names.push(sel.name);														
														}
													}
												}
											}
											newdiv += "value='" + names.join(',') + "' ";
											newdiv += "/>";
											newdiv += "<input type='button' onclick='openTree("+prop.id  +",\" " + prop.name +"\",\"" + prop.col +"\",\"#questionIndexForm\")' value='选择'/>";//+","+prop.name +
										}
										if(prop.type === '文本') {
											newdiv += "<input name='"+prop.col+"' id='"+prop.col+"' type='text' style='width:130px;' value='" + value + "'/> ";
										}
										newdiv += "</div>";
										$("#questionIndexForm").append(newdiv);
									}
									if(zclx == 'yw' || zclx == 'yy') {
										var zcValue = question['zc'] ? question['zc'] : '';
										var zcdiv = "<div style='margin:5px'><label style='display:inline-block;width:80px;text-align:right;' for='zc'>字词：</label>";
										zcdiv += "<input name='zc' id='zc' style='width:130px;' value='" + zcValue + "'/>";
										zcdiv += "<input type='button' onclick='openZcSel(\"" +zclx + "\")' value='选择'/></div>";
										$("#questionIndexForm").append(zcdiv);
									}
									$("#questionIndexForm").append("<div><input type='hidden' name='id' value='" + row.id + "'/></div>");
									$("#questionIndexForm").append("<div><input type='hidden' name='xk' value='" + row.xk + "'/></div>");
									$("#questionIndexForm").append("<div style='margin:5px;text-align:center;'><input type='button' onclick='saveIndex()' value='保存'/><input style='margin-left:10px' type='button' onclick='javascript:closeWindow()' value='关闭'/></div>");
								}
								
								//加载解析标引内容
								$("#questionAnalyses").empty();
								if(data && data.analyses && data.analyses.length) {
									var analysisSels = data.analysisSels;
									for(var i = 0; i < data.analyses.length; i++) {
										var analysis = data.analyses[i];
										var fa = analysis.fa ? analysis.fa : '';
										var lx = analysis.lx ? analysis.lx : '';
										var div = "<div style='background-color:DarkGray;height:18px;'><div style='display:block;float:right'><input type='button' value='标引' onclick='toIndexAnalysis("+row.id+","+JSON.stringify(analysis)+","+JSON.stringify(data.analysesIndex) +","+JSON.stringify(data.analysisSels) +")'/></div></div><div style='background-color:DarkGray;padding:10px;word-wrap:break-word'>";
										if(data.analysesIndex && data.analysesIndex.length) {
											for(var j = 0; j < data.analysesIndex.length; j++) {
												var analysisIndex = data.analysesIndex[j];
												var value = analysis[analysisIndex.col] ? analysis[analysisIndex.col] : '';
												value = value.split(',');
												var labelValue = [];
												for(var m = 0; m < value.length; m++) {
													for(var n = 0; n < analysisSels.length; n++) {
														var sel = analysisSels[n];
														if(analysisIndex.col == sel.col && value[m] == sel.id) {
															labelValue.push(sel.name);
														}
													}
												}
												value = analysisIndex.type === '文本' ? value.join(",") : labelValue.join(",");
												div += "<label style='margin-left:10px'>" + analysisIndex.name + "：</label><label id='" + analysisIndex.col +analysis.id+ "'>" + value + "</label>";
											}
										}
										div += "</div><div>"+analysis.nr+"</div>";
										$("#questionAnalyses").append(div);
									}
								}
						   }
						});
					}
				});
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
		
		
	});
	
	/**
	 * 打开树型标引属性选择界面
	 * @param id 标引属性ID
	 * @param name 标引属性名称
	 * @param col 对应数据库字段
	 * @param which 选择树节点后，赋值到哪
	 * @returns
	 */
	function openTree(id,name,col,which) {
		$("#treeDialog").dialog({
			title : name + "选择树",
			width : 300,
			height : 500,
			modal : true,
			onOpen : function() {
				$("#reverse").val(col);
				$("#which").val(which);
				$('#questionTreeValue').tree({
					parentField : 'pid',
					textFiled : 'text',
					idFiled : 'id',
					url : 'getQuestionTreeValue.do?id='+id,// + '&values=' + $("#" + col).val(),
					checkbox : true,
					onBeforeLoad : function(node,param) {
						param.values=$(which + " #" + col).val();
					},
					onLoadSuccess : function() {
						var nodes = $('#questionTreeValue').tree('getChecked');
						if(nodes && nodes.length) {
							for(var i = 0; i < nodes.length; i++) {
								var node = nodes[i];
								$('#questionTreeValue').tree('expandTo',node.target);
							}
						}
					
					}
				});
			}
		});
	}
	
	function closeWindow() {
		$('#questionViewWin').window('close');
	}
	
	/**
	 * 保存试题标引信息
	 * @param 
	 * @returns
	 */
	function saveIndex() {
		var data = $("#questionIndexForm").serializeArray();
		data = convertToJson(data);
		$.ajax({
			type: "POST",
			url: "saveQuestionIndex.do",
			data: JSON.stringify(data),
			dataType: 'json',  
            contentType:'application/json;charset=UTF-8', //contentType很重要  
			success: function(data){
				alert(data.msg);
				if(data.result === 'success') {
					$('#czsxQstDatagrid').datagrid("reload");
					$('#questionViewWin').window("close");
				}
			}
		});
	}
	
	/**
	 * 将表单对象转为json对象
	 * @param formValues
	 * @returns
	 */
	function convertToJson(formValues) {
	    var result = {};
	    for(var formValue,j=0;j<formValues.length;j++) {
		    formValue = formValues[j];
		    var name = formValue.name;
		    var value = formValue.value;
		    if (name.indexOf('.') < 0) {
		    	if(result[name]) {
		    		result[name] = result[name] + "," + value;
		    	} else {
			    	result[name] = value;
			    }
			    continue;
		    } else {
			    var simpleNames = name.split('.');
			    // 构建命名空间
			    var obj = result;
			    for ( var i = 0; i < simpleNames.length - 1; i++) {
			    	var simpleName = simpleNames[i];
				    if (simpleName.indexOf('[') < 0) {
					    if (obj[simpleName] == null) {
					    	obj[simpleName] = {};
					    }
				    	obj = obj[simpleName];
				    } else { // 数组
					    // 分隔
					    var arrNames = simpleName.split('[');
					    var arrName = arrNames[0];
					    var arrIndex = parseInt(arrNames[1]);
					    if (obj[arrName] == null) {
					    	obj[arrName] = []; // new Array();
					    }
					    obj = obj[arrName];
					    multiChooseArray = result[arrName];
					    if (obj[arrIndex] == null) {
					    	obj[arrIndex] = {}; // new Object();
					    }
				    	obj = obj[arrIndex];
				    }
			    }
			 
			    if(obj[simpleNames[simpleNames.length - 1]] ) {
				    var temp = obj[simpleNames[simpleNames.length - 1]];
				    obj[simpleNames[simpleNames.length - 1]] = temp;
			    }else {
			    	obj[simpleNames[simpleNames.length - 1]] = value;
			    }
		 
		    }
	    }
	    return result;
	}
	
	/**
	 * 选中树节点事件处理
	 */
	function chooseTreeNode() {
		var nodes = $('#questionTreeValue').tree("getChecked");
		if(nodes.length == 0) {
			$.messager.alert('警告','请选择试题属性');    
			return;
		}
		var ids = [];
		var mcs = [];
		for(var i = 0; i < nodes.length; i++) {
			//if(!$('#questionTreeValue').tree("isLeaf",nodes[i].target)) {
			//	$.messager.alert('警告','请选择叶子节点');
			//	return;    
			//}
			//if(nodes[i].attributes) {
			//	ids.push(nodes[i].attributes.url);
			//} else {
			//	ids.push(nodes[i].text);
			//}
			ids.push(nodes[i].id);
			mcs.push(nodes[i].text);
		}
		var which = $("#which").val();
		var idfield = "#" + $("#reverse").val();
		var mcfield = "#" + $("#reverse").val() + "mc";
		$(which + " " + idfield).val(ids.join(","));
		$(which + " " + mcfield).val(mcs.join(","));
		$("#treeDialog").dialog("close");
	}
	
	/**
	 * 打开试题解析标引页面
	 * @parma id 试题ID
	 * @parma analysis 试题解析
	 * @parma indexs 试题解析标引属性
	 * @parma analysisSels 标引属性选项可选值
	 */
	function toIndexAnalysis(id,analysis,indexs,propSels) {
		$("#analysisViewWin").window({
			onOpen : function() {
				$("#analysisIndexForm div:gt(0)").remove();
				$("#analysisIndexForm input[name='st_id'").val(id);
				$("#analysisIndexForm input[name='id'").val(analysis.id);
				if(indexs && indexs.length) {
					for(var j = 0; j < indexs.length; j++) {
						var prop = indexs[j];
						var value = analysis[prop.col] ? analysis[prop.col] : '';
						var newdiv = "<div style='margin:5px'><label style='display:inline-block;width:80px;text-align:right;' for='"+prop.col+"'>"+prop.name+"：</label>";
						if(prop.type === '下拉' || prop.type === '单选') {
							newdiv += "<select name='"+prop.col+"' id='"+prop.col+"' style='width:130px;' value='" + value + "'>";
							newdiv += "<option value=''>--请选择--</option>";
							for(var m = 0; m < propSels.length; m++) {
								var sel = propSels[m];
								if(sel.col === prop.col) {
									newdiv += "<option value='" + sel.id + "'";
									if(sel.id == value) {
										newdiv += "selected='true'";
									}
									newdiv += ">" + sel.name + "</option>";
								}
							}
							newdiv += "</select>";
						}
						
						if(prop.type === '复选') {
							var multiValues = value.split(",");
							for(var m = 0; m < propSels.length; m++) {
								var sel = propSels[m];
								if(sel.col === prop.col) {
									newdiv += "<input type='checkbox' name='"+prop.col+"' value='" + sel.id + "'";
									for(var n = 0; n < multiValues.length; n++) {
										if(sel.id == multiValues[n]) {
											newdiv += "checked='checked'";
										}
									}
									newdiv += "/><label>" + sel.name + "</label>";
								}
							}
						}
						
						if(prop.type === '树形') {
							newdiv += "<input name='"+prop.col+"' id='"+prop.col+"' type='hidden' value='"+value +"'/>";
							newdiv += "<input disabled='disabled' name='"+prop.col+"mc' id='"+prop.col+"mc' type='text' style='width:130px;' ";
							newdiv += "readonly='readonly' ";
							var names = [];
							var multiValues = value.split(",");
							for(var m = 0; m < propSels.length; m++) {
								var sel = propSels[m];
								if(sel.col == prop.col) {
									for(var n = 0; n < multiValues.length; n++) {
										if(sel.id == multiValues[n]) {
											names.push(sel.name);														
										}
									}
								}
							}
							newdiv += "value='" + names.join(',') + "' ";
							newdiv += "/>";
							newdiv += "<input type='button' onclick='openTree("+prop.id  +",\" " + prop.name +"\",\"" + prop.col +"\",\"#analysisIndexForm\")' value='选择'/>";//+","+prop.name +
						}
						if(prop.type === '文本') {
							newdiv += "<input name='"+prop.col+"' id='"+prop.col+"' type='text' style='width:130px;' value='" + value + "'/> ";
						}
						newdiv += "</div>";
						$("#analysisIndexForm").append(newdiv);
					}
				}
				$("#analysisIndexForm").append("<div style='margin:5px;text-align:center;'><input type='button' onclick='saveAnalysisIndex("+analysis.id+")' value='保存'/></div>");
			}
		});
	}
	
	/**
	 * 保存试题解析标引
	 * @parma id 解析ID
	 */
	function saveAnalysisIndex(id) {
		$("#analysisIndexForm").form('submit',{
			url : 'saveAnalysisIndex.do',
			onSubmit : function() {
				//CHECK
			},
			success : function(data) {
				data = $.parseJSON(data);
				alert(data.msg);
				if(data.result === 'success') {
					$('#czsxQstDatagrid').datagrid("reload");
					$('#analysisViewWin').window("close");
					var fields = ["fa","lx","property1","property2","property3","property4","property5"];
					for(var i = 0; i < fields.length; i++) {
						var field = fields[i];
						var mcValue = $("#analysisIndexForm input:text[name='"+field +"mc']").val();
						var textValue = $("#analysisIndexForm input:text[name='"+field +"']").val();
						var selValue = $("#analysisIndexForm select[name='"+field +"']").find("option:selected").text();
						var checkValue = [];
						$("#analysisIndexForm input:checkbox[name='"+field +"']:checked").each(function(){
							checkValue.push($(this).next().html());
						});
						var value = mcValue ? mcValue : (checkValue.length > 0 ? checkValue.join(",") : (selValue ? selValue : (textValue ? textValue : '')));
						$("#" + field + id).html(value);
					}
				}
			}
		});
		
	}
	
	function queryQuestion() {
		$('#czsxQstDatagrid').datagrid('reload',ly.serializeObjct($('#queryForm')));
	}
	
	function openZcSel(zclx) {
		$('#addDialog').dialog({
			title : '添加字词',
			modal : true,
			inline : true,
			width : 600,
			height : 600,
			buttons : [ {
				text : '添加',
				handler : function() {
					var selectedData = $('#selectwordgrid').datagrid('getChecked');
					if (!selectedData) {
						$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
						return;
					}
					var names = [];
					$(selectedData).each(function(){
						names.push(this.name);
					});
					$("#questionIndexForm #zc").val(names.join(','));
					$('#addDialog').dialog('close');
				}
			} ],
			onOpen : function() {
				var url = 'modenWordList';
				$("#wordlibtype").empty();
				if(zclx == 'yw') {
					$("#wordlibtype").append("<option value='modenWordList'>现代字词库</option>");
					$("#wordlibtype").append("<option value='classicalWordList'>文言文字词库</option>");
				} else {
					$("#wordlibtype").append("<option value='englishWordList'>英文字词库</option>");
					$("#wordlibtype").append("<option value='placeNameList'>英文人名地名库</option>");
					url = 'englishWordList';
				}
				var selectwordgrid;
				selectwordgrid = $('#selectwordgrid').datagrid({
					title : '字词列表',
					url : url,
					columns : [[{ field:'ck',checkbox:true },
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
					singleSelect : false,
					border : false,
					nowrap : false,
					fit : true,
					
				});
				
			}
		});
	}
	
	function selectqueryWord() {
		var url = $("#wordlibtype").val();
		var name = $("#wordname").val();
		$('#selectwordgrid').datagrid({
			title : '字词列表',
			url : url,
			queryParams : {name : name},
			columns : [[{ field:'ck',checkbox:true },
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
			singleSelect : false,
			border : false,
			nowrap : false,
			fit : true,
			
		});
	}
</script>

<div class="easyui-layout" data-options="fit:true">
<div data-options="region:'north'" class="datagrid-toolbar">
		<fieldset>
			<legend>查询条件</legend>
			<form id="queryForm" method="post">
				<label>难度：</label>
				<input id="cc" class="easyui-combobox" name="nd" data-options="valueField:'col',textField:'name',url:'getOptions.do?type=nd&subject=<%=request.getParameter("subject") %>'" /> 
				<label style='margin-left:15px;'>题型：</label>
				<input id="cc" class="easyui-combobox" name="tx" data-options="valueField:'col',textField:'name',url:'getOptions.do?type=tx&subject=<%=request.getParameter("subject") %>'" />
				<a href="javascript:void(0)" class="easyui-linkbutton l-btn" id="queryBtn" onclick='queryQuestion()'>
			    <span class="l-btn-left"><span class="l-btn-text icon-search l-btn-icon-left">查询</span></span>
			    </a>
			</form>
		</fieldset>
	</div>
	<div data-options="region:'center'">
		<table id="czsxQstDatagrid"></table>
	</div>
</div>

<div id="questionViewWin" title="试题标引" style="width:740px;height:520px" data-options="modal:true">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center'">
			<div class="easyui-layout" data-options="fit:true">
				<div id="questionInfo" data-options="region:'north'" style="height:350px">
					
				</div>
				<div id="questionAnalyses" data-options="region:'center'">
					
				</div>
			</div>
		</div>
		<div id="QuestionIndexes" data-options="region:'east'" style="width:320px;padding:10px;background-color:Azure">
			<form id='questionIndexForm' action="saveQuestionIndex.do" method='post'>
				<div></div>
			</form>
		</div>
	</div>
</div>

<div id="analysisViewWin" title="解析标引" style="width:350px;height:500px;padding-left:10px" data-options="modal:true">
	<form id='analysisIndexForm' action="saveAnalysisIndex.do" method='post'>
		<div>
			<input type='hidden' name='st_id'/>
			<input type='hidden' name='id'/>
			<input type='hidden' name='subject' value='<%=request.getParameter("subject")%>'/>
		</div>
	</form>
</div>

<div id="treeDialog">
	<div>
		<input type="button" style='position: absolute; right: 5px;' value="保存" onclick="chooseTreeNode()"/>
		<input type="hidden" id="reverse"/>
		<input type="hidden" id="which"/>
	</div>
	<ul id="questionTreeValue" class="easyui-tree"></ul>
</div>

<div id="addDialog">
	<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north'" class="datagrid-toolbar">
		<fieldset>
			<legend>查询条件</legend>
			<form id="selectqueryForm" method="post">
				<label for='wordlibtype'>字词库：</label><select style='width:100px' name='wordlibtype' id='wordlibtype'></select>
				<label for='name' style='margin-left:20px'>字词名称：</label><input type='text' style='width:100px' name='name' id='wordname'/>
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

<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:include page="common/header.jsp"/>

<script type="text/javascript">
	var subjectMenuTree;
	var systemMenuTree;
	var questionMenuTree;
	var wordMenuTree;
	var productMenuTree;
	var paperMenuTree;
	var centerTabs;

	$(function() {
		//左侧的试题管理
		questionMenuTree = $('#questionMenuTree').tree({
			url : 'menuTree?type=1',
			parentField : 'pid',
			textFiled : 'text',
			idFiled : 'id',
			onClick : function(node) {
				if (node.attributes.url) {
					var rootNode = node;
					while($('#questionMenuTree').tree("getParent",rootNode.target)) {
						rootNode = $('#questionMenuTree').tree("getParent",rootNode.target);
					} 
					var zclx = '';//字词用的是英语字词库还是语文字词库
					if(rootNode.text.match(/英语$/)) {
						zclx = 'yy';
					} else if(rootNode.text.match(/语文$/)) {
						zclx = 'yw';
					}
					
					if ($('#centerTabs').tabs('exists', "试题_" + node.text)){//如果tab已经存在,则选中并刷新该tab  
				        $('#centerTabs').tabs('select', "试题_" + node.text);
				        refreshTab({tabTitle:"试题_" + node.text,url:'toQuestionList.do?code='+ node.id + '&subject=' + node.attributes.title + '&zclx=' + zclx});  
				    } else {
			  		// 获取选择的面板
						$('#centerTabs').tabs('add', {
							title: "试题_" + node.text,
							closable: true,
							content: '<iframe scrolling="no" frameborder="0"  src="toQuestionList.do?code='+node.id+'&subject=' +node.attributes.title + '&zclx=' + zclx + '" style="width:100%;height:99.5%;"></iframe>', // 新内容的URL
							tools : [ {
								iconCls : 'icon-mini-refresh',
								handler : function() {
									var selectedTab = $('#centerTabs').tabs('getSelected');
									selectedTab.panel('refresh');
								}
							} ]
						});
					}
				}
			}
		});
		
		//左侧的产品管理
		productMenuTree = $('#productMenuTree').tree({
			url : 'menuTree?type=2',
			parentField : 'pid',
			textFiled : 'text',
			idFiled : 'id',
			onClick : function(node) {
				if (node.attributes.url) {
					if ($('#centerTabs').tabs('exists', "产品_" + node.text)){//如果tab已经存在,则选中并刷新该tab  
				        $('#centerTabs').tabs('select',"产品_" + node.text);
				        refreshTab({tabTitle:"产品_" +node.text,url:node.attributes.url});  
				    } else {
			  		// 获取选择的面板
						$('#centerTabs').tabs('add', {
							title: "产品_" + node.text,
							closable: true,
							content: '<iframe scrolling="no" frameborder="0"  src="'+node.attributes.url+'" style="width:100%;height:99.5%;"></iframe>', // 新内容的URL
							tools : [ {
								iconCls : 'icon-mini-refresh',
								handler : function() {
									var selectedTab = $('#centerTabs').tabs('getSelected');
									selectedTab.panel('refresh');
								}
							} ]
						});
					}
				}
			}
		});
		
		
		//左侧的试卷管理
		paperMenuTree = $('#paperMenuTree').tree({
			url : 'menuTree?type=4',
			parentField : 'pid',
			textFiled : 'text',
			idFiled : 'id',
			onClick : function(node) {
				if (node.attributes.url) {
					if ($('#centerTabs').tabs('exists', "试卷_" + node.text)){//如果tab已经存在,则选中并刷新该tab  
				        $('#centerTabs').tabs('select',"试卷_" + node.text);
				        refreshTab({tabTitle:"试卷_" +node.text,url:node.attributes.url});  
				    } else {
			  		// 获取选择的面板
						$('#centerTabs').tabs('add', {
							title: "试卷_" + node.text,
							closable: true,
							content: '<iframe scrolling="no" frameborder="0"  src="'+node.attributes.url+'" style="width:100%;height:99.5%;"></iframe>', // 新内容的URL
							tools : [ {
								iconCls : 'icon-mini-refresh',
								handler : function() {
									var selectedTab = $('#centerTabs').tabs('getSelected');
									selectedTab.panel('refresh');
								}
							} ]
						});
					}
				}
			}
		});
		
		//左侧的学科管理
		subjectMenuTree = $('#subjectMenuTree').tree({
			url : 'menuTree?type=3',
			parentField : 'pid',
			textFiled : 'text',
			idFiled : 'id',
			onClick : function(node) {
				if (node.attributes.url) {
					var url;
					if(node.attributes.type == '文本'){
						$.messager.alert('警告', '该属性类型为文本，不能添加属性值！', 'warning');
						return;
					}else if(node.attributes.type == '树形'){
						url = node.attributes.url + '&type=树形';
					}else{
						url = node.attributes.url;
					}
					if ($('#centerTabs').tabs('exists', node.attributes.title + "_" + node.text)){//如果tab已经存在,则选中并刷新该tab  
				        $('#centerTabs').tabs('select', node.attributes.title  + "_" + node.text);
				    } else {
			  		// 获取选择的面板
						$('#centerTabs').tabs('add', {
							title: node.attributes.title  + "_" + node.text,
							closable: true,
							content: '<iframe scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:99.5%;"></iframe>', // 新内容的URL
							tools : [ {
								iconCls : 'icon-mini-refresh',
								handler : function() {
									var selectedTab = $('#centerTabs').tabs('getSelected');
									selectedTab.panel('refresh');
								}
							} ]
						});
					}
				}
			}
		});
		
		//左侧的系统管理
		systemMenuTree = $('#systemMenuTree').tree({
			url : 'menuTree?type=4',
			parentField : 'pid',
			textFiled : 'text',
			idFiled : 'id',
			onClick : function(node) {
				if (node.attributes.url) {
					if ($('#centerTabs').tabs('exists', node.text)){//如果tab已经存在,则选中并刷新该tab  
				        $('#centerTabs').tabs('select', node.text);
				        //refreshTab({tabTitle:node.text,url:node.attributes.url});  
				    } else {
			  		// 获取选择的面板
						$('#centerTabs').tabs('add', {
							title: node.text,
							closable: true,
							content: '<iframe scrolling="no" frameborder="0"  src="'+node.attributes.url+'" style="width:100%;height:99.5%;"></iframe>', // 新内容的URL
							tools : [ {
								iconCls : 'icon-mini-refresh',
								handler : function() {
									var selectedTab = $('#centerTabs').tabs('getSelected');
									selectedTab.panel('refresh');
								}
							} ]
						});
					}
				}
			}
		});
		
		//左侧的字词管理
		wordMenuTree = $('#wordMenuTree').tree({
			url : 'menuTree?type=6',
			parentField : 'pid',
			textFiled : 'text',
			idFiled : 'id',
			onClick : function(node) {
				if (node.attributes && node.attributes.url) {
					if ($('#centerTabs').tabs('exists', "字词_" + node.text)){//如果tab已经存在,则选中并刷新该tab  
				        $('#centerTabs').tabs('select', "字词_" + node.text);
				        refreshTab({tabTitle:"字词_" + node.text,url:node.attributes.url});  
				    } else {
			  		// 获取选择的面板
						$('#centerTabs').tabs('add', {
							title: "字词_" + node.text,
							closable: true,
							content: '<iframe scrolling="no" frameborder="0"  src="' + node.attributes.url + '" style="width:100%;height:99.5%;"></iframe>', // 新内容的URL
							tools : [ {
								iconCls : 'icon-mini-refresh',
								handler : function() {
									var selectedTab = $('#centerTabs').tabs('getSelected');
									selectedTab.panel('refresh');
								}
							} ]
						});
					}
				}
				if(node.attributes && node.attributes.title) {
					if($('#wordMenuTree').tree("getParent",$('#wordMenuTree').tree("getParent",node.target).target)) {
						var jctxNode = node;
						while($('#wordMenuTree').tree("getParent",$('#wordMenuTree').tree("getParent",$('#wordMenuTree').tree("getParent",jctxNode.target).target).target)) {
							jctxNode = $('#wordMenuTree').tree("getParent",jctxNode.target);
						}
						var subNode = $('#wordMenuTree').tree("getParent",jctxNode.target);
						var subject = 'yw';
						if(subNode.text.match(/英语$/)) {
							subject = 'yy';
						}
						if ($('#centerTabs').tabs('exists', "字词_" + node.attributes.title)){//如果tab已经存在,则选中并刷新该tab  
					        $('#centerTabs').tabs('select', "字词_" + node.attributes.title);
					        refreshTab({tabTitle:"字词_" + + node.text,url:"toEditionWord.do?textbookId=" + node.id + "&subject=" + subject + "&jctxId=" + jctxNode.id});  
					    } else {
				  		// 获取选择的面板
							$('#centerTabs').tabs('add', {
								title: "字词_" + node.attributes.title,
								closable: true,
								content: '<iframe scrolling="no" frameborder="0"  src="toEditionWord.do?textbookId=' + node.id + '&subject=' + subject + '&jctxId=' + jctxNode.id + '" style="width:100%;height:99.5%;"></iframe>', // 新内容的URL
								tools : [ {
									iconCls : 'icon-mini-refresh',
									handler : function() {
										var selectedTab = $('#centerTabs').tabs('getSelected');
										selectedTab.panel('refresh');
									}
								} ]
							});
						}
					}
				}
			}
		});

		//中间区域的选项卡面板
		centerTabs = $('#centerTabs').tabs({
			fit : true,
			border : false,
			onContextMenu : function(e, title, index) {
				e.preventDefault();
				$(this).tabs('select', title);
				//当只有两个选项卡的时候(首页默认不能关闭)禁用“关闭其他”的菜单选项
				var allTabs = $('#centerTabs').tabs('tabs');
				var item = $('#tabMenu').menu('findItem', '关闭其他');
				if (allTabs.length == 2) {
					// 查找“关闭其他”项并禁用它
					$('#tabMenu').menu('disableItem', item.target);
				}else{
					$('#tabMenu').menu('enableItem', item.target);
				}
				$('#tabMenu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			}
		});

		/*关闭本选项卡*/
		closeThis = function() {
			var selectedTab = $('#centerTabs').tabs('getSelected');
			var selectedTabIndex = $('#centerTabs').tabs('getTabIndex', selectedTab);
			$('#centerTabs').tabs('close', selectedTabIndex);
		};

		/*关闭其他选项卡*/
		closeOthers = function() {
			var selectedTab = $('#centerTabs').tabs('getSelected');
			var selectedTabTitle = selectedTab.panel('options').title;
			var allTabsLength = $('#centerTabs').tabs('tabs').length;
			for (var i = 0; i < allTabsLength - 1; i++) {
				if (selectedTabTitle != $('#centerTabs').tabs('tabs')[1].panel('options').title) {
					$('#centerTabs').tabs('close', 1);
				} else {
					$('#centerTabs').tabs('close', 2);
				}
			}
			$('#centerTabs').tabs('select', 1);
		};

		/*关闭所有选项卡*/
		closeAll = function() {
			var allTabsLength = $('#centerTabs').tabs('tabs').length;
			for (var i = 0; i < allTabsLength - 1; i++) {
				$('#centerTabs').tabs('close', 1);
			}
		};
		
		/*展开导航树*/
		expandTree = function() {
			var selectedNode = subjectMenuTree.tree('getSelected');
			if (selectedNode) {
				subjectMenuTree.tree('expand', selectedNode.target);
			} else {
				subjectMenuTree.tree('expandAll');
			}
		};

		/*折叠导航树*/
		collapseTree = function() {
			var selectedNode = subjectMenuTree.tree('getSelected');
			if (selectedNode) {
				subjectMenuTree.tree('collapse', selectedNode.target);
			} else {
				subjectMenuTree.tree('collapseAll');
			}
		};

		/*刷新导航树*/
		refreshTree = function() {
			subjectMenuTree.tree('reload');
		};

		/*注销菜单按钮*/
		$('#zxButton').splitbutton({
			iconCls : 'cog',
			menu : '#zxMenu'
		});
		
		/*修改密码菜单按钮*/
		$('#pwdButton').splitbutton({
			iconCls : 'lock',
			menu : '#pwdMenu'
		});
		
	});
	
	/*注销登录*/
	function logout(){
		location.href = "logout.do?time=" + new Date().getTime();
	}
	
	/*退出系统*/
	function closeWindow(){
		//window.open('','_parent','');  
		window.close();
	}
	
	/*修改密码*/
	function updatePwd(){
		$('#updatePwdDialog').dialog({
			title : '密码修改',
			modal : true,
			inline : true,
			width : 300,
			height : 200,
			buttons : [ {
				text : '修改',
				handler : function() {
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					}); // 显示进度条
					$('#updatePwdForm').form('submit', {
						url : 'pwdUpdate.do',
						onSubmit : function() {
							var isValid = $(this).form('validate');
							if (!isValid) {
								$.messager.progress('close'); // 如果表单是无效的则隐藏进度条
							}
							return isValid; // 返回false终止表单提交
						},
						success : function(result) {
							$.messager.progress('close'); // 如果提交成功则隐藏进度条
							$('#updatePwdDialog').dialog('close');
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
				$('#updatePwdForm').form('clear');
			}
		});
	}
	
	/**     
	 * 刷新tab 
	 * @cfg  
	 *example: {tabTitle:'tabTitle',url:'refreshUrl'} 
	 *如果tabTitle为空，则默认刷新当前选中的tab 
	 *如果url为空，则默认以原来的url进行reload 
	 */  
	function refreshTab(cfg){  
	    var refresh_tab = cfg.tabTitle?$('#centerTabs').tabs('getTab',cfg.tabTitle):$('#centerTabs').tabs('getSelected');
	    if(refresh_tab && refresh_tab.find('iframe').length > 0){  
		    var _refresh_ifram = refresh_tab.find('iframe')[0];  
		    var refresh_url = cfg.url?cfg.url:_refresh_ifram.src;  
		    //_refresh_ifram.src = refresh_url;  
		    _refresh_ifram.contentWindow.location.href=refresh_url;  
	    }  
	}  
</script>


<div data-options="region:'north'" style="height:60px;">
	<div style="position: absolute; right: 5px;top : -5px">
		<h3>欢迎您，${user.username}</h3>
	</div>
	<div style="position: absolute; right: 0px; bottom: 0px;">
		<a href="javascript:void(0)" id="pwdButton">修改密码</a>
		<a href="javascript:void(0)" id="zxButton">注销</a>
	</div>
</div>
<div data-options="region:'south'" style="height:20px;"></div>
<!-- <div data-options="region:'east',iconCls:'icon-reload',title:'East',split:true" style="width:200px;"></div> -->
<div data-options="region:'west',title:'功能导航'" style="width:250px;">
	<div class="easyui-accordion" data-options="fit:true,border:false">
		<div title="试题管理">
			<ul id="questionMenuTree"></ul>
		</div>
		<div title="字词管理">
			<ul id="wordMenuTree"></ul>
		</div>
		<div title="产品管理">
			<ul id="productMenuTree"></ul>
		</div>
		<div title="试卷管理">
			<ul id="paperMenuTree"></ul>
		</div>
		<div title="学科管理" data-options="tools:'#accordionMenu'">
			<ul id="subjectMenuTree"></ul>
		</div>
		<div title="系统管理">
			<ul id="systemMenuTree"></ul>
		</div>
	</div>
</div>
<div data-options="region:'center',border:false">
	<div id="centerTabs">
		<div title="首页">欢迎进入首页！！</div>
	</div>
</div>

<div id="accordionMenu">
	<a href="javascript:void(0)" class="icon-undo" onclick="collapseTree();"></a> <a href="javascript:void(0)" class="icon-redo" onclick="expandTree();"></a> <a href="javascript:void(0)" class="icon-reload" onclick="refreshTree();"></a>
</div>

<div id="zxMenu" style="width: 100px; display: none;">
	<div onclick="logout();">重新登录</div>
	<div onclick="closeWindow();">退出系统</div>
</div>

<div id="pwdMenu" style="width: 100px; display: none;">
	<div onclick="updatePwd();">修改密码</div>
</div>

<div id="updatePwdDialog">
	<fieldset>
		<legend>
			<b><i>修改密码</i></b>
		</legend>
		<form id="updatePwdForm" method="post">
			<table>
				<tr>
					<td>原密码</td>
					<td><input class="easyui-validatebox" type="password" name="prepwd" data-options="required:true" /></td>
				</tr>
				<tr>
					<td>新密码</td>
					<td><input class="easyui-validatebox" type="password" id="newpwd" name="newpwd" data-options="required:true" /></td>
				</tr>
				<tr>
					<td>确认密码</td>
					<td><input class="easyui-validatebox" type="password" name="confirmpwd" data-options="required:true,validType:'equals[\'#newpwd\']'" /></td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>

<div id="tabMenu" class="easyui-menu" style="width:120px;">
	<div onclick="closeThis();">关闭</div>
	<div onclick="closeOthers();">关闭其他</div>
	<div onclick="closeAll();">关闭所有</div>
</div>

<jsp:include page="common/footer.jsp"/>

var ly = $.extend({}, ly); /* 定义全局变量 */

$.fn.panel.defaults.loadingMessage = '加载中...';
$.fn.datagrid.defaults.loadMsg = '加载中...';
$.fn.treegrid.defaults.loadMsg = '加载中...';

var easyuiErrorFunction = function(XMLHttpRequest) {
	$.messager.progress('close');
	$.messager.alert('错误', XMLHttpRequest.responseText);
};
$.fn.datagrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.treegrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.tree.defaults.onLoadError = easyuiErrorFunction;
$.fn.combogrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.combobox.defaults.onLoadError = easyuiErrorFunction;
$.fn.form.defaults.onLoadError = easyuiErrorFunction;


/* 防止panel/window/dialog组件超出浏览器边界
* @param left
* @param top
*/
var easyuiPanelOnMove = function(left, top) {
	var l = left;
	var t = top;
	if (l < 1) {
		l = 1;
	}
	if (t < 1) {
		t = 1;
	}
	var width = parseInt($(this).parent().css('width')) + 14;
	var height = parseInt($(this).parent().css('height')) + 14;
	var right = l + width;
	var buttom = t + height;
	var browserWidth = $(window).width();
	var browserHeight = $(window).height();
	if (right > browserWidth) {
		l = browserWidth - width;
	}
	if (buttom > browserHeight) {
		t = browserHeight - height;
	}
	$(this).parent().css({/* 修正面板位置 */
		left : l,
		top : t
	});
};
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.panel.defaults.onMove = easyuiPanelOnMove;

/* 将form表单元素的值序列化成对象 */
ly.serializeObjct = function(form) {
	var o = {};
	$.each(form.serializeArray(), function(index) {
		if (o[this['name']]) {
			o[this['name']] = o[this['name']] + "," + this['value'];
		} else {
			o[this['name']] = this['value'];
		}
	});
	return o;
};

/* 扩展datetimebox类型的editor */
$.extend($.fn.datagrid.defaults.editors, {
	datetimebox : {
		init : function(container, options) {
			var editor = $('<input />').appendTo(container);
			options.editable = false;
			editor.datetimebox(options);
			return editor;
		},
		getValue : function(target) {
			return $(target).datetimebox('getValue');
		},
		setValue : function(target, value) {
			$(target).datetimebox('setValue');
		},
		resize : function(target, width) {
			$(target).datetimebox('resize', width);
		},
		destory : function(target) {
			$(target).datetimebox('destroy');
		}
	}
});

/* 扩展datagrid的动态添加或删除editors方法 */
$.extend($.fn.datagrid.methods, {
	addEditor : function(jq, param) {
		if (param instanceof Array) {
			$.each(param, function(index, item) {
				var e = $(jq).datagrid('getColumnOption', item.field);
				e.editor = item.editor;
			});
		} else {
			var e = $(jq).datagrid('getColumnOption', param.field);
			e.editor = param.editor;
		}
	},
	removeEditor : function(jq, param) {
		if (param instanceof Array) {
			$.each(param, function(index, item) {
				var e = $(jq).datagrid('getColumnOption', item);
				e.editor = {};
			});
		} else {
			var e = $(jq).datagrid('getColumnOption', param);
			e.editor = {};
		}
	}
});

$.extend($.fn.validatebox.defaults.rules, {
	/*验证密码是否相同*/
	equals : {
		validator : function(value, param) {
			return value == $(param[0]).val();
		},
		message : '两次密码不一致！'
	},
	/*验证上传文件格式*/
	format : {
		validator : function(value, param) {
			var ext = value.substring(value.lastIndexOf('.') + 1);
			var arr = param[0].split(',');
			for(var i = 0; i < arr.length; i++){
				if(ext == arr[i]){
					return true;
				}
			}
			return false;
		},
		message : '文件格式必须为：{0}'
	},
	/*下拉框选择验证*/
	comboVry : {
        validator: function (value, param) {//param为默认值
            return value != param[0];
        },
        message: '请选择选项！'
    },
    /*验证新增是否存在相同的名称*/
    isExistName : {
    	validator: function (value, param) {//param为默认值
    		var flag = false;
    		$.ajax({  
                url : param[0],  
                type : 'POST',                    
                timeout : 60000,  
                data:{name : value, pid : param[1]},  
                async: false,    
                success : function(result) {     
                   	var r = $.parseJSON(result);
                   	flag = r.success;
                }  
            }); 
            return flag;
        },
        message: '已经存在相同的名称！'
    },
     /*验证更新是否存在相同的名称*/
    isExistNameUpdate : {
    	validator: function (value, param) {//param为默认值
    		var flag = false;
    		$.ajax({  
                url : param[0],  
                type : 'POST',                    
                timeout : 60000,  
                data:{name : value, pid : param[1], id :$(param[2]).val()},  
                async: false,    
                success : function(result) {
                	var r = $.parseJSON(result);
                   	flag = r.success;
                }  
            }); 
            return flag;
        },
        message: '已经存在相同的名称！'
    },
    Hanzi : {
   	 validator: function (value) {
            return /^[\u0391-\uFFE5]+$/.test(value);
        },
        message: '只能输入汉字'
   },
    Excel : {
   	 validator: function (value) {
            return /\.(xls|xlsx)$/.test(value);
        },
        message: '只能上传excel'
   }
});

// 在layout的panle全局配置中,增加一个onCollapse处理title
$.extend($.fn.layout.paneldefaults, {
	onCollapse : function() {
		// 获取layout容器
		//var layout = $(this).parents("div.layout");
		// 获取layout容器
		var layout = $(this).parents(".layout");
		// 获取当前region的配置属性
		var opts = $(this).panel("options");
		// 获取key
		var expandKey = "expand" + opts.region.substring(0, 1).toUpperCase() + opts.region.substring(1);
		// 从layout的缓存对象中取得对应的收缩对象
		var expandPanel = layout.data("layout").panels[expandKey];
		// 针对横向和竖向的不同处理方式
		if (opts.region == "west" || opts.region == "east") {
			// 竖向的文字打竖,其实就是切割文字加br
			var split = [];
			for (var i = 0; i < opts.title.length; i++) {
				split.push(opts.title.substring(i, i + 1));
			}
			expandPanel.panel("body").addClass("panel-title").css("text-align", "center").html(split.join("<br>"));
		} else {
			expandPanel.panel("setTitle", opts.title);
		}
	}
});


/*tree的simpleData加载*/
$.fn.tree.defaults.loadFilter = function(data, parent) {
	var opt = $(this).data().tree.options;
	var idFiled, textFiled, parentField;
	if (opt.parentField) {
		idFiled = opt.idFiled || 'id';
		textFiled = opt.textFiled || 'text';
		parentField = opt.parentField;

		var i, l, treeData = [], tmpMap = [];

		for (i = 0, l = data.length; i < l; i++) {
			tmpMap[data[i][idFiled]] = data[i];
		}

		for (i = 0, l = data.length; i < l; i++) {
			if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
				if (!tmpMap[data[i][parentField]]['children'])
					tmpMap[data[i][parentField]]['children'] = [];
				data[i]['text'] = data[i][textFiled];
				tmpMap[data[i][parentField]]['children'].push(data[i]);
			} else {
				data[i]['text'] = data[i][textFiled];
				treeData.push(data[i]);
			}
		}
		return treeData;
	}
	return data;
};

/*treegrid的simpleData加载
$.fn.treegrid.defaults.loadFilter = function(data, parent) {
	var opt = $(this).data().treegrid.options;
	var idFiled, textFiled, parentField;
	if (opt.parentField) {
		idFiled = opt.idFiled || 'id';
		textFiled = opt.textFiled || 'text';
		parentField = opt.parentField;

		var i, l, treeData = [], tmpMap = [];

		for (i = 0, l = data.length; i < l; i++) {
			tmpMap[data[i][idFiled]] = data[i];
		}

		for (i = 0, l = data.length; i < l; i++) {
			if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
				if (!tmpMap[data[i][parentField]]['children'])
					tmpMap[data[i][parentField]]['children'] = [];
				data[i]['text'] = data[i][textFiled];
				tmpMap[data[i][parentField]]['children'].push(data[i]);
			} else {
				data[i]['text'] = data[i][textFiled];
				treeData.push(data[i]);
			}
		}
		return treeData;
	}
	return data;
};

/*combotree的simpleData加载*/
$.fn.combotree.defaults.loadFilter = function(data, parent) {
	var opt = $(this).data().tree.options;
	var idFiled, textFiled, parentField;
	if (opt.parentField) {
		idFiled = opt.idFiled || 'id';
		textFiled = opt.textFiled || 'text';
		parentField = opt.parentField;

		var i, l, treeData = [], tmpMap = [];

		for (i = 0, l = data.length; i < l; i++) {
			tmpMap[data[i][idFiled]] = data[i];
		}

		for (i = 0, l = data.length; i < l; i++) {
			if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
				if (!tmpMap[data[i][parentField]]['children'])
					tmpMap[data[i][parentField]]['children'] = [];
				data[i]['text'] = data[i][textFiled];
				tmpMap[data[i][parentField]]['children'].push(data[i]);
			} else {
				data[i]['text'] = data[i][textFiled];
				treeData.push(data[i]);
			}
		}
		return treeData;
	}
	return data;
};


/* 更换主题
 function changeThemeFun(themeName) {
 var $easyuiTheme = $('#easyuiTheme');
 var url = $easyuiTheme.attr('href');
 var href = url.substring(0, url.indexOf('themes')) + 'themes/' + themeName
 + '/easyui.css';
 $easyuiTheme.attr('href', href);

 var $iframe = $('iframe');
 if ($iframe.length > 0) {
 for (var i = 0; i < $iframe.length; i++) {
 var ifr = $iframe[i];
 $(ifr).contents().find('#easyuiTheme').attr('href', href);
 }
 }

 $.cookie('easyuiThemeName', themeName, {
 expires : 7
 });
 };
 if ($.cookie('easyuiThemeName')) {
 changeThemeFun($.cookie('easyuiThemeName'));
 } */


/**
 * 增加formatString功能
 * 使用方法：$.formatString('字符串{0}字符串{1}字符串','第一个变量','第二个变量');
 * @returns 格式化后的字符串
 */
$.formatString = function(str) {
	for ( var i = 0; i < arguments.length - 1; i++) {
		str = str.replace("{" + i + "}", arguments[i + 1]);
	}
	return str;
};

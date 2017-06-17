$.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
	_title: function(title) {
		var $title = this.options.title || '&nbsp;';
		if( ("title_html" in this.options) && this.options.title_html == true )
			title.html($title);
		else title.text($title);
	}
}));
var passwordFormValidate;
$(function() {
//
//	$(".container").on("mouseover"," .table td,.table th,.chosen-container .active-result,.chosen-container .chosen-single span",function(){
//		debugger;
//		if($(this).get(0).scrollWidth>$(this).get(0).clientWidth){
//			var title = $(this).attr("title");
//			if(title){
//				return;
//			}
//			var text = $(this).text();
//			$(this).attr("title",text.trim());
//		}
//	});
	
	
	/**
	 * layout保持当前菜单选中状态
	 */
//	var url = window.location.href;
//	$("#sidebar ul li a").each(function(){
//		var role = $(this).attr("role");
//		url = url.replace("http://","").replace("https://","");
//		var s = url.indexOf("/");
//		var e = url.lastIndexOf("/");
//		var biaoshi = url.substring(s+1,e);
//		if((!isnull(role) && role.indexOf(biaoshi)>-1) || (role=="/" && s==e)){
//			$(this).parent().addClass('active');
//			$(this).parent().parent().parent().addClass('open');
//		}
//	});
//	setTimeout(function(){
//		if(top.mainFrame.length>0){
//			top.mainFrame.unmask();
//		}
//	},10);
	
	if (!String.prototype.trim) { //判断下浏览器是否自带有trim()方法  
		String.method('trim', function() {
			return this.replace(/^\s+|\s+$/g, '');
		});
		String.method('ltrim', function() {
			return this.replace(/^\s+/g, '');
		});
		String.method('rtrim', function() {
			return this.replace(/\s+$/g, '');
		});
	}
	
	$("body").bind("mousedown", function(event) {
		if($(event.target).parents("#zTreeDemoBackground").length == 0){
			closeZtreeDemo();
		}
	});
	
	
	/**
	 * 弹出框的打开跟关闭方法
	 */
	$.extend($.fn, {
		open : function() {
			$(this).dialog('open');
		},
		close : function() {
			$(this).dialog('close');
		}
	});
	
	passwordFormValidate = formValidate("passwordForm",{
		password: {
			required: true,
			password: true
		},
		repeatPassword: {
			required: true,
			password: true,
			equalTo: "#myPassword"
		}
	},{
		password:{
			required: "请输入密码"
		},
		repeatPassword:{
			required: "请再次输入密码"
		}
	});
	$("#updateMyPassword").click(function(){
		passwordFormValidate.resetForm();
		$('#passwordForm').find('.form-group').each(function(){
			$(this).removeClass('has-error');
		});
		openDialog("passwordDialog","passwordDialogDiv","修改当前密码",600,300,true,"提交",function(){
			if($("#passwordForm").valid()){
				mask();
				$("#passwordForm").ajaxSubmit(function(data){
					unmask();
					if(data.success){
						$("#passwordDialog").close();
					}
					alertInfo(data.message);
				});
			}
		});
	});
});

/**
 *
 * @param tabId  dataTable的ID
 * @param url    请求的url路径
 * @param objData  数据对象
 * @param showCheckbox  是否显示checkbox
 * @param showNum  是否显示序号
 * @param callback  回调方法
 * @param searchCase  额外传递的参数  数组  格式：[{"tempMatchType":"5","propertyName":loginName,"propertyValue1":"jth","tempType":"String"}]
 * @returns  返回dataTable对象
 */
function initTables(tabId,url,objData,showCheckbox,showNum,searchCase,initComplete,drawCallback){
	var searchs = "";
	var currentPage=0,pageSize=0;
	if(!isnull(searchCase) && searchCase.length>0){
		searchs = "[" + searchCase.join(",") + "]";
	}
	if(showNum){
		objData.unshift({
			"title" : "序号",
			"class" : "center",
			"sortable" : false,
			"data" : 'id',
			"width" : "60px",
			"fnCreatedCell": function (nTd, sData, oData, iRow, iCol) {
				$(nTd).text((iRow + 1)+(currentPage-1)*pageSize);
			}
		});
	}
	if(showCheckbox){
		objData.unshift({
			"title" : createAllCheckBox(),
			"class" : "center",
			"sortable" : false,
			"data" : 'id',
			"width" : "60px",
			"mRender" : function(data, display, row) {
				return createCheckBox(data);
			}
		});
	}
	objData.unshift({
		"title" : "id",
		"visible" : false,
		"data" : 'id'
	});
	var tables = $('#'+tabId).dataTable( {
		"columns": objData,
		"aaSorting": [[ 0, "desc" ]],//页面加载时初始化表格，并以第1列进行降序排列
		"bAutoWidth": true,
		"bDeferRender":true,
		"bLengthChange": true, //开关，是否显示每页大小的下拉框
		"bPaginate": false, //开关，是否显示分页器
		"bPaginate": true,  //是否分页
		"iDisplayLength":10, //每页显示10条记录  默认
		"bInfo": true, //开关，是否显示表格的一些信息
		"bFilter": true, //开关，是否启用客户端过滤器
		"bJQueryUI": false, //开关，是否启用JQueryUI风格
		"bProcessing": true, //当datatable获取数据时候是否显示正在处理提示信息
		"aLengthMenu": [[10, 30, 50, 100], [10, 30, 50, 100]],
		"bServerSide": true,
		"sPaginationType": 'full_numbers',//分页样式
		"sAjaxSource": url,
		"sAjaxDataProp":"aaData",
		"sServerMethod":"POST",
		"oSearch": { "sSearch": searchs},
		"oLanguage": {
			"sProcessing": "正在加载中......",
            "sSearch": "搜索:",
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "没有记录",
            "sInfo": "共 _TOTAL_条记录",
            "sInfoEmpty": "显示0条记录",
            "oPaginate": {
                "sPrevious": " 上一页 ",
                "sNext":     " 下一页 ",
                "sFirst": "首页",
                "sLast": "尾页"
            }
		},
		"sDom":'<"row"<"col-xs-6"r><"col-xs-6"f>>t<"row"<"pull-left"l><"col-xs-2"i><"col-xs-7"p>>',
		"preDrawCallback": function( settings ) {
			var iDisplayStart = settings._iDisplayStart;
			pageSize = settings._iDisplayLength;
			currentPage = iDisplayStart / pageSize +1;
		},
		"drawCallback": function( settings ) {
			$("#"+settings.sInstance+" input[type='checkbox']").prop("checked", false);
			$("#"+settings.sInstance+" tbody input[type='checkbox']").change(function(){
				if(!$(this).prop("checked")){
					$("#"+settings.sInstance+" thead input[type='checkbox']").prop("checked", false);
				}else{
					var len = $("#"+settings.sInstance+" tbody input[type='checkbox']").not("input:checked").length;
					if(len==0){
						$("#"+settings.sInstance+" thead input[type='checkbox']").prop("checked", true);
					}
				}
			});
			if(!isnull(drawCallback)){
				drawCallback(settings);
			}
			document.onkeydown=function(event){
				 var e = event || window.event || arguments.callee.caller.arguments[0];
				 if(e && e.keyCode==13){// enter 键
					 var turnInput = $('#'+tabId).next("div").find(".dataTables_length input");
					 if(turnInput.length>0){
						 var page = turnInput.val();
						 if(!isnull(page)){
							 page = Number(page);
							 freshTable(tables,page);
						 }
					 }
				 }else if(e && e.keyCode==32){//空格事件
					 //e.preventDefault();//阻止它默认行为的发生而发生其他的事件
				 }
			 };
		},
		"initComplete": function(settings, json) {
			if(!isnull(initComplete)){
				initComplete();
			}
			setTimeout(function(){
			    $('#'+tabId).removeAttr("style");
		    },100);
		}
    });
	return tables;
}

//刷新table
function freshTable(dataTable,pageNo){
	var oSettings = dataTable.fnSettings();
	var iDisplayLength = oSettings._iDisplayLength;
	if(!isnull(pageNo)){
		oSettings._iDisplayStart = iDisplayLength*(pageNo-1);
	}
	dataTable.fnDraw(oSettings);
}

/**
 * 查询table
 * @param searchDiv
 * @param dataTable
 * @param otherCase  其他的附加参数
 */
function searchButton(dataTable,otherCase) {
	var searchs = getSearchGroup(searchDiv);

//	searchs = encodeURI(searchs);

	var str = "";
	if(!isnull(otherCase)){
		for(var i=0;i<otherCase.length;i++){
			searchs.push(otherCase[i]);
		}
	}
	str = "[" + searchs.join(",") + "]";

	dataTable.fnFilter(str);
}
//得到查询框的值Div
function getSearchGroup() {
	var json = new Array();
	$('#searchDiv .searchTr').each(function() {
		var propertyValue1 = $(this).find('.propertyValue1').val();
		var propertyValue2 = $(this).find('.propertyValue2').val();
		if (!isnull(propertyValue1) || !isnull(propertyValue2)) {
			if(!isnull(propertyValue2)){
				json.push('{"tempMatchType":"'+$(this).find('.tempMatchType').val()+'","propertyName":"'+$(this).find('.propertyName').val()
						+'","propertyValue1":"'+$(this).find('.propertyValue1').val().trim()+'","tempType":"'+$(this).find('.tempType').val()
						+'","propertyValue2":"'+propertyValue2+'"}');
			}else{
				json.push('{"tempMatchType":"'+$(this).find('.tempMatchType').val()+'","propertyName":"'+$(this).find('.propertyName').val()
						+'","propertyValue1":"'+$(this).find('.propertyValue1').val().trim()+'","tempType":"'+$(this).find('.tempType').val()+'"}');
			}
		}
	});
	return json;
}


/**
 * js异步操作时的session失效判断
 * @param datas
 */
function sessionValidate(datas){
	if(Object.prototype.toString.call(datas) === "[object String]"){
		if(datas.indexOf("<!DOCTYPE>")>-1 || datas.indexOf("<!doctype html>")>-1){//session失效自动刷新页面
			window.location.reload();
			return false;
		}
	}
	
	return true;
}
function isnull(data) {
	if (data == null || data == undefined || data == "" || data == "null") {
		return true;
	} else {
		if(typeof data == "string" && data.trim()==""){
			return true;
		}
		return false;
	}
}
function mask(text){
	if(!isnull(text)){
		$('#rightDivContent').mask(text);
	}else{
		$('#rightDivContent').mask("loading...");
	}
}

function unmask(){
	$('#rightDivContent').unmask();
}
/**
 * 异步加载
 * @param url
 * @param data
 * @param callback
 */
function ajaxRequest(url,data,callback){
	$.ajax({
		url:url,
		data:data,
		type:'POST',
		success:function(datas){
			callback(datas);
		},
		error:function(response){
			if(response.status == 403){
				window.location.reload();
			}
		}
	});
}
/**
 * 同步加载
 * @param url
 * @param data
 * @param callback
 */
function syncRequest(url,data,callback){
	$.ajax({
		url:url,
		data:data,
		type:'POST',
		async:false,
		success:function(datas){
			callback(datas);
		},
		error:function(response){
			if(response.status == 403){
				window.location.reload();
			}
		}
	});
}
/**
 *
 * @param select
 * @param url
 * @param data
 * @param isShowHead  是否显示"请选择"
 * @param value  是否选中
 * @param callback
 */
function createSelect(select, url, data, isShowHead, value, callback) {
	ajaxRequest(url,data,function(result){
		select.empty();
		if (isShowHead) {
			$("<option value=''>请选择</option>").appendTo(select);
		}
		if (result.length > 0) {
			for (var i = 0; i < result.length; i++) {
				$("<option value='" + result[i].id + "'>"
						+ result[i].name + "</option>").appendTo(select);
			}
			if(!isnull(value)){
				select.val(value);// 赋值让其呈选中状态
			}
		}
		if (!isnull(callback)) {
			callback();
		}
	});
}
function createSelect2(select, url, data, isShowHead, value, callback) {
	ajaxRequest(url,data,function(result){
		select.empty();
		if (isShowHead) {
			$("<option value=''>请选择</option>").appendTo(select);
		}
		if (result.length > 0) {
			var firstId = result[0].id;
			for (var i = 0; i < result.length; i++) {
				$("<option value='" + result[i].id + "'>"
						+ result[i].name + "</option>").appendTo(select);
			}
			if(!isnull(value)){
				select.select2("val", value);
			}else if(!isShowHead){
				select.select2("val", firstId);
			}else{
				select.select2("val", "");
			}
		}
		if (!isnull(callback)) {
			callback();
		}
	});
}
//全选
function chooseAll(obj){
	var table = $(obj).parent().parent().parent().parent().parent();
	var tableId = table.attr("id");
	var isChecked = $(obj).is(":checked");
	$("#"+tableId+" input[type='checkbox']").prop("checked", isChecked);
}
//生成全选checkkbox
function createAllCheckBox(){
	return '<label class="position-relative"><input type="checkbox" class="ace" onclick="chooseAll(this)" /><span class="lbl"></span></label>';
}
//生成普通checkbox
function createCheckBox(data,disabled){
	if(!isnull(disabled) && disabled){
		return '<label class="position-relative"><input type="checkbox" disabled value='+data+' class="ace" /><span class="lbl"></span></label>';
	}
	return '<label class="position-relative"><input type="checkbox" value='+data+' class="ace" /><span class="lbl"></span></label>';
}
function createDatePicker(obj,minDate,dateFormat,selectEvent){
	if(isnull(minDate)){
		minDate = null;
	}
	if(isnull(dateFormat)){
		dateFormat = "yy-mm-dd";
	}
	obj.datepicker({
		autoclose: true,
		changeMonth: true,
		changeYear: true,
		dateFormat: dateFormat,
		monthNamesShort: [ "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" ],
		yearRange: '-100:+10',
		todayHighlight: true,
		minDate: minDate,
		onSelect:function(data){
			if(!isnull(selectEvent)){
				selectEvent(data);
			}
		}
	});
}
/**
 * 表单校验
 * @param formId
 * @param rules
 * @param messages
 * @returns
 */
function formValidate(formId,rules,messages){
	return $('#'+formId).validate({
		errorElement: 'div',
		errorClass: 'help-block',
		focusInvalid: false,
		ignore: "",
		rules: rules,
		messages: messages,
		highlight: function (e) {
			$(e).closest('.form-group').removeClass('has-info').addClass('has-error');
		},
		success: function (e) {
			$(e).closest('.form-group').removeClass('has-error');//.addClass('has-info');
			$(e).remove();
		},
		errorPlacement: function (error, element) {
			if(element.is('input[type=checkbox]') || element.is('input[type=radio]')) {
				var controls = element.closest('div[class*="col-"]');
				if(controls.find(':checkbox,:radio').length > 1) controls.append(error);
				else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
			}
			else if(element.is('.select2')) {
				error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
			}
			else if(element.is('.chosen-select')) {
				error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
			}
			else{
				error.insertAfter(element.parent());
			}
		},
		submitHandler: function (form) {
		},
		invalidHandler: function (form) {
		}
	});
}
/**
 *
 * @param divId
 * @param appendDiv  弹框显示的div位置
 * @param title      弹框标题
 * @param width      弹框宽度
 * @param height     弹框高度
 * @param isShowBtn  是否显示弹框上的按钮
 * @param btnName    自定义按钮名字
 * @param method     自定义按钮事件
 * @param cancelMethod  取消的回调
 * @param openMethod  打开窗口后的回调
 * @returns
 */
function openDialog(divId,appendDiv,title,width,height,isShowBtn,btnName,method,cancelMethod,openMethod){
	var screenHeight = window.screen.height-150;
	if(height>screenHeight){
		height = screenHeight;
	}
	var screenWidth = window.screen.width-300;
	if(width>screenWidth){
		width = screenWidth;
	}
	var dialog;
	if(isShowBtn){//显示按钮
		dialog = $("#"+divId).removeClass('hide').dialog({
			appendTo: "#"+appendDiv,
			closeOnEscape:false,
			modal: true,
			width: width,
			height: height,
			closeText:"关闭",
			title: "<div class='widget-header widget-header-small'><h4 class='smaller'><i class='ace-icon fa fa-info-circle'></i>"+title+"</h4></div>",
			title_html: true,
			open : function(event, ui) {
				if(!isnull(openMethod)){
					openMethod();
				}
			},
			close:function(){
				if(!isnull(cancelMethod)){
					cancelMethod();
				}
			},
			buttons: [
				{
					text: "取消",
					"class" : "btn btn-xs",
					click: function() {
						dialog.close();
					}
				},
				{
					text: btnName,
					"class" : "btn btn-primary btn-xs",
					click: function() {
						if(!isnull(method)){
							method();
						}
					}
				}
			]
		});
	}else{
		dialog = $("#"+divId).removeClass('hide').dialog({
			appendTo: "#"+appendDiv,
			closeOnEscape:false,
			modal: true,
			width: width,
			height: height,
			closeText:"关闭",
			title: "",
			resizable: false,
			title_html: true,
			close:function(){
				if(!isnull(cancelMethod)){
					cancelMethod();
				}
			}
		});
	}
	return dialog;
}
/**
 * 确认框
 * @param callback
 * @param title  标题名字  有默认值
 * @param content  内容  有默认值
 * @param btnName  按钮名称  有默认值
 */
function confirmDialog(callback,title,content,btnName,cancelback){
	if(isnull(title)){
		title = "操作提示";
	}
	if(!isnull(content)){
		$("#confirm_content").html(content);
	}else{
		$("#confirm_content").html("确定要执行此操作吗?");
	}
	if(isnull(btnName)){
		btnName = "确定";
	}
	$( "#dialog-confirm" ).removeClass('hide').dialog({
		resizable: false,
		modal: true,
		closeText:"关闭",
		title: "<div class='widget-header'><h4 class='smaller'><i class='ace-icon fa fa-exclamation-triangle red'></i> "+title+"</h4></div>",
		title_html: true,
		buttons: [
			{
				html: btnName,
				"class" : "btn btn-success btn-xs",
				click: function() {
					$( this ).dialog( "close" );
					if(!isnull(callback)){
						callback();
					}
				}
			},
			{
				html: "取消",
				"class" : "btn btn-xs",
				click: function() {
					$( this ).dialog( "close" );
					if(!isnull(cancelback)){
						cancelback();
					}
				}
			}
		]
	});
}

function createImageUpload(obj,removeMethod){
	obj.ace_file_input({
		style:'well',
		btn_choose:'点击选择图片',
		btn_change:null,
		no_icon:'ace-icon fa fa-cloud-upload',
		droppable:true,
		thumbnail:'small',//large | fit
		maxSize:2*1024*1024,//控制2M以内
		preview_error : function(filename, error_code) {
		},
		before_remove:function(){
			if(!isnull(removeMethod)){
				removeMethod();
			}
			return true;
		}
	}).on('change', function(){
	}).on('click', function(){
		var title = $(this).next("span").attr("data-title");
		if(title=="点击选择图片"){
			return true;
		}else{
			var src = $(this).next("span").find("img").attr("src");
			var globalViewPic = window.top.globalViewPic;
			var currentDivObj = globalViewPic.find("#colorbox");
			if(src.indexOf("data:image")==-1){
				globalViewPic.find("#cboxOverlay").show();
				currentDivObj.show();
				src = src.replace("small_","");
				globalViewPic.find("#photos").attr("src",src);
				var dialogWidth = currentDivObj.width();
				var totalWidth = $(document).width();
				var left = (totalWidth - dialogWidth)/2;
				currentDivObj.css("left",left);
				var totalHeight = window.screen.height;
				var dialogHeight = currentDivObj.height();
				var top = (totalHeight - dialogHeight)/2-50;
				currentDivObj.css("top",top);
				
				globalViewPic.find("#cboxClose").unbind("click");
				globalViewPic.find("#cboxClose").click(function(){
					globalViewPic.find("#cboxOverlay").hide();
					currentDivObj.hide();
				});
				globalViewPic.find("#cboxOverlay").unbind("click");
				globalViewPic.find("#cboxOverlay").click(function(){
					globalViewPic.find("#cboxOverlay").hide();
					currentDivObj.hide();
				});
			}
			return false;
		}
	});
	obj.ace_file_input('update_settings',
	{
		'allowExt': ["jpeg", "jpg", "png", "gif" , "bmp"],
		'allowMime': ["image/jpg", "image/jpeg", "image/png", "image/gif", "image/bmp"]
	});
}

/**
 * 图片回显
 * @param inputId  input[type=file]的id
 * @param imgSrc   图片路径
 */
function reviewImage(inputId,imgSrc){
	var fileName = imgSrc;
	if(imgSrc.indexOf("/")>-1){
		fileName = imgSrc.substring(imgSrc.lastIndexOf("=")+1);
	}
	$("#"+inputId).parent().find('.ace-file-name').remove();
	$("#"+inputId).parent().find(".ace-file-container").addClass('hide-placeholder').attr('data-title', null)
	.addClass('selected').html('<span class="ace-file-name" data-title="">'
			 +('<img class="middle" style="width: 50px; height: 50px; background-image: url();" src="'+imgSrc+'"><i class="ace-icon fa fa-picture-o file-image"></i>')
					 +'</span>');
}

/**
 * 文件转换成base64
 * @param files
 * @param callback
 */
function fileTransferBase64(files,callback){
	$.each(files, function (idx, fileInfo) {
		var size = fileInfo.size;
        if (/^image\//.test(fileInfo.type)) {
        	if(size > 2*1024*1024){
        		unmask();
        		alertInfo("上传图片不能大于2M！");
        		return;
        	}
            var loader = $.Deferred(),fReader = new FileReader();
            fReader.onerror = loader.reject;
            fReader.onprogress = loader.notify;
            fReader.readAsDataURL(fileInfo);
            fReader.onload = function (e) {
                loader.resolve(e.target.result);
            };
            $.when(loader.promise()).done(function (dataUrl) {
            	if(!isnull(callback)){
            		callback(dataUrl,fileInfo.name);
            	}
            }).fail(function (e) {
                alert(e);
            });
        }else{
        	unmask();
        	alertInfo("只能上传图片！");
    		return;
        }
    });
}

/**
 * 生成文本编辑器
 * @param obj
 * @returns
 */
function createKindEdit(obj){
	var kindEditor = KindEditor.create(obj, {
		allowFileManager : true,
		extraFileUploadParams : {
			"_csrf":window.token
        },
		items : ['undo', 'redo', '|', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
					'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
					'insertunorderedlist', '|', 'emoticons', 'image', 'link','unlink']
	});
	return kindEditor;
}

function loadSimpleTree(objId,url,isCheck,callback,onClickTree,onCheck){
	var setting = {
			view: {
				selectedMulti: isCheck,
				expandSpeed: "normal"
			},
			check: {
				enable: isCheck,
				chkStyle: "checkbox",
				chkboxType: { "Y": "", "N": "" }
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			async: {
				enable: true,
				url:url,
				autoParam:["id"]
			},
			callback: {
				onAsyncSuccess: function(event, treeId, treeNode, msg){
					if(!isnull(callback)){
						callback(treeNode);
					}
				},
				onClick: function(event, treeId, treeNode) {
				    if(!isnull(onClickTree)){
				    	onClickTree(treeNode);
				    }
				},
				onCheck: function(event, treeId, treeNode) {
					if(!isnull(onCheck)){
						onCheck(treeNode);
					}
				}
			}
		};
	return $.fn.zTree.init($("#"+objId), setting);
}
function showMenu(relativeControl, showDiv) {
	var cityObj = relativeControl;
	var cityOffset = relativeControl.offset();
	showDiv.css({
		width:cityObj.width() + "px",
		left : cityOffset.left + "px",
		top : cityOffset.top + cityObj.outerHeight() + "px"
	}).slideDown("fast");
}
function hideMenu(hideDiv) {
	hideDiv.fadeOut("fast");
}
function closeZtreeDemo(){
	var demoDiv = $("#zTreeDemoBackground");
	if(demoDiv.length > 0){
		hideMenu(demoDiv);
	}
}
Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val)
            return i;
    }
    return -1;
};
//删除指定位置
Array.prototype.removeIndex = function(val) {
    if (val > -1) {
        this.splice(val, 1);
    }
};
/**
 * 日期格式化
 * 如：new Date().format("yyyy-MM-dd hh:mm:ss")
 */
Date.prototype.format = function(format) {
    var o = {
        "M+" : this.getMonth() + 1, // month
        "d+" : this.getDate(), // day
        "h+" : this.getHours(), // hour
        "m+" : this.getMinutes(), // minute
        "s+" : this.getSeconds(), // second
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
        "S" : this.getMilliseconds()
        // millisecond
    };
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
            - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};
function alertInfo(msg,clickMethod){
	bootbox.alert({
        buttons: {
           ok: {
                label: '确定',  
                className: 'btn-sm btn-success'  
            }
        },
        message: '<span class="bigger-160">'+msg+'</span>',  
        callback: function() {
        	if(!isnull(clickMethod)){
        		clickMethod();
        	}
        },
        title: "提示",
    });
}
/**
 * 加载tabs页 
 * @param divId  显示位置的DIV
 * @param tabsObj  [{"name":"xxx","html":"text","id":"tabs_id"}]
 */
function showTabs(divId,tabsObj,callback){
	$("#"+divId).hide();
	$("#"+divId).html("");
	var htmls = '<div id="'+divId+'_tabs"><ul class="nav nav-tabs padding-18">';
	var tabDiv = '';
	if(tabsObj.length>0){
		for(var i=0;i<tabsObj.length;i++){
			var id = tabsObj[i].id;
			if(!isnull(id)){
				htmls += '<li><a href="#'+id+'">'+tabsObj[i].name+'</a></li>';
				tabDiv += '<div id="'+id+'" style="overflow-y: auto; overflow-x: auto;height: 680px;">'+tabsObj[i].html+'</div>';
			}else{
				htmls += '<li><a href="#'+divId+'_tabs-'+i+'">'+tabsObj[i].name+'</a></li>';
				tabDiv += '<div id="'+divId+'_tabs-'+i+'" style="overflow-y: auto; overflow-x: auto;height: 680px;">'+tabsObj[i].html+'</div>';
			}
		}
		htmls += '</ul><div class="tab-content">'+tabDiv+'</div></div>';
		$("#"+divId).html(htmls);
		var firstId = tabsObj[0].id;
		setTimeout(function(){
			$("#"+divId).show();
			if(!isnull(callback)){
				callback();
			}
			$('#'+divId+'_tabs').tabs();
			$('#'+divId+'_tabs li a[href="#'+firstId+'"]').click();
		},100);
	}
}

function createSubGrid(gridTable,gridPager,objList,n,lastRowId,subExpandedCallback){
	var obj = objList[n];
	var url = obj.url;
	var colNames = obj.colNames;
	var colModels = obj.colModels;
	var addMethod = obj.addMethod;
	var subGrid = true;
	var multiselect = false;
	if(n==objList.length-1){
		subGrid = false;
	}
	if(objList.length==1){
		subGrid = false;
		multiselect = true;
	}
	$("#"+gridTable).jqGrid({
		subGrid : subGrid,
		subGridOptions : {
			plusicon : "ace-icon fa fa-plus center bigger-110 blue",
			minusicon  : "ace-icon fa fa-minus center bigger-110 blue",
			openicon : "ace-icon fa fa-chevron-right center orange"
		},
		subGridRowExpanded: function (subgridDivId, rowId) {
			var n2 = n+1;
			var nextObj = objList[n2];
			var subgridTableId = subgridDivId + "Table";
			var subgridPagerId = subgridDivId + "Pager";
			if(!isnull(nextObj)){
				url = nextObj.url;
				if(!isnull(lastRowId)){
					$("#"+gridTable).collapseSubGridRow(lastRowId);
				}
				lastRowId = rowId;
				$("#" + subgridDivId).html("<table id='" + subgridTableId + "'></table><div id='" + subgridPagerId + "'></div>");
				
				if(url.indexOf("?id=")>-1){
					url = url.substring(0,url.indexOf("?id="));
				}
				url += "?id="+rowId;
				nextObj.url = url;
				createSubGrid(subgridTableId,subgridPagerId,objList,n2,lastRowId,subExpandedCallback);
			}
			if(!isnull(subExpandedCallback)){
				subExpandedCallback(rowId,subgridTableId,n2);
			}
		},
		url:url,
		mtype: 'POST',
		datatype: "json",
		height: "auto",
		autowidth: true,
		caption: "",
		viewrecords : true,
		rownumbers:true,
		rowNum:10,
		rowList:[10,20,30],
		pager : gridPager,
		altRows: true,
		prmNames:{sort:'sortname',order:'order',rows:"pageSize",page: "pageNo"},
		colNames:colNames,
		colModel:colModels,
		jsonReader: {
			root: "items", //Get json array data's root property  
			page: "pageNo", //current page  
			total: "totalPageCount", //total page count  
			records: "totalCount" //total records count  
		},
		multiselect: multiselect,
        multiboxonly: true,
        onInitGrid : function() {
			var table = this;
			setTimeout(function(){
				updatePagerIcons(table);
				enableTooltips(table);
				loadPager(gridTable,gridPager,addMethod);
				var width = $("#gview_"+gridTable+" .ui-jqgrid-bdiv").width();
				$("#gview_"+gridTable+" .ui-jqgrid-bdiv").css("width",width+1);
			}, 0);
		},
		loadComplete: function() {
		}
	});
}
function loadPager(grid_selector,pager_selector,callback){
	var gridObj = $("#"+grid_selector);
	gridObj.navGrid("#"+pager_selector,{edit:false,add:false,del:false,search:false,refresh: true,refreshicon : 'ace-icon fa fa-refresh green'});
	if(!isnull(callback)){
		gridObj.navButtonAdd("#"+pager_selector,{
			caption:"",
			title:"添加新纪录",
			buttonicon:"ace-icon fa fa-plus-circle purple",
			onClickButton: function(){
				if(!isnull(callback)){
					callback(gridObj);
				}
			},
			position:"last"
		});
	}
	
}
function updatePagerIcons(table) {
	var replacement = 
	{
		'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
		'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
		'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
		'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
	};
	$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
		var icon = $(this);
		var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
		if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
	});
}
function enableTooltips(table) {
	$('.navtable .ui-pg-button').tooltip({container:'body'});
	$(table).find('.ui-pg-div').tooltip({container:'body'});
}

function turnUrl(url,title){
	var width = screen.width*0.8;
	var height = screen.height*0.8;
	var config = "width="+width+",height="+height;
	window.open(url,title,config);
}


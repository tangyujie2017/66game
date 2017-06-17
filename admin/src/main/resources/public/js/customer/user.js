var userTable;
var userFormValidate;
$(function() {
	var editAuth = $("#editAuth").val();
	var objData = [{
		"title" : "用户ID",
		"class" : "center",
		"sortable" : true,
		"visible" : true,
		"width" : "100px",
		"data" : 'id'
	}, {
		"title" : "手机号",
		"class" : "center",
		"sortable" : true,
		"data" : 'mobile',
		"width" : "120px"
	}, {
		"title" : "真实姓名",
		"class" : "center",
		"sortable" : true,
		"visible" : true,
		"data" : 'realName',
		"width" : "120px"
	},{
		"title" : "到期时间",
		"class" : "center",
		"sortable" : true,
		"visible" : true,
		"data" : 'serverTime',
		"width" : "120px"
	}, {
		"title" : "角色",
		"class" : "center",
		"sortable" : false,
		"visible" : true,
		"data" : 'roleStr',
		'width':'360px'
	}, {
		"title" : "状态",
		"class" : "center",
		"sortable" : false,
		"visible" : true,
		"data" : 'locked',
		"width" : '100px',
		"mRender":function (data, display, row) {
			if(data){
				return "<span class='red'>停用</span>";
			}else{
				return "启用";
			}
		}
	},{ "sortable": false,"data":"id","class": "left","title":"操作",
		   "mRender":function (data, display, row) {
			   if(isnull(editAuth)){
				   return "暂无编辑权限！";
			   }
			   return '<div class="action-buttons"><a class="blue" href="javascript:void(-1);" onclick=openWin("/customer/edit?id='+data+'","编辑用户") title="编辑"><i class="ace-icon fa fa-pencil bigger-160"></i></a>'+
			   '<a class="blue" href="javascript:void(-1);" title="修改密码" onclick="editDialog(this)"><i class="ace-icon fa fa-key bigger-160"></i></a></div>';
 			}
 		} ];
	userTable = initTables("userTable", "load/user", objData, false,false,null, function() {

	});

//	createSelect($("#roleId"), "allRoles", {}, true, null, function(){
//
//	});


	userFormValidate = formValidate("userForm",{
		password: {
			required: true,
			password: true
		},
		repeatPassword: {
			required: true,
			password: true,
			equalTo: "#password"
		},
		realName: {
			required: true
		}
	},{
		password:{
			required: "请输入密码"
		},
		repeatPassword:{
			required: "请再次输入密码"
		}
	});

});

function search(){
	searchButton(userTable);
}
function editDialog(obj){
	userFormValidate.resetForm();
	$('#userForm').find('.form-group').each(function(){
		$(this).removeClass('has-error');
	});
	var oo = $(obj).parents("tr");
	var aData = userTable.fnGetData(oo); // get datarow
	$("#id").val(aData.id);
	$("#realName").val(aData.realName);
	openDialog("userDialog","userDialogDiv","修改密码",700,380,true,"提交",function(){
		if($("#userForm").valid()){
			mask();
			$("#userForm").ajaxSubmit(function(data){
				unmask();
				if(data.success){
					$("#userDialog").close();
				}
				alertInfo(data.message);
			});
		}
	});
}
function delDialog(obj){
	confirmDialog(function(){
		alertInfo("删除！");
	})
}

function openWin (url,title){
	layer.open({
        type: 2,
        skin: 'layui-layer-lan',
        title: title,
        fix: false,
        shadeClose: true,
        maxmin: true,
        area: ['1000px', '500px'],
        content: url,
        end: function(){
        	search();
        }
    });
}



$(function() {
	userFormValidate = formValidate("addUserForm",{
		password: {
			required: true,
			password: true
		},
		repeatPassword: {
			required: true,
			password: true,
			equalTo: "#password"
		},
		roles: {
			required: true
		},
		mobile: {
			required: true,
			mobile: true,
			remote:{
				url:'/user/check/mobile',
				data:{
					'id':function() {
						var id = $('#id').val();
						if(!isnull(id)){
							id = Number(id);
						}else{
							id = 0;
						}
						return id;
					}
				}
			}
		},
		realName: {
			required: true,
			maxlength: 10,
			minlength: 2
		}
	},
	{
		mobile : {
			remote : "该电话已存在"
		},
		password:{
			required: "请输入密码"
		},
		repeatPassword:{
			required: "请再次输入密码"
		}
	});
});
function submitForm(){
	if($("#addUserForm").remoteCount()>0){
		alertInfo("正在远程校验中...");
		return false;
	}
	if($("#addUserForm").valid()){
		var url = $("#addUserForm").attr("action");
		var userUrl = url.substring(0,url.lastIndexOf("/")+1);
		mask();
		$("#addUserForm").ajaxSubmit(function(data){
			unmask();
			if(data.success){
				var index = parent.layer.getFrameIndex(window.name);
				parent.layer.close(index);
			}else{
				alertInfo(data.message);
			}
		});
	}
	
}
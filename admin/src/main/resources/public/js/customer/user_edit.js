$(function() {
	//初始化时间控件
	$("#pickdate").dateDropper({
		animate: false,
		format: 'Y-m-d',
		maxYear: '2050'
	});
	formValidate("editUserForm",{
		roles: {
			required: true
		},
		mobile: {
			required: true,
			mobile: true,
			remote:{
				url:'/customer/check/mobile',
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
		},
		serverTime:{
			required: true
			
		}
	},
	{
		mobile : {
			remote : "该电话已存在"
		}
	});
});
function submitForm(){
	if($("#editUserForm").remoteCount()>0){
		alertInfo("正在远程校验中...");
		return false;
	}
	if($("#editUserForm").valid()){
		var url = $("#editUserForm").attr("action");
		var userUrl = url.substring(0,url.lastIndexOf("/")+1);
		mask();
		$("#editUserForm").ajaxSubmit(function(data){
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
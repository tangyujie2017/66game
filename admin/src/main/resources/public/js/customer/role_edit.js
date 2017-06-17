$(function() {
	formValidate("roleForm",{
		details: {
			required: true
		},
		name: {
			required: true
		}
	},{});
	
	//点击左边权限 取消选中右边权限
	$(".leftAuthMenu").click(function(){
		$(this).parent().parent().next().find('input[type="checkbox"]').each(function(){
			if($(this).prop("checked")){
				$(this).prop("checked", false);
			}
		});
	});
	//点击右边权限 取消选中左边权限
	$(".rightAuthMenu").click(function(){
		$(this).parent().parent().prev("th").find('input[type="checkbox"]').each(function(){
			if($(this).prop("checked")){
				$(this).prop("checked", false);
			}
		});
	});
});

function submitForm(){
	if($("#roleForm").valid()){
		var url = $("#roleForm").attr("action");
		url = url.substring(0,url.lastIndexOf("/")+1);
		mask();
		$("#roleForm").ajaxSubmit(function(data){
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
		
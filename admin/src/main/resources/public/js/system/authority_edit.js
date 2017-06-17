$(function() {
	formValidate("authorityForm",{
		name: {
			required: true
		},
		code: {
			required: true
		},
		sort: {
			digits: true
		}
	},{});
});
var treeObj;
function showTree1() {
	treeObj = loadSimpleTree("treeDemo","findAuthorityForTree",false,function(){
		showMenu($("#parentName"), $("#zTreeDemoBackground"));
	},function(treeNode){
		$("#parentId").val(treeNode.id);		
		$("#parentName").val(treeNode.name);
		hideMenu($("#zTreeDemoBackground"));
	});
}
function closeZtreeDemo(){
	hideMenu($("#zTreeDemoBackground"));
}
function clearParentData(){
	$("#parentId").val("");
	$("#parentName").val("");
}
function submitForm(){
	if($("#authorityForm").valid()){
		var id = $("#id").val();
		if(!isnull(id) && $("#parentId").val()==id){
			alertInfo("当前分类与上级分类不能相同！");
			return;
		}
		var url = $("#authorityForm").attr("action");
		url = url.substring(0,url.lastIndexOf("/")+1);
		mask();
		$("#authorityForm").ajaxSubmit(function(data){
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
		
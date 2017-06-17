var authorityTable;
var editAuth;
$(function(){
	editAuth = $("#editAuth").val();
	var data = {"iDisplayLength":100};
	initData(data);
});

function initData(data){
	$("#authorityTable").empty();
	ajaxRequest("load/authority",data,function(res){
		var config = {
			id : "tg1",
			width : "95%",
			renderTo : "authorityTable",
			headerAlign : "left",
			headerHeight : "30",
			dataAlign : "left",
			indentation : "20",
			hoverRowBackground : "true",
			folderColumnIndex : "1",
			itemClick : "itemClickEvent",
			columns : [ {
				headerText : "ID",
				headerAlign : "center",
				dataAlign : "center",
				width : "50",
				dataField : "id"
			}, {
				headerText : "权限名称",
				dataField : "name",
				headerAlign : "center",
				width : "260px"
			}, {
				headerText : "权限代码",
				dataField : "code",
				headerAlign : "center",
				dataAlign : "center",
				width : "260px"
			}, {
				headerText : "排序",
				dataField : "sort",
				headerAlign : "center",
				dataAlign : "center",
				width : "160px"
			}, {
				headerText : "描述",
				dataField : "details",
				headerAlign : "center",
				dataAlign : "center",
				width : "260px"
			}, {
				headerText : "操作",
				headerAlign : "left",
				dataAlign : "left",
				dataField : "id",
				handler: "customOperater"
			}],
			data : res.aaData
		};
		// 创建一个组件对象
		treeGrid = new TreeGrid(config);
		treeGrid.show();
		treeGrid.expandAll('N');
	});
}
function itemClickEvent(id, index, data) {
//	jQuery("#currentRow").val(
//			id + ", " + index + ", " + TreeGrid.json2str(data));
}
function customOperater(row, col) {
	 if(isnull(editAuth)){
		   return "暂无编辑权限！";
	   }
	return '<div class="action-buttons">'+
	'<a class="blue" href="javascript:void(-1);" onclick=openWin("/customerAuthority/edit?id='+row.id+'","编辑权限") title="编辑"><i class="ace-icon fa fa-pencil bigger-160"></i></a></div>'
}
/*
 * 展开、关闭所有节点。 isOpen=Y表示展开，isOpen=N表示关闭
 */
function expandAll(obj) {
	var biaoshi = $(obj).prop("name");
	var isOpen = "N";
	if(biaoshi=="closeNode"){
		$(obj).html("展开所有节点");
		$(obj).prop("name","openNode");
	}else{
		$(obj).html("关闭所有节点");
		$(obj).prop("name","closeNode");
		isOpen = "Y";
	}
	treeGrid.expandAll(isOpen);
}
function search(){
	var searchs = getSearchGroup();
	var str = "[" + searchs.join(",") + "]";
	var data = {"iDisplayLength":100,"sSearch":str};
	initData(data);
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

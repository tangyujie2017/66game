var roleTable;
$(function(){
	var editAuth = $("#editAuth").val();
	var objData = [{
		"title" : "ID",
		"class" : "center",
		"sortable" : true,
		"visible" : true,
		"width" : "80px",
		"data" : 'id'
	}, {
		"title" : "角色名称",
		"class" : "center",
		"sortable" : true,
		"data" : 'details'
	}, {
		"title" : "角色标识",
		"class" : "center",
		"sortable" : false,
		"data" : 'name'
	}, {
		"title" : "权限",
		"class" : "center",
		"sortable" : false,
		"data" : 'authStr'
	}, {
		"title" : "状态",
		"class" : "center",
		"sortable" : false,
		"visible" : true,
		"data" : 'enable',
		"width" : '150px',
		"mRender":function (data, display, row) {
			if(!data){
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
			   return '<div class="action-buttons"><a class="blue" href="javascript:void(-1);" onclick=openWin("/role/edit?id='+data+'","编辑角色") title="编辑"><i class="ace-icon fa fa-pencil bigger-160"></i></a></div>';
			}
		}];
	roleTable = initTables("roleTable", "load/role", objData, false,false,null, function() {
	});
});
function search(){
	searchButton(roleTable);
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
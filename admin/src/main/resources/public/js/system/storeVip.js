var storeVipTable;
$(function(){
var objData = [
    {'title' : 'ID','class' : 'center','width' : '80px','data' : 'id'},
    {'title' : '姓名','class' : 'center','sortable':false,'data' : 'name'},
    {'title' : '手机','class' : 'center','sortable':false,'data' : 'mobile'},
    {'title' : '关联店铺','class' : 'center','sortable':false,'data' : 'storeName'},
    {'title' : '状态','class' : 'center','sortable':false,'data' : 'enable',
    	"mRender":function (data, display, row) {
    		if(!data){
    			return "<span class='red'>停用</span>";
    		}else{
    			return "启用";
    		}
	}}
];
    storeVipTable = initTables("storeVipTable", "load/storeVip", objData, false,false,null, function() {});
});
function search(){
    searchButton(storeVipTable);
}

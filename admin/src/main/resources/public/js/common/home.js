//菜单状态切换
function siMenu(id,MENU_NAME,MENU_URL){
	top.mainFrame.tabAddHandler(id,MENU_NAME,MENU_URL);
}


function tabAddHandler(mid,mtitle,murl){
	var menuNumbers = $("#tab_menu").find(".tab_item").length;
	if(menuNumbers>10){//控制显示菜单数量，超出最大个数则关闭第一个
		var tabItems = $("#tab_menu").find(".tab_item");
		var menu_id = $(tabItems[1]).attr("id");
		tab.close(menu_id);
	}
	//mask();
	tab.update({
		id :mid,
		title :mtitle,
		url :murl,
		isClosed :true
	});
	tab.add({
		id :mid,
		title :mtitle,
		url :murl,
		isClosed :true
	});
	tab.activate(mid);
}
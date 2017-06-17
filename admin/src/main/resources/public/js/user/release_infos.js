//animate.css动画触动一次方法
$.fn
		.extend({
			animateCss : function(animationName) {
				var animationEnd = 'webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend';
				this.addClass('animated ' + animationName).one(animationEnd,
						function() {
							$(this).removeClass('animated ' + animationName);
						});
			}
		});
/**
 * 显示模态框方法
 * @param targetModel 模态框选择器，jquery选择器
 * @param animateName 弹出动作
 * @ callback 回调方法
 */
var modalShow = function(targetModel, animateName, callback) {
	var animationIn = [ "bounceIn", "bounceInDown", "bounceInLeft",
			"bounceInRight", "bounceInUp", "fadeIn", "fadeInDown",
			"fadeInLeft", "fadeInRight", "fadeOutUp", "fadeInDownBig",
			"fadeInLeftBig", "fadeOutRightBig", "fadeOutUpBig", "flipInX",
			"flipInY", "lightSpeedIn", "rotateIn", "rotateInDownLeft",
			"rotateInDownRight", "rotateInUpLeft", "rotateInUpRight", "zoomIn",
			"zoomInDown", "zoomInLeft", "zoomInRight", "zoomInUp",
			"slideInDown", "slideInLeft", "slideInRight", "slideInUp", "rollIn" ];
	if (!animateName || animationIn.indexOf(animateName) == -1) {
		console.log(animationIn.length);
		var intRandom = Math.floor(Math.random() * animationIn.length);
		animateName = animationIn[intRandom];
	}
	console.log(targetModel + " " + animateName);
	$(targetModel).show().animateCss(animateName);
	//callback.apply(this);
}
/**
 * 隐藏模态框方法
 * @param targetModel 模态框选择器，jquery选择器
 * @param animateName 隐藏动作
 * @ callback 回调方法
 */
var modalHide = function(targetModel, animateName, callback) {
	var animationOut = [ "bounceOut", "bounceOutDown", "bounceOutLeft",
			"bounceOutRight", "bounceOutUp", "fadeOut", "fadeOutDown",
			"fadeOutLeft", "fadeOutRight", "fadeOutUp", "fadeOutDownBig",
			"fadeOutLeftBig", "fadeOutRightBig", "fadeOutUpBig", "flipOutX",
			"flipOutY", "lightSpeedOut", "rotateOut", "rotateOutDownLeft",
			"rotateOutDownRight", "rotateOutUpLeft", "rotateOutUpRight",
			"zoomOut", "zoomOutDown", "zoomOutLeft", "zoomOutRight",
			"zoomOutUp", "zoomOut", "zoomOutDown", "zoomOutLeft",
			"zoomOutRight", "zoomOutUp", "slideOutDown", "slideOutLeft",
			"slideOutRight", "slideOutUp", "rollOut" ];
	if (!animateName || animationOut.indexOf(animateName) == -1) {
		console.log(animationOut.length);
		var intRandom = Math.floor(Math.random() * animationOut.length);
		animateName = animationOut[intRandom];
	}
	$(targetModel).children().click(function(e) {
		e.stopPropagation()
	});
	$(targetModel).animateCss(animateName);
	$(targetModel).delay(900).hide(1, function() {
		$(this).removeClass('animated ' + animateName);
	});
	//callback.apply(this);
}
var modalDataInit = function(info) {
	//alert(info);
	//填充数据，对弹出模态框数据样式初始化或修改
}

$(function() {
	search();
});

function search() {
//	var header = $("meta[name='_csrf_header']").attr("content");
//	var token = $("meta[name='_csrf']").attr("content");
  
	$('#pager').sjAjaxPager({
		url : "/news/list",
		pageSize : 10,
		searchParam : {
			/*
			 * 如果有其他的查询条件，直接在这里传入即可
			 */

			id : 1,
			name : 'test'
		},
		beforeSend : function(xhr) {
			//xhr.setRequestHeader(header, token);
		},
		success : function(data) {

			creatNewsList(data);
		},
		complete : function() {
		}
	});

}
function creatNewsList(data) {
	
	
	var html = "";

	if (data.items.length > 0) {
		var list = data.items;
		for (var i = 0; i < list.length; i++) {
			html += "<tr><td>"
			html += i
			html += "</td>"
			html += "<td>" + list[i].title + "</td>"
			html += "<td>"+list[i].createTime+"</td>"
			html += "<td><a href='#' class='modify' onclick='loadNewsById("+list[i].id+")'>预览</a><a href='#' class='on_delete' onclick='delNews("+list[i].id+")'>删除</a></td>"
			html += "</tr>"
		}

	}
	$("#news_data").html("");
	$("#news_data").html(html);

}
function delNews(news_id){

	var fd = new FormData();
	var header = $("meta[name='_csrf_header']").attr("content");
	var token = $("meta[name='_csrf']").attr("content");
	fd.append('id', news_id);
	$.ajax({
		url : "/news/delNews",
		type : "POST",
		// Form数据
		data : fd,
		cache : false,
		contentType : false,
		processData : false,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		success : function(data) {

			if (data.success) {

				alert("刪除成功");
				search();
			}

		}
	});

}
function loadNewsById(id){
	if(id==""){
		return;
	}

	$.ajax({
		url : "/news/loadNewsById",
		type : 'get',
		data : {
			"newsId" : id
		},
		dataType : 'json',
		timeout : 1000,
		success : function(data, status) {
			console.log(data)
			if (data.success) {
				//
				$("#news_item_title").text(data.result.title);
				$("#news_item_data").html("");
				$("#news_item_data").html(data.result.context);
				modalShow('#bigModal', '', modalDataInit('test'));
				
			} else {
				

			}
		},
		fail : function(err, status) {
			alert("系统错误，请稍后");
		}
	});

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
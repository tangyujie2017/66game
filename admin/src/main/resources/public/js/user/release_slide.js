$(function() {
	search();
	// 图片本地预览
	$('#upload_image').change(function(event) {
		// 根据这个 <input> 获取文件的 HTML5 js 对象
		var files = event.target.files, file;
		if (files && files.length > 0) {
			// 获取目前上传的文件
			file = files[0];
			// 来在控制台看看到底这个对象是什么
			console.log(file);
			// 那么我们可以做一下诸如文件大小校验的动作
			if (file.size > 1024 * 1024 * 2) {
				alert('图片大小不能超过 2MB!');
				return false;
			}
			// !!!!!!
			// 下面是关键的关键，通过这个 file 对象生成一个可用的图像 URL
			// 获取 window 的 URL 工具
			var URL = window.URL || window.webkitURL;
			// 通过 file 生成目标 url
			var imgURL = URL.createObjectURL(file);
			// 用这个 URL 产生一个 <img> 将其显示出来
			$('#image_view').attr('src', imgURL);
			// 使用下面这句可以在内存中释放对此 url 的伺服，跑了之后那个 URL 就无效了
			// URL.revokeObjectURL(imgURL);
		}
	});

	//
	$("#add_slide").click(function() {
		checkSn();
	});

});
function checkSn() {
	if ($("#add_sn").val() == "") {
		$("#add_sn").focus();
		return;
	}else{
		var t=$("#add_sn").val();
		if(!isNaN(t)){
			
			}else{
			  alert("请填写数字");
			  return;
			}
	}
	var upload_image = document.getElementById("upload_image");
	if (upload_image.files.length < 1) {

		alert("没有选择图片");
		return;
	}

	$.ajax({
		url : "slide/check",
		type : 'get',
		data : {
			"sn" : $("#add_sn").val(),
			"type":$("#slide_type").val()
		},
		dataType : 'json',
		timeout : 1000,
		success : function(data, status) {
			console.log(data)
			if (data.success) {
				addSlide();
			} else {
				alert(data.message);

			}
		},
		fail : function(err, status) {
			alert("系统错误，请稍后");
		}
	});

}

function addSlide() {

	var fd = new FormData();

	fd.append('sn', $("#add_sn").val());
	fd.append('type', $("#slide_type").val());
	fd.append('remark', $("#add_remark").val());
	var upload_image = document.getElementById("upload_image");
	for (var i = 0; i < upload_image.files.length; i++) {

		var pic = upload_image.files[i];
		fd.append('slide_file', pic);
	}

	var header = $("meta[name='_csrf_header']").attr("content");
	var token = $("meta[name='_csrf']").attr("content");
	$.ajax({
		url : "slide/addSlide",
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
				$("#add_slide_div").hide();
				$(".deleted_tipsBox,.success_tipsBox,.add_slide_img,.add_user")
						.fadeOut("fast");
				$("#mask,#top_mask").fadeOut("fast");
                //清空值
				$("#add_sn").val("");
				$("#slide_type").val("1");
				$("#add_remark").val("");
				$("#upload_image").val("");
				alert("添加成功");
				search();
			}

		}
	});

}

function search() {

	$('#pager').sjAjaxPager({
		url : "/slide/list",
		pageSize : 10,
		searchParam : {
			/*
			 * 如果有其他的查询条件，直接在这里传入即可
			 */

			id : 1,
			name : 'test'
		},
		beforeSend : function() {

		},
		success : function(data) {

			creatSlideList(data);
		},
		complete : function() {
		}
	});

}
function creatSlideList(data) {
	var html = "";

	if (data.items.length > 0) {
		var list = data.items;
		for (var i = 0; i < list.length; i++) {
			html += "<tr><td>"
			html += list[i].sn
			html += "</td><td><img src='" + data.picPath + "/"
					+ list[i].imgPath + "' /></td>"
					
		 if(list[i].type==1){
			 html += "<td>要闻速递</td>"		 
		 }
		 if(list[i].type==2){
			 html += "<td>A股直击</td>"		 
		 }
		 if(list[i].type==3){
			 html += "<td>名师操盘</td>"		 
		 }
		 if(list[i].type==4){
			html += "<td>黑马池</td>"		 
			 }
			
					
					
			html += "<td>" + list[i].remark + "</td>"

			html += "<td><a href='#' class='on_delete' onclick='delSlide("
					+ list[i].id + ")'>删除</a></td>"
			html += "</tr>"
		}

	}
	$("#slide_data").html("");
	$("#slide_data").html(html);

}

function delSlide(slideid) {
	var fd = new FormData();
	var header = $("meta[name='_csrf_header']").attr("content");
	var token = $("meta[name='_csrf']").attr("content");
	fd.append('id', slideid);
	$.ajax({
		url : "/slide/delSlide",
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
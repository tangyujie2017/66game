$(function() {
	// 新增用户
	search();
	$(".submit_add_user").click(function() {
		checkNewUser();

	});
});

function checkNewUser() {
	var flag = true;

	if ($("#add_user_name").val() == "") {
		flag = false;
		$("#add_user_name").focus();
		return;
	}
	if ($("#add_user_realName").val() == "") {
		flag = false;
		$("#add_user_realName").focus();
		return;
	}
	if ($("#add_user_tel").val() == "") {
		flag = false;
		$("#add_user_tel").focus();
		return;
	}
	if ($("#add_user_pwd").val() == "") {
		flag = false;
		$("#add_user_pwd").focus();
		return;
	}

	if (flag) {

		$.ajax({
			url : "/user/check",
			type : 'get',
			data : {
				"login" : $("#add_user_name").val()
			},
			dataType : 'json',
			timeout : 1000,
			success : function(data, status) {
				console.log(data)
				if (data.result) {
					createUser();
				} else {
					alert("用户名已经存在不能重复添加");

				}
			},
			fail : function(err, status) {
				alert("系统错误，请稍后");
			}
		});

	}

}

function createUser() {
	var header = $("meta[name='_csrf_header']").attr("content");
	var token = $("meta[name='_csrf']").attr("content");
	$.ajax({
		url : "/user/create",
		type : 'post',
		data : {
			"login" : $("#add_user_name").val(),
			"realName" : $("#add_user_realName").val(),
			"mobile" : $("#add_user_tel").val(),
			"password" : $("#add_user_pwd").val()
		},
		dataType : 'json',
		timeout : 1000,
		beforeSend: function(xhr){
	        xhr.setRequestHeader(header, token);
	    },
		success : function(data, status) {
			if (data.success) {
				$("#add_sorts").hide();
				$(".deleted_tipsBox,.success_tipsBox,.add_slide_img,.add_user").fadeOut("fast");
				$("#mask,#top_mask").fadeOut("fast");
				search();
				alert("添加用户成功");
			}
		},
		fail : function(err, status) {
			alert("系统错误，请稍后");
		}
	});

}


function search(){
	
	$('#pager')
	.sjAjaxPager(
			{
				url : "/user/List",
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
					
					creatUserList(data);
				},
				complete : function() {
				}
			});
}



function creatUserList(data) {

	var html = "";
	
	if (data.items.length > 0) {
		var list = data.items;
		for (var i = 0; i < list.length; i++) {
			html += "<tr><td><input type='checkbox' name='checked_box' /></td><td>"
			html += list[i].login
			html += "</td><td>超级管理员</td>"
			html += "<td>" + list[i].realName + "</td>"
			html += "<td>" + list[i].mobile + "</td>"
			html += "<td><a href='#' class='on_delete'>删除</a><a href='#' class='modify'>修改</a><a href='#' class='resetPwd'>重置密码</a></td>"
			html += "</tr>"
		}

	}
	$("#user_data").html("");
	$("#user_data").html(html);

}

	$(function ($) {
		//角色管理 ——> 添加角色
		$(".role_add,.modify,.user_add").on("click",addRoleBox);
		
		
		
		function addRoleBox(){
			$("body").append("<div id='mask'></div>");
			$("#mask").addClass("mask").fadeIn("slow");
			$(".add_role,.add_slide_img,.add_user").fadeIn();
		}
		//关闭box
		$(".close_btn").hover(function () { $(this).css({ color: 'black' }) }, function () { $(this).css({ color: '#999' }) }).on('click', function () {
			$(".deleted_tipsBox,.success_tipsBox,.add_slide_img,.add_user").fadeOut("fast");
			$("#mask,#top_mask").fadeOut("fast");
		});
		$(".cancle_btn").on('click', function () {
			$(".deleted_tipsBox,.success_tipsBox,.add_slide_img,.add_user").fadeOut("fast");
			$("#mask,#top_mask").fadeOut("fast");
		});
		//删除提示信息
		$(".on_delete,.delete").hover(function () {
			$(this).stop().animate({
				opacity: '1'
			}, 600);
		}, function () {
			$(this).stop().animate({
				opacity: '0.85'
			}, 1000);
		}).on('click', function () {
			$("body").append("<div id='top_mask'></div>");
			$("#top_mask").addClass("top_mask").fadeIn("slow");
			$(".deleted_tipsBox,.success_tipsBox").fadeIn("slow");
		});
		//重置密码
		$(".resetPwd,.modify_pwd").hover(function () {
			$(this).stop().animate({
				opacity: '1'
			}, 600);
		}, function () {
			$(this).stop().animate({
				opacity: '0.85'
			}, 1000);
		}).on('click', function () {
			$("body").append("<div id='mask'></div>");
			$("#mask").addClass("mask").fadeIn("slow");
			$(".resetPwd_tipsBox,.modifyPwd_tipsBox").fadeIn("slow");
		});
		//关闭
		$(".close_btn").hover(function () { $(this).css({ color: 'black' }) }, function () { $(this).css({ color: '#999' }) }).on('click', function () {
			$(".resetPwd_tipsBox,.modifyPwd_tipsBox,.add_role").fadeOut("fast");
			$("#mask").fadeOut("fast");
		});
		$(".concle").hover(function(){$(this).css("color","#fff")},function(){$(this).css("color","#fff")});
		
		//后台系统input框输入提示信息
		$(".goods_search input").focus(function(){
			$(this).prev("label").html("");
		});
		$(".goods_search input").blur(function(){
			if($(this).val()==""){
				$(this).prev("label").html($(this).prev("label").attr("default_txt"));
			}
		});
		
		//分页
		$(".page_num:first").css({"color":"#da251c","border":"1px solid #da251c"});
		$(".page_num").click(function(){
			$(this).siblings("a").css({"color":"#666","border":"1px solid #ddd"});
			$(this).css({"color":"#da251c","border":"1px solid #da251c"});
		});
	});
	
	
	
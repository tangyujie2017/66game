package cn.game.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.game.api.contorller.view.CustomerResource;
import cn.game.api.contorller.view.CustomerVo;
import cn.game.api.contorller.view.Resource;
import cn.game.api.controller.req.LoginReq;
import cn.game.api.service.CustomerUserService;
import cn.tz.www.customer.entity.tools.JsonObj;

@Controller
public class CustomerUserController {
	@Autowired
	private CustomerUserService customerUserService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	// 免状态登录
	@GetMapping(value = "/api/customer/login")
	@ResponseBody
	public JsonObj loginCustomer(String login ,String password, HttpServletRequest request,
			HttpServletResponse response) {
         if(login!=null&&!"".equals(login)&&password!=null&&!"".equals(password)){
        	 JsonObj resp = customerUserService.readByMobile(login,password, passwordEncoder);
        	 return resp;
         }else{
        	 //没有用户登陆
         CustomerResource customerResource=new CustomerResource();
        	List<Resource> resource=new ArrayList<>();
        	Resource r1=new Resource("YWSD","要闻速递",false);
      		Resource r2=new Resource("AGZJ","A股直击",false);
      		Resource r3=new Resource("MSCP","名师操盘",false);
      		Resource r4=new Resource("HMC","黑马池",false);
      		Resource r5=new Resource("LCCP","理财产品",false);
      		Resource r6=new Resource("GRZX","个人中心",true);
      		resource.add(r1);
      		resource.add(r2);
      		resource.add(r3);
      		resource.add(r4);
      		resource.add(r5);
      		resource.add(r6);
      		customerResource.setResList(resource);
      		return JsonObj.newSuccessJsonObj("没有用户登录", customerResource);
         }
		 
		
	
		
	   
		

		

	}

	@RequestMapping(value = "/api/customer/loadById")
	@ResponseBody
	public JsonObj loadById(Long customerId) throws Exception {
		if(customerId!=null){
			return customerUserService.LoadCustomerById(customerId);
		}else{
			return JsonObj.newErrorJsonObj("请求参数不正确");
		}
		
	}
  
	@RequestMapping(value = "/api/customer/reset")
	@ResponseBody
	public JsonObj resetPassword(Long customerId,String oldPassword,String newPassword) throws Exception {
		if(customerId!=null&&oldPassword!=null&&!"".equals(oldPassword)&&newPassword!=null&&!"".equals(newPassword)){
			return customerUserService.resetPassword(customerId, oldPassword, newPassword, passwordEncoder);
		}else{
			return JsonObj.newErrorJsonObj("请求参数不正确");
		}
		
	} 	
	
}

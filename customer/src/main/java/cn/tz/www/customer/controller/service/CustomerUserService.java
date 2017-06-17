package cn.tz.www.customer.controller.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import cn.tz.www.customer.entity.tools.JsonObj;
import cn.tz.www.customer.view.CustomerVo;

public interface CustomerUserService {
	public JsonObj readByMobile(String login,String password,PasswordEncoder passwordEncoder);
	public JsonObj resetPassword(Long id , String oldPassword,String newPassword,PasswordEncoder passwordEncoder);
	public JsonObj LoadCustomerById(Long id);
}

package cn.game.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import cn.game.api.contorller.view.CustomerVo;
import cn.game.core.tools.JsonObj;

public interface CustomerUserService {
	public JsonObj readByMobile(String login,String password,PasswordEncoder passwordEncoder);
	public JsonObj resetPassword(Long id , String oldPassword,String newPassword,PasswordEncoder passwordEncoder);
	public JsonObj LoadCustomerById(Long id);
}

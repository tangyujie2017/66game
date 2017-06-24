package cn.game.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cn.game.api.contorller.view.CustomerResource;
import cn.game.api.contorller.view.Resource;
import cn.game.api.service.CustomerUserService;
import cn.game.core.repository.customer.CustomerRepository;
import cn.game.core.repository.customerRole.CustomerRoleRepository;
import cn.game.core.table.Customer;
import cn.game.core.tools.Groups;
import cn.game.core.tools.JsonObj;

@Service
public class CustomerUserServiceImpl implements CustomerUserService {
	@Autowired(required = true)
	private CustomerRepository customerRepository;
	
	
	
	private  static List<Resource> resource=new ArrayList<>();
	static{
		//可以写进配置项
		initResource();
	}

	private static void initResource(){
		Resource r1=new Resource("YWSD","要闻速递",false);
		Resource r2=new Resource("AGZJ","A股直击",false);
		Resource r3=new Resource("MSCP","名师操盘",false);
		Resource r4=new Resource("HMC","黑马池",false);
		Resource r5=new Resource("LCCP","理财产品",false);
		Resource r6=new Resource("GRZX","个人中心",false);
		resource.add(r1);
		resource.add(r2);
		resource.add(r3);
		resource.add(r4);
		resource.add(r5);
		resource.add(r6);
		
	}
	

	

	public JsonObj readByMobile(String login, String password,PasswordEncoder passwordEncoder) {
	
		
		
		Optional<Customer> customer = customerRepository.findByLogin(login);
		//检查密码
		if (customer.isPresent()) {
			Customer user= customer.get();
			if(user.isLocked()){
				return JsonObj.newErrorJsonObj("账号已经被锁定,无法登录");
			}
		    if(user.getServerTime()!=null){
			if(user.getServerTime().before(new Date())){
				return JsonObj.newErrorJsonObj("账号已过期,无法登录");
				
			}
			}
			if(passwordEncoder.matches(password, user.getPassword())){
				CustomerResource customerResource=new CustomerResource();
				customerResource.setId(user.getId());
				customerResource.setLogin(user.getLogin());
				customerResource.setUserName(user.getRealName());
				customerResource.setPassword("");
				customerResource.setResList(resource);//全部不能访问
				Map<String,Resource>map=new HashMap<>();
				//找出角色对应能访问的资源
				user.getRoles().stream().forEach(a->{
					a.getAuthoritys().stream().forEach(b->{
						Resource r=new Resource();
						r.setIsVisit(true);
						r.setResourceCode(b.getCode());
						r.setResourceName(b.getName());
						map.put(b.getCode(), r);
					});
				});
				List<Resource> resourceList=new ArrayList<>();
				customerResource.getResList().stream().forEach(a->{
					if(map.get(a.getResourceCode())!=null){
						resourceList.add(map.get(a.getResourceCode()));
					}else{
						resourceList.add(a);
					}
					
				});
				
				customerResource.setResList(resourceList);
				
				
				return JsonObj.newSuccessJsonObj("登录成功", customerResource);
			}else{
				return JsonObj.newErrorJsonObj("密码不正确");
			}
			
			
		
			
		
		}
	
		
		return JsonObj.newErrorJsonObj("用户名不存在");
	}


	@Transactional
	public JsonObj resetPassword(Long id , String oldPassword,String newPassword,PasswordEncoder passwordEncoder){
		Customer user = customerRepository.find(id);
		
		if(user!=null){
			if(passwordEncoder.matches(oldPassword, user.getPassword())){
				user.setPassword(passwordEncoder.encode(newPassword));
				customerRepository.update(user);
				return JsonObj.newSuccessJsonObj("修改密码成功");	
				
			}else{
				return JsonObj.newErrorJsonObj("旧密码输入不正确");	
			}
		}else{
			return JsonObj.newErrorJsonObj("用户不存在,请联系管理员");
		}
		}


	@Override
	public JsonObj LoadCustomerById(Long id) {
		Customer user = customerRepository.find(id);
		//检查密码
		if (user!=null) {
		
			if(user.isLocked()){
				return JsonObj.newErrorJsonObj("账号已经被锁定,无法登录");
			}
			if(user.getServerTime()!=null){
			if(user.getServerTime().before(new Date())){
				return JsonObj.newErrorJsonObj("账号已过期,无法登录");
				
			}}
			CustomerResource customerResource=new CustomerResource();
			customerResource.setId(user.getId());
			customerResource.setLogin(user.getLogin());
			customerResource.setUserName(user.getRealName());
			customerResource.setPassword("");
			customerResource.setResList(resource);//全部不能访问
			Map<String,Resource>map=new HashMap<>();
			//找出角色对应能访问的资源
			user.getRoles().stream().forEach(a->{
				a.getAuthoritys().stream().forEach(b->{
					Resource r=new Resource();
					r.setIsVisit(true);
					r.setResourceCode(b.getCode());
					r.setResourceName(b.getName());
					map.put(b.getCode(), r);
				});
			});
			List<Resource> resourceList=new ArrayList<>();
			customerResource.getResList().stream().forEach(a->{
				if(map.get(a.getResourceCode())!=null){
					resourceList.add(map.get(a.getResourceCode()));
				}else{
					resourceList.add(a);
				}
				
			});
			
			customerResource.setResList(resourceList);
			
			
			return JsonObj.newSuccessJsonObj("登录成功", customerResource);
		
			
			
		
			
		
		}

		return JsonObj.newErrorJsonObj("用户不存在,请联系客服");
		
	}
}

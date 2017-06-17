package cn.tz.www.admin.controller.customer;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import cn.tz.www.admin.controller.cmd.CustomerAddCmd;
import cn.tz.www.admin.controller.cmd.CustomerAddCmdValidator;
import cn.tz.www.admin.controller.cmd.CustomerEditCmd;
import cn.tz.www.admin.controller.service.CustomerService;
import cn.tz.www.admin.controller.service.detail.system.CustomerDetails;
import cn.tz.www.admin.controller.service.detail.system.CustomerRoleDetails;
import cn.tz.www.admin.controller.service.req.CreateReq;
import cn.tz.www.admin.controller.service.req.CreateResp;
import cn.tz.www.admin.controller.service.req.Resp;
import cn.tz.www.admin.controller.service.req.UpdateReq;
import cn.tz.www.admin.controller.util.AuthorityUtil;
import cn.tz.www.admin.controller.util.TableVo;
import cn.tz.www.customer.entity.table.Customer;
import cn.tz.www.customer.entity.table.User;
import cn.tz.www.customer.entity.tools.CommonUtil;
import cn.tz.www.customer.entity.tools.Constants;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.JsonObj;
import cn.tz.www.customer.entity.tools.Page;
import cn.tz.www.customer.entity.tools.PropertyFilter.MatchType;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzc on 15/11/2016.
 */
@RequestMapping("/customer")
@Controller
public class CustomerController {
	
	private static Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@ModelAttribute("customerAddCmd")
	private CustomerAddCmd customerAddCmd() {
		return new CustomerAddCmd();
	}

	@InitBinder("customerAddCmd")
	protected void initUserAddCmdBinder(WebDataBinder binder) {
		binder.addValidators(new CustomerAddCmdValidator());
	}

	@GetMapping("/")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_USER_VIEW')")
	public String index(Model model) {
		Groups groups = new Groups();
		List<CustomerRoleDetails> roles = customerService.findRoleByGroups(groups);
		model.addAttribute("allRoles", roles);
		return "customer/user";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "load/user")
	@ResponseBody
	public TableVo loadUser(@Valid TableVo tableVo, BindingResult result, HttpServletRequest request) {
		if (result.hasErrors()) {
			tableVo.setAaData(new ArrayList<>());
			tableVo.setiTotalDisplayRecords(0);
			tableVo.setiTotalRecords(0);
			return tableVo;
		}
		int pageSize = tableVo.getiDisplayLength();
		int index = tableVo.getiDisplayStart();
		int currentPage = index / pageSize + 1;
		String params = tableVo.getsSearch();
		int col = tableVo.getiSortCol_0();
		String dir = tableVo.getsSortDir_0();
		String colname = request.getParameter("mDataProp_" + col);

		Page<Customer> page = new Page<Customer>(pageSize, currentPage);
		Groups groups = CommonUtil.filterGroup(params);
		groups.setOrderby("user."+colname);
		if ("desc".equals(dir)) {
			groups.setOrder(false);
		} else {
			groups.setOrder(true);
		}
		
		customerService.findUserPageByGroups(groups, page);
		page.setItems(Customer.toDetailsList(page.getItems()));

		int total = page.getTotalCount();
		tableVo.setAaData(page.getItems());
		tableVo.setiTotalDisplayRecords(total);
		tableVo.setiTotalRecords(total);

		return tableVo;
	}
	
	@RequestMapping(value = "findUserList")
	@ResponseBody
	public Page findUserList(String term,Integer pageSize,Integer pageNo){
		Page<User> page = new Page<User>(pageSize, pageNo);
		Groups groups = new Groups();
		groups.Add("user.locked",false);
		if(!CommonUtil.isNull(term)){
			groups.Add("user.realName",term,MatchType.LIKE);
		}
		CustomerRoleDetails role = customerService.findByDetails(Constants.STAFF_USER);
		if(role != null){
			groups.Add("role.id",role.getId());
		}
		customerService.findUserPageByGroups(groups, page);
		page.setItems(User.toSelect2VoList(page.getItems()));
		return page;
	}
	
	@RequestMapping(value = "findAllUserList")
	@ResponseBody
	public Page findAllUserList(String term,String userIds,Integer pageSize,Integer pageNo){
		Page<User> page = new Page<User>(pageSize, pageNo);
		Groups groups = new Groups();
		groups.Add("user.enable",true);
		groups.Add("user.locked",false);
		if(!CommonUtil.isNull(term)){
			groups.Add("user.login",term,MatchType.LIKE);
		}
		if(!CommonUtil.isNull(userIds)){
			String[] ids = userIds.split(",");
			List<Long> uids = new ArrayList<>();
			for(String id : ids){
				uids.add(Long.parseLong(id));
			}
			if(!uids.isEmpty()){
				groups.Add("user.id",uids,MatchType.NOTIN);//过滤掉一些用户
			}
		}
		customerService.findUserPageByGroups(groups, page);
		page.setItems(User.toSelect2NameVoList(page.getItems()));
		return page;
	}
	
	@RequestMapping(value = "check/mobile")
	@ResponseBody
	public Boolean checkMobile(Long id, String mobile)
	{
		Boolean valid = true;
		CustomerDetails user = customerService.findUserByField("mobile", mobile);
		if (user != null)
		{
			if (user.getId() == id) {//修改时
				return true;
			}
			valid = false;
		}

		return valid;
	}
	
	@RequestMapping(value = "update/password")
	@ResponseBody
	public JsonObj updatePassword(@Valid CustomerAddCmd userAddCmd, BindingResult result){
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		try {
			Long userId = userAddCmd.getId();
			if(userId == null){
				CustomerDetails userDetails = customerService.findUserByField("login", AuthorityUtil.getLoginUsername());
				userId = userDetails.getId();
			}
			String newPassword = passwordEncoder.encode(userAddCmd.getPassword());
			
			customerService.updatePassword(userId, newPassword);
			logger.info("修改用户密码："+userId);
			return JsonObj.newSuccessJsonObj("修改成功");
		} catch (Exception e) {
			logger.error("修改用户密码失败："+e.toString());
			return JsonObj.newSuccessJsonObj("修改失败");
		}
	}

	@GetMapping("/add")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_USER_ADD')")
	public String addForm(Model model) {
		Groups groups = new Groups();
		List<CustomerRoleDetails> roles = customerService.findRoleByGroups(groups);
		model.addAttribute("allRoles", roles);
		return "customer/user_add";
	}

	@PostMapping("/add")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_USER_ADD')")
	@ResponseBody
	public JsonObj addSubmit(@Valid CustomerAddCmd userAddCmd, BindingResult result, Model model) {
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		String newPassword = passwordEncoder.encode(userAddCmd.getPassword());
		userAddCmd.setPassword(newPassword);
		CreateReq<CustomerDetails> req = new CreateReq<>(userAddCmd.toDetails());
		CreateResp<CustomerDetails> resp = customerService.create(req);
		if (resp.getStatus() == Resp.SUCCESS) {
			logger.info("create user:"+userAddCmd.getMobile());
			return JsonObj.newSuccessJsonObj("保存成功");
		}
		return JsonObj.newSuccessJsonObj("保存失败");
	}

	@GetMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_USER_EDIT')")
	public String editForm(Long id,Model model) {
		CustomerDetails userDetails = customerService.findUser(id);
		CustomerEditCmd userEditCmd = CustomerEditCmd.fromDetails(userDetails);
		Groups groups = new Groups();
		
		List<CustomerRoleDetails> roles = customerService.findRoleByGroups(groups);
		model.addAttribute("allRoles", roles);
		
		model.addAttribute("customerEditCmd", userEditCmd);
		return "customer/user_edit";
	}

	@PostMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_USER_EDIT')")
	@ResponseBody
	public JsonObj editSubmit(@Valid CustomerEditCmd userEditCmd, BindingResult result,Model model) {
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		try {
			UpdateReq<CustomerDetails> req = new UpdateReq<CustomerDetails>(userEditCmd.toDetails());
			customerService.updateUser(req);
			return JsonObj.newSuccessJsonObj("保存成功");
		} catch (Exception e) {
			logger.error("修改用户失败："+e.toString());
			return JsonObj.newSuccessJsonObj("保存失败");
		}
	}

}

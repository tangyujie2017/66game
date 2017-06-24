package cn.game.admin.controller.system;


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

import cn.game.admin.controller.cmd.UserAddCmd;
import cn.game.admin.controller.cmd.UserAddCmdValidator;
import cn.game.admin.controller.cmd.UserEditCmd;
import cn.game.admin.controller.service.UserService;
import cn.game.admin.controller.service.req.CreateReq;
import cn.game.admin.controller.service.req.CreateResp;
import cn.game.admin.controller.service.req.Resp;
import cn.game.admin.controller.service.req.UpdateReq;
import cn.game.admin.util.AuthorityUtil;
import cn.game.admin.util.TableVo;
import cn.game.core.service.detail.system.RoleDetails;
import cn.game.core.service.detail.system.UserDetails;
import cn.game.core.table.User;
import cn.game.core.tools.CommonUtil;
import cn.game.core.tools.Constants;
import cn.game.core.tools.Groups;
import cn.game.core.tools.JsonObj;
import cn.game.core.tools.Page;
import cn.game.core.tools.PropertyFilter.MatchType;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzc on 15/11/2016.
 */
@RequestMapping("/user")
@Controller
public class UserController {
	
	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@ModelAttribute("userAddCmd")
	private UserAddCmd userAddCmd() {
		return new UserAddCmd();
	}

	@InitBinder("userAddCmd")
	protected void initUserAddCmdBinder(WebDataBinder binder) {
		binder.addValidators(new UserAddCmdValidator());
	}

	@GetMapping("/")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_USER_VIEW')")
	public String index(Model model) {
		Groups groups = new Groups();
		List<RoleDetails> roles = userService.findRoleByGroups(groups);
		model.addAttribute("allRoles", roles);
		return "system/user";
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

		Page<User> page = new Page<User>(pageSize, currentPage);
		Groups groups = CommonUtil.filterGroup(params);
		groups.setOrderby("user."+colname);
		if ("desc".equals(dir)) {
			groups.setOrder(false);
		} else {
			groups.setOrder(true);
		}
		
		userService.findUserPageByGroups(groups, page);
		page.setItems(User.toDetailsList(page.getItems()));

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
		groups.Add("user.enable",true);
		groups.Add("user.locked",false);
		if(!CommonUtil.isNull(term)){
			groups.Add("user.realName",term,MatchType.LIKE);
		}
		RoleDetails role = userService.findByDetails(Constants.STAFF_USER);
		if(role != null){
			groups.Add("role.id",role.getId());
		}
		userService.findUserPageByGroups(groups, page);
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
		userService.findUserPageByGroups(groups, page);
		page.setItems(User.toSelect2NameVoList(page.getItems()));
		return page;
	}
	
	@RequestMapping(value = "check/mobile")
	@ResponseBody
	public Boolean checkMobile(Long id, String mobile)
	{
		Boolean valid = true;
		UserDetails user = userService.findUserByField("mobile", mobile);
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
	public JsonObj updatePassword(@Valid UserAddCmd userAddCmd, BindingResult result){
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
				UserDetails userDetails = userService.findUserByField("login", AuthorityUtil.getLoginUsername());
				userId = userDetails.getId();
			}
			String newPassword = passwordEncoder.encode(userAddCmd.getPassword());
			
			userService.updatePassword(userId, newPassword);
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
		List<RoleDetails> roles = userService.findRoleByGroups(groups);
		model.addAttribute("allRoles", roles);
		return "system/user_add";
	}

	@PostMapping("/add")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_USER_ADD')")
	@ResponseBody
	public JsonObj addSubmit(@Valid UserAddCmd userAddCmd, BindingResult result, Model model) {
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
		CreateReq<UserDetails> req = new CreateReq<>(userAddCmd.toDetails());
		CreateResp<UserDetails> resp = userService.create(req);
		if (resp.getStatus() == Resp.SUCCESS) {
			logger.info("create user:"+userAddCmd.getMobile());
			return JsonObj.newSuccessJsonObj("保存成功");
		}
		return JsonObj.newSuccessJsonObj("保存失败");
	}

	@GetMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_USER_EDIT')")
	public String editForm(Long id,Model model) {
		UserDetails userDetails = userService.findUser(id);
		UserEditCmd userEditCmd = UserEditCmd.fromDetails(userDetails);
		Groups groups = new Groups();
		
		List<RoleDetails> roles = userService.findRoleByGroups(groups);
		model.addAttribute("allRoles", roles);
		
		model.addAttribute("userEditCmd", userEditCmd);
		return "system/user_edit";
	}

	@PostMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_USER_EDIT')")
	@ResponseBody
	public JsonObj editSubmit(@Valid UserEditCmd userEditCmd, BindingResult result,Model model) {
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		try {
			UpdateReq<UserDetails> req = new UpdateReq<UserDetails>(userEditCmd.toDetails());
			userService.updateUser(req);
			return JsonObj.newSuccessJsonObj("保存成功");
		} catch (Exception e) {
			logger.error("修改用户失败："+e.toString());
			return JsonObj.newSuccessJsonObj("保存失败");
		}
	}

}

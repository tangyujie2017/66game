package cn.tz.www.admin.controller.system;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tz.www.admin.controller.cmd.system.RoleCmd;
import cn.tz.www.admin.controller.service.UserService;
import cn.tz.www.admin.controller.service.detail.system.AuthorityDetails;
import cn.tz.www.admin.controller.service.detail.system.RoleDetails;
import cn.tz.www.admin.controller.util.TableVo;
import cn.tz.www.customer.entity.table.Role;
import cn.tz.www.customer.entity.tools.CommonUtil;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.JsonObj;
import cn.tz.www.customer.entity.tools.Page;
import cn.tz.www.customer.entity.tools.PropertyFilter.MatchType;



@RequestMapping("/role")
@Controller
public class RoleController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_ROLE_VIEW')")
	public String index() {
		
		return "system/role";
	}
	
	@RequestMapping(value = "load/role")
	@ResponseBody
	public TableVo loadRole(@Valid TableVo tableVo, BindingResult result, HttpServletRequest request){
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
		Groups groups = CommonUtil.filterGroup(params);
//		groups.Add("enable",true);
		groups.setOrderby(colname);
		if ("desc".equals(dir)) {
			groups.setOrder(false);
		} else {
			groups.setOrder(true);
		}
		Page<Role> page = new Page<Role>(pageSize,currentPage);
		userService.findRolePageByGroups(groups, page);
		page.setItems(Role.toDetailsList(page.getItems()));
		int total = page.getTotalCount();
		tableVo.setAaData(page.getItems());
		tableVo.setiTotalDisplayRecords(total);
		tableVo.setiTotalRecords(total);
		return tableVo;
	}
	
	@GetMapping("/add")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_ROLE_ADD')")
	public String addForm() {
		return "system/role_add";
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_ROLE_ADD')")
	@ResponseBody
	public JsonObj addSubmit(@Valid RoleCmd roleCmd, BindingResult result){
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		try {
			RoleDetails roleDetails = roleCmd.toDetails();
			userService.saveRole(roleDetails);
			return JsonObj.newSuccessJsonObj("保存成功");
		} catch (Exception e) {
			return JsonObj.newSuccessJsonObj("保存失败："+e.toString());
		}
	}
	
	@GetMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_ROLE_EDIT')")
	public String editForm(Long id,Model model) {
		RoleDetails roleDetails = userService.findRole(id);
		Groups groups = new Groups();
		groups.Add("parent","",MatchType.NULL);
		groups.setOrder(true);
		groups.setOrderby("sort");
		List<AuthorityDetails> authoritys = userService.findAuthorityByGroups(groups);
		model.addAttribute("authoritys", authoritys);
		model.addAttribute("roleCmd", RoleCmd.fromDetails(roleDetails));
		return "system/role_edit";
	}
	
	@PostMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_ROLE_EDIT')")
	@ResponseBody
	public JsonObj editSubmit(@Valid RoleCmd roleCmd, BindingResult result){
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		try {
			RoleDetails roleDetails = roleCmd.toDetails();
			userService.updateRole(roleDetails);
			return JsonObj.newSuccessJsonObj("保存成功");
		} catch (Exception e) {
			return JsonObj.newSuccessJsonObj("保存失败："+e.toString());
		}
	}

}

package cn.tz.www.admin.controller.customer;

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

import cn.tz.www.admin.controller.cmd.customer.CustomerRoleCmd;
import cn.tz.www.admin.controller.service.CustomerService;
import cn.tz.www.admin.controller.service.detail.system.CustomerAuthorityDetails;
import cn.tz.www.admin.controller.service.detail.system.CustomerRoleDetails;
import cn.tz.www.admin.controller.util.TableVo;
import cn.tz.www.customer.entity.table.CustomerRole;
import cn.tz.www.customer.entity.table.Role;
import cn.tz.www.customer.entity.tools.CommonUtil;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.JsonObj;
import cn.tz.www.customer.entity.tools.Page;
import cn.tz.www.customer.entity.tools.PropertyFilter.MatchType;



@RequestMapping("/customerRole")
@Controller
public class CustomerRoleController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','customer_ROLE_VIEW')")
	public String index() {
		
		return "customer/role";
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
		Page<CustomerRole> page = new Page<CustomerRole>(pageSize,currentPage);
		customerService.findRolePageByGroups(groups, page);
		page.setItems(CustomerRole.toDetailsList(page.getItems()));
		int total = page.getTotalCount();
		tableVo.setAaData(page.getItems());
		tableVo.setiTotalDisplayRecords(total);
		tableVo.setiTotalRecords(total);
		return tableVo;
	}
	
	@GetMapping("/add")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','customer_ROLE_ADD')")
	public String addForm() {
		return "customer/role_add";
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','customer_ROLE_ADD')")
	@ResponseBody
	public JsonObj addSubmit(@Valid CustomerRoleCmd roleCmd, BindingResult result){
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		try {
			CustomerRoleDetails roleDetails = roleCmd.toDetails();
			customerService.saveRole(roleDetails);
			return JsonObj.newSuccessJsonObj("保存成功");
		} catch (Exception e) {
			return JsonObj.newSuccessJsonObj("保存失败："+e.toString());
		}
	}
	
	@GetMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','customer_ROLE_EDIT')")
	public String editForm(Long id,Model model) {
		CustomerRoleDetails roleDetails = customerService.findRole(id);
		Groups groups = new Groups();
		groups.Add("parent","",MatchType.NULL);
		groups.setOrder(true);
		groups.setOrderby("sort");
		List<CustomerAuthorityDetails> authoritys = customerService.findAuthorityByGroups(groups);
		model.addAttribute("authoritys", authoritys);
		model.addAttribute("roleCmd", CustomerRoleCmd.fromDetails(roleDetails));
		return "customer/role_edit";
	}
	
	@PostMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','customer_ROLE_EDIT')")
	@ResponseBody
	public JsonObj editSubmit(@Valid CustomerRoleCmd roleCmd, BindingResult result){
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		try {
			CustomerRoleDetails roleDetails = roleCmd.toDetails();
			customerService.updateRole(roleDetails);
			return JsonObj.newSuccessJsonObj("保存成功");
		} catch (Exception e) {
			return JsonObj.newSuccessJsonObj("保存失败："+e.toString());
		}
	}

}

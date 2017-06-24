package cn.game.admin.controller.system;

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

import cn.game.admin.controller.cmd.system.AuthorityCmd;
import cn.game.admin.controller.service.UserService;
import cn.game.admin.util.TableVo;
import cn.game.admin.util.ZtreeVo;
import cn.game.core.service.detail.system.AuthorityDetails;
import cn.game.core.table.Authority;
import cn.game.core.tools.CommonUtil;
import cn.game.core.tools.Groups;
import cn.game.core.tools.JsonObj;
import cn.game.core.tools.Page;
import cn.game.core.tools.PropertyFilter.MatchType;


@RequestMapping("/authority")
@Controller
public class AuthorityController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_AUTHORITY_VIEW')")
	public String index() {
		
		return "system/authority";
	}
	
	@RequestMapping(value = "load/authority")
	@ResponseBody
	public TableVo loadAuthority(@Valid TableVo tableVo, BindingResult result, HttpServletRequest request){
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
		groups.Add("parent","",MatchType.NULL);
		groups.setOrderby(colname);
		groups.setOrder(true);
		groups.setOrderby("sort");
		Page<Authority> page = new Page<Authority>(pageSize,currentPage);
		userService.findAuthorityPageByGroups(groups, page);
		int total = page.getTotalCount();
		tableVo.setAaData(Authority.toDetailsList(page.getItems()));
		tableVo.setiTotalDisplayRecords(total);
		tableVo.setiTotalRecords(total);
		return tableVo;
	}
	
	@RequestMapping(value = "findAuthorityForTree")
	@ResponseBody
	public List<ZtreeVo> findAuthorityForTree(Long id)
	{
		List<ZtreeVo> ztreeVos = new ArrayList<ZtreeVo>();
		Groups groups = new Groups();
		if(id == null || id == 0)
		{
			groups.Add("parent","",MatchType.NULL);
		}
		else
		{
			groups.Add("parent.id", id);
		}
		groups.setOrder(true);
		groups.setOrderby("sort");
		List<AuthorityDetails> authorityDetailses = userService.findAuthorityByGroups(groups);
		for(AuthorityDetails authorityDetails : authorityDetailses)
		{
			ZtreeVo ztree = new ZtreeVo();
			ztree.setId(authorityDetails.getId());
			ztree.setName(authorityDetails.getName());
			if(id == null || id == 0)
			{
				ztree.setIsParent(false);//暂时只展示顶级分类
			}
			ztreeVos.add(ztree);
		}
		
		return ztreeVos;
	}
	
	@GetMapping("/add")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_AUTHORITY_ADD')")
	public String addForm() {
		return "system/authority_add";
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_AUTHORITY_ADD')")
	@ResponseBody
	public JsonObj addSubmit(@Valid AuthorityCmd authorityCmd, BindingResult result){
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		try {
			AuthorityDetails authorityDetails = authorityCmd.toDetails();
			userService.saveAuthority(authorityDetails);
			return JsonObj.newSuccessJsonObj("保存成功");
		} catch (Exception e) {
			return JsonObj.newSuccessJsonObj("保存失败："+e.toString());
		}
	}
	
	@GetMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_AUTHORITY_EDIT')")
	public String editForm(Long id,Model model) {
		AuthorityDetails authorityDetails = userService.findAuthority(id);
		
		model.addAttribute("authorityDetails", authorityDetails);
		return "system/authority_edit";
	}
	
	@PostMapping("/edit")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SYSTEM_AUTHORITY_EDIT')")
	@ResponseBody
	public JsonObj editSubmit(@Valid AuthorityCmd authorityCmd, BindingResult result){
		if (result.hasErrors()) {
			StringBuffer errorMsg = new StringBuffer();
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				errorMsg.append(error.getDefaultMessage()+"<br/>");
			}
			return JsonObj.newErrorJsonObj(errorMsg.toString());
		}
		try {
			AuthorityDetails authorityDetails = authorityCmd.toDetails();
			userService.updateAuthority(authorityDetails);
			return JsonObj.newSuccessJsonObj("保存成功");
		} catch (Exception e) {
			return JsonObj.newSuccessJsonObj("保存失败："+e.toString());
		}
	}

}

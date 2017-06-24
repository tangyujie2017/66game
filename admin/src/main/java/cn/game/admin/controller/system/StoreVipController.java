package cn.game.admin.controller.system;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;


@RequestMapping("/storeVip")
@Controller
public class StoreVipController {
//	
//	@Autowired
//	private StoreVipService storeVipService;
//
//	@PreAuthorize("hasAnyAuthority('SYSTEM_STOREVIP')")
//    @RequestMapping("/")
//    public String index() {
//        return "system/storeVip";
//    }
//
//    @RequestMapping("load/storeVip")
//    @ResponseBody
//    public TableVo loadStoreVip(@Valid TableVo tableVo, BindingResult result, HttpServletRequest request) {
//        if (result.hasErrors()) {
//            tableVo.setAaData(new ArrayList<>());
//            tableVo.setiTotalDisplayRecords(0);
//            tableVo.setiTotalRecords(0);
//            return tableVo;
//        }
//        int pageSize = tableVo.getiDisplayLength();
//        int index = tableVo.getiDisplayStart();
//        int currentPage = index / pageSize + 1;
//        String params = tableVo.getsSearch();
//        int col = tableVo.getiSortCol_0();
//        String dir = tableVo.getsSortDir_0();
//        String colname = request.getParameter("mDataProp_" + col);
//        Groups groups = CommonUtil.filterGroup(params);
//        groups.setOrderby(colname);
//        if ("desc".equals(dir)) {
//            groups.setOrder(false);
//        } else {
//            groups.setOrder(true);
//        }
//        Page<StoreVip> page = new Page<StoreVip>(pageSize,currentPage);
//        storeVipService.findStoreVipPageByGroups(groups, page);
//        page.setItems(StoreVip.toStoreVipRespList(page.getItems()));
//        int total = page.getTotalCount();
//        tableVo.setAaData(page.getItems());
//        tableVo.setiTotalDisplayRecords(total);
//        tableVo.setiTotalRecords(total);
//        return tableVo;
//    }
//
}
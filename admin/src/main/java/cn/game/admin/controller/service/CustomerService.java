package cn.game.admin.controller.service;

import java.util.List;

import cn.game.admin.controller.service.req.CreateReq;
import cn.game.admin.controller.service.req.CreateResp;
import cn.game.admin.controller.service.req.DeleteReq;
import cn.game.admin.controller.service.req.DeleteResp;
import cn.game.admin.controller.service.req.ReadListResp;
import cn.game.admin.controller.service.req.ReadReq;
import cn.game.admin.controller.service.req.ReadResp;
import cn.game.admin.controller.service.req.Resp;
import cn.game.admin.controller.service.req.UpdateReq;
import cn.game.core.service.detail.system.CustomerAuthorityDetails;
import cn.game.core.service.detail.system.CustomerDetails;
import cn.game.core.service.detail.system.CustomerRoleDetails;
import cn.game.core.table.Customer;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;

public interface CustomerService {


	  CreateResp<CustomerDetails> create(CreateReq<CustomerDetails> req);
	  ReadResp<CustomerDetails, Long> read(ReadReq<Long> req);
	  ReadListResp<CustomerDetails> readAll();
	  void updateUser(UpdateReq<CustomerDetails> req) throws Exception;
	  DeleteResp<CustomerDetails, Long> delete(DeleteReq<Long> req);
	  ReadResp<CustomerDetails, String> readByMobile(ReadReq<String> req);


	  long findTotalCountByGroups(Groups groups);
	  Page findUserPageByGroups(Groups groups, Page page);
	  void updatePassword(Long id, String password) throws Exception;
	  CustomerDetails findUser(Long id);
	  CustomerDetails findUserByField(String propertyName, Object value);
	  
	  CustomerRoleDetails findRole(Long id);
	  CustomerRoleDetails findByDetails(String details);
	  List<CustomerRoleDetails> findRoleByGroups(Groups groups);
	  Page findRolePageByGroups(Groups groups, Page page);
	  void saveRole(CustomerRoleDetails roleDetails) throws Exception;
	  void updateRole(CustomerRoleDetails roleDetails) throws Exception;
	  
	  CustomerAuthorityDetails findAuthority(Long id);
	  Page findAuthorityPageByGroups(Groups groups, Page page);
	  List<CustomerAuthorityDetails> findAuthorityByGroups(Groups groups);
	  void saveAuthority(CustomerAuthorityDetails authorityDetails) throws Exception;
	  void updateAuthority(CustomerAuthorityDetails authorityDetails) throws Exception;
	  
	  Integer excuteSql(String sql);
	  Resp deleteAll();
	  //原来的
	 public Boolean checkUserName(String login);
	 public void createUser(Customer user)throws Exception;
	 public Page<Customer> loadUserList(Groups groups, Page<Customer> page);
	 public Customer loadUserByLogin(String login);
	  

}

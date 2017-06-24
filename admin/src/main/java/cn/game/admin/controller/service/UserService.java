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
import cn.game.core.service.detail.system.AuthorityDetails;
import cn.game.core.service.detail.system.RoleDetails;
import cn.game.core.service.detail.system.UserDetails;
import cn.game.core.table.User;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;

public interface UserService {

	  CreateResp<UserDetails> create(CreateReq<UserDetails> req);
	  ReadResp<UserDetails, Long> read(ReadReq<Long> req);
	  ReadListResp<UserDetails> readAll();
	  void updateUser(UpdateReq<UserDetails> req) throws Exception;
	  DeleteResp<UserDetails, Long> delete(DeleteReq<Long> req);
	  ReadResp<UserDetails, String> readByMobile(ReadReq<String> req);


	  long findTotalCountByGroups(Groups groups);
	  Page findUserPageByGroups(Groups groups, Page page);
	  void updatePassword(Long id, String password) throws Exception;
	  UserDetails findUser(Long id);
	  UserDetails findUserByField(String propertyName, Object value);
	  
	  RoleDetails findRole(Long id);
	  RoleDetails findByDetails(String details);
	  List<RoleDetails> findRoleByGroups(Groups groups);
	  Page findRolePageByGroups(Groups groups, Page page);
	  void saveRole(RoleDetails roleDetails) throws Exception;
	  void updateRole(RoleDetails roleDetails) throws Exception;
	  
	  AuthorityDetails findAuthority(Long id);
	  Page findAuthorityPageByGroups(Groups groups, Page page);
	  List<AuthorityDetails> findAuthorityByGroups(Groups groups);
	  void saveAuthority(AuthorityDetails authorityDetails) throws Exception;
	  void updateAuthority(AuthorityDetails authorityDetails) throws Exception;
	  
	  Integer excuteSql(String sql);
	  Resp deleteAll();
	  //原来的
	  public Boolean checkUserName(String login);
		public void createUser(User user)throws Exception;
		public Page<User> loadUserList(Groups groups, Page<User> page);
		public User loadUserByLogin(String login);
	  
}

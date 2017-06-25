package cn.game.admin.controller.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.game.admin.controller.service.UserService;
import cn.game.admin.controller.service.req.CreateReq;
import cn.game.admin.controller.service.req.CreateResp;
import cn.game.admin.controller.service.req.DeleteReq;
import cn.game.admin.controller.service.req.DeleteResp;
import cn.game.admin.controller.service.req.ReadListResp;
import cn.game.admin.controller.service.req.ReadReq;
import cn.game.admin.controller.service.req.ReadResp;
import cn.game.admin.controller.service.req.Resp;
import cn.game.admin.controller.service.req.UpdateReq;
import cn.game.core.repository.role.RoleRepository;
import cn.game.core.repository.role.RoleRepositoryCustom;
import cn.game.core.repository.system.AuthorityRepository;
import cn.game.core.repository.user.UserRepository;
import cn.game.core.service.detail.system.AuthorityDetails;
import cn.game.core.service.detail.system.RoleDetails;
import cn.game.core.service.detail.system.UserDetails;
import cn.game.core.table.Authority;
import cn.game.core.table.Role;
import cn.game.core.table.User;
import cn.game.core.tools.Group;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private RoleRepositoryCustom roleRepositoryCustom;
	@Autowired
	private AuthorityRepository authorityRepository;

	@Override
	public CreateResp<UserDetails> create(CreateReq<UserDetails> req) {
		User user = User.fromDetails(req.getDetails());
		user = userRepository.save(user);
		return user == null ? new CreateResp<>(Resp.FAIL, req.getDetails())
				: new CreateResp<>(Resp.SUCCESS, user.toDetails());
	}

	@Override
	public ReadResp<UserDetails, Long> read(ReadReq<Long> req) {
		User user = userRepository.findOne(req.getValue());
		return user == null ? new ReadResp<>(Resp.FAIL, null, req.getValue())
				: new ReadResp<>(Resp.SUCCESS, user.toDetails(), req.getValue());
	}

	@Override
	public ReadListResp<UserDetails> readAll() {
		List<UserDetails> list = new ArrayList<>();
		userRepository.findAll().forEach(user -> list.add(user.toDetails()));
		return new ReadListResp<>(Resp.SUCCESS, list);
	}

	@Transactional
	@Override
	public void updateUser(UpdateReq<UserDetails> req) throws Exception {
		User user = User.fromDetails2(req.getDetails());
		User temp = userRepository.find(user.getId());
		temp.setLocked(user.isLocked());
		temp.setMobile(user.getMobile());
		temp.setRealName(user.getRealName());
		temp.setLogin(user.getMobile());
		temp.getRoles().clear();
		temp.setRoles(user.getRoles());
		userRepository.update(temp);
	}

	@Override
	public DeleteResp<UserDetails, Long> delete(DeleteReq<Long> req) {
		long id = req.getId();
		User user = userRepository.findOne(id);
		if (user == null) {
			return new DeleteResp<>(Resp.FAIL, null, id);
		}
		userRepository.delete(user);
		return new DeleteResp<>(Resp.SUCCESS, user.toDetails(), id);
	}

	@Override
	public Resp deleteAll() {
		userRepository.deleteAll();
		return Resp.success();
	}

	@Override
	public ReadResp<UserDetails, String> readByMobile(ReadReq<String> req) {
		User user = userRepository.findByMobile(req.getValue());
		if(user != null){
			return new ReadResp<>(Resp.SUCCESS, user.toDetails(), req.getValue());
		}
		return new ReadResp<>(Resp.FAIL, null, req.getValue());
	}

	@Override
	public long findTotalCountByGroups(Groups groups) {
		// TODO Auto-generated method stub
		return userRepository.findTotalCountByGroups(groups);
	}

	@Override
	public Page findUserPageByGroups(Groups groups, Page page) {
//		String sql = "select user.id id,user.login login,user.mobile mobile,user.real_name realName,role.name from user user "
//				+ "inner join user_role userRole on userRole.user_id=user.id inner join role role on role.id=userRole.role_id";
//		return userRepository.findPageByGroups(groups, page, sql, UserDetails.class);
		String hql = "select user from User user";
		for (Group group : groups.getGroupList()) {
			if("role.id".equals(group.getPropertyName())){//角色查询用户比较特殊
				hql += " join user.roles role";
				break;
			}
		}
		return userRepository.findEntityPageByGroups(groups, hql, page);
	}
	
	@Transactional
	public void updatePassword(Long id, String password) throws Exception{
		User user = userRepository.find(id);
		user.setPassword(password);
		userRepository.save(user);
	}

	@Override
	public UserDetails findUser(Long id) {
		User user = userRepository.find(id);
		return user.toDetails();
	}
	
	public UserDetails findUserByField(String propertyName, Object value){
		Groups groups = new Groups();
		groups.Add(propertyName,value);
		List<User> list = userRepository.findEntityByGroups(groups);
		if(!list.isEmpty()){
			User user = list.get(0);
			return user.toDetails();
		}
		return null;
	}
	
	
	@Override
	public RoleDetails findRole(Long id){
		Role role = roleRepositoryCustom.find(id);
		if(role != null){
			return role.toDetails();
		}
		return null;
	}

	@Override
	public RoleDetails findByDetails(String details) {
		Role role = roleRepository.findByDetails(details);
		if(role != null){
			return role.toDetails();
		}
		return null;
	}
	
	@Override
	public List<RoleDetails> findRoleByGroups(Groups groups){
		List<Role> roleList = roleRepositoryCustom.findEntityByGroups(groups);
		
		return Role.toSimpDetailsList(roleList);
	}
	
	@Override
	public Page findRolePageByGroups(Groups groups, Page page){
		return roleRepositoryCustom.findEntityPageByGroups(groups, page);
	}
	
	@Override
	@Transactional
	public void saveRole(RoleDetails roleDetails) throws Exception{
		Role role = Role.fromDetails(roleDetails);
		roleRepositoryCustom.persist(role);
	}
	
	
	@Override
	@Transactional
	public void updateRole(RoleDetails roleDetails) throws Exception{
		Role role = roleRepositoryCustom.find(roleDetails.getId());
		role.setName(roleDetails.getName());
		role.setDetails(roleDetails.getDetails());
		
		role.setAuthoritys(Authority.fromDetailsList(roleDetails.getAuthoritys()));
		roleRepositoryCustom.update(role);
	}

	

	@Override
	public AuthorityDetails findAuthority(Long id) {
		Authority authority = authorityRepository.find(id);
		if(authority != null){
			return authority.toDetails();
		}
		return null;
	}

	@Override
	public Page findAuthorityPageByGroups(Groups groups, Page page) {
		// TODO Auto-generated method stub
		return authorityRepository.findEntityPageByGroups(groups, page);
	}
	
	@Override
	public List<AuthorityDetails> findAuthorityByGroups(Groups groups){
	List<Authority> list = authorityRepository.findEntityByGroups(groups);
		return Authority.toDetailsList(list);
	}

	@Override
	@Transactional
	public void saveAuthority(AuthorityDetails authorityDetails) throws Exception {
		Authority authority = Authority.fromDetails(authorityDetails);
		authorityRepository.persist(authority);
	}

	@Override
	@Transactional
	public void updateAuthority(AuthorityDetails authorityDetails) throws Exception {
		Authority authority = authorityRepository.find(authorityDetails.getId());
		authority.setName(authorityDetails.getName());
		authority.setCode(authorityDetails.getCode());
		authority.setDetails(authorityDetails.getDetails());
		authority.setSort(authorityDetails.getSort());
		Authority parent = null;
		if(authorityDetails.getParentId() != null){
			parent = new Authority();
			parent.setId(authorityDetails.getParentId());
		}
		authority.setParent(parent);
		authorityRepository.update(authority);
	}
	
	
	@Override
	public Integer excuteSql(String sql) {
		// TODO Auto-generated method stub
		return userRepository.excuteSql(sql,null);
	}
	@Override
	public Boolean checkUserName(String login) {
		Optional<User> userOpt = userRepository.findByLogin(login);
		if (userOpt.isPresent()) {
			return false;
		}
		return true;
	}

	@Transactional
	public void createUser(User user) throws Exception {
		userRepository.persist(user);
	}

	public Page<User> loadUserList(Groups groups, Page<User> page) {
		return userRepository.findEntityPageByGroups(groups, page);
		

	}
	
	public User loadUserByLogin(String login){
		
		return userRepository.findByLogin(login).get();
	}
}

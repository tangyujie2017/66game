package cn.game.admin.controller.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.game.admin.controller.service.CustomerService;
import cn.game.admin.controller.service.req.CreateReq;
import cn.game.admin.controller.service.req.CreateResp;
import cn.game.admin.controller.service.req.DeleteReq;
import cn.game.admin.controller.service.req.DeleteResp;
import cn.game.admin.controller.service.req.ReadListResp;
import cn.game.admin.controller.service.req.ReadReq;
import cn.game.admin.controller.service.req.ReadResp;
import cn.game.admin.controller.service.req.Resp;
import cn.game.admin.controller.service.req.UpdateReq;
import cn.game.core.repository.customer.CustomerRepository;
import cn.game.core.repository.customerAuthority.CustomerAuthorityRepository;
import cn.game.core.repository.customerRole.CustomerRoleRepository;
import cn.game.core.repository.customerRole.CustomerRoleRepositoryCustom;
import cn.game.core.service.detail.system.CustomerAuthorityDetails;
import cn.game.core.service.detail.system.CustomerDetails;
import cn.game.core.service.detail.system.CustomerRoleDetails;
import cn.game.core.table.Customer;
import cn.game.core.table.CustomerAuthority;
import cn.game.core.table.CustomerRole;
import cn.game.core.tools.Group;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;
@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private CustomerRoleRepository customerRoleRepository;
	@Autowired
	private CustomerRoleRepositoryCustom customerRoleRepositoryCustom;
	@Autowired
	private CustomerAuthorityRepository customerAuthorityRepository;

	@Override
	public CreateResp<CustomerDetails> create(CreateReq<CustomerDetails> req) {
		Customer user = Customer.fromDetails(req.getDetails());
		user = customerRepository.save(user);
		return user == null ? new CreateResp<>(Resp.FAIL, req.getDetails())
				: new CreateResp<>(Resp.SUCCESS, user.toDetails());
	}

	@Override
	public ReadResp<CustomerDetails, Long> read(ReadReq<Long> req) {
		Customer user = customerRepository.findOne(req.getValue());
		return user == null ? new ReadResp<>(Resp.FAIL, null, req.getValue())
				: new ReadResp<>(Resp.SUCCESS, user.toDetails(), req.getValue());
	}

	@Override
	public ReadListResp<CustomerDetails> readAll() {
		List<CustomerDetails> list = new ArrayList<>();
		customerRepository.findAll().forEach(user -> list.add(user.toDetails()));
		return new ReadListResp<>(Resp.SUCCESS, list);
	}

	@Transactional
	@Override
	public void updateUser(UpdateReq<CustomerDetails> req) throws Exception {
		Customer user = Customer.fromDetails2(req.getDetails());
		Customer temp = customerRepository.find(user.getId());
		temp.setLocked(user.isLocked());
		temp.setMobile(user.getMobile());
		temp.setRealName(user.getRealName());
		temp.setLogin(user.getMobile());
		temp.setServerTime(user.getServerTime());
		temp.getRoles().clear();
		temp.setRoles(user.getRoles());
		customerRepository.update(temp);
	}

	@Override
	public DeleteResp<CustomerDetails, Long> delete(DeleteReq<Long> req) {
		long id = req.getId();
		Customer user = customerRepository.findOne(id);
		if (user == null) {
			return new DeleteResp<>(Resp.FAIL, null, id);
		}
		customerRepository.delete(user);
		return new DeleteResp<>(Resp.SUCCESS, user.toDetails(), id);
	}

	@Override
	public Resp deleteAll() {
		customerRepository.deleteAll();
		return Resp.success();
	}

	@Override
	public ReadResp<CustomerDetails, String> readByMobile(ReadReq<String> req) {
		Customer user = customerRepository.findByMobile(req.getValue());
		if(user != null){
			return new ReadResp<>(Resp.SUCCESS, user.toDetails(), req.getValue());
		}
		return new ReadResp<>(Resp.FAIL, null, req.getValue());
	}

	@Override
	public long findTotalCountByGroups(Groups groups) {
		// TODO Auto-generated method stub
		return customerRepository.findTotalCountByGroups(groups);
	}

	@Override
	public Page findUserPageByGroups(Groups groups, Page page) {
//		String sql = "select user.id id,user.login login,user.mobile mobile,user.real_name realName,role.name from user user "
//				+ "inner join user_role userRole on userRole.user_id=user.id inner join role role on role.id=userRole.role_id";
//		return customerRepository.findPageByGroups(groups, page, sql, CustomerDetails.class);
		String hql = "select user from Customer user";
		for (Group group : groups.getGroupList()) {
			if("role.id".equals(group.getPropertyName())){//角色查询用户比较特殊
				hql += " join user.roles role";
				break;
			}
		}
		return customerRepository.findEntityPageByGroups(groups, hql, page);
	}
	
	@Transactional
	public void updatePassword(Long id, String password) throws Exception{
		Customer user = customerRepository.find(id);
		user.setPassword(password);
		customerRepository.save(user);
	}

	@Override
	public CustomerDetails findUser(Long id) {
		Customer user = customerRepository.find(id);
		return user.toDetails();
	}
	
	public CustomerDetails findUserByField(String propertyName, Object value){
		Groups groups = new Groups();
		groups.Add(propertyName,value);
		List<Customer> list = customerRepository.findEntityByGroups(groups);
		if(!list.isEmpty()){
			Customer user = list.get(0);
			return user.toDetails();
		}
		return null;
	}
	
	
	@Override
	public CustomerRoleDetails findRole(Long id){
		CustomerRole role = customerRoleRepositoryCustom.find(id);
		if(role != null){
			return role.toDetails();
		}
		return null;
	}

	@Override
	public CustomerRoleDetails findByDetails(String details) {
		CustomerRole role = customerRoleRepository.findByDetails(details);
		if(role != null){
			return role.toDetails();
		}
		return null;
	}
	
	@Override
	public List<CustomerRoleDetails> findRoleByGroups(Groups groups){
		List<CustomerRole> roleList = customerRoleRepositoryCustom.findEntityByGroups(groups);
		
		return CustomerRole.toSimpDetailsList(roleList);
	}
	
	@Override
	public Page findRolePageByGroups(Groups groups, Page page){
		return customerRoleRepositoryCustom.findEntityPageByGroups(groups, page);
	}
	
	@Override
	@Transactional
	public void saveRole(CustomerRoleDetails CustomerRoleDetails) throws Exception{
		CustomerRole role = CustomerRole.fromDetails(CustomerRoleDetails);
		customerRoleRepositoryCustom.persist(role);
	}
	
	
	@Override
	@Transactional
	public void updateRole(CustomerRoleDetails CustomerRoleDetails) throws Exception{
		CustomerRole role = customerRoleRepositoryCustom.find(CustomerRoleDetails.getId());
		role.setName(CustomerRoleDetails.getName());
		role.setDetails(CustomerRoleDetails.getDetails());
		
		role.setAuthoritys(CustomerAuthority.fromDetailsList(CustomerRoleDetails.getAuthoritys()));
		customerRoleRepositoryCustom.update(role);
	}

	

	@Override
	public CustomerAuthorityDetails findAuthority(Long id) {
		CustomerAuthority authority = customerAuthorityRepository.find(id);
		if(authority != null){
			return authority.toDetails();
		}
		return null;
	}

	@Override
	public Page findAuthorityPageByGroups(Groups groups, Page page) {
		// TODO Auto-generated method stub
		return customerAuthorityRepository.findEntityPageByGroups(groups, page);
	}
	
	@Override
	public List<CustomerAuthorityDetails> findAuthorityByGroups(Groups groups){
	List<CustomerAuthority> list = customerAuthorityRepository.findEntityByGroups(groups);
		return CustomerAuthority.toDetailsList(list);
	}

	@Override
	@Transactional
	public void saveAuthority(CustomerAuthorityDetails CustomerAuthorityDetails) throws Exception {
		CustomerAuthority authority = CustomerAuthority.fromDetails(CustomerAuthorityDetails);
		customerAuthorityRepository.persist(authority);
	}

	@Override
	@Transactional
	public void updateAuthority(CustomerAuthorityDetails CustomerAuthorityDetails) throws Exception {
		CustomerAuthority authority = customerAuthorityRepository.find(CustomerAuthorityDetails.getId());
		authority.setName(CustomerAuthorityDetails.getName());
		authority.setCode(CustomerAuthorityDetails.getCode());
		authority.setDetails(CustomerAuthorityDetails.getDetails());
		authority.setSort(CustomerAuthorityDetails.getSort());
		CustomerAuthority parent = null;
		if(CustomerAuthorityDetails.getParentId() != null){
			parent = new CustomerAuthority();
			parent.setId(CustomerAuthorityDetails.getParentId());
		}
		authority.setParent(parent);
		customerAuthorityRepository.update(authority);
	}
	
	
	@Override
	public Integer excuteSql(String sql) {
		// TODO Auto-generated method stub
		return customerRepository.excuteSql(sql,null);
	}
	@Override
	public Boolean checkUserName(String login) {
		Optional<Customer> userOpt = customerRepository.findByLogin(login);
		if (userOpt.isPresent()) {
			return false;
		}
		return true;
	}

	@Transactional
	public void createUser(Customer user) throws Exception {
		customerRepository.persist(user);
	}

	public Page<Customer> loadUserList(Groups groups, Page<Customer> page) {
		return customerRepository.findEntityPageByGroups(groups, page);
		

	}
	
	public Customer loadUserByLogin(String login){
		
		return customerRepository.findByLogin(login).get();
	}
}

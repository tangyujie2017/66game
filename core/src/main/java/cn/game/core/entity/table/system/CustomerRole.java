package cn.game.core.entity.table.system;

import javax.persistence.*;

import cn.game.core.entity.BaseEntity;
import cn.game.core.service.detail.system.CustomerRoleDetails;
import cn.game.core.service.detail.system.RoleDetails;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "customer_role")
public class CustomerRole extends BaseEntity {

	private static final long serialVersionUID = -9196730643894509307L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	  @Column(name = "name", length = 20)
	  private String name;

	  //描述
	  @Column(length = 20)
	  private String details;

	  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles", cascade = CascadeType.DETACH)
	  private List<Customer> users;

	  @ManyToMany(fetch = FetchType.EAGER)
	  @JoinTable(name = "customerrole_customerauthority")
	  private List<CustomerAuthority> authoritys;

	  public CustomerRole() {
		    // 保留缺省构造方法
		  }

		  public CustomerRole(Long id) {
		    this.id = id;
		  }

		  public CustomerRole(String name) {
		    this.name = name;
		  }

		  public CustomerRole(Long id, String name, String details) {
		    this.id = id;
		    this.name = name;
		    this.details = details;
		  }

		  public String getName() {
		    return name;
		  }

		  public void setName(String name) {
		    this.name = name;
		  }

		 

		  // ----------------- convert -------------------

		  public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getDetails() {
			return details;
		}

		public void setDetails(String details) {
			this.details = details;
		}

		public List<Customer> getUsers() {
			return users;
		}

		public void setUsers(List<Customer> users) {
			this.users = users;
		}

		public List<CustomerAuthority> getAuthoritys() {
			return authoritys;
		}

		public void setAuthoritys(List<CustomerAuthority> authoritys) {
			this.authoritys = authoritys;
		}

		public CustomerRoleDetails toDetails() {
		    return new CustomerRoleDetails(id, name, details, CustomerAuthority.toDetailsList(authoritys));
		  }

		  public CustomerRoleDetails toSimpDetails() {
		    return new CustomerRoleDetails(id, name, details);
		  }

		  public static CustomerRole fromDetails(CustomerRoleDetails details) {
		    return new CustomerRole(details.getId(), details.getName(), details.getDetails());
		  }

		  public static List<CustomerRoleDetails> toDetailsList(List<CustomerRole> roleList) {
		    List<CustomerRoleDetails> detailsList = new ArrayList<>();
		    if (roleList != null) {
		      roleList.stream().forEach(r -> detailsList.add(r.toDetails()));
		    }
		    return detailsList;
		  }

		  public static List<CustomerRoleDetails> toSimpDetailsList(List<CustomerRole> roleList) {
		    List<CustomerRoleDetails> detailsList = new ArrayList<>();
		    if (roleList != null) {
		      roleList.stream().forEach(r -> detailsList.add(r.toSimpDetails()));
		    }
		    return detailsList;
		  }

		  public static List<CustomerRole> fromDetailsList(List<CustomerRoleDetails> detailsList) {
		    List<CustomerRole> roleList = new ArrayList<>(detailsList.size());
		    if (detailsList != null) {
		      detailsList.stream().forEach(d -> roleList.add(fromDetails(d)));
		    }
		    return roleList;
		  }

		
}

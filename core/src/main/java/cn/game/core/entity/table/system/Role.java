package cn.game.core.entity.table.system;

import javax.persistence.*;

import cn.game.core.entity.BaseEntity;
import cn.game.core.service.detail.system.RoleDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzc on 14/11/2016.
 */
@Entity
@Table(name = "role", indexes = { @Index(name = "idx_name", columnList = "name") })
public class Role extends BaseEntity {

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
	  private List<User> users;

	  @ManyToMany(fetch = FetchType.EAGER)
	  @JoinTable(name = "role_authority")
	  private List<Authority> authoritys;

	  public Role() {
		    // 保留缺省构造方法
		  }

		  public Role(Long id) {
		    this.id = id;
		  }

		  public Role(String name) {
		    this.name = name;
		  }

		  public Role(Long id, String name, String details) {
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

		  public List<User> getUsers() {
		    return users;
		  }

		  public void setUsers(List<User> users) {
		    this.users = users;
		  }

		  // ----------------- convert -------------------

		  public RoleDetails toDetails() {
		    return new RoleDetails(id, name, details, Authority.toDetailsList(authoritys));
		  }

		  public RoleDetails toSimpDetails() {
		    return new RoleDetails(id, name, details);
		  }

		  public static Role fromDetails(RoleDetails details) {
		    return new Role(details.getId(), details.getName(), details.getDetails());
		  }

		  public static List<RoleDetails> toDetailsList(List<Role> roleList) {
		    List<RoleDetails> detailsList = new ArrayList<>();
		    if (roleList != null) {
		      roleList.stream().forEach(r -> detailsList.add(r.toDetails()));
		    }
		    return detailsList;
		  }

		  public static List<RoleDetails> toSimpDetailsList(List<Role> roleList) {
		    List<RoleDetails> detailsList = new ArrayList<>();
		    if (roleList != null) {
		      roleList.stream().forEach(r -> detailsList.add(r.toSimpDetails()));
		    }
		    return detailsList;
		  }

		  public static List<Role> fromDetailsList(List<RoleDetails> detailsList) {
		    List<Role> roleList = new ArrayList<>(detailsList.size());
		    if (detailsList != null) {
		      detailsList.stream().forEach(d -> roleList.add(fromDetails(d)));
		    }
		    return roleList;
		  }

		  public String getDetails() {
		    return details;
		  }

		  public void setDetails(String details) {
		    this.details = details;
		  }

		  public List<Authority> getAuthoritys() {
		    return authoritys;
		  }

		  public void setAuthoritys(List<Authority> authoritys) {
		    this.authoritys = authoritys;
		  }
}

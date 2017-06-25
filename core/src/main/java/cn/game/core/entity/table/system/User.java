package cn.game.core.entity.table.system;


import javax.persistence.*;

import cn.game.core.entity.BaseEntity;
import cn.game.core.service.detail.system.UserDetails;
import cn.game.core.service.vo.Select2NameVo;
import cn.game.core.service.vo.Select2Vo;

import java.util.*;

/**
 * Created by zzc on 11/11/2016.
 */
@Entity
@Table(name = "user", indexes = { @Index(name = "idx_login", columnList = "login"), @Index(name = "idx_locked", columnList = "locked") })
public class User extends BaseEntity {
    
	private static final long serialVersionUID = 4285275098457224134L;
	@Id
	@GeneratedValue
	@Column(name = "id")
	protected Long id;
	@Column(name = "mobile")
    private String mobile;
	
    @Column(name = "real_name")
    private String realName;
    
    @Column(name = "login")
    private String login;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "locked")
    private boolean locked;
    
    //身份证号
    @Column(length = 50)
    private String idcard;
    
    //头像
    @Column(length = 50, name = "head_photo")
    private String headPhoto;
    
    @Column(name = "create_time")
    private Date createTime = new Date();
   
    
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id") })
    private List<Role> roles;   

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String realName) {
        this.id = id;
        this.realName = realName;
    }

    public User(String mobile, String realName, String login, String password, boolean locked, List<Role> roles) {
        this.mobile = mobile;
        this.realName = realName;
        this.login = login;
        this.password = password;
        this.locked = locked;
        this.roles = roles;
    }
    public static List<UserDetails> toDetailsList(List<User> userList) {
        if (userList == null) return null;
        List<UserDetails> detailsList = new ArrayList<>(userList.size());
        userList.stream().forEach(user -> detailsList.add(user.toDetails()));
        return detailsList;
      }
  

   

    public User(Long id, String mobile, String realName, String login, boolean locked, List<Role> roles) {
        this.id = id;
        this.mobile = mobile;
        this.login = login;
        this.realName = realName;
        this.locked = locked;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }
    // ------------- convert --------------

    public UserDetails toDetails() {
        return new UserDetails(
            id,
            mobile,
            realName,
            login,
            password,
            locked,
            Role.toDetailsList(roles),
            headPhoto,
            createTime);
      }
    public static User fromDetails(UserDetails details) {
        return new User(
            details.getMobile(),
            details.getRealName(),
            details.getLogin(),
            details.getPassword(),
            details.isLocked(),
            Role.fromDetailsList(details.getRoles()));
      }
    public static User fromDetails2(UserDetails details) {
        return new User(
            details.getId(),
            details.getMobile(),
            details.getRealName(),
            details.getLogin(),
            details.isLocked(),
            Role.fromDetailsList(details.getRoles()));
      }
    public static List<Select2Vo> toSelect2VoList(List<User> userList) {
		if (userList == null)
			return null;
		List<Select2Vo> selectList = new ArrayList<>(userList.size());
		userList.stream().forEach(user -> {
			Select2Vo vo = new Select2Vo();
			vo.setId(user.getId());
			vo.setText(user.getRealName());
			selectList.add(vo);
		});
		return selectList;
	}
    public static List<Select2NameVo> toSelect2NameVoList(List<User> userList) {
		if (userList == null)
			return null;
		List<Select2NameVo> selectList = new ArrayList<>(userList.size());
		userList.stream().forEach(user -> {
			Select2NameVo vo = new Select2NameVo();
			vo.setId(user.getMobile());
			vo.setText(user.getRealName()+"("+vo.getId()+")");
			selectList.add(vo);
		});
		return selectList;
	}
}

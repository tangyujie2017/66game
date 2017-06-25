package cn.game.core.entity.table.system;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import cn.game.core.entity.BaseEntity;
import cn.game.core.entity.em.CustomerEnum;
import cn.game.core.service.detail.system.CustomerDetails;
import cn.game.core.service.detail.system.UserDetails;
import cn.game.core.service.vo.Select2NameVo;
import cn.game.core.service.vo.Select2Vo;

@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {


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
    @Column(name = "server_time")
    private Date serverTime = new Date();
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "customer_customerrole", joinColumns = { @JoinColumn(name = "customerid", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "roleid", referencedColumnName = "id") })
    private List<CustomerRole> roles;



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



	public Date getServerTime() {
		return serverTime;
	}



	public void setServerTime(Date serverTime) {
		this.serverTime = serverTime;
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



	public String getIdcard() {
		return idcard;
	}



	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}



	public String getHeadPhoto() {
		return headPhoto;
	}



	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}



	public Date getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



	
	 public List<CustomerRole> getRoles() {
		return roles;
	}



	public void setRoles(List<CustomerRole> roles) {
		this.roles = roles;
	}

	public Customer() {
      
    }

	public Customer(Long id, String realName) {
	        this.id = id;
	        this.realName = realName;
	    }

	    public Customer(String mobile, String realName, String login, String password, boolean locked, List<CustomerRole> roles,Date serverTime) {
	        this.mobile = mobile;
	        this.realName = realName;
	        this.login = login;
	        this.password = password;
	        this.locked = locked;
	        this.roles = roles;
	        this.serverTime=serverTime;
	    }
	
	    public Customer(Long id, String mobile, String realName, String login, boolean locked, List<CustomerRole> roles,Date serverTime) {
	        this.id = id;
	        this.mobile = mobile;
	        this.login = login;
	        this.realName = realName;
	        this.locked = locked;
	        this.roles = roles;
	        this.serverTime=serverTime;
	    }
	
	
	
	
	
    public CustomerDetails toDetails() {
        return new CustomerDetails(
            id,
            mobile,
            realName,
            login,
            password,
            locked,
            CustomerRole.toDetailsList(roles),
            headPhoto,
            createTime,dateToString(serverTime));
      }
    
    public static Customer fromDetails(CustomerDetails details) {
    	
        return new Customer(
            details.getMobile(),
            details.getRealName(),
            details.getLogin(),
            details.getPassword(),
            details.isLocked(),
            CustomerRole.fromDetailsList(details.getRoles()),dateToString(details.getServerTime()));
      }
    public static Customer fromDetails2(CustomerDetails details) {
        return new Customer(
            details.getId(),
            details.getMobile(),
            details.getRealName(),
            details.getLogin(),
            details.isLocked(),
            CustomerRole.fromDetailsList(details.getRoles()),dateToString(details.getServerTime()));
      }
    public static List<Select2Vo> toSelect2VoList(List<Customer> userList) {
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
    public static List<Select2NameVo> toSelect2NameVoList(List<Customer> userList) {
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

    public static List<CustomerDetails> toDetailsList(List<Customer> userList) {
        if (userList == null) return null;
        List<CustomerDetails> detailsList = new ArrayList<>(userList.size());
        userList.stream().forEach(user -> detailsList.add(user.toDetails()));
        return detailsList;
      }
	
 private static String  dateToString(Date serverTime){
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 String dateString="";
	 if(serverTime!=null){
		  dateString = formatter.format(serverTime);
	 }
	 
	return dateString;
 }
 private static Date  dateToString(String  serverTime){
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 Date date = null;
	try {
		date = formatter.parse(serverTime);
	} catch (ParseException e) {
		
		e.printStackTrace();
	}
	return date;
 }
}

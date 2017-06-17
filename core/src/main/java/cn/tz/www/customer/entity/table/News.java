package cn.tz.www.customer.entity.table;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "news")
public class News {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	private Integer status;
	
	private String title;
	@Column(name = "type")
	private Integer type;
	
	@Column(name="declare_context",length = 255)
	private String declareContext;
	
	@Column(length = 65536)
	private String context;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User createUser;
	
	@Column(name = "create_time")
    private Date createTime;
	
	@Column(name = "view_times")
	private Integer viewTimes;
	@Column(name = "newsMainImg")
    private String newsMainImg;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getViewTimes() {
		return viewTimes;
	}

	public void setViewTimes(Integer viewTimes) {
		this.viewTimes = viewTimes;
	}

	public String getDeclareContext() {
		return declareContext;
	}

	public void setDeclareContext(String declareContext) {
		this.declareContext = declareContext;
	}

	public String getNewsMainImg() {
		return newsMainImg;
	}

	public void setNewsMainImg(String newsMainImg) {
		this.newsMainImg = newsMainImg;
	}

	
}

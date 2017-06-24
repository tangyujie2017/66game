package cn.game.api.contorller.view;

public class SliderVo {

	protected Long id;

	private Integer sn;

	private String imgPath;
	private Integer type;
	private String remark;
	private String baseimgUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBaseimgUrl() {
		return baseimgUrl;
	}

	public void setBaseimgUrl(String baseimgUrl) {
		this.baseimgUrl = baseimgUrl;
	}

}

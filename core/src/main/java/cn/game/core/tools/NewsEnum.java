package cn.game.core.tools;

public enum NewsEnum {
	Z_Q_NEWS("证券要闻", 1), A_G_ANALYSE("A股分析", 2), H_G_STRATEGY("宏观策略", 3), D_J_REFERENCE("独家参考", 4), G_H_TAOJIN("股海淘金",
			5);
	private String remark;
	private int type;

	private NewsEnum(String remark, int type) {
		this.remark = remark;
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}

package cn.game.core.tools;

public class Constants {
	
	
	/**
	 * 店铺状态：已签约
	 */
	public static Integer HAS_SIGNED = 7;
	
	
	/**
	 * 待签约
	 */
	public static Integer READY_SIGNED = 6;
	
	
	/**
	 * 审核通过
	 */
	public static Integer AUDIT_SUCCESS = 5;
	
	
	/**
	 * 审核失败
	 */
	public static Integer AUDIT_FAUILURE = 4;
	
	
	/**
	 * 审核中
	 */
	public static Integer AUDITTING = 3;
	
	/**
	 * 待审核
	 */
	public static Integer READY_AUDIT = 2;
	
	/**
	 * 待认证
	 */
	public static Integer READY_CERTIFICATE = 1;
	
	/**
	 * 备注：审核操作
	 */
	public static Integer REMARK_AUDIT_TYPE = 1;
	
	/**
	 * 备注：修改操作
	 */
	public static Integer REMARK_UPDATE_TYPE = 2;
	
	
	/**
	 * 角色：系统管理员
	 */
	public static String SYSTEM_MANAGER = "系统管理员";
	
	
	/**
	 * 店铺拓展员
	 */
	public static String STORE_MANAGER = "店铺拓展员";
	
	/**
	 * 普通用户
	 */
	public static String COMMON_USER = "普通用户";
	
	
	/**
	 * 公司员工
	 */
	public static String STAFF_USER = "公司员工";
	
	
	
	/**
	 * 预定义标签
	 */
	public static Integer PREDEFINED_LABEL = 1;
	
	/**
	 * 自定义标签
	 */
	public static Integer CUSTOM_LABEL = 2;
	
	/**
	 * 管理标签
	 */
	public static Integer MANAGE_LABEL = 3;
	
	
	/**
	 * 缩略图宽度
	 */
	public static Integer THUMBNAIL_WIDTH = 320;
	
	
	/**
	 * 缩略图高度
	 */
	public static Integer THUMBNAIL_HEIGHT = 320;
	/**
	 * 标签已选择
	 */
	public static Integer LABEL_SELECTED = 1;
	
	/**
	 * 商品上架
	 */
	public static Integer UP_SHELF = 1;

	/**
	 * 商品下架
	 */
	public static Integer DOWN_SHELF = 2;

	/**
	 * 标签为选择
	 */
	public static Integer LABEL_NOSELECTED = 0;

	/**
	 * 商品轮播图片
	 */
	public static final String PICTURE_TYPE_GOODS = "goods";

	/**
	 * 商品介绍图片
	 */
	public static final String PICTURE_TYPE_INTRODUCE = "introducts";

	/**
	 * 加法
	 */
	public static final Integer INCREASE_TYPE = 0;

	/**
	 * 减法
	 */
	public static final Integer SUBTRACT_TYPE = 1;
	/**
	 * 购物车项修改
	 */
	public static final Integer CARTITEM_UPDATE_TYPE = 0;
	/**
	 * 购物车项删除
	 */
	public static final Integer CARTITEM_DELETE_TYPE = 1;
	/**
	 * classify all
	 */
	public static final Long CLASSIFY_ALL = 0L;

	/**
	 * 发货
	 */
	public static final Integer DELIVERY_TYPE = 1;

	/**
	 * 调货
	 */
	public static final Integer SWAP_TYPE = 2;

	/**
	 * 按比例结算
	 */
	public static final Integer SETTLEMENT_RATIO = 1;

	/**
	 * 按金额计算
	 */
	public static final Integer SETTLEMENT_MONEY = 2;




	/**
	 * 保存查询条件的key
	 */
	public static final String SESSION_GROUPS = "session_groups";

	public static final String SUPPLIER_GROUPS = "supplier_groups";
	
	public static final String WALLET_GROUPS = "wallet_groups";

	/**
	 * 备注类型 1:代表审核
	 */
	public static final Integer REMARK_ADUIT_TYPE =1;

	/**
	 * 冻结天数
	 */
	public static final Integer freezeDays = 7;


	/**
	 * 属性类型：文本输入
	 */
	public static final Integer ATTRBUTE_TEXT_INPUT = 3;

	/**
	 * 导购id
	 */
	public static final Long SHOP_GUIDE = 3L;

	/**
	 * 店铺老板
	 */
	public static final Long SHOP_BOSS = 1L;

	/**
	 * 店铺店长
	 */
	public static final Long SHOP_MANAGER = 2L;
	/*
	 *
	 * 发送短信的客服热线
	 */
	public static final String SERVICE_HOTLINE = "028-66668888";
	
	/**
	 *客户热线 key
	 */
	public static final String SERVICE_HOTLINE_KEY = "SERVICE_HOTLINE";
	
	
	

}

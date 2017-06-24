package cn.game.core.entity.em;

public enum CustomerEnum implements EnumValue{
	SERVICE(1,"启用"),UNSERVICE(2,"禁用"),EXPIRE(3,"过期");

	@Override
	public Integer getValue() {
		
		return null;
	}
   private Integer value;
	
	private String describle;
	private CustomerEnum(Integer value, String describle){
		this.value = value;
		this.describle = describle;
	}
	public String getDescrible() {
		return describle;
	}
	public void setDescrible(String describle) {
		this.describle = describle;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	
}

package cn.game.api.service.arithmetic;

import java.util.ArrayList;
import java.util.List;

public class TypeOfAnimal {
	private Long animalId;
	private List<UserAnimal> userAnimalList =new ArrayList<UserAnimal>();
	private Integer total=0;//赔偿总额
	private Integer income=0;
	
	public Long getAnimalId() {
		return animalId;
	}
	public void setAnimalId(Long animalId) {
		this.animalId = animalId;
	}
	
	
	public List<UserAnimal> getUserAnimalList() {
		return userAnimalList;
	}
	public void setUserAnimalList(List<UserAnimal> userAnimalList) {
		this.userAnimalList = userAnimalList;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getIncome() {
		return income;
	}
	public void setIncome(Integer income) {
		this.income = income;
	}
	public TypeOfAnimal(Long animalId) {
		super();
		this.animalId = animalId;
	}
	public TypeOfAnimal() {
		super();
	}
	
	

}

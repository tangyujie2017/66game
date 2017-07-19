package cn.game.api.service.arithmetic;

import java.util.Map;

public class UserAnimal {
	private Long userid;
	private Map<Long, Animal> animalMap;
    private Integer inTotalScore;//用户购买总金额
    private Integer outTotalScore;//用户中奖后赔偿金额
    
	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	

	public Map<Long, Animal> getAnimalMap() {
		return animalMap;
	}

	public void setAnimalMap(Map<Long, Animal> animalMap) {
		this.animalMap = animalMap;
	}
	

	
	public Integer getInTotalScore() {
		return inTotalScore;
	}

	public void setInTotalScore(Integer inTotalScore) {
		this.inTotalScore = inTotalScore;
	}

	public Integer getOutTotalScore() {
		return outTotalScore;
	}

	public void setOutTotalScore(Integer outTotalScore) {
		this.outTotalScore = outTotalScore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userid == null) ? 0 : userid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAnimal other = (UserAnimal) obj;
		if (userid == null) {
			if (other.userid != null)
				return false;
		} else if (!userid.equals(other.userid))
			return false;
		return true;
	}

}

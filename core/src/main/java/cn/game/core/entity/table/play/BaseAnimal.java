package cn.game.core.entity.table.play;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity(name="base_animal")
public class BaseAnimal implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private Integer multiple;
	
	@Column
	private String type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public Integer getMultiple() {
		return multiple;
	}

	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BaseAnimal(Long id, String name, Integer score, Integer multiple, String type) {
		super();
		this.id = id;
		this.name = name;
		this.multiple = multiple;
		this.type=type;
	}

	public BaseAnimal() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BaseAnimal other = (BaseAnimal) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

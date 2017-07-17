package cn.game.core.entity.table.play;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity(name = "player_agency")
public class PlayerAgency implements Serializable{

	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "agency_id")
	private Long agencyId;
	
	@Column(name = "agency_Name")
	private  String agencyName;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "status")
	private Boolean status=false;
	
	@Column(name = "agency_union_code", unique=true)
	private String agencyUnionCode;
	
	
	
	
	
	

}

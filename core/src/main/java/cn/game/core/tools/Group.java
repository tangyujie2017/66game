package cn.game.core.tools;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.game.core.entity.em.EnumValue;
import cn.game.core.tools.PropertyFilter.MatchType;
import cn.game.core.tools.PropertyFilter.Type;




public class Group {
	private boolean isDate = false;

	private MatchType relation = MatchType.AND;

	private Type type = Type.STRING;

	private String tempType = "";

	private MatchType matchType = MatchType.EQ;

	private String tempMatchType = "";

	private String propertyName = null;

	private Object propertyValue1 = null;

	private Object propertyValue2 = null;
	
	public Group() {

	}

	public Group(String propertyName, Object propertyValue) {
		this.propertyName = propertyName;
		this.propertyValue1 = propertyValue;
	}

	public Group(String propertyName, Object propertyValue, MatchType matchType) {
		this.propertyName = propertyName;
		this.propertyValue1 = propertyValue;
		this.matchType = matchType;
	}

	public Group(String propertyName, MatchType matchType, MatchType relation) {
		this.propertyName = propertyName;
		this.matchType = matchType;
		this.relation = relation;
	}

	public Group(String propertyName, Object propertyValue, MatchType matchType, MatchType relation) {
		this.propertyName = propertyName;
		this.propertyValue1 = propertyValue;
		this.matchType = matchType;
		this.relation = relation;
	}

	public Group(String propertyName, Object propertyValue1, Object propertyValue2, MatchType matchType) {
		this.propertyName = propertyName;
		this.propertyValue1 = propertyValue1;
		this.propertyValue2 = propertyValue2;
		this.matchType = matchType;
	}
	
	public Group(String propertyName, Object propertyValue1, Object propertyValue2, MatchType matchType,
			MatchType relation) {
		this.propertyName = propertyName;
		this.propertyValue1 = propertyValue1;
		this.propertyValue2 = propertyValue2;
		this.matchType = matchType;
		this.relation = relation;
	}

	public MatchType getRelation() {
		return relation;
	}

	public void setRelation(MatchType relation) {
		this.relation = relation;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Object getPropertyValue1() {
		return propertyValue1;
	}

	public void setPropertyValue1(Object propertyValue1) {
		this.propertyValue1 = propertyValue1;
	}

	public Object getPropertyValue2() {
		return propertyValue2;
	}

	public void setPropertyValue2(Object propertyValue2) {
		this.propertyValue2 = propertyValue2;
	}

	public String getTempMatchType() {
		return tempMatchType;
	}

	public void setTempMatchType(String tempMatchType) {
		this.tempMatchType = tempMatchType;
		if (tempMatchType.equals("3")) {
			matchType = MatchType.NE;
		} else if (tempMatchType.equals("4")) {
			matchType = MatchType.EQ;
		} else if (tempMatchType.equals("5")) {
			matchType = MatchType.LIKE;
		} else if (tempMatchType.equals("6")) {
			matchType = MatchType.LT;
		} else if (tempMatchType.equals("7")) {
			matchType = MatchType.GT;
		} else if (tempMatchType.equals("8")) {
			matchType = MatchType.LE;
		} else if (tempMatchType.equals("9")) {
			matchType = MatchType.GE;
		} else if (tempMatchType.equals("10")) {
			matchType = MatchType.BETWEEN;
		} else if ("11".equals(tempMatchType)) {
			matchType = MatchType.IN;
		} else if ("12".equals(tempMatchType)) {
			matchType = MatchType.NOTIN;
		} else if ("13".equals(tempMatchType)) {
			matchType = MatchType.NULL;
			propertyValue1 = "";
		} else if ("14".equals(tempMatchType)) {
			matchType = MatchType.NOTNULL;
			propertyValue1 = "";
		}
		if (getPropertyValue1() == null) {
			matchType = MatchType.NULL;
		}

	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTempType() {
		return tempType;
	}

	public void setTempType(String tempType) throws ParseException {
		this.tempType = tempType;
		tempType = tempType.toUpperCase();
		if ("INTEGER".equals(tempType)) {
			Float obj = Float.valueOf(propertyValue1.toString());
			propertyValue1 = obj.intValue();
			if (propertyValue2 != null) {
				propertyValue2 = Integer.valueOf(propertyValue1.toString());
			}
		} else if ("LONG".equals(tempType)) {
			propertyValue1 = Long.valueOf(propertyValue1.toString());
			if (propertyValue2 != null) {
				propertyValue2 = Integer.valueOf(propertyValue1.toString());
			}
		} else if ("FLOAT".equals(tempType)) {
			propertyValue1 = Float.valueOf(propertyValue1.toString());
			if (propertyValue2 != null) {
				propertyValue2 = Float.valueOf(propertyValue1.toString());
			}
		} else if ("DOUBLE".equals(tempType)) {
			propertyValue1 = Double.valueOf(propertyValue1.toString());
			if (propertyValue2 != null) {
				propertyValue2 = Double.valueOf(propertyValue1.toString());
			} else if ("YYDATE".equals(tempType)) {
				propertyValue1 = CommonUtil.parseDate(1, propertyValue1.toString());
				if (propertyValue2 != null) {
					propertyValue2 = CommonUtil.parseDate(1, propertyValue2.toString());
				}
			} else if ("LONG".equals(tempType)) {
				propertyValue1 = Long.valueOf(propertyValue1.toString());
				if (propertyValue2 != null) {
					propertyValue2 = Long.valueOf(propertyValue1.toString());
				}
			} else if ("BOOLEAN".equals(tempType)) {
				propertyValue1 = Boolean.valueOf(propertyValue1.toString());
				if (propertyValue2 != null) {
					propertyValue2 = Boolean.valueOf(propertyValue1.toString());
				}
			} else if ("DOUBLE".equals(tempType)) {
				propertyValue1 = Double.valueOf(propertyValue1.toString());
				if (propertyValue2 != null) {
					propertyValue2 = Double.valueOf(propertyValue1.toString());
				}
			}
		} else if ("BOOLEAN".equals(tempType)) {
			propertyValue1 = Boolean.valueOf(propertyValue1.toString());
			if (propertyValue2 != null) {
				propertyValue2 = Boolean.valueOf(propertyValue1.toString());
			}
		} else if ("DATE".equals(tempType)) {
			if(!CommonUtil.isNull(propertyValue1)){
				propertyValue1 = CommonUtil.parseDate(1, propertyValue1.toString());
				if (!CommonUtil.isNull(propertyValue2)) {
					propertyValue2 = CommonUtil.parseDate(2, propertyValue2.toString()+" 23:59:59");
				}else{
					propertyValue2 = CommonUtil.parseDate(2, "2100-12-12 23:59:59");
				}
			}else if(!CommonUtil.isNull(propertyValue2)){
				propertyValue1 = CommonUtil.parseDate(1, "1970-01-01");
				propertyValue2 = CommonUtil.parseDate(2, propertyValue2.toString()+" 23:59:59");
			}
			
		} else if ("LIST".equals(tempType)) {
			String values = propertyValue1.toString();

			String[] datas = values.split(",");
			List<String> list = Arrays.asList(datas);
			propertyValue1 = list;
		} else if ("LIST<LONG>".equals(tempType)) {
			String values = propertyValue1.toString();
			String[] datas = values.split(",");
			List<Long> list = new ArrayList<Long>();
			for(String data : datas){
				list.add(Long.parseLong(data));
			}
			propertyValue1 = list;
		} else if("ENUM".equals(tempType)){
			String values = propertyValue1.toString();
			String[] arrays = values.split("-");
			String className = arrays[0];
			Integer index = Integer.parseInt(arrays[1]);
			try {
				Class<?> clazz = Class.forName("cn.gaiasys.retail.services.enums.trade."+className);
				Method method = clazz.getMethod("values");
				EnumValue[] es = (EnumValue[]) method.invoke(null);
				for(EnumValue e : es){
					if(e.getValue() == index){
						propertyValue1 = e;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isDate() {
		return isDate;
	}

	public void setDate(boolean isDate) {
		this.isDate = isDate;
	}

}

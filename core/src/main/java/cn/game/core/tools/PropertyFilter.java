package cn.game.core.tools;

import java.util.Date;
import org.springframework.util.Assert;

public class PropertyFilter {

	public static final String OR_SEPARATOR = "_OR_";

	public enum MatchType {
		AND, OR, NULL, NOTNULL, BETWEEN, IN, EQ, // ==EQuivalent with
		LIKE, // like '%value%'
		LIKESTART, // like 'value%'
		LT, // < Less than
		GT, // > Greater Than
		LE, // <= Less than or Equivalent with
		GE, // >= Greater than or Equivalent with
		NOTIN, NE; // Not Equivalent with !=
	}

	public enum Type {
		INTEGER, LONG, FLOAT, STRING, DATE, DOUBLE, BOOLEAN
	}

	public enum PropertyType {
		S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class), B(Boolean.class);

		private Class<?> clazz;

		private PropertyType(Class<?> clazz) {
			this.clazz = clazz;
		}

		public Class<?> getValue() {
			return clazz;
		}
	}

	private MatchType matchType = null;
	private Object matchValue = null;

	private Class<?> propertyClass = null;
	private String[] propertyNames = null;

	public PropertyFilter() {
	}

	public Class<?> getPropertyClass() {
		return propertyClass;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public Object getMatchValue() {
		return matchValue;
	}

	public String[] getPropertyNames() {
		return propertyNames;
	}

	public String getPropertyName() {
		Assert.isTrue(propertyNames.length == 1, "There are not only one property in this filter.");
		return propertyNames[0];
	}

	public boolean hasMultiProperties() {
		return (propertyNames.length > 1);
	}
}
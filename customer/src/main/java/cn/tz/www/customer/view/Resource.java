package cn.tz.www.customer.view;

public class Resource {
private  String resourceCode;
private  String resourceName;
private  Boolean isVisit;
public String getResourceCode() {
	return resourceCode;
}
public void setResourceCode(String resourceCode) {
	this.resourceCode = resourceCode;
}
public Boolean getIsVisit() {
	return isVisit;
}
public void setIsVisit(Boolean isVisit) {
	this.isVisit = isVisit;
}
public String getResourceName() {
	return resourceName;
}
public void setResourceName(String resourceName) {
	this.resourceName = resourceName;
}
public Resource() {
	
}
public Resource(String resourceCode, String resourceName, Boolean isVisit) {
	super();
	this.resourceCode = resourceCode;
	this.resourceName = resourceName;
	this.isVisit = isVisit;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((resourceCode == null) ? 0 : resourceCode.hashCode());
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
	Resource other = (Resource) obj;
	if (resourceCode == null) {
		if (other.resourceCode != null)
			return false;
	} else if (!resourceCode.equals(other.resourceCode))
		return false;
	return true;
}

}

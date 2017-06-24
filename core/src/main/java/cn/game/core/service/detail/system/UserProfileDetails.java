
package cn.game.core.service.detail.system;


/**
 * 用户信息详情
 * @author Wang.ch
 *
 */
public class UserProfileDetails {
    /**
     * 主键
     */
    private Long id;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别 1:男/2:女
     */
    private Integer sex;
    
    /**
     * 用户
     */
    private UserDetails user;
    /**
     * 地区文字  四川-成都
     */
    private String orgStr;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    

    /**
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * @return the age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return the sex
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

   

    /**
     * @return the user
     */
    public UserDetails getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserDetails user) {
        this.user = user;
    }

    /**
     * @return the orgStr
     */
    public String getOrgStr() {
        return orgStr;
    }

    /**
     * @param orgStr the orgStr to set
     */
    public void setOrgStr(String orgStr) {
        this.orgStr = orgStr;
    }

    public UserProfileDetails() {
    }

    public UserProfileDetails(Long id, String avatar, Integer age, Integer sex, UserDetails user, String orgStr) {
        super();
        this.id = id;
        this.avatar = avatar;
        this.age = age;
        this.sex = sex;
        
        this.user = user;
        this.orgStr = orgStr;
    }

}

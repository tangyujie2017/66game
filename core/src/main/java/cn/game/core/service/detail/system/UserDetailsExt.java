
package cn.game.core.service.detail.system;

/**
 * 继承UserDetails ,新增storeId和providerId
 * @author Wang.ch
 *
 */
public class UserDetailsExt extends UserDetails {
    /**
     * 店铺id
     */
    protected Long storeId;
    /**
     * 供货商id
     */
    protected Long providerId;

    /**
     * @return the storeId
     */
    public Long getStoreId() {
        return storeId;
    }

    /**
     * @param storeId the storeId to set
     */
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    /**
     * @return the providerId
     */
    public Long getProviderId() {
        return providerId;
    }

    /**
     * @param providerId the providerId to set
     */
    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }
}

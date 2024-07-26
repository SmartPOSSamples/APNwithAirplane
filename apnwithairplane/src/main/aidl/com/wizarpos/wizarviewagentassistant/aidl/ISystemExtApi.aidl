package com.wizarpos.wizarviewagentassistant.aidl;
// Declare any non-default types here with import statements

import com.wizarpos.wizarviewagentassistant.aidl.NetworkType;
interface ISystemExtApi{
 	/**
     * Get the preferred network type.
     * Used for device configuration by some CDMA operators.
     * <p>
     * Requires Permission:
     *   {@link android.Manifest.permission#MODIFY_PHONE_STATE MODIFY_PHONE_STATE}
     * Or the calling app has carrier privileges. @see #hasCarrierPrivileges
     *
     * @return the preferred network type, defined in RILConstants.java.
     * @hide
     */
   int getPreferredNetworkType(int phoneId);
   /**
     * Set the preferred network type.
     * Used for device configuration by some CDMA operators.
     * <p>
     * Requires Permission:
     *   {@link android.Manifest.permission#MODIFY_PHONE_STATE MODIFY_PHONE_STATE}
     * Or the calling app has carrier privileges. @see #hasCarrierPrivileges
     *
     * @param subId the id of the subscription to set the preferred network type for.
     * @param networkType the preferred network type, defined in RILConstants.java.
     * @return true on success; false on any failure.
     * @hide
     */
   boolean setPreferredNetworkType(int phoneId, int networkType);
   /**
     * Request to put this activity in a mode where the user is locked to a restricted set of applications.
     * <p>
     * Requires Permission:
     *	{@link android.Manifest.permission#MANAGE_ACTIVITY_STACKS} 
     * @param taskid.
     * @return true on success; false on any failure.
     * @hide
     */
   boolean startLockTaskMode(int taskId);
   /**
	 * Added by Stone for task #22834 to add a interface to set screen off timeout.
	 * @param milliseconds the time you want to set, can be one of following:
	 *        15000 - 15s
	 *        30000 - 30s
	 *        60000 - 1 minute
	 *        120000 - 2 minutes
	 *        300000 - 5 minutes
	 *        600000 - 10 minutes
	 *        1800000 - 30 minutes
	 *        2147483647(Integer.MAX_VALUE) - never
	 * @return whether the new milliseconds has been set.
	 */
   boolean setScreenOffTimeout(int milliseconds);
   
   /**
	 * Added by Stone for task #22857 to add a interface to enable/disable mobile data.
	 * @param enable true if it should be enabled, false if it should be disabled.
	 * @return whether the new state has been set.
	 */
   boolean setMobileDataEnabled(int slot, boolean enable);
   
   /**
	 * Added by Stone for task #22857 to add a interface to enable/disable mobile data roaming.
	 * @param roaming 1 if it should be enabled, 0 if it should be disabled.
	 * @return whether the new state has been set.
	 */
   boolean setMobileDataRoamingEnabled(int slot, int roaming);

    /**
	 * @param enable: true means enable counter mode, false means disable counter mode.
     * @return true on success; false on any failure.
	 */
   boolean setBatteryCounterMode(boolean enable);

   /**
    * get supported network type array
    * NetworkMode: name, modeId;
    * @return network type array.
    * */
    NetworkType[] getSupportedNetworkTypes();
    /**
     * @param touch:Add wake on touch；none: Only the power button wakes up
     * @return return result for success or failed!.
     * */
    boolean setTouchScreenWakeupValue(String value);
    /**
     * @return return touch screen wakeup value. touch:Add wake on touch；none: Only the power button wakes up
     * */
    String getTouchScreenWakeupValue();
    /**
     * enable/disable auto timezone item.
     * @param enable true: enable; false: disable
     * */
    void enableAutoTimezone(boolean enable);
    /**
     * get status for enable/disable auto timezone item.
     * @return enable true: enable; false: disable
     * */
    boolean isEnableAutoTimezone();
    /**
     * enable/disable show auto timezone item GUI.
     * @param enable true: enable; false: disable
     * */
    void enableAutoTimezoneGUI(boolean enable);
    /**
     * get status for enable/disable auto timezone item GUI.
     * @return enable true: enable; false: disable
     * */
    boolean isEnableAutoTimezoneGUI();
    /**
     * enable/disable auto time item.
     * @param enable true: enable; false: disable
     * */
    void enableAutoTime(boolean enable);
    /**
     * get status for enable/disable auto time item.
     * @return enable true: enable; false: disable
     * */
    boolean isEnableAutoTime();
    /**
     * enable/disable show auto time item GUI.
     * @param enable true: enable; false: disable
     * */
    void enableAutoTimeGUI(boolean enable);
    /**
     * get status for enable/disable auto time item GUI.
     * @return enable true: enable; false: disable
     * */
    boolean isEnableAutoTimeGUI();

    /**
     * @param pkg: package name.
     * @param deviceCls: policyReceiver's class name.
     * @return active success for true ; active failed for false
     * */
    boolean setDeviceOwner(String pkg, String deviceCls);

    /**
     *  Set "persist.wp.usr.${key}"'s property , max count = 10.
     * Permission： android.permission.CLOUDPOS_SET_USR_PROP
     * @param key: property's key, length less than 16. for example: persist.wp.usr.${key} ${value}.
     * @param value: property's value, length less than 32.
     * @return set success for true ; set failed for false
     * */
    boolean setUsrProp(String key, String value);

    /**
     * Enabling ‘show touches’ in Android screen recordings for user research.
     * @param enbale: true, false;
     *
     **/
    void enableShowTouches(boolean enable);
    /**
     * get ‘show touches’ state for enable or disable.
     * @return ture: enable ‘show touches’; false: disable ‘show touches’.
     **/
    boolean getShowTouchesState();
    /**
     * Set the status bar locked as true will make the status bar can not be pull down.
     * @permission: android.permission.CLOUDPOS_LOCK_STATUS_BAR
     * @param lock, true for lock, false for disable lock.
     **/
    void setStatusBarLocked(boolean lock);
    /**
     * Block or Release The Power Key
     * @param enbale: true:Block The Power Key ; false:Release The Power Key ;
     * @permission: android.permission.CLOUDPOS_DISABLE_POWER_KEY
     */
    void setPowerKeyBlocked(boolean enable);

    /**
     * check PowerKey Button 's block status.
     * @return true : blocked the power key; false: not block the power key.
     */
    boolean isPowerKeyBlocked();
    /**
     * Enable/Disable MTP
     * @param enbale: true:Enable MTP ; false:Disable MTP;
     */
     void enableMtp(boolean enable);
    /**
     * Get MTP Status
     * @return true:Enable MTP ; false:Disable MTP;
     */
    boolean getMtpStatus();

    /**
     * set Language
     * @link https://developer.android.com/reference/java/util/Locale
     * @param language: (Null is not allowed)An ISO 639 alpha-2 or alpha-3 language code, or a language subtag up to 8 characters in length. See the Locale class description about valid language values.
     * @param country : (Null is allowed)An ISO 3166 alpha-2 country code or a UN M.49 numeric-3 area code. See the Locale class description about valid country values.
     * @param variant : (Null is allowed)Any arbitrary value used to indicate a variation of a Locale. See the Locale class description for the details.
     * <p>for example: Locale(String language, String country, String variant) or Locale(String language, String country) or Locale(String language)</p>
     * @return true:set success ; false: set failed;
     *
     */
    boolean setLanguage(String language, String country, String variant);
    /**
     * enable/disable Airplane Mode
     * @param true: alirplane mode on ; false: alirplane mode off.
     */
    void enableAirplaneMode(boolean enable);

    /**
      * visibility/hide statusbar settings button
      * @param true: statusbar settings button show ; false: statusbar settings button hide.
      */
    void setStatusbarSettingsButtonVisibility(boolean visibility);

    /**
      * get statusbar settings button status.
      * @return true: statusbar settings button visibility ; false: statusbar settings button hide.
      */
    boolean getStatusbarSettingsButtonVisibility();

    /**
      * Enable packageName the permission to install apps from unknown sources.
      * @permission: android.permission.CLOUDPOS_SET_UNKOWN_SOURCE
      * @return true: set success ; false: set failed, packageName not found.
      */
    boolean enableUnkownSourceApp(String packageName, boolean enable);


    boolean setAutoBatteryControlMode(boolean enable);


}
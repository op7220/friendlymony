package com.nect.friendlymony.Utils;


import com.nect.friendlymony.Model.FilterModel;
import com.nect.friendlymony.Model.SignupModel;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.orhanobut.hawk.Hawk;
import com.quickblox.users.model.QBUser;


@SuppressWarnings("unused")
public class HawkAppUtils {
    public static final String SIGNUP = "SIGNUP";
    public static final String USERDATA = "USERDATA";
    public static final String FILTER = "FILTER";
    public static String ISLOGIN = "ISLOGIN";
    public static String ACCESSTOKEN = "ACCESSTOKEN";
    public static String ISUSER_DATA = "ISUSER_DATA";

    public static String QBUSER_DATA = "QBUSER_DATA";

    private static final HawkAppUtils ourInstance = new HawkAppUtils();

    static public HawkAppUtils getInstance() {
        return ourInstance;
    }

    private HawkAppUtils() {
    }

    /**
     * Check user is Login or not.
     *
     * @return boolean.
     */
    public boolean getIsLogin() {
        return Hawk.get(ISLOGIN, false);
    }

    /**
     * Set value od user login.
     *
     * @param islogin set boolean value.
     */
    public void setIsLogin(boolean islogin) {
        Hawk.put(ISLOGIN, islogin);
    }

    /**
     * Check user is Login or not.
     *
     * @return boolean.
     */
    public boolean getIsUSERDATA() {
        return Hawk.get(ISUSER_DATA, false);
    }

    /**
     * Set value od user login.
     *
     * @param islogin set boolean value.
     */
    public void setIsUSERDATA(boolean islogin) {
        Hawk.put(ISUSER_DATA, islogin);
    }

    /**
     * Clear preference value.
     */
    public void clear() {
        Hawk.deleteAll();
    }

    public SignupModel getSIGNUP() {
        return Hawk.get(SIGNUP);
    }

    public void setSIGNUP(SignupModel signupModel) {
        Hawk.put(SIGNUP, signupModel);
    }

    public LoginResponse getUSERDATA() {
        return Hawk.get(USERDATA);
    }

    public void setUSERDATA(LoginResponse loginResponse) {
        Hawk.put(USERDATA, loginResponse);
    }

    public FilterModel getFiltr() {
        return Hawk.get(FILTER);
    }

    public void setFILTER(FilterModel filter) {
        Hawk.put(FILTER, filter);
    }

    public void resetFilter() {
        Hawk.delete(FILTER);
    }

  /*  public String getACCESSTOKEN() {
        if (Hawk.contains(USER)) {
            return ((RegisterResponse) Hawk.get(USER)).getToken();
        }
        return "";
    }*/


    public QBUser getQBUser() {
        return Hawk.get(QBUSER_DATA);
    }

    public void setQbuserData(QBUser filter) {
        Hawk.put(QBUSER_DATA, filter);
    }


}
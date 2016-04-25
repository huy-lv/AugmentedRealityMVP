package vnu.uet.augmentedrealitymvp.helper;

/**
 * Created by hienbx94 on 3/21/16.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {

    private static String TAG = SessionManager.class.getSimpleName();

    SharedPreferences pref;

    Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AugmentedReality";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setRegToken(String reg_token) {
        editor.putString("reg_token", reg_token);

        editor.commit();

        Log.d(TAG, "Reg token modified!");
    }

    public String getRegToken() {
        return pref.getString("reg_token", "");
    }

    public void addValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String key){
        return pref.getString(key, "");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}

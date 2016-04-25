package vnu.uet.augmentedrealitymvp.common.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Device Utils
 * Created by neo on 2/16/2016.
 */
public class DeviceUtils {
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}

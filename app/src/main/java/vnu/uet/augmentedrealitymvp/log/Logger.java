package vnu.uet.augmentedrealitymvp.log;

import android.annotation.SuppressLint;
import android.os.Debug;
import android.util.Log;

import java.text.DecimalFormat;

import vnu.uet.augmentedrealitymvp.BuildConfig;


/**
 * Logger utility. Disable logging when released
 * Created by Tuandt on 11/14/2014.
 */
public class Logger {
    // Max memory usage to restart app
    // private static final double MEMORY_BUFFER_LIMIT_FOR_RESTART = 0;

    // Change to false when release
    public static final boolean DEBUG_MODE = BuildConfig.DEBUG;

    private static final String TAG = "cxz";

    public static void e(String msg) {
        if (DEBUG_MODE)
            Log.e(TAG, msg);
    }

    public static void i(String msg) {
        if (DEBUG_MODE)
            Log.i(TAG, msg);
    }

    public static void w(String msg) {
        if (DEBUG_MODE)
            Log.w(TAG, msg);
    }

    public static void v(String msg) {
        if (DEBUG_MODE)
            Log.v(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG_MODE)
            Log.e(tag, msg);
    }

    public static void d(String msg) {
        if (DEBUG_MODE)
            Log.d(Logger.TAG_MEM, msg);
    }

    public static void i(String tag, String msg) {
        if (DEBUG_MODE)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG_MODE)
            Log.w(tag, msg);
    }

    private static final String TAG_MEM = "MEMORY";

    public static void v(String tag, String msg) {
        if (DEBUG_MODE)
            Log.v(tag, msg);
    }

    @SuppressLint("DefaultLocale")
    @SuppressWarnings("rawtypes, unused")
    public static void logHeap(Class clazz) {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);

        String memMessage = String.format("App Memory: Pss=%.2f MB, Private=%.2f MB, Shared=%.2f MB", memoryInfo.getTotalPss() / 1024.0, memoryInfo.getTotalPrivateDirty() / 1024.0, memoryInfo.getTotalSharedDirty() / 1024.0);

        Logger.i(TAG_MEM, memMessage);

        Double allocated = (double) Debug.getNativeHeapAllocatedSize() / (double) (1048576);
        Double available = (double) Debug.getNativeHeapSize() / 1048576.0;
        Double free = (double) Debug.getNativeHeapFreeSize() / 1048576.0;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        Logger.d("debug. =================================");
        Logger.d("debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free) in [" + clazz.getName().replaceAll("com.myapp.android.", "") + "]");
        Logger.d("debug.memory: allocated: " + df.format((double) (Runtime.getRuntime().totalMemory() / 1048576)) + "MB of " + df.format((double) (Runtime.getRuntime().maxMemory() / 1048576)) + "MB (" + df.format((double) (Runtime.getRuntime().freeMemory() / 1048576)) + "MB free)");
        System.gc();
        System.gc();

        // don't need to add the following lines, it's just an app specific
        // handling in my app
        // if (allocated>=(new Double(Runtime.getRuntime().maxMemory())/new
        // Double((1048576))-MEMORY_BUFFER_LIMIT_FOR_RESTART)) {
        // android.os.Process.killProcess(android.os.Process.myPid());
    }
}

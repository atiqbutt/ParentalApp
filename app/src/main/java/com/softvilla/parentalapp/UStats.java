package com.softvilla.parentalapp;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Malik on 02/01/2018.
 */

public class UStats {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
    public static final String TAG = UStats.class.getSimpleName();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("ResourceType")
    public static void getStats(Context context) {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        int interval = UsageStatsManager.INTERVAL_YEARLY;
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();
        Log.d(TAG, "Range start:" + dateFormat.format(startTime));
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));
        UsageEvents uEvents = usm.queryEvents(startTime, endTime);
        while (uEvents.hasNextEvent()) {
            UsageEvents.Event e = new UsageEvents.Event();
            uEvents.getNextEvent(e);
            if (e != null) {
                Toast.makeText(context, "Event: " + e.getPackageName() + "\t" + e.getTimeStamp(), Toast.LENGTH_SHORT).show();
               // Log.d(TAG, "Event: " + e.getPackageName() + "\t" + e.getTimeStamp());
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static List<UsageStats> getUsageStatsList(Context context) {
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();
        Log.d(TAG, "Range start:" + dateFormat.format(startTime));
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));
        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        return usageStatsList;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void printUsageStats(List<UsageStats> usageStatsList) {
        String value = null;
        for (UsageStats u : usageStatsList) {
            if(u.getPackageName().equals("com.softvilla.parentalapp")){
                u.getLastTimeUsed();

           // Toast.makeText(, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: " + u.getTotalTimeInForeground(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: "
                    + TimeUnit.MILLISECONDS.toMinutes(u.getTotalTimeInForeground()));
            value = "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: " + u.getTotalTimeInForeground();
        }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void printCurrentUsageStatus(Context context) {
        printUsageStats(getUsageStatsList(context));
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String printUsageStatus(Context context) {
        return printUsageStatss(getUsageStatsList(context));
    }
    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context) {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String printUsageStatss(List<UsageStats> usageStatsList) {
        String value = null;
        for (UsageStats u : usageStatsList) {
            if(u.getPackageName().equals("com.softvilla.parentalapp")){
                u.getLastTimeUsed();
                Log.d(TAG, "Pkg: " + u.getPackageName().equalsIgnoreCase("com.softvilla.childapp") + "\t" + "ForegroundTime: "
                        + u.getTotalTimeInForeground());
                value = "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: " + TimeUnit.MILLISECONDS.toMinutes(u.getTotalTimeInForeground());
            }


        }
        return value;
    }


}
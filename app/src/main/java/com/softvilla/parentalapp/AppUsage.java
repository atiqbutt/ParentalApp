package com.softvilla.parentalapp;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class AppUsage extends AppCompatActivity {

    Button statsBtn;
    TextView status;
    Context mContext;

    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 100;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage);
        /*ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
       *//* Log.d("topActivity", "CURRENT Activity ::"
                + taskInfo.get(0).topActivity.getClassName());*//*
        Toast.makeText(AppUsage.this, "topActivity CURRENT Activity ::"+ taskInfo.get(0).topActivity.getClassName(), Toast.LENGTH_SHORT).show();
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        componentInfo.getPackageName();*/

       /* status = (TextView) findViewById(R.id.usage_stats);
        mContext = AppUsage.this;
        //Check if permission enabled
        if (UStats.getUsageStatsList(this).isEmpty()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        statsBtn = (Button) findViewById(R.id.stats_btn);
        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  UStats.printCurrentUsageStatus(MainActivity.this);
                if(isRunning(AppUsage.this)==true){
                    status.setText("Running");
                }
                else {
                status.setText(UStats.printUsageStatus(mContext));
            }}
        });*/
         //fillStats();
       // printForegroundTask();
        //startService(new Intent(AppUsage.this,CheckForeGroundApp.class));
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void fillStats() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasPermission()) {
                getStats();
            } else {
                requestPermission();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MainActivity", "resultCode " + resultCode);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS:
                fillStats();
                break;
        }
    }

    private void requestPermission() {
        Toast.makeText(this, "Need to request permission", Toast.LENGTH_SHORT).show();
        startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
//        return ContextCompat.checkSelfPermission(this,
//                Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getStats() {
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        UsageStatsManager lUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> lUsageStatsList = lUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime,endTime);

       TextView lTextView = (TextView) findViewById(R.id.usage_stats);

        StringBuilder lStringBuilder = new StringBuilder();

        for (UsageStats lUsageStats:lUsageStatsList){
            lStringBuilder.append(MILLISECONDS.toMinutes(lUsageStats.getLastTimeUsed()));
            //lStringBuilder.append(" - ");*/
            lStringBuilder.append(lUsageStats.getPackageName());
            lStringBuilder.append(" - ");
            lStringBuilder.append(MILLISECONDS.toMinutes(lUsageStats.getTotalTimeInForeground()));
            lStringBuilder.append("\r\n");
            Toast.makeText(this, lUsageStatsList.size()+lUsageStats.getPackageName()+MILLISECONDS.toMinutes(lUsageStats.getTotalTimeInForeground()), Toast.LENGTH_SHORT).show();
           // Toast.makeText(this,lUsageStatsList.size() , Toast.LENGTH_SHORT).show();
            //lStringBuilder.append(lUsageStatsList.size());
        }


        lTextView.setText(lStringBuilder.toString());
    }

    public static boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        Toast.makeText(this, UStats.printUsageStatus(mContext), Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    private void printForegroundTask() {
        String currentApp = "NULL";
        long time1 = System.currentTimeMillis();
        long time = System.currentTimeMillis();
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);

            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    time1 = TimeUnit.MILLISECONDS.toMinutes(mySortedMap.get(mySortedMap.lastKey()).getTotalTimeInForeground());
                }
            }
        } else {
            ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
        Toast.makeText(AppUsage.this, "Current App in foreground is: " + currentApp+time1, Toast.LENGTH_SHORT).show();

        //Log.e(TAG, "Current App in foreground is: " + currentApp);
    }

}

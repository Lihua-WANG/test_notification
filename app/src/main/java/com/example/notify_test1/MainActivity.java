package com.example.notify_test1;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final String CHANNEL_ID = "ReceiptUpdate_notifications";
    private final int NOTIFICATION_ID = 001;

    int type = AlarmManager.RTC_WAKEUP;
    //new Date()：表示当前日期，可以根据项目需求替换成所求日期
    //getTime()：日期的该方法同样可以表示从1970年1月1日0点至今所经历的毫秒数
    long triggerAtMillis = new Date().getTime();
    long intervalMillis = 100 * 60;
    public class GlobalValues {
        // 周期性的闹钟
        public final static String TIMER_ACTION_REPEATING = "com.example.TIMER_ACTION_REPEATING";
        // 定时闹钟
        public final static String TIMER_ACTION = "com.example.TIMER_ACTION";
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void DisplayNotification(View view)
    {
        createdNotificationChannel();

        Intent receiptIntent = new Intent(this, ReceiptsActivity.class);
        receiptIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent receiptPendingIntent = PendingIntent.getActivity(this, 0, receiptIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent YesIntent = new Intent(this, yesactivity.class);
        YesIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent YesPendingIntent = PendingIntent.getActivity(this, 0, YesIntent, PendingIntent.FLAG_ONE_SHOT);


        AlarmManager alarmmanager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmmanager != null;
//        alarmmanager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), intervalMillis, receiptPendingIntent);

        //Intent设置要启动的组件，这里启动广播
        Intent alarmIntent = new Intent();
        alarmIntent.setAction(GlobalValues.TIMER_ACTION);
        //PendingIntent对象设置动作,启动的是Activity还是Service,或广播!
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, alarmIntent,0);
        //注册闹钟
        alarmmanager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1 * 1000, sender);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_notifications);
        builder.setTicker("Grocery receipt update notification");
        builder.setContentTitle("Receipt Update");
        builder.setContentText("Hi, please add your latest grocery receipts");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(receiptPendingIntent);
        builder.addAction(R.drawable.ic_baseline_notifications, "Add Receipts", YesPendingIntent);
        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createdNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "Personal Notifications";
            String description = "Include all the personal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificatiionManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificatiionManager.createNotificationChannel(notificationChannel);
        }
    }


}

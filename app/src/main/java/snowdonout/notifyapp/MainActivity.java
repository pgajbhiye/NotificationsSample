package snowdonout.notifyapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    Button alertBtn, cancelBtn, listBtn, accessBtn;
    NotificationManager manager;
    static final int NOTIFICATION_ID = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        alertBtn = (Button) findViewById(R.id.alert);
        cancelBtn = (Button) findViewById(R.id.cancel);
        listBtn = (Button) findViewById(R.id.list);

        if (!hasNotificationAccess()) {
            Log.i(LOG_TAG, "Noification Access needs to be enabled ");
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Notification Access already enabled", Toast.LENGTH_SHORT).show();
        }

        alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.cancel(NOTIFICATION_ID);
            }
        });

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAllActiveNotifications();
            }
        });

    }

    private void fetchAllActiveNotifications() {
        StatusBarNotification[] activeNotifications = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activeNotifications = manager
                    .getActiveNotifications();
        } else {
            ArrayList list = (ArrayList) MyListenerService.getUpdatedNotificationsList();
            if (list.size() > 0) {
                activeNotifications = (StatusBarNotification[]) list.toArray(new StatusBarNotification[list.size()]);
            }
        }

        if (activeNotifications != null && activeNotifications.length > 0) {
            for (StatusBarNotification notification : activeNotifications) {
                if (notification != null) {
                    Log.d(LOG_TAG, "Notification details " + notification.getPackageName() + " -- " + notification.getPostTime() + " -- " + notification.getNotification().tickerText);
                }
            }
            Toast.makeText(this, "Total notifications " + activeNotifications.length, Toast.LENGTH_SHORT).show();
        }
    }

    public void showNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Sample Notification")
                .setPriority(Notification.PRIORITY_HIGH)
                .setOngoing(true) // User cannot dismiss the notification
                .setContentTitle("Notification Title")
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(new long[]{10, 20, 10, 40}) //custom vibration
                .setSmallIcon(R.drawable.ic_launcher);

        Notification notification = builder.build();

        if (notification != null) {
            manager.notify(NOTIFICATION_ID, notification);
        }
    }

    public boolean hasNotificationAccess() {
        String enabled = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        return (enabled != null && enabled.contains(getPackageName()));

    }
}

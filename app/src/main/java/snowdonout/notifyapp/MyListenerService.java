package snowdonout.notifyapp;

import android.app.Notification;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyListenerService extends NotificationListenerService {

    private static final String LOG_TAG = MyListenerService.class.getName();

    public MyListenerService() {
        super();
        Log.d(LOG_TAG, "constructor ");

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.d(LOG_TAG, "onNotificationPosted: ");
        Notification noti = sbn.getNotification();
        Log.d(LOG_TAG, "package " + sbn.getPackageName());
        Log.d(LOG_TAG, "post time  " + sbn.getPostTime());
        Log.d(LOG_TAG, "ticket text  " + noti.tickerText);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Bundle extras = noti.extras;
            String title = extras.getString(Notification.EXTRA_TITLE);
            Bitmap xLargeIcon = extras.getParcelable(Notification.EXTRA_LARGE_ICON_BIG);
            Bitmap largeIcon = extras.getParcelable(Notification.EXTRA_LARGE_ICON);
            Bitmap smallIcon = extras.getParcelable(Notification.EXTRA_SMALL_ICON);
            String bigText = extras.getString(Notification.EXTRA_BIG_TEXT);
            String subText = extras.getString(Notification.EXTRA_SUB_TEXT);

            Log.d(LOG_TAG, "Extras " + title + " -- " + bigText + " -- " + subText);

        }

        list.add(sbn);

        StatusBarNotification[] activeNotifications = getActiveNotifications();

        if (activeNotifications != null && activeNotifications.length > 0) {
            for (StatusBarNotification notification : activeNotifications) {
                if (notification != null) {
                    Log.d(LOG_TAG, "Notification details:service " + notification.getPackageName() + " -- " + notification.getPostTime() + " -- " + notification.getNotification().tickerText);
                }
            }

        }
    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.d(LOG_TAG, "removing  " + sbn.getPackageName());
        list.remove(sbn);
    }


    @Override
    public StatusBarNotification[] getActiveNotifications() {
        return super.getActiveNotifications();
    }

    static List<StatusBarNotification> list = new ArrayList<>();

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(LOG_TAG, "listener connected ");
        StatusBarNotification[] noti = getActiveNotifications();
        for (StatusBarNotification sbn : noti) {
            Log.d(LOG_TAG, "Adding " + sbn.getPackageName());
            list.add(sbn);
        }
    }

    public static List getUpdatedNotificationsList() {
        return list;
    }
}

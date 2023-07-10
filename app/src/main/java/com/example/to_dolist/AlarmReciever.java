package com.example.to_dolist;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReciever extends BroadcastReceiver {
    private static final String CHANNEL_ID = "my_channel";
    private static final int NOTIFICATION_ID = 123;

    private static final String EXTRA_NOTE_TITLE = "extra_note_title";
    private static final String EXTRA_NOTE_DESCRIPTION = "extra_note_description";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Получение данных о заметке из Intent
        String noteTitle = intent.getStringExtra("extra_note_title");
        String noteDescription = intent.getStringExtra("extra_note_description");

        // Создание уведомления
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Создание канала уведомлений (для Android 8.0 и выше)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(channel);

            // Создание и настройка уведомления с указанием информации о заметке
            Notification.Builder builder = new Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Дедлайн истек!")
                    .setContentText("Заметка: " + noteTitle + " истекла.")
                    .setPriority(Notification.PRIORITY_DEFAULT);

            // Отправка уведомления
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }


    }

    // Метод для создания PendingIntent с информацией о заметке
    public static PendingIntent getPendingIntent(Context context, String noteTitle, String noteDescription) {
        Intent intent = new Intent(context, AlarmReciever.class);
        intent.setAction("MY_NOTIFICATION_ACTION");
        intent.putExtra(EXTRA_NOTE_TITLE, noteTitle);
        intent.putExtra(EXTRA_NOTE_DESCRIPTION, noteDescription);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

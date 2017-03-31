package net.pkhapps.dart.statuspanel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import net.pkhapps.dart.statuspanel.json.JsonSetStatusCommand;
import net.pkhapps.dart.statuspanel.model.IStatusDescriptor;
import net.pkhapps.dart.statuspanel.model.IStatusService;
import net.pkhapps.dart.statuspanel.model.IStatusServiceListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by peholmst on 30/03/2017.
 */

public class StatusService extends Service implements IStatusService, IRabbitMQManagerStateListener {

    private static final String TAG = "StatusService";

    private static final int NOTIFICATION_ID = 123;

    private static final long[] VIBRATE_CONNECTING = {0, 200, 200, 200, 200, 200};
    private static final long[] VIBRATE_CONNECTED = {0, 1000};

    private final IBinder binder = new LocalBinder();

    private List<IStatusDescriptor> statusDescriptors = new ArrayList<>();

    private IStatusDescriptor currentStatus = null;

    private Date lastStatusChange = null;

    private Set<IStatusServiceListener> listeners = new HashSet<>();

    private final AtomicBoolean started = new AtomicBoolean(false);
    private final RabbitMQManager rabbitMQManager = new RabbitMQManager();


    public StatusService() {
        statusDescriptors.add(new StatusDescriptorImpl(1L, "På väg", Color.CYAN, true, true, Arrays.asList(2L, 3L)));
        statusDescriptors.add(new StatusDescriptorImpl(2L, "Framme", Color.BLUE, true, true, Collections.singletonList(3L)));
        statusDescriptors.add(new StatusDescriptorImpl(3L, "Ledig", Color.GREEN, true, true, Arrays.asList(4L, 5L)));
        statusDescriptors.add(new StatusDescriptorImpl(4L, "På stationen", Color.YELLOW, false, true, Arrays.asList(3L, 5L)));
        statusDescriptors.add(new StatusDescriptorImpl(5L, "Ej alarmerbar", Color.RED, false, true, Arrays.asList(3L, 4L)));
        statusDescriptors.add(new StatusDescriptorImpl(6L, "Uppdrag mottaget", Color.MAGENTA, true, true, Arrays.asList(1L, 3L)));
        statusDescriptors.add(new StatusDescriptorImpl(7L, "Alarmerad", Color.WHITE, true, false, Arrays.asList(1L, 6L, 3L)));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!started.getAndSet(true)) {
            Log.d(TAG, "Starting StatusService");
            startForeground(NOTIFICATION_ID, createStatusBarNotification());
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        rabbitMQManager.init();
        rabbitMQManager.connect();
        rabbitMQManager.addStateListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rabbitMQManager.removeStateListener(this);
        rabbitMQManager.disconnect();
        rabbitMQManager.destroy();
    }

    private Notification createStatusBarNotification() {
        // Create an intent for showing the main activity. Build an artificial back stack to make
        // sure navigating backward from the main activity leads to the home screen.
        final Intent intent = new Intent(this, MainActivity.class);
        final TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        final PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setContentText(getResources().getString(R.string.status_notification_text));
        int icon = R.drawable.ic_error_white_24dp;
        int title;
        switch (rabbitMQManager.getState()) {
            case CONNECTED:
                title = R.string.status_notification_title_connected;
                icon = R.drawable.ic_whatshot_white_24dp;
                builder.setVibrate(VIBRATE_CONNECTED);
                break;
            case DISCONNECTED:
            case DISCONNECTING:
                title = R.string.status_notification_title_disconnected;
                break;
            case CONNECTING:
                title = R.string.status_notification_title_connecting;
                builder.setProgress(0, 0, true);
                builder.setVibrate(VIBRATE_CONNECTING);
                break;
            default:
                title = R.string.status_notification_title_setup;
        }
        builder
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), icon))
                .setContentTitle(getResources().getString(title));
        return builder.build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Binding to StatusService");
        return binder;
    }

    @NonNull
    @Override
    public List<IStatusDescriptor> getStatusDescriptors() {
        return statusDescriptors;
    }

    @Override
    public void sendStatus(@NonNull IStatusDescriptor statusDescriptor) {
        rabbitMQManager.publishToQueue(Routing.STATUS_COMMAND_QUEUE, null, new JsonSetStatusCommand(statusDescriptor.getId()));
        // TODO Wait for message from the server that contains the status
        this.currentStatus = statusDescriptor;
        this.lastStatusChange = new Date();
        for (IStatusServiceListener listener : new HashSet<>(listeners)) {
            try {
                listener.onCurrentStatusChanged(this);
            } catch (Exception ex) {
                // Ignore it
            }
        }
    }

    @Nullable
    @Override
    public IStatusDescriptor getCurrentStatus() {
        return currentStatus;
    }

    @Nullable
    @Override
    public Date getLastStatusChange() {
        return lastStatusChange;
    }

    @Override
    public void addStatusServiceListener(@Nullable IStatusServiceListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeStatusServiceListener(@Nullable IStatusServiceListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    @Override
    public void shutdown() {
        stopSelf();
    }

    @Override
    public void onStateChange(@NonNull RabbitMQManager rabbitMQManager, @NonNull RabbitMQManagerState newState) {
        // Update the status bar notification
        Log.i(TAG, "RabbitMQ manager state has changed, updating status bar notification");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, createStatusBarNotification());
    }

    public class LocalBinder extends Binder {

        @NonNull
        public IStatusService getService() {
            return StatusService.this;
        }
    }

    private static class StatusDescriptorImpl implements IStatusDescriptor {

        private final long id;
        private final String caption;
        private final int backgroundColor;
        private final boolean locationBroadcastEnabled;
        private final boolean userSelectable;
        private final Set<Long> validTransitions;

        public StatusDescriptorImpl(long id, String caption, int backgroundColor,
                                    boolean locationBroadcastEnabled, boolean userSelectable,
                                    Collection<Long> validTransitions) {
            this.id = id;
            this.caption = caption;
            this.backgroundColor = backgroundColor;
            this.locationBroadcastEnabled = locationBroadcastEnabled;
            this.userSelectable = userSelectable;
            this.validTransitions = Collections.unmodifiableSet(new HashSet<>(validTransitions));
        }

        @Override
        public long getId() {
            return id;
        }

        @Override
        public String getCaption() {
            return caption;
        }

        @Override
        public int getBackgroundColor() {
            return backgroundColor;
        }

        @Override
        public boolean isLocationBroadcastEnabled() {
            return locationBroadcastEnabled;
        }

        @Override
        public boolean isUserSelectable() {
            return userSelectable;
        }

        @Override
        public Set<Long> getValidTransitions() {
            return validTransitions;
        }
    }
}

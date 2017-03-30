package net.pkhapps.dart.statuspanel;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import net.pkhapps.dart.statuspanel.model.IStatusDescriptor;
import net.pkhapps.dart.statuspanel.model.IStatusService;
import net.pkhapps.dart.statuspanel.model.IStatusServiceListener;

public class MainActivity extends Activity implements IStatusServiceListener {

    private static final String TAG = "MainActivity";

    private IStatusService statusService;
    private final ServiceConnection statusServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            statusService = ((StatusService.LocalBinder) service).getService();
            onStatusServiceBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            statusService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView currentStatusDescription = (TextView) findViewById(R.id.currentStatusDescription);
        currentStatusDescription.setText(getResources().getString(R.string.waiting_for_service));
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Intent intent = new Intent(this, StatusService.class);
        startService(intent);
        Log.d(TAG, "Binding to StatusService");
        bindService(intent, statusServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isStatusServiceAvailable()) {
            Log.d(TAG, "Unbinding from StatusService");
            getStatusService().removeStatusServiceListener(this);
            unbindService(statusServiceConnection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void onStatusServiceBound() {
        createStatusButtons(getStatusService());
        getStatusService().addStatusServiceListener(this);
    }

    private void createStatusButtons(@NonNull IStatusService statusService) {
        final FlexboxLayout statusButtonLayout = (FlexboxLayout) findViewById(R.id.statusButtonLayout);
        statusButtonLayout.removeAllViews();
        for (IStatusDescriptor status : statusService.getStatusDescriptors()) {
            if (status.isUserSelectable()) {
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "Creating button for status " + status);
                }
                Button button = createButton(status);
                statusButtonLayout.addView(button);
            }
        }
        onCurrentStatusChanged(statusService);
    }

    private Button createButton(@NonNull final IStatusDescriptor status) {
        final Button button = new Button(this);
        button.setText(status.getCaption());
        button.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                getResources().getDisplayMetrics()));
        button.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                getResources().getDisplayMetrics()));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStatusService().sendStatus(status);
            }
        });
        button.setTag(status);
        return button;
    }

    private boolean isStatusServiceAvailable() {
        return statusService != null;
    }

    @NonNull
    private IStatusService getStatusService() {
        if (statusService == null) {
            throw new IllegalStateException("StatusService is not available");
        }
        return statusService;
    }

    @Override
    public void onCurrentStatusChanged(@NonNull IStatusService statusService) {
        Log.d(TAG, "Status has changed");
        // Update status buttons
        final FlexboxLayout statusButtonLayout = (FlexboxLayout) findViewById(R.id.statusButtonLayout);
        final IStatusDescriptor currentStatus = statusService.getCurrentStatus();
        for (int i = 0; i < statusButtonLayout.getChildCount(); ++i) {
            View view = statusButtonLayout.getChildAt(i);
            if (view.getTag() instanceof IStatusDescriptor) {
                final IStatusDescriptor status = (IStatusDescriptor) view.getTag();
                PorterDuff.Mode mode;
                int typefaceStyle;
                if (status.equals(currentStatus)) {
                    mode = PorterDuff.Mode.SRC_IN;
                    typefaceStyle = Typeface.BOLD;
                } else {
                    mode = PorterDuff.Mode.MULTIPLY;
                    typefaceStyle = Typeface.NORMAL;
                }
                view.getBackground().setColorFilter(status.getBackgroundColor(), mode);
                ((Button) view).setTypeface(null, typefaceStyle);
                view.setEnabled(currentStatus == null || currentStatus.getValidTransitions().contains(status.getId()));
            }
        }

        // Update status text
        TextView currentStatusDescription = (TextView) findViewById(R.id.currentStatusDescription);
        if (currentStatus == null) {
            currentStatusDescription.setText(getResources().getString(R.string.no_status));
        } else {
            currentStatusDescription.setText(Html.fromHtml(getResources().getString(R.string.current_status,
                    currentStatus.getCaption(), getStatusService().getLastStatusChange())));
        }
    }

    @Override
    public void onStatusDescriptorsChanged(@NonNull IStatusService statusService) {
        // This will recreate the buttons...
        createStatusButtons(statusService);
        // ... and update the state
        onCurrentStatusChanged(statusService);
    }

    public void onSettingsClick(MenuItem menuItem) {
        // TODO Implement me!
    }

    public void onShutdownClick(MenuItem menuItem) {
        if (isStatusServiceAvailable()) {
            getStatusService().shutdown();
        }
        finish();
    }
}

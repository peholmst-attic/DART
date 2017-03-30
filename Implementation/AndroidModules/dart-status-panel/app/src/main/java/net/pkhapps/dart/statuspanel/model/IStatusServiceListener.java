package net.pkhapps.dart.statuspanel.model;

import android.support.annotation.NonNull;

/**
 * Created by peholmst on 28/03/2017.
 */
public interface IStatusServiceListener {

    void onCurrentStatusChanged(@NonNull IStatusService statusService);

    void onStatusDescriptorsChanged(@NonNull IStatusService statusService);
}

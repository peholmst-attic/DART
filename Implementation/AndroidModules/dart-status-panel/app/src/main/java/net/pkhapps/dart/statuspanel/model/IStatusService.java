package net.pkhapps.dart.statuspanel.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

/**
 * Created by peholmst on 28/03/2017.
 */
public interface IStatusService {

    @NonNull
    List<IStatusDescriptor> getStatusDescriptors();

    void sendStatus(@NonNull IStatusDescriptor statusDescriptor);

    @Nullable
    IStatusDescriptor getCurrentStatus();

    @Nullable
    Date getLastStatusChange();

    void addStatusServiceListener(@Nullable IStatusServiceListener listener);

    void removeStatusServiceListener(@Nullable IStatusServiceListener listener);

    void shutdown();
}

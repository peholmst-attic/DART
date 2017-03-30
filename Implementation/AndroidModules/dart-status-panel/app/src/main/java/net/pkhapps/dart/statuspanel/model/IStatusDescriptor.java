package net.pkhapps.dart.statuspanel.model;

import android.support.annotation.NonNull;

import java.util.Set;

/**
 * Created by peholmst on 28/03/2017.
 */

public interface IStatusDescriptor {

    long getId();

    @NonNull
    String getCaption();

    int getBackgroundColor();

    boolean isLocationBroadcastEnabled();

    boolean isUserSelectable();

    @NonNull
    Set<Long> getValidTransitions();
}

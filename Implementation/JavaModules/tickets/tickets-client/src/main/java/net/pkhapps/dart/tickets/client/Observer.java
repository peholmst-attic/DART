package net.pkhapps.dart.tickets.client;

/**
 * Created by peholmst on 16/01/2017.
 */
public interface Observer<T> {

    void onTimeout();

    void onError(Throwable error);

    void onSuccess(T result);
}

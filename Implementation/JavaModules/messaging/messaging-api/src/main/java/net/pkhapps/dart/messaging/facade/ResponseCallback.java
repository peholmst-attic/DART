package net.pkhapps.dart.messaging.facade;

import net.pkhapps.dart.messaging.messages.Response;

import java.io.IOException;

public interface ResponseCallback<R extends Response> {

    void onResponse(R response);

    void onTimeout();

    void onReceiveError(IOException exception);
}

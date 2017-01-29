package net.pkhapps.dart.platform.messages;

/**
 * Created by peholmst on 21/01/2017.
 */
public interface Reply extends Message {

    /**
     * @return
     */
    default boolean isSuccessful() {
        return getStatusCode().isSuccessful();
    }

    /**
     * @return
     */
    StatusCode getStatusCode();
}

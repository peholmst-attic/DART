package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.messaging.messages.Request;

public class GetAllResources extends Request<AllResources> {

    private final boolean disabledIncluded;

    public GetAllResources(boolean disabledIncluded) {
        super(null, null);
        this.disabledIncluded = disabledIncluded;
    }

    public boolean isDisabledIncluded() {
        return disabledIncluded;
    }
}

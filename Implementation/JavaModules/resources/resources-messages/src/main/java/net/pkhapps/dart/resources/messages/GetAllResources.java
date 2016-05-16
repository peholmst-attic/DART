package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.messaging.messages.Request;

public interface GetAllResources extends Request<AllResources> {

    boolean isDisabledIncluded();
}

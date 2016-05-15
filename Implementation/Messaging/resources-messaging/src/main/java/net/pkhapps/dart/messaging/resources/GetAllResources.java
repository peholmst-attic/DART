package net.pkhapps.dart.messaging.resources;

import net.pkhapps.dart.messaging.common.messages.Request;

public interface GetAllResources extends Request<AllResources> {

    boolean isDisabledIncluded();
}

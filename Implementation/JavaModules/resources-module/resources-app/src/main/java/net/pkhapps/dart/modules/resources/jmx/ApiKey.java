package net.pkhapps.dart.modules.resources.jmx;

import net.pkhapps.dart.modules.resources.apikeys.ApiKeyService;

/**
 * Created by petterprivate on 02/02/2017.
 */
class ApiKey implements ApiKeyMBean {

    ApiKey(ApiKeyService apiKeyService) {

    }

    @Override
    public String createApiKey(String systemIdentifier) {
        return null;
    }

    @Override
    public String[] getSystemIdentifiers() {
        return new String[0];
    }

    @Override
    public void deleteApiKey(String systemIdentifier) {

    }
}

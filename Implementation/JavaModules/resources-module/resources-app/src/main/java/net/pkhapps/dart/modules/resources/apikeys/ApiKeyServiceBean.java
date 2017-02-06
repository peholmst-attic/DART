package net.pkhapps.dart.modules.resources.apikeys;

/**
 * Created by petterprivate on 02/02/2017.
 */
class ApiKeyServiceBean implements ApiKeyService {

    @Override
    public boolean isValidApiKey(String apiKey) {
        return false;
    }
}

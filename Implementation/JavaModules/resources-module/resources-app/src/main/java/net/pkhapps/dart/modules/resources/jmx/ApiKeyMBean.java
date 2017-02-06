package net.pkhapps.dart.modules.resources.jmx;

/**
 * Created by petterprivate on 02/02/2017.
 */
public interface ApiKeyMBean {

    String createApiKey(String systemIdentifier);

    String[] getSystemIdentifiers();

    void deleteApiKey(String systemIdentifier);
}

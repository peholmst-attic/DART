package net.pkhapps.dart.modules.resources;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Created by petterprivate on 02/02/2017.
 */
public class ResourcesApp {

    public static void main(String[] args) throws Exception {
        // Register the MBeans
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        //ObjectName name = new ObjectName("net.pkhapps.dart.modules.resources:type=ApiKey");
        //ApiKeyMBean apiKeyMBean = new ApiKey(null);
        //mbs.registerMBean(apiKeyMBean, name);

        // Connect to RabbitMQ

        // Enter read loop
    }
}

package net.pkhapps.dart.modules.accounts;

import com.mchange.v2.c3p0.PooledDataSource;
import com.rabbitmq.client.AuthenticationFailureException;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.pkhapps.dart.modules.accounts.domain.PasswordUtil;
import net.pkhapps.dart.modules.accounts.domain.db.tables.records.AccountTypesRecord;
import net.pkhapps.dart.modules.accounts.domain.db.tables.records.AccountsRecord;
import org.jooq.DSLContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static net.pkhapps.dart.modules.accounts.domain.db.DartAccounts.DART_ACCOUNTS;

/**
 * Simple integration test for the DART Accounts App. For this test to work, database must be setup and running,
 * RabbitMQ must be running and configured to use <b>rabbit_auth_backend_amqp</b> and DART Accounts App must be running.
 */
public class AccountsAppIntegrationTest {

    private static final RabbitMQProperties rabbitMQProperties = new RabbitMQProperties();

    private static DatabaseProvider databaseProvider;
    private static PooledDataSource dataSource;
    private static DSLContext dslContext;

    private static String accountType;
    private static String accountName;

    @BeforeClass
    public static void setUp() {
        databaseProvider = new DatabaseProvider();
        dataSource = databaseProvider.createDataSource();
        dslContext = databaseProvider.createDSLContext(dataSource);

        // Create a test account type
        accountType = AccountsAppIntegrationTest.class.getName() + System.currentTimeMillis();
        AccountTypesRecord accountTypeRecord = dslContext.newRecord(DART_ACCOUNTS.ACCOUNT_TYPES);
        accountTypeRecord.setName(accountType);
        accountTypeRecord.store();

        // Create a test account
        accountName = "joecool" + System.currentTimeMillis();
        AccountsRecord accountRecord = dslContext.newRecord(DART_ACCOUNTS.ACCOUNTS);
        accountRecord.setName(accountName);
        accountRecord.setPassword(PasswordUtil.hashPassword("mypassword"));
        accountRecord.setTypeId(accountTypeRecord.getId());
        accountRecord.setEnabled(true);
        accountRecord.store();
    }

    @AfterClass
    public static void tearDown() {
        // Delete test account type
        dslContext.deleteFrom(DART_ACCOUNTS.ACCOUNTS).where(DART_ACCOUNTS.ACCOUNTS.NAME.eq(accountName)).execute();

        // Delete test account type
        dslContext.deleteFrom(DART_ACCOUNTS.ACCOUNT_TYPES).where(DART_ACCOUNTS.ACCOUNT_TYPES.NAME.eq(accountType))
                .execute();

        databaseProvider.destroyDSLContext(dslContext);
        databaseProvider.destroyDataSource(dataSource);
    }

    @Test
    public void loginToRabbitMQ_validUserNameAndPassword_loginSuccessful() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitMQProperties.getHost().get());
        connectionFactory.setVirtualHost(rabbitMQProperties.getVirtualHost().get());
        connectionFactory.setPort(rabbitMQProperties.getPort().get());

        connectionFactory.setUsername(accountName);
        connectionFactory.setPassword("mypassword");

        Connection connection = connectionFactory.newConnection();
        connection.close();
        // No errors expected
    }

    @Test(expected = AuthenticationFailureException.class)
    public void loginToRabbitMQ_validUserNameAndInvalidPassword_loginFailed() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitMQProperties.getHost().get());
        connectionFactory.setVirtualHost(rabbitMQProperties.getVirtualHost().get());
        connectionFactory.setPort(rabbitMQProperties.getPort().get());

        connectionFactory.setUsername(accountName);
        connectionFactory.setPassword("incorrectpassword");

        connectionFactory.newConnection();
    }
}

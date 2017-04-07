package net.pkhapps.dart.modules.accounts.domain;

import net.pkhapps.dart.modules.accounts.domain.db.tables.records.AccountTypePermissionsRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.regex.Pattern;

import static net.pkhapps.dart.modules.accounts.domain.db.DartAccounts.DART_ACCOUNTS;

/**
 * Default implementation of {@link AuthenticationBackend} that reads the user data from the database using JOOQ and
 * uses {@link PasswordUtil} to verify passwords.
 */
@ApplicationScoped
class AuthenticationBackendImpl implements AuthenticationBackend {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationBackendImpl.class);

    @Inject
    DSLContext dslContext;

    @Override
    public boolean login(@NotNull String name, @NotNull String password) {
        // @formatter:off
        final String actualPassword = dslContext.select(DART_ACCOUNTS.ACCOUNTS.PASSWORD)
                .from(DART_ACCOUNTS.ACCOUNTS)
                .where(DART_ACCOUNTS.ACCOUNTS.NAME.equalIgnoreCase(name)
                        .and(DART_ACCOUNTS.ACCOUNTS.ENABLED.isTrue()))
                .fetchOne(DART_ACCOUNTS.ACCOUNTS.PASSWORD);
        // @formatter:on
        final boolean result = actualPassword != null && PasswordUtil.verifyPassword(actualPassword, password);
        if (result) {
            LOGGER.debug("Authentication of account [{}] succeeded", name);
        } else {
            LOGGER.warn("Authentication of account [{}] failed", name);
        }
        return result;
    }

    @Override
    public boolean checkResource(@NotNull String name, @NotNull String resourceName, @NotNull ResourceType resourceType,
                                 @NotNull ResourcePermission permission) {
        TableField<AccountTypePermissionsRecord, String> permissionField;
        switch (permission) {
            case CONFIGURE:
                permissionField = DART_ACCOUNTS.ACCOUNT_TYPE_PERMISSIONS.CONFIGURE;
                break;
            case READ:
                permissionField = DART_ACCOUNTS.ACCOUNT_TYPE_PERMISSIONS.READ;
                break;
            case WRITE:
                permissionField = DART_ACCOUNTS.ACCOUNT_TYPE_PERMISSIONS.WRITE;
                break;
            default:
                LOGGER.warn("Unknown permission type: [{}]", permission);
                return false;

        }
        // @formatter:off
        String permissionRegex = dslContext.select(permissionField).from(DART_ACCOUNTS.ACCOUNT_TYPE_PERMISSIONS)
                .join(DART_ACCOUNTS.ACCOUNT_TYPES)
                    .on(DART_ACCOUNTS.ACCOUNT_TYPES.ID.eq(DART_ACCOUNTS.ACCOUNT_TYPE_PERMISSIONS.TYPE_ID))
                .join(DART_ACCOUNTS.ACCOUNTS)
                    .on(DART_ACCOUNTS.ACCOUNTS.TYPE_ID.eq(DART_ACCOUNTS.ACCOUNT_TYPES.ID))
                .where(DART_ACCOUNTS.ACCOUNTS.NAME.equalIgnoreCase(name))
                    .and(DART_ACCOUNTS.ACCOUNT_TYPE_PERMISSIONS.RESOURCE_TYPE.equalIgnoreCase(resourceType.name()))
                .fetchOne(permissionField);
        // @formatter:on
        final boolean result = permissionRegex != null && Pattern.matches(permissionRegex, resourceName);
        if (result) {
            LOGGER.debug("Account [{}] was granted [{}] access to resource [{}] of type [{}]", name, permission,
                    resourceName, resourceType);
        } else {
            LOGGER.warn("Account [{}] was denied [{}] access to resource [{}] of type [{}]", name, permission,
                    resourceName, resourceType);
        }
        return result;
    }

    @Override
    public boolean checkTopic(@NotNull String name, @NotNull String resourceName, @NotNull ResourceType resourceType,
                              @NotNull ResourcePermission permission, @NotNull String routingKey) {
        // TODO We currently don't support topic authentication.
        return checkResource(name, resourceName, resourceType, permission);
    }

    @Override
    public boolean setPassword(@NotNull String name, @NotNull String password) {
        final int updatedRecords = dslContext.update(DART_ACCOUNTS.ACCOUNTS)
                .set(DART_ACCOUNTS.ACCOUNTS.PASSWORD, PasswordUtil.hashPassword(password))
                .where(DART_ACCOUNTS.ACCOUNTS.NAME.equalIgnoreCase(name)).execute();
        if (updatedRecords == 1) {
            LOGGER.debug("Password of account [{}] was changed successfully", name);
            return true;
        } else {
            LOGGER.warn("Password of account [{}] could not be changed", name);
            return false;
        }
    }

    @Override
    public boolean enableAccount(@NotNull String name) {
        final int updatedRecords = setEnabledFlag(name, true);
        if (updatedRecords == 1) {
            LOGGER.debug("Account [{}] was enabled successfully", name);
            return true;
        } else {
            LOGGER.warn("Account [{}] could not be enabled", name);
            return false;
        }
    }

    @Override
    public boolean disableAccount(@NotNull String name) {
        final int updatedRecords = setEnabledFlag(name, false);
        if (updatedRecords == 1) {
            LOGGER.debug("Account [{}] was disabled successfully", name);
            return true;
        } else {
            LOGGER.warn("Account [{}] could not be disabled", name);
            return false;
        }
    }

    private int setEnabledFlag(String name, boolean flag) {
        return dslContext.update(DART_ACCOUNTS.ACCOUNTS).set(DART_ACCOUNTS.ACCOUNTS.ENABLED, flag)
                .where(DART_ACCOUNTS.ACCOUNTS.NAME.equalIgnoreCase(name)).execute();
    }
}

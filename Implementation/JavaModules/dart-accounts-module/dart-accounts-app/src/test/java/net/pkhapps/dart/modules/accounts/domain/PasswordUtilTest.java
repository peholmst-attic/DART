package net.pkhapps.dart.modules.accounts.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link PasswordUtil}.
 */
public class PasswordUtilTest {

    @Test
    public void testHashAndVerify() {
        final String clearText = "this is my clear text";
        String hash = PasswordUtil.hashPassword(clearText);

        assertThat(PasswordUtil.verifyPassword(hash, clearText)).isTrue();

        assertThat(PasswordUtil.verifyPassword(hash, "this is not my clear text")).isFalse();
    }
}

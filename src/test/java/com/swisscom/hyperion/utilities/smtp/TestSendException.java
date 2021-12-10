package com.swisscom.hyperion.utilities.smtp;

import com.swisscom.hyperion.utilities.smtp.transport.connection.ClosableSmtpConnection;
import com.swisscom.hyperion.utilities.smtp.transport.factory.SmtpConnectionFactoryBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Assert;
import org.junit.Test;
import com.swisscom.hyperion.utilities.smtp.exception.MailSendException;
import com.swisscom.hyperion.utilities.smtp.pool.SmtpConnectionPool;

public class TestSendException extends AbstractTest {
    @Test
    public void testReturnedOnException() throws Exception {
        try (ClosableSmtpConnection connection = smtpConnectionPool.borrowObject()) {
            MimeMessage mimeMessage = createMessage(connection.getSession(),
                    "hyperion@example.com", "hyperion@example.com", "foo", "example");
            // We stop the server before we actually send the message
            stopServer();
            connection.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            Assert.fail("The connection should fail since the server is stopped");
        } catch (MailSendException | MessagingException e) {
            // It should come here, but the connection should not be returned in the pool
        }
        Assert.assertEquals(1, smtpConnectionPool.getBorrowedCount());
        Assert.assertEquals(0, smtpConnectionPool.getDestroyedCount());
        Assert.assertEquals(1, smtpConnectionPool.getReturnedCount());
    }

    @Test
    public void testInvalidateOnException() throws Exception {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(getMaxTotalConnection());
        genericObjectPoolConfig.setTestOnBorrow(true);

        // We need to instantiate a new factory and pool to set the flag on the factory
        transportFactory = SmtpConnectionFactoryBuilder.newSmtpBuilder().port(PORT).invalidateConnectionOnException(true).build();
        smtpConnectionPool = new SmtpConnectionPool(transportFactory, genericObjectPoolConfig);

        try (ClosableSmtpConnection connection = smtpConnectionPool.borrowObject()) {
            MimeMessage mimeMessage = createMessage(connection.getSession(),
                    "hyperion@example.com", "hyperion@example.com", "foo", "example");
            // We stop the server before we actually send the message
            stopServer();        Assert.assertEquals(1, smtpConnectionPool.getBorrowedCount());

            connection.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            Assert.fail("The connection should fail since the server is stopped");
        } catch (MailSendException | MessagingException e) {
            // It should come here, but the connection should not be returned in the pool
        }
        Assert.assertEquals(1, smtpConnectionPool.getDestroyedCount());
        Assert.assertEquals(0, smtpConnectionPool.getReturnedCount());
    }
}

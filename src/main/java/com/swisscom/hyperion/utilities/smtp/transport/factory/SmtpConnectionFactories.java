package com.swisscom.hyperion.utilities.smtp.transport.factory;

import com.swisscom.hyperion.utilities.smtp.transport.strategy.ConnectionStrategyFactory;
import com.swisscom.hyperion.utilities.smtp.transport.strategy.TransportStrategyFactory;
import javax.mail.Session;

import java.util.Properties;

import static com.swisscom.hyperion.utilities.smtp.transport.strategy.ConnectionStrategyFactory.newConnectionStrategy;
import static com.swisscom.hyperion.utilities.smtp.transport.strategy.TransportStrategyFactory.newSessionStrategy;

/**
 * {@link SmtpConnectionFactory} factory
 */
public final class SmtpConnectionFactories {

    private SmtpConnectionFactories() {
    }

    /**
     * Initialize the {@link SmtpConnectionFactory} with a
     * {@link Session} initialized to {@code Session.getInstance(new Properties())},
     * {@link TransportStrategyFactory#newSessionStrategy},
     * {@link ConnectionStrategyFactory#newConnectionStrategy}
     *
     * @return
     */
    public static SmtpConnectionFactory newSmtpFactory() {
        return new SmtpConnectionFactory(Session.getInstance(new Properties()), newSessionStrategy(), newConnectionStrategy(), false);
    }

    /**
     * Initialize the {@link SmtpConnectionFactory} using the provided
     * {@link Session} and
     * {@link TransportStrategyFactory#newSessionStrategy},
     * {@link ConnectionStrategyFactory#newConnectionStrategy}
     *
     * @param session
     * @return
     */
    public static SmtpConnectionFactory newSmtpFactory(Session session) {
        return new SmtpConnectionFactory(session, newSessionStrategy(), newConnectionStrategy(), false);
    }


}

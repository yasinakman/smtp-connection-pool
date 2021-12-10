package com.swisscom.hyperion.utilities.smtp.transport.strategy;

import javax.mail.MessagingException;
import javax.mail.Transport;

/**
 * Connection strategy that abstract {@link Transport#connect}
 * <p>
 * Created by nlabrot on 04/06/15.
 */
public interface ConnectionStrategy {

    void connect(Transport transport) throws MessagingException;

}

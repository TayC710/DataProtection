package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserNotRegisterErr extends Exception {
    final Logger logger = LoggerFactory.getLogger(getClass());

    public UserNotRegisterErr(String username) {
        super(username + " is not registered.");
        logger.warn(username + " is not registered.", this.getClass());
    }
}

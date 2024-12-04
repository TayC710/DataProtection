package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExistUserErr extends Exception {
    final Logger logger = LoggerFactory.getLogger(getClass());
    public ExistUserErr(String message) {
        super(message);
        logger.warn(message,this.getClass());
    }
}

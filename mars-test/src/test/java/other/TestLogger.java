package other;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogger {

    static Logger logger = LoggerFactory.getLogger(TestLogger.class);

    public static void main(String[] args) {
        logger.info("message");
        logger.warn("messagewarning");
    }
}

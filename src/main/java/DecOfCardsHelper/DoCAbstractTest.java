package DecOfCardsHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public abstract class DoCAbstractTest {
    protected static Logger logger;
    protected static Properties properties;
    protected static String baseUrl;

    public DoCAbstractTest() {
        logger  = Logger.getLogger(String.valueOf(this.getClass()));
        properties = new Properties();
        try {
            properties.load(new FileInputStream("gradle.properties"));
        } catch (IOException ex) { ;
            ex.printStackTrace();
        }
        baseUrl = properties.getProperty("deck.of.card.url");
    }

}

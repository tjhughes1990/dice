package dice.service.application;

import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dice.service.roll.DiceRoller;

/**
 * Main web application.
 */
@SpringBootApplication
public class Application {

    /**
     * Main.
     *
     * @param args
     *            unused.
     */
    public static void main(final String[] args) {
        // Add a shutdown hook to clean up the C++ memory.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                DiceRoller.cleanUp();

                LogFactory.getLog(Application.class).info("Clean up complete");
            }
        });

        SpringApplication.run(Application.class, args);
    }
}

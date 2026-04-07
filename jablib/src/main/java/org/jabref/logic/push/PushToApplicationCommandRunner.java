package org.jabref.logic.push;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.jabref.logic.util.HeadlessExecutorService;

import org.slf4j.Logger;

class PushToApplicationCommandRunner {

    private PushToApplicationCommandRunner() {
    }

    static boolean run(String[] command, Logger logger, String applicationName) throws IOException {
        logger.atDebug()
              .setMessage("Executing command {}")
              .addArgument(() -> Arrays.toString(command))
              .log();

        final Process process = Runtime.getRuntime().exec(command);

        boolean[] couldNotPush = new boolean[] {false};
        HeadlessExecutorService.INSTANCE.executeAndWait(() -> {
            try (InputStream out = process.getErrorStream()) {
                int c;
                StringBuilder sb = new StringBuilder();
                try {
                    while ((c = out.read()) != -1) {
                        sb.append((char) c);
                    }
                } catch (IOException e) {
                    logger.warn("Could not read from stderr.", e);
                }
                // Error stream has been closed. See if there were any errors:
                if (!sb.toString().trim().isEmpty()) {
                    logger.warn("Push to {} error: {}", applicationName, sb);
                    couldNotPush[0] = true;
                }
            } catch (IOException e) {
                logger.warn("Error handling std streams", e);
            }
        });

        return couldNotPush[0];
    }
}

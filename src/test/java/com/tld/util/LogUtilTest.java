package com.tld.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest
public class LogUtilTest {
	@Test
    void testLog() throws IOException {
        ByteArrayOutputStream logOutput = new ByteArrayOutputStream();
        
        Handler handler = new StreamHandler(logOutput, new SimpleFormatter());
        
        LogManager.getLogManager().reset();
        
        Logger logger = Logger.getLogger(LogUtil.class.getName());
        logger.addHandler(handler);
        
        LogUtil.log(LogUtil.class, Level.INFO, "Test message");

        handler.flush();

        String logContents = logOutput.toString();
        assertTrue(logContents.contains("Test message"), "Log should contain the expected message.");
        
        logger.removeHandler(handler);
    }
}

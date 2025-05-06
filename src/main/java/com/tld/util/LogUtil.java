package com.tld.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtil {
	public static void log(Class<?> clazz, Level level, String message) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.log(level, message);
    }
}

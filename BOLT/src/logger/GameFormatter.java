package logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * a formatter class for logging
 * 
 * @author marcel
 */
public class GameFormatter extends Formatter {
	
	@Override
	public String format(LogRecord log) {
		String classname = log.getSourceClassName();
		if (classname.contains(".")) {
			String[] split = classname.split("[.]");
			classname = split[split.length - 1];
		}
		String methodname = log.getSourceMethodName();
		String message = log.getMessage();
		
		return String.format("[%tT]%s.%s: %s%n", log.getMillis(), classname, methodname, message);
	}
}

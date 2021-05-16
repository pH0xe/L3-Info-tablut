package global;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
    public synchronized String format(LogRecord record) {

        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        sb.append(dtf.format(now));
        sb.append(" ");

        if (record.getSourceClassName() != null) {
            sb.append(record.getSourceClassName());
        } else {
            sb.append(record.getLoggerName());
        }

        if (record.getSourceMethodName() != null) {
            sb.append(" ");
            sb.append(record.getSourceMethodName());
        }
        String message = formatMessage(record);

        sb.append("\n\t");
        sb.append(record.getLevel().getLocalizedName());
        sb.append(": ");

        int iOffset = (1000 - record.getLevel().intValue()) / 100;
        for( int i = 0; i < iOffset;  i++ ){
            sb.append(" ");
        }


        sb.append(message);
        sb.append("\n");
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ex) {
            }
        }
        return sb.toString();
    }
}

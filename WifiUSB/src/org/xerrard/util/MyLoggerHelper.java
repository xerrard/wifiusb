package org.xerrard.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MyLoggerHelper {

    private static Logger instance = null;

    public static synchronized Logger getLogger() {
        // TODO Auto-generated method stub

        if (instance == null) {
            instance = new ToastLogger();
        }
        return instance;
    }

    private static Context context;
    private static Handler mHandler;

    public static void setConext(Context ctx) {
        context = ctx;
    }

    public static void setHandler(Handler handler) {
        mHandler = handler;
    }

    public static class ToastLogger implements Logger {

        private Logger proxy;

        protected void display(String text, Throwable t) {

            StringWriter sw = null;
            PrintWriter pw = null;
            String errMsg = null;
            try {
                sw = new StringWriter();
                pw = new PrintWriter(sw);
                t.printStackTrace(pw);
                pw.flush();
                sw.flush();

                errMsg = sw.toString();

                pw.close();
                sw.close();
                pw = null;
                sw = null;
            }
            catch (Throwable ioe) {
                ioe.printStackTrace();
            }
            finally {
                if (sw != null) {
                    try {
                        sw.close();
                    }
                    catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (pw != null) {
                    pw.close();
                }
            }

            display(text + "err:" + errMsg);
        }

        protected void display(String text) {
            Message msg = new Message();
            msg.obj = text;
            msg.what = 1;
            //mHandler.sendMessage(msg);

        }

        public ToastLogger() {
            super();
            proxy = LoggerFactory.getLogger("WifiUSBLogger");
        }

        public void debug(Marker marker, String format, Object arg1, Object arg2) {
            proxy.debug(marker, format, arg1, arg2);
        }

        public void debug(Marker marker, String format, Object arg) {
            proxy.debug(marker, format, arg);
        }

        public void debug(Marker marker, String format, Object[] argArray) {
            proxy.debug(marker, format, argArray);
        }

        public void debug(Marker marker, String msg, Throwable t) {
            proxy.debug(marker, msg, t);
        }

        public void debug(Marker marker, String msg) {
            proxy.debug(marker, msg);
        }

        public void debug(String arg0, Object arg1, Object arg2) {
            proxy.debug(arg0, arg1, arg2);
            display(arg0 + arg1 + arg2);
        }

        public void debug(String arg0, Object arg1) {
            proxy.debug(arg0, arg1);
            display(arg0 + arg1);
        }

        public void debug(String arg0, Object[] arg1) {
            proxy.debug(arg0, arg1);
            display(arg0 + arg1);
        }

        public void debug(String msg, Throwable t) {
            proxy.debug(msg, t);
            display(msg, t);
        }

        public void debug(String msg) {
            proxy.debug(msg);
            display(msg);
        }

        public boolean equals(Object o) {
            return proxy.equals(o);
        }

        public void error(Marker marker, String format, Object arg1, Object arg2) {
            proxy.error(marker, format, arg1, arg2);
        }

        public void error(Marker marker, String format, Object arg) {
            proxy.error(marker, format, arg);
        }

        public void error(Marker marker, String format, Object[] argArray) {
            proxy.error(marker, format, argArray);
        }

        public void error(Marker marker, String msg, Throwable t) {
            proxy.error(marker, msg, t);
        }

        public void error(Marker marker, String msg) {
            proxy.error(marker, msg);
        }

        public void error(String arg0, Object arg1, Object arg2) {
            proxy.error(arg0, arg1, arg2);
            display(arg0 + arg1 + arg2);
        }

        public void error(String arg0, Object arg1) {
            proxy.error(arg0, arg1);
            display(arg0 + arg1);
        }

        public void error(String arg0, Object[] arg1) {
            proxy.error(arg0, arg1);
            display(arg0 + arg1);
        }

        public void error(String msg, Throwable t) {
            proxy.error(msg, t);
            display(msg, t);
        }

        public void error(String msg) {
            proxy.error(msg);
            display(msg);

        }

        public String getName() {
            return proxy.getName();
        }

        public int hashCode() {
            return proxy.hashCode();
        }

        public void info(Marker marker, String format, Object arg1, Object arg2) {
            proxy.info(marker, format, arg1, arg2);
        }

        public void info(Marker marker, String format, Object arg) {
            proxy.info(marker, format, arg);
        }

        public void info(Marker marker, String format, Object[] argArray) {
            proxy.info(marker, format, argArray);
        }

        public void info(Marker marker, String msg, Throwable t) {
            proxy.info(marker, msg, t);
        }

        public void info(Marker marker, String msg) {
            proxy.info(marker, msg);
        }

        public void info(String arg0, Object arg1, Object arg2) {
            proxy.info(arg0, arg1, arg2);
            display(arg0 + " " + arg1 + " " + arg2);
        }

        public void info(String arg0, Object arg1) {
            proxy.info(arg0, arg1);
            display(arg0 + " " + arg1);
        }

        public void info(String arg0, Object[] arg1) {
            proxy.info(arg0, arg1);
            display(arg0 + " " + arg1);
        }

        public void info(String msg, Throwable t) {
            proxy.info(msg, t);
            display(msg, t);
        }

        public void info(String msg) {
            proxy.info(msg);
            display(msg);
        }

        public boolean isDebugEnabled() {
            return proxy.isDebugEnabled();
        }

        public boolean isDebugEnabled(Marker marker) {
            return proxy.isDebugEnabled(marker);
        }

        public boolean isErrorEnabled() {
            return proxy.isErrorEnabled();
        }

        public boolean isErrorEnabled(Marker marker) {
            return proxy.isErrorEnabled(marker);
        }

        public boolean isInfoEnabled() {
            return proxy.isInfoEnabled();
        }

        public boolean isInfoEnabled(Marker marker) {
            return proxy.isInfoEnabled(marker);
        }

        public boolean isTraceEnabled() {
            return proxy.isTraceEnabled();
        }

        public boolean isTraceEnabled(Marker marker) {
            return proxy.isTraceEnabled(marker);
        }

        public boolean isWarnEnabled() {
            return proxy.isWarnEnabled();
        }

        public boolean isWarnEnabled(Marker marker) {
            return proxy.isWarnEnabled(marker);
        }

        public String toString() {
            return proxy.toString();
        }

        public void trace(Marker marker, String format, Object arg1, Object arg2) {
            proxy.trace(marker, format, arg1, arg2);
        }

        public void trace(Marker marker, String format, Object arg) {
            proxy.trace(marker, format, arg);
        }

        public void trace(Marker marker, String format, Object[] argArray) {
            proxy.trace(marker, format, argArray);
        }

        public void trace(Marker marker, String msg, Throwable t) {
            proxy.trace(marker, msg, t);
        }

        public void trace(Marker marker, String msg) {
            proxy.trace(marker, msg);
        }

        public void trace(String arg0, Object arg1, Object arg2) {
            proxy.trace(arg0, arg1, arg2);
            display(arg0 + arg1 + arg2);
        }

        public void trace(String arg0, Object arg1) {
            proxy.trace(arg0, arg1);
            display(arg0 + arg1);
        }

        public void trace(String arg0, Object[] arg1) {
            proxy.trace(arg0, arg1);
            display(arg0 + arg1);
        }

        public void trace(String msg, Throwable t) {
            proxy.trace(msg, t);
            display(msg, t);
        }

        public void trace(String msg) {
            proxy.trace(msg);
            display(msg);
        }

        public void warn(Marker marker, String format, Object arg1, Object arg2) {
            proxy.warn(marker, format, arg1, arg2);
        }

        public void warn(Marker marker, String format, Object arg) {
            proxy.warn(marker, format, arg);
        }

        public void warn(Marker marker, String format, Object[] argArray) {
            proxy.warn(marker, format, argArray);
        }

        public void warn(Marker marker, String msg, Throwable t) {
            proxy.warn(marker, msg, t);
        }

        public void warn(Marker marker, String msg) {
            proxy.warn(marker, msg);
        }

        public void warn(String arg0, Object arg1, Object arg2) {
            proxy.warn(arg0, arg1, arg2);
            display(arg0 + arg1 + arg2);
        }

        public void warn(String arg0, Object arg1) {
            proxy.warn(arg0, arg1);
            display(arg0 + arg1);
        }

        public void warn(String arg0, Object[] arg1) {
            proxy.warn(arg0, arg1);
            display(arg0 + arg1);
        }

        public void warn(String msg, Throwable t) {
            proxy.warn(msg, t);
            display(msg, t);
        }

        public void warn(String msg) {
            proxy.warn(msg);
            display(msg);
        }

    }

    public static class FileLogger implements Logger {

        private Logger proxy;
        protected FileWriter fw = null;

        protected void display(String text, Throwable t) {

            StringWriter sw = null;
            PrintWriter pw = null;
            String errMsg = null;
            try {
                sw = new StringWriter();
                pw = new PrintWriter(sw);
                t.printStackTrace(pw);
                pw.flush();
                sw.flush();

                errMsg = sw.toString();

                pw.close();
                sw.close();
                pw = null;
                sw = null;
            }
            catch (Throwable ioe) {
                ioe.printStackTrace();
            }
            finally {
                if (sw != null) {
                    try {
                        sw.close();
                    }
                    catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (pw != null) {
                    pw.close();
                }
            }

            display(text + "err:" + errMsg);
        }

        public FileLogger() {
            super();
            proxy = LoggerFactory.getLogger("WifiUSBLogger");
            try {
                File logFile = new File(String.format("%s/%s", Environment
                        .getExternalStorageDirectory().getAbsolutePath(),
                        "wifiusblog.txt"));

                if (!logFile.exists()) {
                    logFile.createNewFile();
                }

                fw = new FileWriter(logFile, false);
            }
            catch (Throwable e) {
                // TODO Auto-generated catch block
            }

        }

        public void debug(Marker marker, String format, Object arg1, Object arg2) {
            proxy.debug(marker, format, arg1, arg2);
        }

        public void debug(Marker marker, String format, Object arg) {
            proxy.debug(marker, format, arg);
        }

        public void debug(Marker marker, String format, Object[] argArray) {
            proxy.debug(marker, format, argArray);
        }

        public void debug(Marker marker, String msg, Throwable t) {
            proxy.debug(marker, msg, t);
        }

        protected void display(String text) {
           // Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            // System.out.print(text);
            try {
                if (fw != null) {
                    String line = String.format("[%s] %s \r\n",
                            DateUtil.toCompleteFmtString(DateUtil.getNow())
                            // , Thread.currentThread().getName()
                            , text
                    // , t == null? "" : ExceptionUtil.getExcetpionTrace(t)
                            );
                    fw.write(line);
                    fw.flush();
                }
            }
            catch (Throwable e) {
                // TODO Auto-generated catch block

            }
        }

        public void debug(Marker marker, String msg) {
            proxy.debug(marker, msg);
        }

        public void debug(String arg0, Object arg1, Object arg2) {
            proxy.debug(arg0, arg1, arg2);
            display(arg0 + arg1 + arg2);
        }

        public void debug(String arg0, Object arg1) {
            proxy.debug(arg0, arg1);
            display(arg0 + arg1);
        }

        public void debug(String arg0, Object[] arg1) {
            proxy.debug(arg0, arg1);
            display(arg0 + arg1);
        }

        public void debug(String msg, Throwable t) {
            proxy.debug(msg, t);
            display(msg, t);
        }

        public void debug(String msg) {
            proxy.debug(msg);
            display(msg);
        }

        public boolean equals(Object o) {
            return proxy.equals(o);
        }

        public void error(Marker marker, String format, Object arg1, Object arg2) {
            proxy.error(marker, format, arg1, arg2);
        }

        public void error(Marker marker, String format, Object arg) {
            proxy.error(marker, format, arg);
        }

        public void error(Marker marker, String format, Object[] argArray) {
            proxy.error(marker, format, argArray);
        }

        public void error(Marker marker, String msg, Throwable t) {
            proxy.error(marker, msg, t);
        }

        public void error(Marker marker, String msg) {
            proxy.error(marker, msg);
        }

        public void error(String arg0, Object arg1, Object arg2) {
            proxy.error(arg0, arg1, arg2);
            display(arg0 + arg1 + arg2);
        }

        public void error(String arg0, Object arg1) {
            proxy.error(arg0, arg1);
            display(arg0 + arg1);
        }

        public void error(String arg0, Object[] arg1) {
            proxy.error(arg0, arg1);
            display(arg0 + arg1);
        }

        public void error(String msg, Throwable t) {
            proxy.error(msg, t);
            display(msg, t);
        }

        public void error(String msg) {
            proxy.error(msg);
            display(msg);

        }

        public String getName() {
            return proxy.getName();
        }

        public int hashCode() {
            return proxy.hashCode();
        }

        public void info(Marker marker, String format, Object arg1, Object arg2) {
            proxy.info(marker, format, arg1, arg2);
        }

        public void info(Marker marker, String format, Object arg) {
            proxy.info(marker, format, arg);
        }

        public void info(Marker marker, String format, Object[] argArray) {
            proxy.info(marker, format, argArray);
        }

        public void info(Marker marker, String msg, Throwable t) {
            proxy.info(marker, msg, t);
        }

        public void info(Marker marker, String msg) {
            proxy.info(marker, msg);
        }

        public void info(String arg0, Object arg1, Object arg2) {
            proxy.info(arg0, arg1, arg2);
            display(arg0 + " " + arg1 + " " + arg2);
        }

        public void info(String arg0, Object arg1) {
            proxy.info(arg0, arg1);
            display(arg0 + " " + arg1);
        }

        public void info(String arg0, Object[] arg1) {
            proxy.info(arg0, arg1);
            display(arg0 + " " + arg1);
        }

        public void info(String msg, Throwable t) {
            proxy.info(msg, t);
            display(msg, t);
        }

        public void info(String msg) {
            proxy.info(msg);
            display(msg);
        }

        public boolean isDebugEnabled() {
            return proxy.isDebugEnabled();
        }

        public boolean isDebugEnabled(Marker marker) {
            return proxy.isDebugEnabled(marker);
        }

        public boolean isErrorEnabled() {
            return proxy.isErrorEnabled();
        }

        public boolean isErrorEnabled(Marker marker) {
            return proxy.isErrorEnabled(marker);
        }

        public boolean isInfoEnabled() {
            return proxy.isInfoEnabled();
        }

        public boolean isInfoEnabled(Marker marker) {
            return proxy.isInfoEnabled(marker);
        }

        public boolean isTraceEnabled() {
            return proxy.isTraceEnabled();
        }

        public boolean isTraceEnabled(Marker marker) {
            return proxy.isTraceEnabled(marker);
        }

        public boolean isWarnEnabled() {
            return proxy.isWarnEnabled();
        }

        public boolean isWarnEnabled(Marker marker) {
            return proxy.isWarnEnabled(marker);
        }

        public String toString() {
            return proxy.toString();
        }

        public void trace(Marker marker, String format, Object arg1, Object arg2) {
            proxy.trace(marker, format, arg1, arg2);
        }

        public void trace(Marker marker, String format, Object arg) {
            proxy.trace(marker, format, arg);
        }

        public void trace(Marker marker, String format, Object[] argArray) {
            proxy.trace(marker, format, argArray);
        }

        public void trace(Marker marker, String msg, Throwable t) {
            proxy.trace(marker, msg, t);
        }

        public void trace(Marker marker, String msg) {
            proxy.trace(marker, msg);
        }

        public void trace(String arg0, Object arg1, Object arg2) {
            proxy.trace(arg0, arg1, arg2);
            display(arg0 + arg1 + arg2);
        }

        public void trace(String arg0, Object arg1) {
            proxy.trace(arg0, arg1);
            display(arg0 + arg1);
        }

        public void trace(String arg0, Object[] arg1) {
            proxy.trace(arg0, arg1);
            display(arg0 + arg1);
        }

        public void trace(String msg, Throwable t) {
            proxy.trace(msg, t);
            display(msg, t);
        }

        public void trace(String msg) {
            proxy.trace(msg);
            display(msg);
        }

        public void warn(Marker marker, String format, Object arg1, Object arg2) {
            proxy.warn(marker, format, arg1, arg2);
        }

        public void warn(Marker marker, String format, Object arg) {
            proxy.warn(marker, format, arg);
        }

        public void warn(Marker marker, String format, Object[] argArray) {
            proxy.warn(marker, format, argArray);
        }

        public void warn(Marker marker, String msg, Throwable t) {
            proxy.warn(marker, msg, t);
        }

        public void warn(Marker marker, String msg) {
            proxy.warn(marker, msg);
        }

        public void warn(String arg0, Object arg1, Object arg2) {
            proxy.warn(arg0, arg1, arg2);
            display(arg0 + arg1 + arg2);
        }

        public void warn(String arg0, Object arg1) {
            proxy.warn(arg0, arg1);
            display(arg0 + arg1);
        }

        public void warn(String arg0, Object[] arg1) {
            proxy.warn(arg0, arg1);
            display(arg0 + arg1);
        }

        public void warn(String msg, Throwable t) {
            proxy.warn(msg, t);
            display(msg, t);
        }

        public void warn(String msg) {
            proxy.warn(msg);
            display(msg);
        }

    }
}

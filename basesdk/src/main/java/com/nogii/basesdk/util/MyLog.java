package com.nogii.basesdk.util;

import android.util.Log;

import static com.nogii.basesdk.util.MyLog.LOGTYPE.D;
import static com.nogii.basesdk.util.MyLog.LOGTYPE.E;
import static com.nogii.basesdk.util.MyLog.LOGTYPE.I;
import static com.nogii.basesdk.util.MyLog.LOGTYPE.V;
import static com.nogii.basesdk.util.MyLog.LOGTYPE.W;

public class MyLog {
    public static final String LOG_PRE = "";
    public static final String LOG_TAG = "IntelligenceTestingLog";
    public static final String LOG_BOOK_TAG = "IntelligenceTestingLog(bookPosition)";

    private static boolean mLogEnable = false;

    // ========================================================================================
    //  打印一些发布版本也需要记录的打印信息
    // start

    public static void d( String msg ){
        d ( "", msg );
    }

    public static void d( String tag, String msg ){
        d ( true, tag, msg );
    }


    public static void i( String msg ){
        i ( "", msg );
    }

    public static void i(String tag, String msg){
        i ( true, tag, msg );
    }


    public static void v( String msg ){
        v ( "", msg );
    }

    public static void v( String tag, String msg ){
        v ( true, tag, msg );
    }


    public static void e( String msg ){
        e ( "", msg );
    }

    public static void e( String tag, String msg ){
        e ( true, tag, msg );
    }


    public static void w( String msg ){
        w ( "", msg );
    }

    public static void w( String tag, String msg ){
        w ( true, tag, msg );
    }
    // end
    //  打印一些发布版本也需要记录的打印信息
    // ========================================================================================

    // ========================================================================================
    //  测试时打印信息，发布时 将 mLogEnable 改为 false
    // start

    public static void md( String tag, String msg ){
        d (mLogEnable, tag, msg );
    }

    public static void mi( String tag, String msg ){
        i (mLogEnable, tag, msg );
    }

    public static void mv( String tag, String msg ){
        v (mLogEnable, tag, msg );
    }

    public static void me( String tag, String msg ){
        e (mLogEnable, tag, msg );
    }

    public static void mw( String tag, String msg ){
        w (mLogEnable, tag, msg );
    }

    // end
    //  测试时打印信息，发布时 将 mLogEnable 改为 false
    // ========================================================================================


    public static void d( boolean logEnable, String tag, String msg ){
        if( logEnable ){
            log ( D, tag, LOG_PRE + msg );
        }
    }

    public static void i( boolean logEnable, String tag, String msg ){
        if( logEnable ){
            log ( I, tag, LOG_PRE + msg);
        }
    }

    public static void v(boolean logEnable, String tag, String msg){
        if( logEnable ){
            log ( V, tag, LOG_PRE + msg );
        }
    }

    public static void e( boolean logEnable, String tag, String msg ){
        if( logEnable ){
            log ( E, tag, LOG_PRE + msg );
        }
    }

    public static void w(boolean logEnable, String tag, String msg){
        if( logEnable ){
            log( W, tag, LOG_PRE + msg );
        }
    }

    private static void log( LOGTYPE logType, String tag, String msg ){
        if( msg == null ){
            return;
        }
        // 可打印出长的 log
        String tmpMsg = "";
        for ( int i = 0; i < msg.length(); i += 4000 ) {
            if ( i + 4000 < msg.length() ){
                tmpMsg = msg.substring( i, i + 4000 );
            }else{
                tmpMsg = msg.substring( i, msg.length() );
            }

            switch ( logType ){
                case D:
                    Log.d(tag, tmpMsg);
                    break;

                case I:
                    Log.i(tag, tmpMsg);
                    break;

                case V:
                    Log.v(tag, tmpMsg);
                    break;

                case E:
                    Log.e(tag, tmpMsg);
                    break;

                case W:
                    Log.w(tag, tmpMsg);
                    break;
            }
        }
    }

    enum LOGTYPE{
        D, I, V, E, W
    }
}

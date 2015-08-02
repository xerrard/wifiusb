package com.huaqin.wifiusb.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 
 * @ClassName:Util
 * @Description:工具类
 * @author:xerrard
 * @date:2014年7月21日
 */
public class Util {

    /**
     * <p>
     * Description:把InitStr字串写入到DecFilePath对应地址的文件中
     * <p>
     * @date:2014年7月21日
     * @param InitStr
     * @param DecFilePath
     * @throws IOException
     */
    public static void stringToFile(String InitStr,String DecFilePath) throws IOException{ 
        File sourceFile = new File(DecFilePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(sourceFile);
            fos.write(InitStr.getBytes());
            fos.close();
        }
        catch (FileNotFoundException e) {

        }

    }
    
    /**
     * <p>
     * Description:将int型的ip地址转换成ip地址字符串
     * <p>
     * @date:2014年7月21日
     * @param ip
     * @return
     */
    public static String intToIpAddress(int ip) {
        StringBuffer ipBuf = new StringBuffer();
        ipBuf.append(ip & 0xff).append('.').append((ip >>>= 8) & 0xff)
                .append('.').append((ip >>>= 8) & 0xff).append('.')
                .append((ip >>>= 8) & 0xff);

        return ipBuf.toString();
    }

}

package com.hl.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by UTT on 2018/7/1.
 */

public class StreamUtil {

    /**
     * 将流转换为字符串
     * @param is 要转换的流
     * @return
     */
    public static String streamToString(InputStream is){
        //1,在读取的过程中,将读取的内容存储值缓存中,然后一次性的转换成字符串返回
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //2.读流操作,读到没有为止
        byte[] bufffer = new byte[1024];
        //读取内容的临时变量、
        int temp=-1;
        try {
            while ((temp = is.read(bufffer))!=-1){
                bos.write(bufffer,0,temp);
            }
            return bos.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

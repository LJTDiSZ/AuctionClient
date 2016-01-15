package com.jcc.auctionclient.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by juyuan on 1/8/2016.
 */
public class HttpUtil {
    public static final String BASE_URL = "http://10.0.2.2:8008/auction/android/";

    public static void sendHttpRequest(final String address, final String encode, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
//                    connection.connect();
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void submitPostData(final String url, final Map<String, String> params, final String encode, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
                    URL Url = new URL(url);
                    httpURLConnection = (HttpURLConnection)Url.openConnection();
                    httpURLConnection.setConnectTimeout(3000);        //设置连接超时时间
                    httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
                    httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
                    httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
                    httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
                    //设置请求体的类型是文本类型
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //设置请求体的长度
                    httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
                    //获得输出流，向服务器写入数据
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(data);
                    int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
                    if(response == HttpURLConnection.HTTP_OK) {
                        InputStream inptStream = httpURLConnection.getInputStream();
                        if (listener != null)
                            listener.onFinish(dealResponseResult(inptStream, encode));    //处理服务器的响应结果
                    } else{
                        //InputStream inptStream = httpURLConnection.getErrorStream();
                        throw new Exception(httpURLConnection.getResponseMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null){
                        listener.onError(e);
                    }
                } finally {
                    if (httpURLConnection != null)
                    {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    /*
 2      * Function  :   封装请求体信息
 3      * Param     :   params请求体内容，encode编码格式
 4      * Author    :   博客园-依旧淡然
 5      */
     public static StringBuffer getRequestData(Map<String, String> params, String encode) {
         StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
         try {
             for (Map.Entry<String, String> entry : params.entrySet()) {
                 stringBuffer.append(entry.getKey())
                         .append("=")
                         .append(URLEncoder.encode(entry.getValue(), encode))
                         .append("&");
             }
             stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
         } catch (Exception e) {
             e.printStackTrace();
         }
         return stringBuffer;
     }

    /*
 2      * Function  :   处理服务器的响应结果（将输入流转化成字符串）
 3      * Param     :   inputStream服务器的响应输入流
 4      * Author    :   博客园-依旧淡然
 5      */
    public static String dealResponseResult(InputStream inputStream, String encode) throws Exception{
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray(), encode);
        return resultData;
    }
}

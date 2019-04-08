package com.ullas.majorproject;



import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class FCMNotification {


    public final static String AUTH_KEY_FCM = "AIzaSyD-vhyivEJyH5ikX2CBDGAvLR7FMOpesU0";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    public static String sendPushNotification(String deviceToken)
            throws Exception {
        String result = "";
        URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();

        json.put("to", deviceToken.trim());
        JSONObject info = new JSONObject();
        info.put("title", "notification title"); // Notification title
        info.put("body", "message body"); // Notification
        // body
        json.put("notification", info);
        try {
            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            result = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            result = "BAD";
        }
        System.out.println("GCM Notification is sent successfully");

        return result;

    }

    public static void main(String[] args) {
        try {
            FCMNotification.sendPushNotification("d7StVzAtHsw:APA91bEHnux5fiKhdhwSo52SBIV_02z45HT8Qx7DR40LU_ljA7hbWzwmwh5lr7nld74zYyThW_-pt_ifZk9uIvYbARRl3LBDeE-ATu4EglPzPJrM2Mx909EN5cr9LMAsNbtWriyaDnlV");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
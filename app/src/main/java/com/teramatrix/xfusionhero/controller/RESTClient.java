package com.teramatrix.xfusionhero.controller;
import android.content.Context;
import android.util.Log;
import com.teramatrix.xfusionhero.utils.SPUtils;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
public class RESTClient {
    //XFusion Hero API URls
    public static final String LOGIN = "ThirdPartyHeroApplication/oauth/token";
    public static final String DASHBOARD_PERFORMANCE_KPI  = "ThirdPartyHeroApplication/dashboard/get/performance/kpi";
    public static final String DASHBOARD_MAIN_KPI  = "ThirdPartyHeroApplication/hero/dashboard/get/kpi";
    public static final String DASHBOARD_MAIN_KPI_EQUIPMENT  = "ThirdPartyHeroApplication/hero/dashboard/get/kpi/equipments";
    public static final String DASHBOARD_PERFORMANCE_KPI_NOTIFICATION_WEEKLY_COUNTS  = "ThirdPartyHeroApplication/dashboard/get/performance/kpi/alert/notification";
    public static final String DASHBOARD_PERFORMANCE_KPI_ALERTS_WEEKLY_COUNTS  = "ThirdPartyHeroApplication/dashboard/get/performance/kpi/alerts";
    public static final String DASHBOARD_MAIN_RO_LOGS  = "ThirdPartyHeroApplication/hero/dashboard/get/kpi/testing/log/status";
    public static final String ALL_ALERTS  = "ThirdPartyHeroApplication/hero/notification/get/all/alert/phone";
    public static final String ALL_NOTIFICATIONS  = "ThirdPartyHeroApplication/hero/notification/get/all/phone";

    private static OkHttpClient client = new OkHttpClient();
    private static MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");


    //Login
    public static Call Login(Context context, String body, Callback callback) {
        Request request = new Request.Builder().url(new SPUtils(context).getRestEndPoint() + LOGIN)
                .post(RequestBody.create(mediaType, body)).build();

        Call call = client.newCall(request);

        call.enqueue(callback);
        printRequest(request);
        return call;
    }

    //Dashboard Performance KPI Data
    public static Call dashboard_PerformaceData(Context context, String body, Callback callback) {
        Request request = new Request.Builder().url(new SPUtils(context).getRestEndPoint() + DASHBOARD_PERFORMANCE_KPI)
                .post(RequestBody.create(mediaType, body)).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        printRequest(request);
        return call;
    }
    //Dashboard Main KPI Data
    public static Call dashboard_MainData(Context context, String body, Callback callback) {
        Request request = new Request.Builder().url(new SPUtils(context).getRestEndPoint() + DASHBOARD_MAIN_KPI)
                .post(RequestBody.create(mediaType, body)).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        printRequest(request);
        return call;
    }
    //Dashboard Main Data - Equipments
    public static Call dashboard_MainData_Equipments(Context context, String body, Callback callback) {
        Request request = new Request.Builder().url(new SPUtils(context).getRestEndPoint() + DASHBOARD_MAIN_KPI_EQUIPMENT)
                .post(RequestBody.create(mediaType, body)).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        printRequest(request);
        return call;
    }
    //Dashboard Main Data - Alerts Weekly count
    public static Call dashboard_Performance_alerts_weekly_count(Context context, String body, Callback callback) {
        Request request = new Request.Builder().url(new SPUtils(context).getRestEndPoint() + DASHBOARD_PERFORMANCE_KPI_ALERTS_WEEKLY_COUNTS)
                .post(RequestBody.create(mediaType, body)).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        printRequest(request);
        return call;
    }
    //Dashboard Main Data - Notifications Weekly count
    public static Call dashboard_Performance_notification_weekly_count(Context context, String body, Callback callback) {
        Request request = new Request.Builder().url(new SPUtils(context).getRestEndPoint() + DASHBOARD_PERFORMANCE_KPI_NOTIFICATION_WEEKLY_COUNTS)
                .post(RequestBody.create(mediaType, body)).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        printRequest(request);
        return call;
    }
    //ALl Alerts
    public static Call getAllAlerts(Context context, String body, Callback callback) {
        Request request = new Request.Builder().url(new SPUtils(context).getRestEndPoint() + ALL_ALERTS)
                .post(RequestBody.create(mediaType, body)).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        printRequest(request);
        return call;
    }
    //ALl Notifications
    public static Call getAllNotifications(Context context, String body, Callback callback) {
        Request request = new Request.Builder().url(new SPUtils(context).getRestEndPoint() + ALL_NOTIFICATIONS)
                .post(RequestBody.create(mediaType, body)).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        printRequest(request);
        return call;
    }
    //Dashboard main -RO  Logs Data
    public static Call getROLogs(Context context, String body, Callback callback) {
        Request request = new Request.Builder().url(new SPUtils(context).getRestEndPoint() + DASHBOARD_MAIN_RO_LOGS)
                .post(RequestBody.create(mediaType, body)).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        printRequest(request);
        return call;
    }

    /**
     * Method will print Http Request body to the LogCat Error
     * @param request pass the request to print its body
     */
    private static void printRequest(Request request) {
        Log.e("-----------", "-------------------------------------------------------------------------");
        try {
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            Log.e("WEB_SERVICE", "BODY \t-> " + buffer.readUtf8());
            Log.e("WEB_SERVICE", "URL\t-> " + request.url().toString());
            Log.e("-----------", "-------------------------------------------------------------------------");
        } catch (IOException |StringIndexOutOfBoundsException e) {
            Log.e("WEB_SERVICE", e.getMessage());
        }
    }
}

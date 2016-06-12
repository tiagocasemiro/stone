package br.com.stone.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by tiago.casemiro on 21/09/2015.
 */
public class InternetReceive extends BroadcastReceiver {
    private final String INTERNET_FILTER = "android.net.conn.CONNECTIVITY_CHANGE";
    private final String WIFI_FILTER = "android.net.wifi.WIFI_STATE_CHANGED";
    private OnInternetListener listener;
    private static InternetReceive instance;

    private InternetReceive(Context context) {
        context.registerReceiver(this, new IntentFilter(INTERNET_FILTER));//status da internet
        context.registerReceiver(this, new IntentFilter(WIFI_FILTER));//status do wifi
    }

    public static InternetReceive getInstance(Context context){
        if(instance == null)
            instance = new InternetReceive(context);

        return instance;
    }

    public void register(OnInternetListener listener) {
        this.listener = listener;
    }

    public void unregister(){
        this.listener = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(INTERNET_FILTER) || action.equals(WIFI_FILTER)) {
            try{
                if(InternetUtil.isOnline(context)){
                    if(listener != null)
                        listener.onConnect();
                }else{
                    if(listener != null)
                        listener.onDisconnect();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public interface OnInternetListener{
        /**
         * Listerner para nova conexao com a internet
         */
        public void onConnect();

        /**
         * Listener para queda na conexao com a internet
         */
        public void onDisconnect();
    }
}

package br.com.stone.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetUtil {	
	public static boolean isOnline(Context context) { 
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);	 
	    return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
	} 
}

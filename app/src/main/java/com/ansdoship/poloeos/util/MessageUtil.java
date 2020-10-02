package com.ansdoship.poloeos.util;

import android.os.Handler;
import android.os.Message;

public class MessageUtil
{
	public static void sendMessage(Handler handler,int type){
		Message msg = new Message();
		msg.what = type;
		handler.sendMessage(msg);
	}
}

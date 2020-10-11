package com.ansdoship.poloeos.util;

import android.os.Handler;
import android.os.Message;

public class MessageUtils
{
	public class MessageType
	{
		public static final int ACTION_TEXTBOX_SHOW = 0x01;
		public static final int ACTION_ERRORDIALOG_SHOW = 0x02;
		public static final int ACTION_FPS_UPDATE=  0x03;
		public static final int ACTION_LAMP_ON = 0x10;
		public static final int ACTION_LAMP_GONE = 0x11;
	}
	public static void sendMessage(Handler handler,int type){
		Message msg = new Message();
		msg.what = type;
		handler.sendMessage(msg);
	}
}

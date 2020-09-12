package com.ansdoship.poloeos.view;

import android.app.AlertDialog;
import android.content.Context;

public class DialogSet
{
	public static final int MENU_ABOUT = 0xA1;
	private AlertDialog dialog;
	private Context context;
	
	public DialogSet(Context ctx,int type){
		this.context = ctx;
		switch(type){
			case MENU_ABOUT:
				
				break;
		}
	}
	
	public void show(){
		dialog.show();
	}
}

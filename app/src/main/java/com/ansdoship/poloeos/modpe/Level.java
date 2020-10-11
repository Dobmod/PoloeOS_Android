package com.ansdoship.poloeos.modpe;

import org.mozilla.javascript.*;
import org.mozilla.javascript.annotations.*;
import com.ansdoship.poloeos.util.*;

public class Level extends ScriptableObject
{
	
	public static String text = "";
	
	@JSStaticFunction
	public static String getSignText(int x,int y,int z,int row){
		return text;
	}
	
	@JSStaticFunction
	public static void playSound(int x,int y,int z,String name,int volume,double rate){
		
		SoundPoolUtils.getInstance().play(name,0,Float.parseFloat(String.valueOf((Math.pow(2,(rate-13d)/12d)))));
	}
	
	@Override
	public String getClassName()
	{
		return "Level";
	}

}

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
	public static void playSound(int x,int y,int z,String name,int volume,int rate){
		SoundPoolUtil.getInstance().play(name,1,(float)Math.pow(2,(rate-13)/12));
	}
	
	@Override
	public String getClassName()
	{
		return "Level";
	}

}

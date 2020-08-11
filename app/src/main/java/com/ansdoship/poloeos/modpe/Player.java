package com.ansdoship.poloeos.modpe;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

public class Player extends ScriptableObject
{

	@JSStaticFunction
	public static void addItemCreativeInv(int id,int num,int data){
		return;
	}
	
	@Override
	public String getClassName()
	{
		return "Player";
	}
}

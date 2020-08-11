package com.ansdoship.poloeos.modpe;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

public class Block extends ScriptableObject
{
	
	@JSStaticFunction
	public static void setRedstoneConsumer(int id,boolean is,int data){
		return;
	}
	
	@JSStaticFunction
	public static void defineBlock(int id,String name,String texture,int proto,boolean tm,int data){
		return;
	}

	@Override
	public String getClassName()
	{
		return "Block";
	}

}

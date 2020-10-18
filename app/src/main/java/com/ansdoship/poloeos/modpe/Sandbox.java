package com.ansdoship.poloeos.modpe;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

public class Sandbox extends ScriptableObject
{
	@JSStaticFunction
	public static void setTile(int id,int x,int y,int z){
		com.ansdoship.poloeos.box.MyRenderer.sandbox.getBlockWorld().setTile(id,x,y,z);
	}

	@Override
	public String getClassName()
	{
		return "Sandbox";
	}
}

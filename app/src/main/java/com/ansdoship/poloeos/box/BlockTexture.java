package com.ansdoship.poloeos.box;

import com.threed.jpct.TextureManager;
import com.threed.jpct.Texture;

public class BlockTexture
{
	public static int getTextureById(int id,int direction){
		String name = "unknown";
		switch(id){
			case BlockInfo.GRASS:
				if(direction==Block.DIRECTION_UP){
					name = "grass_top";
				}else if(direction==Block.DIRECTION_DOWN){
					name = "dirt";
				}else{
					name = "grass_side";
				}
				break;
			case BlockInfo.LAMP:
				name = "lamp_off";
				break;
			case BlockInfo.STONE:
				name = "stone";
				break;
			case BlockInfo.DIRT:
				name = "dirt";
				break;
		}
		return TextureManager.getInstance().getTextureID(name);
	}
	
	public static float[] getTextcoorsById(int id){
		float[] coors = {
			0,0,1.0f,0,0,1.0f,1.0f,0,1.0f,1.0f,0,1.0f,
			0,0,1.0f,0,0,1.0f,1.0f,0,1.0f,1.0f,0,1.0f,
			0,0,1.0f,0,0,1.0f,1.0f,0,1.0f,1.0f,0,1.0f,
			0,0,1.0f,0,0,1.0f,1.0f,0,1.0f,1.0f,0,1.0f,
			0,0,1.0f,0,0,1.0f,1.0f,0,1.0f,1.0f,0,1.0f,
			0,0,1.0f,0,0,1.0f,1.0f,0,1.0f,1.0f,0,1.0f
		};
		switch(id){
			case BlockInfo.STONE:
				
				break;
		}
		return coors;
	}
	
	private static float[] getTexcoors(int x,int y){
		float posX = x*0.125f,posY = y*0.125f,offsetX = posX+0.125f,offsetY = posY+0.125f;
		float[] coors = new float[72];
		for(int i = 0;i<72;i+=12){
			coors[i] = posX;
			coors[i+1] = posY;
			
			coors[i+2] = offsetX;
			coors[i+3] = posY;
			
			coors[i+4] = posX;
			coors[i+5] = offsetY;
			
			coors[i+6] = offsetX;
			coors[i+7] = posY;
			
			coors[i+8] = offsetX;
			coors[i+9] = offsetY;
			
			coors[i+10] = posX;
			coors[i+11] = offsetY;
		}
		return coors;
	}
	
	private static String getDirection(int direct){
		String result = "";
		switch(direct){
			case 1:
				result =  "front";
				break;
			case 2:
				result = "back";
				break;
			case 3:
				result = "left";
				break;
			case 4:
				result = "right";
				break;
			case 5:
				result = "up";
				break;
			case 6:
				result = "down";
				break;
			case 7:
				result = "surround";
				break;
		}
		return result;
	}
}

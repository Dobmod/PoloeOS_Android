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

package com.ansdoship.poloeos.box;

import com.threed.jpct.World;
import com.threed.jpct.SimpleVector;
import java.util.ArrayList;
import com.threed.jpct.Object3D;


public class BlockWorld
{
	private short sizeX = 8,sizeY = 16,sizeZ = 8;
	private short[][][] map = new short[sizeX][sizeY][sizeZ];
	
	
	public BlockWorld(){
		for(int i = 0;i<getSizeX();i++)
		for(int j = 0;j<getSizeY();j++)
		for(int k = 0;k<getSizeZ();k++){
			setTile(BlockInfo.AIR, i,j,k);
		}
	}

	public short getSizeX()
	{
		return sizeX;
	}

	public short getSizeY()
	{
		return sizeY;
	}

	public short getSizeZ()
	{
		return sizeZ;
	}
	
	public int getTile(int x,int y,int z){
		if(x<0||x>=8||y<0||y>=16||z<0||z>=8){
			return -1;
		}else{
			return map[x][y][z];
		}
	}
	
	public void setTile(int id,int x,int y,int z){
		if(x<0||x>=8||y<0||y>=16||z<0||z>=8){}else{
			map[x][y][z] = (short)id;
		}
	}
	
}

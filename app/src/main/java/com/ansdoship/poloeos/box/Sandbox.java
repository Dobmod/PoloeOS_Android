package com.ansdoship.poloeos.box;

import com.threed.jpct.World;
import com.threed.jpct.SimpleVector;

public class Sandbox
{
	private BlockWorld world;
	
	public Sandbox(World world){
		this.world = new BlockWorld(world);
	}
	
	public void init(){
		for(int i = 0;i<8;i++){
			for(int j= 0;j<8;j++){
				world.setTile(BlockInfo.STONE,i,0,j);
				world.setTile(BlockInfo.GRASS,i,1,j);
			}
		}
		world.setTile(BlockInfo.STONE,2,2,4);
		world.setTile(BlockInfo.STONE,2,3,4);
		world.setTile(BlockInfo.DIRT,2,4,4);
	}
	
	public BlockWorld getBlockWorld(){
		return this.world;
	}
}

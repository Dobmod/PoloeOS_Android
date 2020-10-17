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
		for(int i = 18;i<26;i++){
			for(int j= 0;j<12;j++){
				world.setTile(BlockInfo.STONE,i,0,j);
				world.setTile(BlockInfo.GRASS,i,1,j);
			}
		}
		world.setTile(BlockInfo.STONE,20,2,4);
		world.setTile(BlockInfo.STONE,20,3,4);
		world.setTile(BlockInfo.DIRT,20,4,4);
	}
	
	public SimpleVector getCenter(){
		return new SimpleVector(20*2,0,6*2);
	}
}

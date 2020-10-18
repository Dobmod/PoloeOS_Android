package com.ansdoship.poloeos.box;

import com.threed.jpct.World;
import com.threed.jpct.SimpleVector;
import java.util.ArrayList;
import com.threed.jpct.Object3D;


public class BlockWorld
{
	private World world;
	private int offsetX = 18,offsetY = -2,offsetZ = 0;
	private ArrayList<String> cacheBlock = new ArrayList<>();
	
	
	public BlockWorld(World world){
		this.world = world;
	}
	
	public void setTile(int id,int x,int y,int z){
		String name = id+"#"+x+"#"+y+"#"+z;
		int posX = offsetX+x,posY = offsetY+y,posZ = offsetZ+z;
		Block newBlock = new Block(id);
		SimpleVector sv = new SimpleVector(posX*2,posY*(-2),posZ*2);
		newBlock.setOrigin(sv);
		newBlock.setName(name);
		world.addObject(newBlock);
	}
	
}

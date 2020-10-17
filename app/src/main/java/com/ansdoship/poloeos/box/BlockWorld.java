package com.ansdoship.poloeos.box;

import com.threed.jpct.World;
import com.threed.jpct.SimpleVector;
import java.util.ArrayList;
import com.threed.jpct.Object3D;

public class BlockWorld
{
	private World world;
	private ArrayList<Block> cacheBlock = new ArrayList<>();
	
	public BlockWorld(World world){
		this.world = world;
		Block grassBlock = new Block(BlockInfo.GRASS);
		cacheBlock.add(grassBlock);
	}
	
	public void setTile(int id,int x,int y,int z){
		Block newBlock = new Block(id);
		newBlock.setOrigin(new SimpleVector(x*2,y*(-2),z*2));
		world.addObject(newBlock);
	}
}

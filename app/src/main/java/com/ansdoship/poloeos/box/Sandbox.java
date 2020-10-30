package com.ansdoship.poloeos.box;

import com.threed.jpct.World;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Object3D;
import java.util.ArrayList;
import com.threed.jpct.TextureManager;

public class Sandbox
{
	private BlockWorld world;
	private World renderWorld;
	private Object3D chunk;
	private int offsetX = 18,offsetY = -2,offsetZ = 0;
	private ArrayList<Float> vertexList = new ArrayList<>(),texcoorList = new ArrayList<>();
	
	public Sandbox(World world){
		this.renderWorld = world;
		this.world = new BlockWorld();
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
		update();
	}
	
	private void update(){
		renderWorld.removeAllObjects();
		for(int i = 0;i<world.getSizeX();i++)
		for(int j = 0;j<world.getSizeY();j++)
		for(int k = 0;k<world.getSizeZ();k++){
			int blockId = world.getTile(i,j,k);
			if(blockId==BlockInfo.AIR) continue;
			
			int posX = offsetX+i,posY = offsetY+j,posZ = offsetZ+k;
			SimpleVector sv = new SimpleVector(posX*2,posY*(-2),posZ*2);
			generateBlockVertex(sv);
			addTexcoorList(BlockTexture.getTextcoorsById(blockId));
		}
		float[] vertex = new float[vertexList.size()],texcoor = new float[texcoorList.size()];
		for(int i = 0,size = vertex.length;i<size;i++){
			vertex[i] = vertexList.get(i);
		}
		for(int i =0,size = texcoor.length;i<size;i++){
			texcoor[i] = texcoorList.get(i);
		}
		chunk = new Object3D(vertex,texcoor,null,TextureManager.getInstance().getTextureID("lamp_on"));
		chunk.setCulling(true);
		chunk.strip();
		chunk.build();
		renderWorld.addObject(chunk);
	}
	
	public void generateBlockVertex(SimpleVector sv){
		SimpleVector[] coors = {
			new SimpleVector(1,1,-1),//0
			new SimpleVector(1,1,1),//1
			new SimpleVector(1,-1,-1),//2
			new SimpleVector(1,-1,1),//3
			new SimpleVector(-1,1,-1),//4
			new SimpleVector(-1,1,1),//5
			new SimpleVector(-1,-1,-1),//6
			new SimpleVector(-1,-1,1)//7
		};
		for(int i = 0;i<8;i++){
			coors[i].add(sv);
		}
		int[] indices = {
			1,5,3,5,7,3,//前
			3,7,2,7,6,2,//上
			2,6,0,6,4,0,//后
			0,4,1,4,5,1,//下
			0,1,2,1,3,2,//左
			5,4,7,4,6,7//右
		};
		for(int i = 0,size = indices.length;i<size;i++) addVertexList(coors[indices[i]]);
		
	}
	
	private void addVertexList(SimpleVector sv){
		vertexList.add(sv.x);
		vertexList.add(sv.y);
		vertexList.add(sv.z);
	}
	
	private void addTexcoorList(float[] coors){
		for(int i =0,size = coors.length;i<size;i++){
			texcoorList.add(coors[i]);
		}
	}
	
	public void setTile(int id,int x,int y,int z){
		this.world.setTile(id,x,y,z);
		update();
	}
	
	
	public BlockWorld getBlockWorld(){
		return this.world;
	}
	
	public Object3D getChunk(){
		return this.chunk;
	}
}

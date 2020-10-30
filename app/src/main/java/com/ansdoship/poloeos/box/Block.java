package com.ansdoship.poloeos.box;

import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;

public class Block extends Object3D
{
	public static final int DIRECTION_FRONT = 1;
	public static final int DIRECTION_BACK = 2;
	public static final int DIRECTION_LEFT = 3;
	public static final int DIRECTION_RIGHT = 4;
	public static final int DIRECTION_UP = 5;
	public static final int DIRECTION_DOWN = 6;
	
	
	public Block(int id)
	{
		super(12);
		
		int currentTexture;
		//前
		currentTexture = BlockTexture.getTextureById(id,DIRECTION_FRONT);
		addTriangle(getPoint(-1.0f, -1.0f, 1.0f), 1.0f, 1.0f,getPoint(1.0f, -1.0f, 1.0f), 0.0f, 1.0f, getPoint(-1.0f, 1.0f, 1.0f), 1.0f,0.0f,currentTexture);
		addTriangle(getPoint(1.0f, -1.0f, 1.0f), 0.0f, 1.0f,getPoint(1.0f, 1.0f, 1.0f), 0.0f, 0.0f, getPoint(-1.0f, 1.0f, 1.0f), 1.0f,0.0f,currentTexture);
		// 上
		currentTexture = BlockTexture.getTextureById(id,DIRECTION_UP);
		addTriangle(getPoint(-1.0f, 1.0f, 1.0f), 1.0f, 1.0f,getPoint(1.0f, 1.0f, 1.0f), 0.0f, 1.0f, getPoint(-1.0f, 1.0f, -1.0f), 1.0f,0.0f,currentTexture);
		addTriangle(getPoint(1.0f, 1.0f, 1.0f), 0.0f, 1.0f,getPoint(1.0f, 1.0f, -1.0f), 0.0f, 0.0f, getPoint(-1.0f, 1.0f, -1.0f),1.0f, 0.0f,currentTexture);
		// 后
		currentTexture = BlockTexture.getTextureById(id,DIRECTION_BACK);
		addTriangle(getPoint(-1.0f, 1.0f, -1.0f), 1.0f, 1.0f,getPoint(1.0f, 1.0f, -1.0f), 0.0f, 1.0f, getPoint(-1.0f, -1.0f, -1.0f),1.0f, 0.0f,currentTexture);
		addTriangle(getPoint(1.0f, 1.0f, -1.0f), 0.0f, 1.0f,getPoint(1.0f, -1.0f, -1.0f), 0.0f, 0.0f, getPoint(-1.0f, -1.0f, -1.0f),1.0f, 0.0f,currentTexture);
		// 下
		currentTexture = BlockTexture.getTextureById(id,DIRECTION_DOWN);
		addTriangle(getPoint(-1.0f, -1.0f, -1.0f), 1.0f, 1.0f,getPoint(1.0f, -1.0f, -1.0f), 0.0f, 1.0f, getPoint(-1.0f, -1.0f, 1.0f),1.0f, 0.0f,currentTexture);
		addTriangle(getPoint(1.0f, -1.0f, -1.0f), 0.0f, 1.0f,getPoint(1.0f, -1.0f, 1.0f), 0.0f, 0.0f, getPoint(-1.0f, -1.0f, 1.0f), 1.0f,0.0f,currentTexture);
		// 左
		currentTexture = BlockTexture.getTextureById(id,DIRECTION_LEFT);
		addTriangle(getPoint(-1.0f, -1.0f, -1.0f), 1.0f, 1.0f,getPoint(-1.0f, -1.0f, 1.0f), 0.0f, 1.0f, getPoint(-1.0f, 1.0f, -1.0f),1.0f, 0.0f,currentTexture);
		addTriangle(getPoint(-1.0f, -1.0f, 1.0f), 0.0f, 1.0f,getPoint(-1.0f, 1.0f, 1.0f), 0.0f, 0.0f, getPoint(-1.0f, 1.0f, -1.0f),1.0f, 0.0f,currentTexture);
		// 右
		currentTexture = BlockTexture.getTextureById(id,DIRECTION_RIGHT);
		addTriangle(getPoint(1.0f, -1.0f, 1.0f), 1.0f, 1.0f,getPoint(1.0f, -1.0f, -1.0f), 0.0f, 1.0f, getPoint(1.0f, 1.0f, 1.0f), 1.0f,0.0f,currentTexture);
		addTriangle(getPoint(1.0f, -1.0f, -1.0f), 0.0f, 1.0f,getPoint(1.0f, 1.0f, -1.0f), 0.0f, 0.0f, getPoint(1.0f, 1.0f, 1.0f), 1.0f,0.0f,currentTexture);
		strip();
		build();
		setCulling(true);
		rotateX(3.1415926f);
	}

	private SimpleVector getPoint(float p0, float p1, float p2)
	{
		return new SimpleVector(p0, p1, p2);
	}
}

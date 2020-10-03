package com.ansdoship.poloeos.box;

import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.opengles.*;
import com.ansdoship.poloeos.activity.*;
import com.threed.jpct.*;
import com.threed.jpct.util.*;
import android.os.Handler;
import android.os.Message;
import com.ansdoship.poloeos.R;
import java.io.IOException;
import com.ansdoship.poloeos.util.MessageType;
import java.util.ArrayList;

public class MyRenderer implements GLSurfaceView.Renderer
{

	private MainActivity activity;
	private Handler activityHandler;
	private boolean isOpen;
	private FrameBuffer fb = null;
	private String[] textures = {"lamp_on","lamp_off","firework_pattern"};
	
	private ArrayList<Object3D> fireworkList = new ArrayList<>();
	private ArrayList<Float> kList = new ArrayList<>();
	private World world;
	private Light sun;
	private Object3D cube = null;
	private Ticker ticker = new Ticker(20);
	
	private boolean isScaled = false;
	
	private Handler rendererHandler;


	public MyRenderer(MainActivity act, Handler handler,boolean isOpen)
	{
		this.activity = act;
		this.activityHandler = handler;
		this.isOpen = isOpen;
		rendererHandler = new Handler(){

			@Override
			public void handleMessage(Message msg)
			{
				switch(msg.what){
					case MessageType.ACTION_LAMP_ON:
						if(cube!=null){
							cube.setTexture("lamp_on");
						}
						break;
					case MessageType.ACTION_LAMP_GONE:
						isScaled = true;
						break;
				}
			}
		};
	}

	@Override
	public void onSurfaceCreated(GL10 p1, EGLConfig p2)
	{
		p1.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	}

	@Override
	public void onSurfaceChanged(GL10 p1, int p2, int p3)
	{
		if (fb != null)
		{
			fb.dispose();
		}
		fb = new FrameBuffer(p1, p2, p3);
		if(!isOpen) return;
		if (activity.getMaster() == null)
		{

			world = new World();
			world.setAmbientLight(20, 20, 20);

			sun = new Light(world);
			sun.setIntensity(250, 250, 250);

			// Create a texture out of the icon...:-)
			try
			{
				for(String theTexture :textures){
					Texture texture = new Texture(BitmapHelper.rescale(android.graphics.BitmapFactory.decodeStream(activity.getAssets().open(theTexture+".png")), 16, 16),true);
					texture.setFiltering(false);
					TextureManager.getInstance().addTexture(theTexture, texture);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			cube = createFirework();
			world.addObject(cube);
			
			

			Camera cam = world.getCamera();
			cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
			cam.lookAt(cube.getTransformedCenter());

			SimpleVector sv = new SimpleVector();
			sv.set(cube.getTransformedCenter());
			world.removeObject(cube);
			sv.z -= 100;
			sun.setPosition(sv);
			MemoryHelper.compact();
			
			for(int i = 0;i<0;i++){
				Object3D obj = createFirework();
				int tX = (int)(Math.random()*100)-50;
				kList.add((float)Math.random()*6);
				obj.setOrigin(getPoint(tX,0,0));
				obj.rotateY(3.1415f);
				world.addObject(obj);
				fireworkList.add(obj);
			}

			if (activity.getMaster() == null)
			{
				activity.setMaster();
			}
		}
	}

	@Override
	public void onDrawFrame(GL10 p1)
	{
		fb.clear(new RGBColor(0, 0, 0, 0));
		if(!isOpen) return;
		world.renderScene(fb);
		world.draw(fb);
		fb.display();

		/*
		 if (System.currentTimeMillis() - time >= 1000) {
		 Logger.log(fps + "fps");
		 fps = 0;
		 time = System.currentTimeMillis();
		 }
		 fps++;
		 */
	}


	private void createBox()
	{
		cube = new Object3D(12);
		// 前
		cube.addTriangle(getPoint(-30, -30, 30), 0.0f, 0.0f,
						 getPoint(30, -30, 30), 1.0f, 0.0f, getPoint(-30, 30, 30), 0.0f,
						 1.0f, TextureManager.getInstance().getTextureID("lamp_off"));
		cube.addTriangle(getPoint(30, -30, 30), 1.0f, 0.0f,
						 getPoint(30, 30, 30), 1.0f, 1.0f, getPoint(-30, 30, 30), 0.0f,
						 1.0f, TextureManager.getInstance().getTextureID("lamp_off"));
		// 上
		cube.addTriangle(getPoint(-30, 30, 30), 0.0f, 0.0f,
						 getPoint(30, 30, 30), 1.0f, 0.0f, getPoint(-30, 30, -30), 0.0f,
						 1.0f, TextureManager.getInstance().getTextureID("lamp_off"));
		cube.addTriangle(getPoint(30, 30, 30), 1.0f, 0.0f,
						 getPoint(30, 30, -30), 1.0f, 1.0f, getPoint(-30, 30, -30),
						 0.0f, 1.0f, TextureManager.getInstance().getTextureID("lamp_off"));
		// 后
		cube.addTriangle(getPoint(-30, 30, -30), 0.0f, 0.0f,
						 getPoint(30, 30, -30), 1.0f, 0.0f, getPoint(-30, -30, -30),
						 0.0f, 1.0f, TextureManager.getInstance().getTextureID("lamp_off"));
		cube.addTriangle(getPoint(30, 30, -30), 1.0f, 0.0f,
						 getPoint(30, -30, -30), 1.0f, 1.0f, getPoint(-30, -30, -30),
						 0.0f, 1.0f, TextureManager.getInstance().getTextureID("lamp_off"));
		// 下
		cube.addTriangle(getPoint(-30, -30, -30), 0.0f, 0.0f,
						 getPoint(30, -30, -30), 1.0f, 0.0f, getPoint(-30, -30, 30),
						 0.0f, 1.0f, TextureManager.getInstance().getTextureID("lamp_off"));
		cube.addTriangle(getPoint(30, -30, -30), 1.0f, 0.0f,
						 getPoint(30, -30, 30), 1.0f, 1.0f, getPoint(-30, -30, 30), 0.0f,
						 1.0f, TextureManager.getInstance().getTextureID("lamp_off"));
		// 左
		cube.addTriangle(getPoint(-30, -30, -30), 0.0f, 0.0f,
						 getPoint(-30, -30, 30), 1.0f, 0.0f, getPoint(-30, 30, -30),
						 0.0f, 1.0f, TextureManager.getInstance().getTextureID("lamp_off"));
		cube.addTriangle(getPoint(-30, -30, 30), 1.0f, 0.0f,
						 getPoint(-30, 30, 30), 1.0f, 1.0f, getPoint(-30, 30, -30),
						 0.0f, 1.0f, TextureManager.getInstance().getTextureID("lamp_off"));
		// 右
		cube.addTriangle(getPoint(30, -30, 30), 0.0f, 0.0f,
						 getPoint(30, -30, -30), 1.0f, 0.0f, getPoint(30, 30, 30), 0.0f,
						 1.0f, TextureManager.getInstance().getTextureID("lamp_off"));
		cube.addTriangle(getPoint(30, -30, -30), 1.0f, 0.0f,
						 getPoint(30, 30, -30), 1.0f, 1.0f, getPoint(30, 30, 30), 0.0f,
						 1.0f, TextureManager.getInstance().getTextureID("lamp_off"));
						 
		cube.strip();
		cube.build();
		world.addObject(cube);
		cube.setCulling(false);
		cube.scale(0.1f);
	}
	
	public Object3D createFirework(){
		Object3D obj = new Object3D(2);
		float index = Math.round(Math.random()*6);
		obj.addTriangle(getPoint(-30, -30, 30), 0.125f*(index-1), 1.0f,
						 getPoint(30, -30, 30), 0.125f*index, 1.0f, getPoint(-30, 30, 30), 0.125f*(index-1),
						 0.0f, TextureManager.getInstance().getTextureID("firework_pattern"));
		obj.addTriangle(getPoint(30, -30, 30), 0.125f*index, 1.0f,
						 getPoint(30, 30, 30), 0.125f*index, 0.0f, getPoint(-30, 30, 30), 0.125f*(index-1),
						 0.0f, TextureManager.getInstance().getTextureID("firework_pattern"));
		obj.strip();
		obj.build();
		obj.setCulling(false);
		obj.scale(0.05f);
		return obj;
	}


	public SimpleVector getPoint(int x, int y, int z)
	{
		return new SimpleVector(x, y, z);
	}
	
	public Handler getHandler(){
		return this.rendererHandler;
	}
}

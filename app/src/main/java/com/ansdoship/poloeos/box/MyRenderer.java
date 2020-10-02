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

public class MyRenderer implements GLSurfaceView.Renderer
{

	private MainActivity activity;
	private Handler activityHandler;
	private FrameBuffer fb = null;

	private World world;
	private Light sun;
	private Object3D cube = null;
	
	private boolean isScaled = false;
	
	private Handler rendererHandler = new Handler(){

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


	public MyRenderer(MainActivity act, Handler handler)
	{
		this.activity = act;
		this.activityHandler = handler;
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
		if (activity.getMaster() == null)
		{

			world = new World();
			world.setAmbientLight(20, 20, 20);

			sun = new Light(world);
			sun.setIntensity(250, 250, 250);

			// Create a texture out of the icon...:-)
			try
			{
				Texture texture = new Texture(BitmapHelper.rescale(android.graphics.BitmapFactory.decodeStream(activity.getAssets().open("lamp_on.png")), 16, 16));
				texture.setFiltering(false);
				TextureManager.getInstance().addTexture("lamp_on", texture);
				Texture texture_1 = new Texture(BitmapHelper.rescale(android.graphics.BitmapFactory.decodeStream(activity.getAssets().open("lamp_off.png")), 16, 16));
				texture_1.setFiltering(false);
				TextureManager.getInstance().addTexture("lamp_off", texture_1);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			/*
			 cube = new Object3D(12);
			 cube.calcTextureWrap();
			 cube.setTexture("lamp_off");
			 cube.strip();
			 cube.build();*/
			createBox();
			cube.setTexture("lamp_off");

			Camera cam = world.getCamera();
			cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
			cam.lookAt(cube.getTransformedCenter());

			SimpleVector sv = new SimpleVector();
			sv.set(cube.getTransformedCenter());
			sv.y -= 100;
			sv.z -= 100;
			sun.setPosition(sv);
			MemoryHelper.compact();

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
		world.renderScene(fb);
		world.draw(fb);
		cube.rotateY(0.02f);
		if(isScaled){
			cube.scale(0.99f);
			if(cube.getScale()<=0.02f){
				world.removeAll();
			}
		}
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


	public SimpleVector getPoint(int x, int y, int z)
	{
		return new SimpleVector(x, y, z);
	}
	
	public Handler getHandler(){
		return this.rendererHandler;
	}
}

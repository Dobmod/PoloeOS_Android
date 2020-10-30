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
import java.util.ArrayList;
import com.ansdoship.poloeos.util.MessageUtils;

public class MyRenderer implements GLSurfaceView.Renderer
{

	private MainActivity activity;
	private Handler activityHandler;
	private boolean isOpen;
	private FrameBuffer fb = null;
	private String[] textures = {"lamp_on","lamp_off","firework_pattern","grass_top","grass_side","dirt","stone","blocks"};
	
	private World world;
	private Object3D cube = null;
	public static Sandbox sandbox;
	private Camera cam;
	private Ticker fpsTicker = new Ticker(1000);//1秒一tick
	private short fpsCounter = 0,shareFps = 0;
	
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
					case MessageUtils.MessageType.ACTION_LAMP_ON:
						if(cube!=null){
							cube.setTexture("lamp_on");
						}
						break;
					case MessageUtils.MessageType.ACTION_LAMP_GONE:
						isScaled = true;
						break;
				}
			}
		};
	}

	@Override
	public void onSurfaceCreated(GL10 p1, EGLConfig p2)
	{
	}

	@Override
	public void onSurfaceChanged(GL10 p1, int p2, int p3)
	{
		if (fb != null)
		{
			fb.dispose();
		}
		fb = new FrameBuffer(p2, p3);
		if(!isOpen) return;
		if (activity.getMaster() == null)
		{

			world = new World();
			world.setAmbientLight(170,170,170);
			sandbox = new Sandbox(world);
			
			try
			{
				for(String theTexture :textures){
					Texture texture = new Texture(BitmapHelper.rescale(android.graphics.BitmapFactory.decodeStream(activity.getAssets().open("texture/"+theTexture+".png")), 16, 16),true);
					texture.setFiltering(false);
					TextureManager.getInstance().addTexture(theTexture, texture);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			sandbox.init();
			Object3D chunk = sandbox.getChunk();

			cam = world.getCamera();
			//cam.moveCamera(Camera.CAMERA_MOVEOUT, 86);
			//cam.moveCamera(Camera.CAMERA_MOVEUP,10);
			SimpleVector sv = chunk.getTransformedCenter();
			sv.z -= 20;
			sv.y -= 3;
			cam.setPosition(sv);
			cam.lookAt(chunk.getTransformedCenter());
			
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
		if(!isOpen) return;
		
		world.renderScene(fb);
		world.draw(fb);
		
		fb.display();
		
		fpsCounter++;
		if(fpsTicker.getTicks()==1){
			shareFps = new Short(fpsCounter);
			fpsCounter = 0;
		}
	}

	public short getCurrentFps(){
		return this.shareFps;
	}
	
	public SimpleVector getPoint(int x, int y, int z)
	{
		return new SimpleVector(x, y, z);
	}
	
	public Handler getHandler(){
		return this.rendererHandler;
	}
}

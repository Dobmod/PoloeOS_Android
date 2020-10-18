package com.ansdoship.poloeos.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Handler;
import com.ansdoship.poloeos.util.MessageUtils;
import android.view.WindowManager;

public class ScreenView extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
	private SurfaceHolder mHolder;
	private Canvas mCanvas;
	private boolean mIsRunning;
	private ScreenEntity mScreen;
	private Thread mThread;
	public static Handler mHandler;
	
	private int fps = 0,sharedFps = 0;

	public ScreenView(Context context)
	{
		super(context);
		init();
	}

	public ScreenView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public ScreenView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init()
	{
		mHolder = getHolder();
		mHolder.addCallback(this);
		mScreen = new ScreenEntity();
		mScreen.setUnitSize(calculateProperUnitSize());
		mScreen.loadResource(getContext());
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		mIsRunning = true;
		mThread = new Thread(this);
		mThread.start();
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		mIsRunning = false;
		//mScreen.destroyed();
	}
	
	public void setHandler(Handler h){
		this.mHandler = h;
	}
	
	public int getScreenWidth(){
		return mScreen.getScreenWidth();
	}
	
	public int getScreenHeight(){
		return mScreen.getScreenHeight();
	}
	
	public int getCurrentFps(){
		return this.sharedFps;
	}
	
	private int calculateProperUnitSize()
	{
		WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		return width / 2 / getScreenWidth();
	}

	@Override
	public void run()
	{
		while (mIsRunning)
		{
			draw();
		}
	}

	long time = 0;
	private void draw()
	{
		mCanvas = mHolder.lockCanvas();
		if (mCanvas != null)
		{
			try
			{
				mScreen.draw(mCanvas);
				if(System.currentTimeMillis()-time>=1000){
					sharedFps = fps;
					fps = 0;
					time = System.currentTimeMillis();
					MessageUtils.sendMessage(mHandler,MessageUtils.MessageType.ACTION_FPS_UPDATE);
				}
				fps++;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				mHolder.unlockCanvasAndPost(mCanvas);
			}
		}
	}
}

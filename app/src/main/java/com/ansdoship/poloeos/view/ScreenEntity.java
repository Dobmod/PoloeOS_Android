package com.ansdoship.poloeos.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.content.Context;
import java.io.IOException;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;
import android.widget.TextView;
import android.os.Message;
import com.ansdoship.poloeos.util.MessageUtils;
import android.os.Bundle;

public class ScreenEntity extends ScriptableObject
{

	private Paint mPaint;
	private Bitmap lampOnTexture;
	private Bitmap lampOffTexture;
	private static int[][] bufferArray;
	private int width = 48,height = 27;
	private int mSize;

	public ScreenEntity(){
		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		bufferArray = new int[height][width];
		for(int i = 0;i<height;i++) 
			for(int j = 0;j<width;j++){
				bufferArray[i][j] = 0;
			}
	}

	public void destroyed()
	{
		lampOnTexture = null;
		lampOffTexture = null;
		bufferArray = null;
	}
	
	public int getScreenWidth(){
		return this.width;
	}
	
	public int getScreenHeight(){
		return this.height;
	}

	public void loadResource(Context ctx){
		try{
			lampOnTexture = BitmapFactory.decodeStream(ctx.getAssets().open("texture/lamp_on.png"));
			lampOffTexture = BitmapFactory.decodeStream(ctx.getAssets().open("texture/lamp_off.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void draw(Canvas canvas){
		int width = this.width*mSize,height = this.height*mSize;
		canvas.drawColor(Color.GRAY);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.argb(255,85,87,85));
	
		canvas.drawRect(0,0,width+5,height+5,mPaint);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(0,0,width+5,height+5,mPaint);
		
		
		for(int i = 0;i<this.height;i++){
			for(int j = 0;j<this.width;j++){
				int left = j*mSize+3;
				int top = (this.height-1-i)*mSize+3;
				Rect unitRect = new Rect(0,0,16,16);
				Rect dstRect = new Rect(left,top,left+mSize,top+mSize);
				if(bufferArray[i][j]==241)
					canvas.drawBitmap(lampOnTexture,unitRect,dstRect,mPaint);
				else{
					canvas.drawBitmap(lampOffTexture,unitRect,dstRect,mPaint);
				}
			}
		}
	}
	
	public void setUnitSize(int  size){
		this.mSize = size;
	}
	
	@JSStaticFunction
	public static void clientMessage(String text){
		Message msg = new Message();
		msg.what = MessageUtils.MessageType.ACTION_TEXTBOX_SHOW;
		Bundle data = new Bundle();
		data.putString("text",text);
		msg.setData(data);
		ScreenView.mHandler.sendMessage(msg);
	}

	@JSStaticFunction
	public static void drawPoint(int x,int y,int id){
		bufferArray[y][x] = id;
	}

	@JSStaticFunction
	public static void print(String text){
		Log.i("PRINT",text);
	}

	@Override
	public String getClassName()
	{
		return "Screen";
	}
}

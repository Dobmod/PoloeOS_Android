package com.ansdoship.poloeos.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import java.util.zip.*;
import java.io.*;
import android.util.Log;
import android.net.Uri;
import android.content.Intent;

import com.ansdoship.poloeos.R;
import com.ansdoship.poloeos.engine.JsEngine;
import com.ansdoship.poloeos.modpe.Level;
import com.ansdoship.poloeos.view.ScreenView;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import android.os.CountDownTimer;
import com.ansdoship.poloeos.util.MessageType;
import android.content.pm.PackageManager;
import android.os.Build;
import android.Manifest;
import com.ansdoship.poloeos.util.*;
import android.content.res.AssetFileDescriptor;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import com.ansdoship.poloeos.box.MyRenderer;
import javax.microedition.khronos.egl.*;
import java.lang.reflect.Field;



public class MainActivity extends Activity implements View.OnClickListener
{
	
	private static MainActivity master = null;
	
	public static String dir = "";
	public JsEngine mEngine;
	private TextView textBox;
	private Button sendBt,upBt,leftBt,rightBt,downBt,enterBt,spaceBt,fBt,qBt,backBt,homeBt,bootBt,menuBt,menuTriggerBt;
	private EditText editText;
	private FrameLayout mainLayout;
	private LinearLayout panel,menuPanel;
	private ScreenView screenView;
	private GLSurfaceView animView;
	private MyRenderer renderer;
	private Typeface tf;
	private int unitSize;
	private String[] noteSoundName = {"bdrum","bell","bass","flute","guitar","harp","icechime","pling","snare","xylobone"};
	private String[] osSoundName = {"click","start","error","noise","beep"};
	private Bitmap fireWorkPattern;

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case  MessageType.ACTION_TEXTBOX_SHOW:
					textBox.setText(msg.getData().getString("text"));
					textBox.setVisibility(View.VISIBLE);
					Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.text_box_out);
					textBox.startAnimation(anim);

					CountDownTimer timer = new CountDownTimer(2000, 2000){
						@Override
						public void onTick(long p1)
						{
						}
						
						@Override
						public void onFinish()
						{
							Animation anim_1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.text_box_in);
							textBox.startAnimation(anim_1);
							textBox.setVisibility(View.GONE);

						}
					};
					timer.start();
					break;
				case MessageType.ACTION_ERRORDIALOG_SHOW:
					View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.error_dialog, null);
					TextView dialogTitle = view.findViewById(R.id.error_dialog_title),dialogContent = view.findViewById(R.id.error_dialog_content),dialogIntroduction = view.findViewById(R.id.error_dialog_introduction);
					Button okButton = view.findViewById(R.id.error_dialog_button);
					dialogTitle.setTypeface(tf);
					dialogContent.setTypeface(tf);
					dialogIntroduction.setTypeface(tf);
					okButton.setTypeface(tf);
					dialogContent.setText(msg.getData().getString("error"));
					final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setView(view).setCancelable(false). create();
					final Window window = dialog.getWindow();
					window.setBackgroundDrawable(new ColorDrawable(0));
					okButton.setOnClickListener(new View.OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								dialog.dismiss();
							}
						});
					dialog.show();
					SoundPoolUtil.getInstance(). play("os.error", 1);
					break;
			}
		}

	};


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		if(master!=null){
			copy(master);
		}
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		if (Build.VERSION.SDK_INT >= 29)
		{
			File dirFile = getExternalFilesDir("PoloeOS");
			dir = dirFile.getPath();
			if (!dirFile.exists()) dirFile.mkdir();
		}
		else
		{
			dir = Environment.getExternalStorageDirectory().getPath() + "/PoloeOS";
		}
		mCheckPermision();
		checkUsing();

		tf = Typeface.createFromFile(MainActivity.dir + "/fonts/Mouse.otf");

		mainLayout = findViewById(R.id.layout_main);
		textBox = findViewById(R.id.text_box);

		sendBt = findViewById(R.id.bt_send);
		upBt = findViewById(R.id.bt_up);
		leftBt = findViewById(R.id.bt_left);
		rightBt = findViewById(R.id.bt_right);
		downBt = findViewById(R.id.bt_down);
		enterBt = findViewById(R.id.bt_enter);
		spaceBt = findViewById(R.id.bt_space);
		backBt = findViewById(R.id.bt_back);
		fBt = findViewById(R.id.bt_f);
		qBt = findViewById(R.id.bt_q);
		homeBt = findViewById(R.id.bt_home);
		bootBt = findViewById(R.id.bt_boot);
		menuBt = findViewById(R.id.bt_menu);
		animView = findViewById(R.id.activity_anim_view);

		sendBt.setTypeface(tf);
		upBt.setTypeface(tf);
		leftBt.setTypeface(tf);
		rightBt.setTypeface(tf);
		downBt.setTypeface(tf);
		enterBt.setTypeface(tf);
		spaceBt.setTypeface(tf);
		fBt.setTypeface(tf);
		backBt.setTypeface(tf);
		qBt.setTypeface(tf);
		homeBt.setTypeface(tf);
		bootBt.setTypeface(tf);
		menuBt.setTypeface(tf);

		textBox.setTypeface(tf);

		upBt.setOnClickListener(this);
		leftBt.setOnClickListener(this);
		rightBt.setOnClickListener(this);
		downBt.setOnClickListener(this);
		enterBt.setOnClickListener(this);
		spaceBt.setOnClickListener(this);
		fBt.setOnClickListener(this);
		backBt.setOnClickListener(this);
		qBt.setOnClickListener(this);
		homeBt.setOnClickListener(this);
		bootBt.setOnClickListener(this);
		menuBt.setOnClickListener(this);
		editText = findViewById(R.id.something);
		editText.setTypeface(tf);
		screenView = findViewById(R.id.screen_view);
		panel = findViewById(R.id.button_panel);
		menuPanel = findViewById(R.id.menu_panel);
		menuTriggerBt = findViewById(R.id.menu_trigger);
		menuTriggerBt.setTypeface(tf);
		sendBt.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String text = editText.getEditableText().toString();
					Level.text = text;
					mEngine.callScriptMethod("keyEvent", new Object[]{text});
					SoundPoolUtil.getInstance().play("os.click", 0);
				}

			});
		menuTriggerBt.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					menuPanel.setVisibility(View.VISIBLE);
					Animation anim_1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.menu_panel_out);
					menuPanel.startAnimation(anim_1);
					Animation anim_2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.menu_button_in);
					menuTriggerBt.startAnimation(anim_2);
					menuTriggerBt.setVisibility(View.GONE);
					SoundPoolUtil.getInstance().play("os.click", 0);
				}
			});
		menuBt.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.menu_layout, null);
					Button aboutButton = view.findViewById(R.id.menu_layout_about);
					Button optionButton = view.findViewById(R.id.menu_layout_option);
					Button helpButton = view.findViewById(R.id.menu_layout_help);
					aboutButton.setTypeface(tf);
					optionButton.setTypeface(tf);
					helpButton.setTypeface(tf);
					aboutButton.setOnClickListener(new View.OnClickListener(){

							@Override
							public void onClick(View p1)
							{

							}
						});
					optionButton.setOnClickListener(new View.OnClickListener(){

							@Override
							public void onClick(View p1)
							{

							}
						});
					helpButton.setOnClickListener(new View.OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								Uri uri = Uri.parse("https://www.showdoc.com.cn/poloeos");
								Intent intent = new Intent(Intent.ACTION_VIEW, uri);
								startActivity(intent);
								SoundPoolUtil.getInstance().play("os.click", 0);
							}
						});
					AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
						.setCancelable(true)
						.setView(view)
						.create();
					final Window window = dialog.getWindow();
					window.setBackgroundDrawable(new ColorDrawable(0));
					dialog.show();
					SoundPoolUtil.getInstance().play("os.click", 0);
				}
			});
		unitSize = calculateProperUnitSize();
		screenView.setUnitSize(unitSize);
		screenView.setHandler(mHandler);
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)screenView.getLayoutParams();;
		params.width = screenView.getScreenWidth() * unitSize + 6;
		params.height = screenView.getScreenHeight() * unitSize + 6;
		screenView. setLayoutParams(params);
		panel.setVisibility(View.VISIBLE);
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_panel_out);
		panel.startAnimation(anim);
		Animation anim_1 = AnimationUtils.loadAnimation(this, R.anim.screen_out);
		screenView.startAnimation(anim_1);
		
		//Anim View
		/*
		animView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
				public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
					// Ensure that we get a 16bit framebuffer. Otherwise, we'll fall
					// back to Pixelflinger on some device (read: Samsung I7500)
					int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };
					EGLConfig[] configs = new EGLConfig[1];
					int[] result = new int[1];
					egl.eglChooseConfig(display, attributes, configs, 1, result);
					return configs[0];
				}
			});
		*/
		animView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		animView.setZOrderOnTop(true);  
		animView.getHolder().setFormat(android.graphics.PixelFormat.TRANSLUCENT); 
		renderer = new MyRenderer(this,mHandler);
		animView.setRenderer(renderer);
		
		loadResources();

		initializeEngine();
    }

	private void loadResources()
	{
		new Thread(new Runnable(){

				@Override
				public void run()
				{
					try
					{
						//音效加载
						for (int i = 0;i < noteSoundName.length;i++)
						{
							AssetFileDescriptor afd = getAssets().openFd("sounds/note/" + noteSoundName[i] + ".ogg");
							SoundPoolUtil.getInstance().loadRF("note." + noteSoundName[i], afd);
						}
						for (int i = 0;i < osSoundName.length;i++)
						{
							AssetFileDescriptor afd = getAssets().openFd("sounds/os/" + osSoundName[i] + ".ogg");
							SoundPoolUtil.getInstance().loadRF("os." + osSoundName[i], afd);
						}
						//贴图
						fireWorkPattern = BitmapFactory.decodeStream(getAssets().open("firework_pattern.png"));
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}).start();
	}

	private void mCheckPermision()
	{
		/**
		 * 请求授权
		 */
		if (Build.VERSION.SDK_INT <= 21) return;
		if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
		{ //表示未授权时
            //进行授权
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
	}

	/**
	 * 权限申请返回结果
	 * @param requestCode 请求码
	 * @param permissions 权限数组
	 * @param grantResults  申请结果数组，里面都是int类型的数
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode)
		{
			case 1:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
				{ //同意权限申请
				}
				else
				{ //拒绝权限申请
					finish();
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onClick(View p1)
	{
		String words ="";
		switch (p1.getId())
		{

			case R.id.bt_up:
				words = "↑";
				break;
			case R.id.bt_left:
				words = "←";
				break;
			case R.id.bt_right:
				words = "→";
				break;
			case R.id.bt_down:
				words = "↓";
				break;
			case R.id.bt_enter:
			case R.id.bt_space:
			case R.id.bt_back:
			case R.id.bt_f:
			case R.id.bt_q:
				Button button = (Button)p1;
				words = button.getText().toString();
				break;
			case R.id.bt_boot:
				words = "Boot";
				SoundPoolUtil.getInstance().play("os.beep", 0);
				break;
			case R.id.bt_menu:
				break;
			default:
		}
		mEngine.callScriptMethod("keyEvent", new Object[]{words});
		SoundPoolUtil.getInstance().play("os.click", 0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (menuPanel.getVisibility() == View.VISIBLE)
		{
			Animation anim_1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.menu_panel_in);
			menuPanel.startAnimation(anim_1);
			menuPanel.setVisibility(View.GONE);
			menuTriggerBt.setVisibility(View.VISIBLE);
		}
		return super.onTouchEvent(event);
	}

	private void checkUsing()
	{
		SharedPreferences.Editor editor = getSharedPreferences("using", MODE_PRIVATE).edit();
		SharedPreferences pref = getSharedPreferences("using", MODE_PRIVATE);
		int version = 0;
		try
		{
			version = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			Log.i("MainActivity", "versionCode:" + version);
		}
		catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
		}
		File barnFile = new File(dir);
		if (pref.getBoolean("first", true))
		{
			//第一次使用

			if (barnFile.exists()) FileIO.delAllFile(dir);
			releaseConfFile();
		}
		else
		{

			if (!barnFile.exists())
			{
				releaseConfFile();
			}
			else
			{
				if (version > pref.getInt("version", 0))
				{
					FileIO.delAllFile(dir);
					releaseConfFile();
				}
			}
		}
		editor.putBoolean("first", false);
		editor.putInt("version", version);
		editor.apply();
	}

	public void releaseConfFile()
	{
		unzipFileFromAssets("core.zip", dir);
		Message msg = new Message();
		msg.what = MessageType.ACTION_TEXTBOX_SHOW;
		Bundle data = new Bundle();
		data.putString("text", "配置文件已释放");
		msg.setData(data);
	}

	public void loadFireworks()
	{
	}

	/**
	 * 从assets中解压zip文件的方法
	 *
	 * @param zipName    压缩文件的名称
	 * @param targetPath 解压的路径
	 */
	public void unzipFileFromAssets(String zipName, String targetPath)
	{
		try
		{
			int bufferSize = 1024 * 10;
			// 建立zip输入流
			ZipInputStream zipInputStream = new ZipInputStream(getAssets().open(zipName));
			// 每一个zip文件的实例对象
			ZipEntry zipEntry;
			// 文件路径名称
			String fileName = "";
			// 从输入流中得到实例
			while ((zipEntry = zipInputStream.getNextEntry()) != null)
			{
				fileName = zipEntry.getName();
				//如果为文件夹,则在目标目录创建文件夹
				if (zipEntry.isDirectory())
				{
					// 截取字符串,去掉末尾的分隔符
					fileName = fileName.substring(0, fileName.length() - 1);
					// 创建文件夹
					File folder = new File(targetPath + File.separator + fileName);
					if (!folder.exists())
					{
						folder.mkdirs();
						Log.i("ZipUtils", "unzip mkdirs : " + folder.getAbsolutePath());
					}
				}
				else
				{
					File file = new File(targetPath + File.separator + fileName);
					if (!file.exists())
					{
						file.createNewFile();
						// 得到目标路径的输出流
						FileOutputStream fos = new FileOutputStream(file);
						// 开始读写数据
						int length = 0;
						byte[] buffer = new byte[bufferSize];
						while ((length = zipInputStream.read(buffer)) != -1)
						{
							fos.write(buffer, 0, length);
						}
						fos.close();
						Log.i("ZipUtils", "unzip createNewFile : " + file.getAbsolutePath());
					}
				}
			}
			zipInputStream.close();
			if (new File(zipName).delete()) Log.i("ZipUtils", "zipFile has been deleted");

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		animView.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		animView.onPause();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mEngine.shutdown();
	}


	private int calculateProperUnitSize()
	{
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		return width / 2 / screenView.getScreenWidth();
	}

	public void initializeEngine()
	{
		mEngine = new JsEngine(this);
		mEngine.setHandler(mHandler);
		mEngine.request();

	}
	
	private void copy(Object src) {
		try {
			Field[] fs = src.getClass().getDeclaredFields();
			for (Field f : fs) {
				f.setAccessible(true);
				f.set(this, f.get(src));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public MainActivity getMaster(){
		return master;
	}
	
	public void setMaster(){
		master = MainActivity.this;
	}

	public static String getBarnDir()
	{
		return dir;
	}
}

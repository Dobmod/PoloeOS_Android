package com.ansdoship.poloeos.engine;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import android.app.Activity;
import android.widget.EditText;

import com.ansdoship.poloeos.modpe.*;
import com.ansdoship.poloeos.util.FileIO;
import com.ansdoship.poloeos.view.ScreenEntity;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import com.ansdoship.poloeos.activity.MainActivity;
import android.widget.Toast;

public class JsEngine
{
    private Class clazz;
    private org.mozilla.javascript.Context rhino;
    private Scriptable scope;
	private Activity context;
	private MyThread modTickThread;

    private String jsCode = "";
    private String testCode;
	private String code = "";
    public JsEngine(Activity ctx)
	{
		this.context = ctx;
        this.clazz = JsEngine.class;
		this.testCode = FileIO.getFromAssets(ctx.getResources(),"conf.js");
		this.code = FileIO.readLocalFile(MainActivity.dir+"/poloeos.js");
		this.modTickThread = new MyThread();
        initJsEngine();
    }

	public void shutdown()
	{
		callScriptMethod("leaveGame",new Object[]{});
		System.exit(0);
	}

    private void initJsEngine()
	{
        jsCode = "var ScriptAPI = java.lang.Class.forName(\"" + JsEngine.class.getName() + "\", true, javaLoader);\n"
			+ testCode + code + "";
		jsCode = "var barnDir = \""+MainActivity.dir+"\";\n"+testCode+"\n"+code +"\nfunction keyEvent(word){OS.keyEvent(word);}";
    }

    public void request()
	{
        rhino = org.mozilla.javascript.Context.enter();
        rhino.setOptimizationLevel(-1);
        try
		{
			Reader reader = new FileReader(new File(MainActivity.dir+"/poloeos.js"));
			Script script = rhino.compileReader(reader,"poloeos.js",0,null);
            scope = rhino.initStandardObjects();
            // 这两句是设置当前的类做为上下文以及获取当前的类加载器，以便于 rhino 通过反射获取档期类
            ScriptableObject.putProperty(scope, "javaContext", org.mozilla.javascript.Context.javaToJS(this, scope));
            ScriptableObject.putProperty(scope, "javaLoader", org.mozilla.javascript.Context.javaToJS(clazz.getClassLoader(), scope));
			ScriptableObject.defineClass(scope, ScreenEntity.class);
			ScriptableObject.defineClass(scope, Level.class);
			ScriptableObject.defineClass(scope, Player.class);
			ScriptableObject.defineClass(scope, Block.class);
            //执行 js 代码
           Object x = rhino.evaluateString(scope, jsCode, clazz.getSimpleName(), 1, null);
			//script.exec(rhino,scope);
			modTickThread.start();
			callScriptMethod("useItem",new Object[]{0,-1,0,280,1});
			callScriptMethod("keyEvent",new Object[]{"Boot"});
			org.mozilla.javascript.Context.exit();
        }
		catch(Exception e){
			e.printStackTrace();
			Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
		}
		finally
		{
            //退出
            //org.mozilla.javascript.Context.exit();
        }
    }
	
	public void typeSomething(String text){
		callScriptMethod("",new Object[]{text});
	}
	
	public void callScriptMethod(String name,Object[] params){
		Context ctx = Context.enter();
		ctx.setOptimizationLevel(-1);
		Object obj = scope.get(name, scope);
		if(obj != null && obj instanceof Function) {
			Function func = (Function) obj;
			func.call(ctx, scope, scope, params);
		}
	}
	
	public int getScreenWidth(){
		Context ctx = Context.enter();
		ctx.setOptimizationLevel(-1);
		Object o = ScriptableObject.getProperty(scope,"OS.SCREEN_WIDTH");
		return (int)o;
	}
	
	public int getScreenHeight(){
		Context ctx = Context.enter();
		ctx.setOptimizationLevel(-1);
		Object o = ScriptableObject.getProperty(scope,"OS.SCREEN_HEIGHT");
		return (int)o;
	}
	
	
    // 对应类中需要需要被调用的方法，可以做为 JS 代码执行时的回调
	
	private class MyThread extends Thread implements Runnable
	{

		@Override
		public void run() 
		{
			super.run();
			while(true){
				try
				{
					callScriptMethod("modTick",new Object[]{});
					this.sleep(50);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				catch(IllegalStateException e){
					e.printStackTrace();
				}
			}
		}
	}
	}

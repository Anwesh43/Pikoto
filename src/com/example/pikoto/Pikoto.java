package com.example.pikoto;

import android.os.Bundle;
import android.content.*;
import android.view.*;
import android.content.pm.*;
import android.graphics.*;

import android.app.Activity;
import android.view.Menu;

public class Pikoto extends Activity {
MyView m;
Thread t1;
Bitmap bs[]=new Bitmap[1000];
boolean isr=true;
float x[]=new float[1000],y[]=new float[1000],k[]=new float[1000],t[]=new float[1000];
int j=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m=new MyView(this);
		setContentView(m);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	public void onPause()
	{
		super.onPause();
		isr=false;
		while(true)
		{
			try
			{
				t1.join();
				break;
			}
			catch(Exception ex)
			{
				
			}
		}
	}
	public void onResume()
	{
		super.onResume();
		isr=true;
		t1=new Thread(m);
		t1.start();
	}
	class MyView extends SurfaceView implements Runnable
	{
		int w,h;
		Canvas canvas;
		Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
		SurfaceHolder sh;
		Bitmap b1;
		int rgb[];
		int r[],g[],b[];
		Bitmap bx[];
		public void run()
		{
			while(isr)
			{
				if(!sh.getSurface().isValid())
					continue;
				canvas=sh.lockCanvas();
				canvas.drawColor(Color.WHITE);
				for(int i=0;i<j;i++)
				{
					canvas.save();
					canvas.translate(x[i],y[i]);
					canvas.rotate(t[i]);
					
					canvas.drawBitmap(bs[i],new Rect(0,0,w,h),new RectF(-100,-100,100,100),p);
					canvas.restore();
					t[i]+=k[i];
					if(t[i]>=360)
						insert(i);
				}
				sh.unlockCanvasAndPost(canvas);
				try
				{
					Thread.sleep(100);
				}
				catch(Exception ex)
				{
					
				}
			}
		}
		public boolean onTouchEvent(MotionEvent event)
		{
			if(event.getAction()==MotionEvent.ACTION_DOWN)
			{
				x[j]=event.getX();
				y[j]=event.getY();
				t[j]=0;
				k[j]=10;
				bs[j]=bx[j%3];
				j++;
			}
			return true;
		}
		public MyView(Context context)
		{
			super(context);
			sh=getHolder();
			b1=BitmapFactory.decodeResource(getResources(), R.drawable.c);
			w=b1.getWidth();
			h=b1.getHeight();
			rgb=new int[w*h];
			r=new int[w*h];
			b=new int[w*h];
			g=new int[w*h];
			bx=new Bitmap[3];
			for(int i=0;i<3;i++)
			bx[i]=BitmapFactory.decodeResource(getResources(),R.drawable.c);
			for(int i=0;i<w*h;i++)
			{
				rgb[i]=b1.getPixel(i%w, i/w);
				r[i]=Color.red(rgb[i]);
				b[i]=Color.blue(rgb[i]);
				g[i]=Color.green(rgb[i]);
				bx[0].setPixel(i%w,i/w,Color.rgb(r[i],0,0));
				bx[1].setPixel(i%w,i/w,Color.rgb(0,g[i],0));
				bx[2].setPixel(i%w,i/w,Color.rgb(0,0,b[i]));
			}
			t1=new Thread(this);
			t1.start();
		}
		public void insert(int index)
		{
			for(int i=index;i<j-1;i++)
			{
				x[i]=x[i+1];
				y[i]=y[i+1];
				t[i]=t[i+1];
				k[i]=k[i+1];
				bs[i]=bs[i+1];
			}
			j--;
		}
	}

}

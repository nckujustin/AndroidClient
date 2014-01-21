package org.wanghai.CameraTest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CameraTest extends Activity {
	SurfaceView sView;
	SurfaceHolder surfaceHolder;
	int screenWidth, screenHeight;
	Camera camera;                    //define the camera of phone
	boolean isPreview = false;        //check if previewing
	private String ipname; 
    public String str_recv;
    public int flag = 0;

	
	// onCreate is use for doing initialization
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set to full screen
     	requestWindowFeature(Window.FEATURE_NO_TITLE);
     	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        
        //got the ip information
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        ipname = data.getString("ipname");
        
        final TextView tv = (TextView)findViewById(R.id.textView1);
        Button bt = (Button) findViewById(R.id.button1);
        Button bt2 = (Button) findViewById(R.id.button1);
        		
		screenWidth = 160;//640
		screenHeight = 120;//480
		sView = (SurfaceView) findViewById(R.id.sView);                  // got the surfaceview
		surfaceHolder = sView.getHolder();                               // got surface holder
	    
		// add a callback
		surfaceHolder.addCallback(new Callback() {
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {				
			}
			@Override
			public void surfaceCreated(SurfaceHolder holder) {							
				initCamera();                                            // initialize the camera
			}
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// if camera != null release the camera
				if (camera != null) {
					if (isPreview)
						camera.stopPreview();
					camera.release();
					camera = null;
				}
			    System.exit(0);
			}
		});
		// set the suface not to maintain buffer    
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		final Thread ChatThread = new Thread(){
			
		    String str1;
		    String str2;
		    //str_recv = "receiving";
		    
		    private String ipname;

		    public void run(){
		    	str_recv = "receiving";
		    	try{
                    doStep1();
                    Thread.sleep(300);
                    Message msg = new Message();
                    msg.what = 123;
                    uiMessageHandler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
		    	
		    	try{
		    		  Intent intent = getIntent();
		              Bundle data = intent.getExtras();
		              ipname = data.getString("ipname");
		    	      //String server=ed.getText().toString();
		    	      int servPort = 6001;
		    	      Socket socket = new Socket(ipname,servPort);
		    	      //InputStream in = socket.getInputStream();
		    	      OutputStream out = socket.getOutputStream();
		    	      str_recv = "connected.....";
		    	      try{
		                    doStep1();
		                    Thread.sleep(300);
		                    Message msg = new Message();
		                    msg.what = 123;
		                    uiMessageHandler.sendMessage(msg);
		                }catch (Exception e){
		                    e.printStackTrace();
		                }
		    	      while(true){
				    	        InputStream in=socket.getInputStream();
				    	        System.out.println("Connected!!");
				    	        byte[] rebyte = new byte[18];
				    	        in.read(rebyte);
				    	        str2 ="(Client Received) : "+ new String(new String(rebyte));
				    	        str_recv = str2;
				    	        try{
				                    doStep1();
				                    Thread.sleep(300);
				                    Message msg = new Message();
				                    msg.what = 123;
				                    uiMessageHandler.sendMessage(msg);
				                }catch (Exception e){
				                    e.printStackTrace();
				                }
				                try {
									doStep1();
									Thread.sleep(300);
				                    Message msg = new Message();
				                    msg.what = 123;
				                    uiMessageHandler.sendMessage(msg);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
		    	      		}
		    			}catch(IOException e){
		    	    	  
		    			}
		    }
		};
		
		ChatThread.start();
		
		bt.setOnClickListener(new Button.OnClickListener(){ 

            @Override

            public void onClick(View v) {
            	str_recv = "clicked";
            	try{
                    doStep1();
                    Thread.sleep(300);
                    Message msg = new Message();
                    msg.what = 123;
                    uiMessageHandler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            	flag = 1;
		    }
  	     

        });
		
    }
	
	private void doStep1() throws InterruptedException{
        Thread.sleep(300);
        Message msg = new Message();
        uiMessageHandler.sendMessage(msg);
    }
	
	Handler uiMessageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
        	TextView tv = (TextView)findViewById(R.id.textView1);
            tv.setText(str_recv);
            super.handleMessage(msg);
        }
    };
	
    private void initCamera() {
    	if (!isPreview) {
			camera = Camera.open();
		}
		if (camera != null && !isPreview) {
			try{
				Camera.Parameters parameters = camera.getParameters();				
				parameters.setPreviewSize(screenWidth, screenHeight);    // set the size of preview image				
				parameters.setPreviewFpsRange(20,30);                    // set the preview fps
				parameters.setPictureFormat(ImageFormat.NV21);           // set image format to yuv
				parameters.setPictureSize(screenWidth, screenHeight);    // set the picture size
				camera.setPreviewDisplay(surfaceHolder);                 // set the surface to display

		        camera.setPreviewCallback(new StreamIt(ipname));         // set the callback function
				camera.startPreview();                                   // start to preview
				camera.autoFocus(null);                                  // set to auto focus
			} catch (Exception e) {
				e.printStackTrace();
			}
			isPreview = true;
		}
    }
    private void initChat() {
    	
    }
    
}

class StreamIt implements Camera.PreviewCallback {
	private String ipname;
	public StreamIt(String ipname){
		this.ipname = ipname;
	}
	
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Size size = camera.getParameters().getPreviewSize();          
        try{
        	//Transfer the image data to JPG
            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);  
            if(image!=null){
            	ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 20, outstream); 
                outstream.flush();

                //use a new thread to send the jpg
                Thread th = new ImageThread(outstream,ipname);
                th.start();               
            }  
        }catch(Exception ex){  
            Log.e("Sys","Error:"+ex.getMessage());  
        }        
    }
}

class ImageThread extends Thread{	
	private byte byteBuffer[] = new byte[1024];
	private OutputStream outsocket;	
	private ByteArrayOutputStream myoutputstream;
	private String ipname;
	
	public ImageThread(ByteArrayOutputStream myoutputstream,String ipname){
		this.myoutputstream = myoutputstream;
		this.ipname = ipname;
        try {
			myoutputstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public void run() {
        try{
        	//send the img through socket
            Socket tempSocket = new Socket(ipname, 6000);
            outsocket = tempSocket.getOutputStream();
            ByteArrayInputStream inputstream = new ByteArrayInputStream(myoutputstream.toByteArray());
            int amount;
            while ((amount = inputstream.read(byteBuffer)) != -1) {
                outsocket.write(byteBuffer, 0, amount);
            }
            myoutputstream.flush();
            myoutputstream.close();
            tempSocket.close();                   
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

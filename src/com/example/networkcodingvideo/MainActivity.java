package com.example.networkcodingvideo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

//import com.example.adhocktest.SenderUDP;

//import com.mainactivity.AdHocEnabler;
//import com.mainactivity.MainActivity;
//import com.mainactivity.R;

import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	  String Stream;
	  private String array_tmp[];
	  String ip;
	  String essid="BENGAL";
	  private int cameraId = 0;
	  static int imageCount = 0;  
	  int imgQuality=100;

	  static boolean CameraOn=false;
	  static boolean start=true;
	  static boolean finish=true;
	  boolean Pause=true;
	  
	  static byte[] jdata;
	  static byte[] DataIn;
	  static byte[] DataInJpeg;

	  Bitmap myBitmap;
	  static ImageView imageView;
	  private SurfaceView surfaceView = null;
	  private SurfaceHolder surfaceHolder = null;
	  Size previewSize;
	  WifiManager wifi;
	  AdHocEnabler AHE;
	  private static Camera camera;
	  private Routing routing;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("GALDEBUG","TEST");
	    //Application orientation
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    //Setup incoming preview picture  
	    imageView=(ImageView)findViewById(R.id.imageView);
	    imageView.setScaleType(ScaleType.FIT_XY);
	    //Setup self preview

	    surfaceView = (SurfaceView) findViewById( R.id.preview );
	    surfaceHolder = surfaceView.getHolder();
	    surfaceHolder.setType( SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS );
	    surfaceHolder.addCallback( surfaceCallback );
	    //Setup IP, wifi and ad-hoc
	    wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	    ip="192.168.2."+Integer.toString(Math.abs(wifi.getConnectionInfo().getMacAddress().hashCode()%255));
	    
	    Log.i("GALDEBUG","my ip :" + ip);
	    routing = new Routing(ip,this); 
	    
		if (wifi.isWifiEnabled() && wifi.getWifiState() != WifiManager.WIFI_STATE_DISABLING)
			wifi.setWifiEnabled(false);

		while (wifi.getWifiState() == WifiManager.WIFI_STATE_DISABLING);
		AHE = new AdHocEnabler("BENGAL", ip);
		AHE.ActivateAdHoc();

		//Spinner setting	
		array_tmp=new String[62];
	    array_tmp[0]="40";
	    array_tmp[1]="41";
	    array_tmp[2]="42";
	    array_tmp[3]="43";
	    array_tmp[4]="44";
	    array_tmp[5]="45";
	    array_tmp[6]="46";
	    array_tmp[7]="47";
	    array_tmp[8]="48";
	    array_tmp[9]="49";
	    array_tmp[10]="50";
	    array_tmp[11]="51";
	    array_tmp[12]="52";
	    array_tmp[13]="52";
	    array_tmp[14]="53";
	    array_tmp[15]="54";
	    array_tmp[16]="55";
	    array_tmp[17]="56";
	    array_tmp[18]="57";
	    array_tmp[19]="58";
	    array_tmp[20]="59";
	    array_tmp[21]="60";
	    array_tmp[22]="61";
	    array_tmp[23]="62";
	    array_tmp[24]="63";
	    array_tmp[25]="64";
	    array_tmp[26]="65";
	    array_tmp[27]="66";
	    array_tmp[28]="67";
	    array_tmp[29]="68";
	    array_tmp[30]="69";
	    array_tmp[31]="70";
	    array_tmp[32]="71";
	    array_tmp[33]="72";
	    array_tmp[34]="73";
	    array_tmp[35]="74";
	    array_tmp[36]="75";
	    array_tmp[37]="76";
	    array_tmp[38]="77";
	    array_tmp[39]="78";
	    array_tmp[40]="79";
	    array_tmp[41]="80";
	    array_tmp[42]="81";
	    array_tmp[43]="82";
	    array_tmp[44]="83";
	    array_tmp[45]="84";
	    array_tmp[46]="85";
	    array_tmp[47]="86";
	    array_tmp[48]="87";
	    array_tmp[49]="88";
	    array_tmp[50]="89";
	    array_tmp[51]="90";
	    array_tmp[52]="91";
	    array_tmp[53]="92";
	    array_tmp[54]="93";
	    array_tmp[55]="94";
	    array_tmp[56]="95";
	    array_tmp[57]="96";
	    array_tmp[58]="97";
	    array_tmp[59]="98";
	    array_tmp[60]="99";
	    array_tmp[61]="100";

	}
	
	
	
	 //Surface setup    
	  SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
	      public void surfaceCreated( SurfaceHolder holder )
	      {
	    	  Pause=false;
	    	  String Model=Build.MODEL;
	    	  if (Model.equals("GT-S5830")) 
	    		  cameraId=0;
	    	  else 
	    		  cameraId = findFrontFacingCamera();
	          camera = Camera.open(cameraId);
	          imageCount = 0;

	          try {
	              camera.setPreviewDisplay( surfaceHolder );
	          } catch ( Throwable t )
	          {
	              Log.e( "surfaceCallback", "Exception in setPreviewDisplay()", t );
	          }

	          Log.e( getLocalClassName(), "END: surfaceCreated" );
	      }
	      //What to do on a preview change
	      public void surfaceChanged( SurfaceHolder holder, int format, int width, int height )
	      {
		          if ( camera != null)
		          {
		              camera.setPreviewCallback( new PreviewCallback() {
		
		                  public void onPreviewFrame( byte[] data, Camera camera ) {
		                	 if (!Pause){
		                
		                  	  
		                  	  if ( camera != null )
		                      {
		                    	  if (DataIn != null){
		                    		//load incoming image
		          				  	Bitmap myBitmap =  BitmapFactory.decodeByteArray(DataIn, 0, DataIn.length);
		          		            	if (myBitmap != null)
		          		            		imageView.setImageBitmap(myBitmap);
		          					}
		                    	  Camera.Parameters parameters = camera.getParameters();
		                          int imageFormat = parameters.getPreviewFormat();
		                          Bitmap bitmap = null;
		                          //Compress to jpeg	
		                          if ( imageFormat == ImageFormat.NV21 )
		                          {
		                              jdata = NV21toJpeg(data);
		                          }
		                          else if ( imageFormat == ImageFormat.JPEG || imageFormat == ImageFormat.RGB_565 )
		                          {
		                        	  jdata=data;
		                          }
		                          //Send the current preview to other user
		                          if ( jdata != null )
		                          {
		                        	  if (start){
	                        			  SenderUDP senderUDP = new SenderUDP("192.168.2.255", jdata);                        		  
		                        		  if (ip.equals("192.168.2.207")){
		                        			  senderUDP.ChangeTargetIp("192.168.2.96");  
		                        		  }
		                        		  if (ip.equals("192.168.2.96")){
		                        			  senderUDP.ChangeTargetIp("192.168.2.207");  
		                        		  }
//			                				try {
//			                					senderUDP.sendMsg();
//			                				} catch (IOException e) {
//			                					e.printStackTrace();
//			                				}
		                        		  
//		                        		  AppService.ImageCntOut++;
		                        	  }
		                          }
		                      }
		                  }
		                }
		              });
		              //Setup camera's parameters
		              Parameters parameters = camera.getParameters();
		              if ( parameters != null )
		              {
		                Camera.Size previewSize=getSmallestPreviewSize(parameters);
		                  parameters.setPreviewFpsRange(16000, 16000); 
		                  parameters.setPreviewSize(previewSize.width,previewSize.height);
		                  camera.setParameters( parameters );
		                  camera.startPreview();
		                  }
		          }
		      }
	      public void surfaceDestroyed(SurfaceHolder holder) {
	          if ( camera != null )
	          {
	              camera.stopPreview();
	              camera.release();
	              camera = null;
	          }
	      }
	  };
	
	
		private int findFrontFacingCamera() {
			int cameraId = -1;
			int numberOfCameras = Camera.getNumberOfCameras();
			for (int i = 0; i < numberOfCameras; i++) {
				CameraInfo info = new CameraInfo();
				Camera.getCameraInfo(i, info);
				if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
					cameraId = i;
					break;
				}
			}
			return cameraId;
		}
	
	  	//Function that find the smallest preview size
		private Camera.Size getSmallestPreviewSize(Camera.Parameters parameters) {
		   Camera.Size result=null;
		    for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
		    	if (result == null) {
		    		result=size;
		    	}
		    	else {
		    		int resultArea=result.width * result.height;
		    		int newArea=size.width * size.height;
		    		if (newArea < resultArea) {
			        	result=size;
			   			}
		    	}
		    }
		    return(result);  
		}
	
		byte[] NV21toJpeg(byte[] data){
			 previewSize = camera.getParameters().getPreviewSize(); 
			 YuvImage yuvimage=new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height, null);
			 ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), imgQuality, baos);
			 return baos.toByteArray();
		}
		
		
	static final int REQUEST_VIDEO_CAPTURE = 1;

	private void dispatchTakeVideoIntent() {
	    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
	  
	    }
	}



}

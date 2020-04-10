package com.example.justloginregistertest;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.widget.TextView;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

public class cameraActivity extends Activity implements SurfaceHolder.Callback{
    Camera mCamera = null;
    SurfaceView surfaceView;
    SurfaceHolder holder;
    ImageView mImageView;
    Button cameraBtn, exitBtn;
    TextView textView;
    int i = 0;
    String filename = "camera.jpg";     //图片文件名
    String path = "";                   //图片保存路径

    /**
     *  Override the onCreate
     *  function：重载构造函数，关联布局文件和控制文件，注册回调监听器
     **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        cameraPermision.verifyStoragePermissions(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //关联ID
        mImageView = (ImageView)findViewById(R.id.imageView1);
        cameraBtn = (Button)findViewById(R.id.btn1);
        exitBtn = (Button)findViewById(R.id.btn2);
        cameraBtn.setOnClickListener(new mClick()); //设置监听事件
        exitBtn.setOnClickListener(new mClick());
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView1);
        textView = (TextView)findViewById(R.id.test);
        //System.out.println("begin to holder...");
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    /**
     *  class：mClick
     *  function：设置按键监听事件，对应拍照和退出按钮
     * */
    class mClick implements OnClickListener{
        @Override
        public void onClick(View v) {
            if (v == cameraBtn){
                mCamera.takePicture(null, null, new jpegCallback());    //拍照
                //surfaceCreated(holder);         //调用构造函数
            }
            else if (v == exitBtn){

            }
        }
    }
    void exit(){
        mCamera.release();
        mCamera = null;
    }

    /**
     * 判断文件夹是否存在，不存在则创建
     *
     * */
    boolean isFolderExists(String strFolder)
    {
        File file = new File(strFolder);
        if (!file.exists())
        {
            if (file.mkdir())
            {
                return true;
            }
            else
                return false;
        }
        return true;
    }


    /**
     * class：jpegCallback
     * function：实现拍照和保存图片功能
     * */
    public class jpegCallback implements PictureCallback{
        @Override
        public void onPictureTaken (byte[] data, Camera camera){
            isFolderExists("/sdcard/test");     //检测文件夹是否存在
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            ArrayList<String> list = getFile("/sdcard/test");
            path = "/sdcard/test/" + checkFileName(list, filename, 0);
            textView.setText("文件保存路径：" + path);
            try{
                BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(path));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
                outStream.flush();
                outStream.close();
                mImageView.setImageBitmap(bitmap);  //在ImageView显示拍照的图片
                mCamera.startPreview();
            }catch (Exception e){
                Log.e("error", e.getMessage());
            }
        }
    }

    private static ArrayList<String> getFile(String path){   //检测路径下所有的文件,并返回文件名列表
        File file = new File(path);  		// 获得指定文件对象
        File[] array = file.listFiles();   	// 获得该文件夹内的所有文件
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<array.length;i++){
            if(array[i].isFile()){			//如果是文件,将文件名保存在列表中
                list.add(array[i].getName());
            }
        }
        return list;	//返回储存文件名的列表
    }
    private static String checkFileName(ArrayList<String> names,String name,int index) {	//检测文件是否存在,并返回合理的文件名
        if(names.contains(name.substring(0,name.indexOf("."))+index+name.substring(name.indexOf("."),name.length()))) {
            //System.out.println(name.substring(0,name.indexOf("."))+index+name.substring(name.indexOf("."),name.length())+ "Hello");
            //name.substring(0,name.indexOf("."))  -->  返回"."之前的字符串
            //文件存在,再次调用checkFileName方法
            name = checkFileName(names,name,index+1);
        } else {
            //文件不存在,返回合理的文件名
            return name.substring(0,name.indexOf("."))+index+name.substring(name.indexOf("."),name.length());
        }
        return name;
    }


    /**
     *  Override the surfaceCreated
     *  function：创建相机时触发，开启相机预览功能
     * */
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        if (mCamera != null){
            ReleaseCamera();    //首先释放相机资源
        }
        mCamera = Camera.open();    //开启摄像头
        //System.out.println("\n\n\nCamera.open() is OK !!!\n\n\n");

        //mCamera = android.hardware.Camera.open();
        //mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        //System.out.println("Camera is OK!");
        try{
            mCamera.setPreviewDisplay(holder);  //设置相机预览
        }catch (IOException e){
            System.out.println("预览错误");
        }
    }
    private void ReleaseCamera()    //重置相机
    {
        if(mCamera != null)
        {
            mCamera.release();
            mCamera = null;
        }
    }


    /**
     *  Override the surfaceChanged
     *  function：当画面发生改变时触发，重置相机参数
     * */
    @Override
    public void surfaceChanged (SurfaceHolder holder, int format, int width, int height){
        //System.out.println("Camera is going to ready...");
        initCamera();   //重置相机参数
    }
    //设置相机参数
    public void initCamera(){
        //System.out.println("here1....");
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);  //照片格式
        //parameters.getSupportedPreviewSizes();
        parameters.setPreviewSize(320, 240);    //预览规格大小
        parameters.setPictureSize(320, 240);    //图片大小
        parameters.setRotation(90);           //设置照片数据旋转90°
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);    //设置浏览画面水平转90°

        mCamera.startPreview(); //开始预览
    }


    /**
     *  Override the surfaceDestroyed
     *  function：关闭相机时触发，空
     * */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){    }    //消灭相机时触发

}

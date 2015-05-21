package com.yliec.frescodemo;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.request.Postprocessor;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    public static final String IMAGE_TYPE = "image/*";
    public static final int REQUEST_CODE_PHOTO = 0;
    public static final String TAG = "MainActivity";

    SimpleDraweeView draweeView;

    private Button btnGetPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        draweeView = (SimpleDraweeView) findViewById(R.id.sdv_img);
        draweeView.setImageURI(Uri.parse("http://lecion.qiniudn.com/qjrs.jpeg"));
        //hierarchy相当于mvc中的model，用于控制显示在view中的drawable对象
        GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
        //通过得到的hierarchy对象进行调整
        //placeholder
        hierarchy.setPlaceholderImage(R.drawable.abc_btn_radio_to_on_mtrl_000);
        //缩放效果
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);



        //控制器
        //在图片下载过程中监听一些事件。如下载失败等
        ControllerListener listener = new BaseControllerListener();

        //后处理器
        Postprocessor postprocessor = new Postprocessor() {
            @Override
            public CloseableReference<Bitmap> process(Bitmap bitmap, PlatformBitmapFactory platformBitmapFactory) {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }
        };


        DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(Uri.parse("http://lecion.qiniudn.com/qjrs.jpeg"))
                .setOldController(draweeView.getController())//设置旧的controller可以节省内存
                .setControllerListener(listener).build();
        draweeView.setController(controller);

        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy2 = builder.setFadeDuration(1000).build();

        btnGetPhoto = $(R.id.btn_get_photo);
        btnGetPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();
            }
        });
    }

    /**
     * 获取系统照片
     */
    private void getPhoto() {
        Intent getPhoto = new Intent(Intent.ACTION_GET_CONTENT);
        getPhoto.setType(IMAGE_TYPE);
        startActivityForResult(getPhoto, REQUEST_CODE_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //系统自定义常量，ok
        if (resultCode != RESULT_OK) {
            Log.d(TAG, "获取图片失败");
            return;
        }

        Bitmap bitmap = null;
        ContentResolver contentResolver = getContentResolver();
        if (requestCode == REQUEST_CODE_PHOTO) {
            //获得图片的uri
            Uri origin = data.getData();
            if (origin != null) {
            }
            Log.d(TAG, "origin uri: " + origin.toString());
            try {
                //获得bitmap
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, origin);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }



    private <T extends View>T $(int id) {
        return (T)findViewById(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

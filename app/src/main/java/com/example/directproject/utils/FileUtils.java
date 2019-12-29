package com.example.directproject.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author YunJin Choi
 * @since 2019-12-27 18:01
 * @description 저장 및 수정 불러오기 관련 File Util Class
 */
public class FileUtils {


    /**
     * @author YunJin Choi
     * @since 2019-12-27 18:06
     * @description Bitmap을 전달받아 해당 fileName으로 저장.
     * @param {Bitmap} bitmap
     * @param {String} fileName
     * @return void
     */
    public void saveBitmapToJpeg(Bitmap bitmap , String fileName) {
        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Direct");
        if (!folder.exists()) {
            folder.mkdir();
        }

        //storage 에 파일 인스턴스를 생성합니다.
        String filePath = Environment.getExternalStorageDirectory().toString() + "/Direct/" + fileName + ".jpeg";
        File tempFile = new File(filePath);

        try {

            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream out = new FileOutputStream(tempFile);

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            // 스트림 사용후 닫아줍니다.
            out.close();

        } catch (FileNotFoundException e) {
            Log.e("MyTag", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag", "IOException : " + e.getMessage());
        }
    }

    /**
     * @author YunJin Choi
     * @since 2019-12-27 18:10
     * @description
     * @param filePath Uri로 변환하고자 하는 파일의 경로
     * @return {Uri}
     */
    public Uri stringPathToUri(String filePath){
        return Uri.fromFile(new File(filePath));
    }


    /**
     * @author YunJin Choi
     * @since 2019-12-27 18:52
     * @description 해당 파일의 절대 경로.
     * @return {String}
     */
    public String getTempFilePath(String targetFileName){
        return Environment.getExternalStorageDirectory().toString() + "/Direct/" + targetFileName + ".jpeg";
    }

    /**
     * 현재 화면을 캡쳐한다.(액티비티 전체를 캡쳐)
     *
     * @param context
     * @param path
     */
    public static void captureActivity(Activity context, String path , String fileName)
    {
        if(context == null) return;
        View root = context.getWindow().getDecorView().getRootView();
        root.setDrawingCacheEnabled(true);
        root.buildDrawingCache(true);
        // 루트뷰의 캐시를 가져옴
        Bitmap screenshot = viewToBitmap(root);

        // get view coordinates
        int[] location = new int[2];
        root.getLocationInWindow(location);

        // 이미지를 자를 수 있으나 전체 화면을 캡쳐 하도록 함
        Bitmap bmp = Bitmap.createBitmap(screenshot, location[0], location[1], root.getWidth(), root.getHeight(), null, false);

        File folder = new File(path);
        if(!folder.exists()) {
            folder.mkdirs();
        }

        String strFilePath = path + "/" + fileName + ".png";
        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;
        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if (view instanceof SurfaceView) {
            SurfaceView surfaceView = (SurfaceView) view;
            surfaceView.setZOrderOnTop(true);
            surfaceView.draw(canvas);
            surfaceView.setZOrderOnTop(false);
            return bitmap;
        } else {
            // For ViewGroup & View
            view.draw(canvas);
            return bitmap;
        }
    }
}

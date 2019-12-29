package com.example.directproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.directproject.R;
import com.example.directproject.contract.StartStudyContract;
import com.example.directproject.presenter.StartStudyPresenter;
import com.example.directproject.utils.FileUtils;
import com.example.directproject.utils.SettingInfo;
import com.theartofdev.edmodo.cropper.CropImage;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 *
 * @author YunJin Choi
 * @since 2019-12-27 17:49
 *
 */
public class StartStudyActivity extends AppCompatActivity  implements StartStudyContract.View, SurfaceHolder.Callback {

    StartStudyContract.Presenter presenter;

    @BindView(R.id.addQuestion)
    Button addQuestion;
    @BindView(R.id.contentImage)
    ImageView contentImage;
    @BindView(R.id.contentCanvas)
    SurfaceView contentCanvas;

    Context mCtx;
    Canvas cacheCanvas;
    Bitmap backBuffer;
    int width, height, clientHeight;
    Paint paint;
    Context context;
    SurfaceHolder mHolder;
    RenderingThread mThread;

    FileUtils fileUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_study);
        ButterKnife.bind(this);
        initComponents();
        initCanvas();
    }

    /**
     * @author YunJin Choi
     * @description 컴포넌트 초기화
     * @since 2019-12-27 17:56
     */
    @Override
    public void initComponents() {
        fileUtils = new FileUtils();
        presenter = new StartStudyPresenter();
        presenter.setView(this);
        mCtx = this;
    }


    /**
     * @author YunJin Choi
     * @description 현재 풀고있는 문제의 ImageView로 부터 bitmap을 가져와 임시 저장 및 Cropping 시작.
     * @since 2019-12-27 17:56
     */
    @OnClick(R.id.addQuestion)
    public void imageCropping() {
        Bitmap captureImageBitmap = ((BitmapDrawable) contentImage.getDrawable()).getBitmap();
        presenter.saveBitmapToJpeg(captureImageBitmap, SettingInfo.CROP_IMAGE_NAME);
        presenter.startCrop(SettingInfo.CROP_IMAGE_NAME);
    }


    /**
     * @author YunJin Choi
     * @description show Toast
     * @since 2019-12-27 17:59
     */
    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    /**
     * @author YunJin Choi
     * @description CropImage Library를 사용하여 현재 풀고있는 문제를 Cropping할수 있도록 실행.
     * @since 2019-12-27 17:59
     */
    @Override
    public void startCrop(Uri targetUri) {
        CropImage.activity(targetUri).start(this);
    }

    /**
     * @author YunJin Choi
     * @description Cropping을 하고 난 이후의 후처리 로직
     * @since 2019-12-27 18:00
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.cropResult(requestCode, resultCode, data);
    }
    ////////////////////

    private void initCanvas() {
        mThread = new RenderingThread();
        mHolder = contentCanvas.getHolder();
        mHolder.addCallback(this);
    }

    int lastX, lastY, currX, currY;
    boolean isDeleting;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDeleting) break;
                currX = (int) event.getX();
                currY = (int) event.getY();
                cacheCanvas.drawLine(lastX, lastY, currX, currY, paint);
                lastX = currX;
                lastY = currY;
                break;
            case MotionEvent.ACTION_UP:
                if (isDeleting) isDeleting = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                cacheCanvas.drawColor(Color.TRANSPARENT);
                isDeleting = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
        //draw(); // SurfaceView에 그리는 기능을 직접 호출해야 한다
        return true;
    }

    protected void draw() {
//        if (clientHeight == 0) {
//            clientHeight = getClientHeight();
//            height = clientHeight;
//            backBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            backBuffer.eraseColor(0);
//            cacheCanvas.drawColor(Color.TRANSPARENT);
//            cacheCanvas.setBitmap(backBuffer);
//        }
//        Canvas canvas = null;
//        try {
//            canvas = mHolder.lockCanvas(null); //back buffer에 그려진 비트맵을 스크린 버퍼에 그린다
//
//            canvas.drawBitmap(backBuffer, 0, 0, paint);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if (mHolder != null) mHolder.unlockCanvasAndPost(canvas);
//        }
    } /* 상태바, 타이틀바를 제외한 클라이언트 영역의 높이를 구한다 */

    private int getClientHeight() {
        Rect rect = new Rect();
        Window window = ((Activity) mCtx).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;
        return ((Activity) mCtx).getWindowManager().getDefaultDisplay().getHeight() - statusBarHeight - titleBarHeight;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        width = contentCanvas.getWidth();
        height = contentCanvas.getHeight();
        cacheCanvas = new Canvas();
        backBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); // back buffer
        backBuffer.eraseColor(0);
        cacheCanvas.drawColor(Color.TRANSPARENT);
        cacheCanvas.setBitmap(backBuffer);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        System.out.println(width);
        System.out.println(height);
        //draw();
        mThread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class RenderingThread extends Thread {
        //Bitmap img_android;
        public RenderingThread() {

        }

        public void run() {
            while (true) {
                System.out.println("kk");
                if (clientHeight == 0) {
                    clientHeight = getClientHeight();
                    height = clientHeight;
                    backBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    backBuffer.eraseColor(Color.TRANSPARENT);
                    cacheCanvas.drawColor(Color.TRANSPARENT);
                    cacheCanvas.setBitmap(backBuffer);
                }
                Canvas canvas = null;
                try {
                    canvas = mHolder.lockCanvas(null); //back buffer에 그려진 비트맵을 스크린 버퍼에 그린다
                    canvas.drawColor(Color.TRANSPARENT);
                    canvas.drawBitmap(backBuffer, 0, 0, paint);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (mHolder != null) mHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    } // RenderingThread

}

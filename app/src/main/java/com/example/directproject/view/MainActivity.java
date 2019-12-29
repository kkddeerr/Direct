package com.example.directproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.directproject.R;
import com.example.directproject.contract.MainContract;
import com.example.directproject.presenter.MainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author YunJin Choi
 *  @since 2019-12-27 17:42
 */
public class MainActivity extends AppCompatActivity implements MainContract.View {


    @BindView(R.id.inputUser)
    TextView userName;

    // 현재 Activity this의미
    Context mCtx;

    // Presenter
    MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ButterKnife 사용 바인딩
        ButterKnife.bind(this);
        initComponents();
    }

    /**
     * @author YunJin Choi
     * @since 2019-12-27 17:44
     * @description 컴포넌트 초기화
     */
    @Override
    public void initComponents(){

        mCtx = this;
        presenter = new MainPresenter();
        presenter.setView(this);
    }



    /**
     * @author YunJin Choi
     * @since 2019-12-27 17:44
     * @description 문제풀이 Activity 로 Intent 이동
     */
    @OnClick(R.id.startStudy)
    public void intentStudy() {
        presenter.moveActivity(StartStudyActivity.class);
    }


    /**
     * @author YunJin Choi
     * @since 2019-12-27 17:45
     * @description Q&A Activity 로 Intent 이동
     */
    @OnClick(R.id.questionAndAnswer)
    public void intentQuestionAndAnswer(){
        presenter.moveActivity(QuestionAndAnswerActivity.class);
    }


    /**
     * @author YunJin Choi
     * @since 2019-12-27 17:48
     * @description show toast
     */
    @Override
    public void showToast(String text) {
        Toast.makeText(this,text , Toast.LENGTH_SHORT).show();
    }


    /**
     * @author YunJin Choi
     * @since 2019-12-27 17:48
     * @description Intent 이동
     */
    @Override
    public void moveActivity(Class targetClass) {
        Intent intent = new Intent(this , targetClass);
        startActivity(intent);
    }
}



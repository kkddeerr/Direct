package com.example.directproject.presenter;

import com.example.directproject.contract.MainContract;

import butterknife.BindView;

public class MainPresenter implements MainContract.Presenter {

    MainContract.View view;

    @Override
    public void setView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }

    @Override
    public void moveActivity(Class targetClass) {
        view.moveActivity(targetClass);
    }
}

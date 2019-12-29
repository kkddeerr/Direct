package com.example.directproject.contract;

public interface MainContract {

    interface View extends BaseContract.View{
        void showToast(String text);
        void moveActivity(Class targetClass);
    }

    interface Presenter extends  BaseContract.Presenter <View>{

        @Override
        void setView(View view);

        @Override
        void releaseView();

        void moveActivity(Class targetClass);
    }
}

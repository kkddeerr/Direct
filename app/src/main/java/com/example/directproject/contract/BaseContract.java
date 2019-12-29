package com.example.directproject.contract;

public class BaseContract {

    public interface View {
        void initComponents();

    }

    public interface Presenter<T> {
        void setView(T view);

        void releaseView();
    }
}

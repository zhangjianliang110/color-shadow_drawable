package com.test.testapp;

import com.test.testapp.logger.LoggerUtils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description:
 * Created by zhangjianliang on 2019/12/17
 */
public class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LoggerUtils.d("------------------onAttach:" + getClass().getSimpleName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoggerUtils.d("------------------onCreate:" + getClass().getSimpleName());
    }

    @Override
    public void onStart() {
        super.onStart();
        LoggerUtils.d("------------------onStart:" + getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        LoggerUtils.d("------------------onResume:" + getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LoggerUtils.d("------------------onCreateView:" + getClass().getSimpleName());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoggerUtils.d("------------------onViewCreated:" + getClass().getSimpleName());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoggerUtils.d("------------------onActivityCreated:" + getClass().getSimpleName());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LoggerUtils.d("------------------setUserVisibleHint:" + getClass().getSimpleName());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LoggerUtils.d("------------------onHiddenChanged:" + getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
        LoggerUtils.d("------------------onStop:" + getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LoggerUtils.d("------------------onDestroyView:" + getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoggerUtils.d("------------------onDestroy:" + getClass().getSimpleName());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LoggerUtils.d("------------------onDetach:" + getClass().getSimpleName());
    }
}

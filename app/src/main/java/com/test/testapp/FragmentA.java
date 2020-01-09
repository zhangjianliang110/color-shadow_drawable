package com.test.testapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description:
 * Created by zhangjianliang on 2019/12/17
 */
public class FragmentA extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_testa, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.tvFmtA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentB fmtB = new FragmentB();
                FragmentUtils.showFragmentWithOutAnimation(getActivity().getSupportFragmentManager(), fmtB, false);
            }
        });
    }
}

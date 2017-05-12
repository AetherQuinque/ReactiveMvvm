package com.quinque.aether.reactivemvvm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.quinque.aether.reactivemvvm.base.BaseActivity;
import com.quinque.aether.reactivemvvm.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ViewModel> {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main); //Биндим view
        binding.setViewModel(getData()); //Устанавливаем ViewModel, при этом методом getData, что бы вручную не сохронять данные
    }


    //Тут можно делать какие угодно предварительные шаги для создания ViewModel
    @Override
    protected void connectData() {
        setData(new ViewModel()); //Но данные устанавливаются только методом setData
    }
}

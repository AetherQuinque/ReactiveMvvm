package com.quinque.aether.reactivemvvm.base;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.Serializable;


public abstract class BaseActivity<T extends BaseViewModel> extends AppCompatActivity {

    private static final String DATA = "data"; //Для сохранения данных
    private T data; //Дженерик, ибо для каждого активити используется своя ViewModel


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null)
            data = savedInstanceState.getParcelable(DATA); //Восстанавливаем данные если они есть
        else
            connectData(); //Если нету - подключаем новые


        setActivity(); //Привязываем активити для ViewModel (если не используем Dagger)
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (data != null) {
            Log.d("my", "Данные сохранены");
            outState.putParcelable(DATA, (Parcelable) data);
        }

    }


    /**
     * Метод onDestroy будет вызываться при любом повороте экрана, так что нам нужно знать
     * что мы сами закрываем активити, что бы уничтожить данные.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("my", "onDestroy");
        if (isFinishing())
            destroyData();
    }


    /**
     * Этот метод нужен только если вы не используете DI.
     * А так, это простой способ передать активити для каких-то действий с preferences или DB
     */
    private void setActivity() {
        if (data != null) {
            if (!data.isSetActivity())
                data.setActivity(this);
        }
    }


    /**
     * Возврощаем данные
     *
     * @return возврощает ViewModel, которая прикреплена за конкретной активити
     */
    public T getData() {
        Log.d("my", "Отдаем данные");
        return data;
    }

    /**
     * Прикрепляем ViewModel к активити
     *
     * @param data
     */
    public void setData(T data) {
        this.data = data;
    }


    /**
     * Уничтожаем данные, предварительно отписываемся от всех подписок Rx
     */
    public void destroyData() {
        if (data != null) {
            data.globalDispose();
            data = null;
            Log.d("my", "Данные уничтожены");
        }
    }


    /**
     * Абстрактный метод, который вызывается, если у нас еще нет сохраненных данных
     */
    protected abstract void connectData();


}

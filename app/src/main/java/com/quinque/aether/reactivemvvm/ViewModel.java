package com.quinque.aether.reactivemvvm;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.quinque.aether.reactivemvvm.base.BaseViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;


public class ViewModel extends BaseViewModel {


    private ObservableBoolean isButtonEnabled = new ObservableBoolean(false);
    private ObservableField<String> count = new ObservableField<>();
    private ObservableField<String> inputText = new ObservableField<>();

    public ViewModel() {
        count.set("0"); //Что бы не делать проверку на ноль при плюсе
        newDisposable(toObservable(inputText)
                .debounce(2000, TimeUnit.MILLISECONDS) //Для имитации ответа от сервера
                .subscribeOn(Schedulers.newThread()) //Работаем не в основном потоке
                .subscribe(s -> {
                            if (s.contains("7"))
                                isButtonEnabled.set(true);
                            else
                                isButtonEnabled.set(false);
                        },
                        Throwable::printStackTrace));
    }

    public ObservableField<String> getInputText() {
        return inputText;
    }

    public ObservableField<String> getCount() {
        return count;
    }

    public ObservableBoolean getIsButtonEnabled() {
        return isButtonEnabled;
    }

    /**
     * Добавляем значение в счетчик
     */
    public void addCount() {
        count.set(String.valueOf(Integer.valueOf(count.get()) + 1));
    }


}

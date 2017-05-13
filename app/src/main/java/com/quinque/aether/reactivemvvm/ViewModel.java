package com.quinque.aether.reactivemvvm;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Parcel;
import android.os.Parcelable;

import com.quinque.aether.reactivemvvm.base.BaseViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;


public class ViewModel extends BaseViewModel implements Parcelable {
 

    public static final Creator<ViewModel> CREATOR = new Creator<ViewModel>() {
        @Override
        public ViewModel createFromParcel(Parcel in) {
            return new ViewModel(in);
        }

        @Override
        public ViewModel[] newArray(int size) {
            return new ViewModel[size];
        }
    };
    private ObservableBoolean isButtonEnabled = new ObservableBoolean(false);
    private ObservableField<String> count = new ObservableField<>();
    private ObservableField<String> inputText = new ObservableField<>();

    public ViewModel() {
        count.set("0"); //Что бы не делать проверку на ноль при плюсе
        setInputText();
    }

    protected ViewModel(Parcel in) {
        isButtonEnabled = in.readParcelable(ObservableBoolean.class.getClassLoader());
        inputText = (ObservableField<String>) in.readSerializable();
        count = (ObservableField<String>) in.readSerializable();
        setInputText();
    }

    private void setInputText() {
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


    /**
     * Добавляем значение в счетчик
     */
    public void addCount() {
        count.set(String.valueOf(Integer.valueOf(count.get()) + 1));

//        Toast.makeText(getActivity(), String.valueOf(test.getAge()), Toast.LENGTH_SHORT).show();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(isButtonEnabled, flags);
        dest.writeSerializable(inputText);
        dest.writeSerializable(count);
    }


}

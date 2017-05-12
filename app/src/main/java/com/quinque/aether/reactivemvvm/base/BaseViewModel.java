package com.quinque.aether.reactivemvvm.base;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Базовый класс для всех ViewModel.
 * Обязательно реализуем интерфейс Parcelable, что бы сохронять данные методом onSaveInstanceState.
 * Наследуем BaseObservable для реализации конвертера из BaseObservable data binding в Observable
 * RxJava 2.
 */
public class BaseViewModel extends BaseObservable implements Parcelable {


    public static final Creator<BaseViewModel> CREATOR = new Creator<BaseViewModel>() {
        @Override
        public BaseViewModel createFromParcel(Parcel in) {
            return new BaseViewModel(in);
        }

        @Override
        public BaseViewModel[] newArray(int size) {
            return new BaseViewModel[size];
        }
    };
    private CompositeDisposable disposables; //Для удобного управления подписками
    private Activity activity;


    protected BaseViewModel() {
        disposables = new CompositeDisposable();
    }

    private BaseViewModel(Parcel in) {

    }

    /**
     * Метод, преобразующий ObservableField в Observable RxJava 2
     *
     * @param observableField - принимаем на вход ObservableField
     * @param <T>             - дженерик для обозначения любого типа данных: String, Integer и т.п.
     * @return - возвращается обычный Observable, который отдает T тип данных
     */
    protected static <T> Observable<T> toObservable(@NonNull final ObservableField<T> observableField) {

        return Observable.fromPublisher(asyncEmitter -> {
            final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable dataBindingObservable, int propertyId) {
                    if (dataBindingObservable == observableField) {
                        asyncEmitter.onNext(observableField.get());
                    }
                }
            };
            observableField.addOnPropertyChangedCallback(callback);
        });
    }

    //Тоже самое, только для ObservableBoolean
    protected static Observable<Boolean> toObservable(@NonNull final ObservableBoolean observableBoolean) {

        return Observable.fromPublisher(asyncEmitter -> {
            final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable dataBindingObservable, int propertyId) {
                    if (dataBindingObservable == observableBoolean) {
                        asyncEmitter.onNext(observableBoolean.get());
                    }
                }
            };
            observableBoolean.addOnPropertyChangedCallback(callback);
        });
    }

    /**
     * Метод добавления новых подписчиков
     */
    protected void newDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    /**
     * Метод для отписки всех подписок разом
     */
    public void globalDispose() {
        disposables.dispose();
    }


    protected Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isSetActivity() {
        return (activity != null);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

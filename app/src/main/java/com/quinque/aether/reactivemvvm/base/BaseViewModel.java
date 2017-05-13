package com.quinque.aether.reactivemvvm.base;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
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
public abstract class BaseViewModel extends BaseObservable {


    private CompositeDisposable disposables; //Для удобного управления подписками
    private Activity activity;


    protected BaseViewModel() {
        disposables = new CompositeDisposable();
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


}

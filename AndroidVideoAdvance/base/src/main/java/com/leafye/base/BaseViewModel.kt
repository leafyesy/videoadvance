package com.leafye.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.leafye.base.utils.Utils
import com.trello.rxlifecycle2.LifecycleProvider
import java.lang.ref.WeakReference
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

open class BaseViewModel<M : BaseModel>(val model: M)
    : AndroidViewModel(Utils.getContext() as Application),
        IBaseViewModel,
        Consumer<Disposable> {
    override fun accept(t: Disposable?) {
        t?.also { addSubscribe(it) }
    }


    private var lifecycle: WeakReference<LifecycleProvider<*>>? = null

    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private val mCompositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    fun injectLifecycleProvider(lifecycle: LifecycleProvider<*>) {
        this.lifecycle = WeakReference(lifecycle)
    }

    fun getLifecycleProvider(): LifecycleProvider<*>? {
        return lifecycle?.get()
    }

    protected fun addSubscribe(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun onCreate() {
    }

    override fun onDestroy() {
        mCompositeDisposable.clear()
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun registerRxBus() {

    }

    override fun removeRxBus() {
    }

    override fun onCleared() {
        super.onCleared()
        model.onCleared()
    }
}
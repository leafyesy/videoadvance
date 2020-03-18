package com.leafye.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext
import androidx.databinding.DataBindingUtil
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<VM : BaseViewModel<out BaseModel>, B : ViewDataBinding> :
    RxAppCompatActivity(), LifecycleOwner {

    protected lateinit var viewModel: VM//by viewModels<VM> { getViewModelFactory() }

    protected lateinit var binding: B

    private var viewModelId: Int = 0

    private val job: Job by lazy { Job() }

    private val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Main + job
    }
    /**
     * 协程启动容器
     */
    val scope = CoroutineScope(coroutineContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //页面接受的参数方法
        beforeView()
        initParam()
        initViewDataBinding(savedInstanceState)
        initData()
        setupObservers()
        viewModel.registerRxBus()
        activityPrepared()
    }

    open fun beforeView() {

    }


    /**
     * 从bundle中获取参数使用
     */
    open fun initParam() {

    }

    /**
     * 初始化页面使用数据
     */
    open fun initData() {

    }


    /**
     * 注入绑定
     */
    private fun initViewDataBinding(savedInstanceState: Bundle?) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState))
        viewModelId = initVariableId()
        val fromImp = getVM()
        viewModel = if (fromImp == null) {
            val modelClass: Class<out BaseViewModel<out BaseModel>> = findClazzFromT()
            createViewModel(modelClass)
        } else {
            fromImp
        }
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel)
        binding.lifecycleOwner = this
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel)
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this)
    }

    private fun findClazzFromT(): Class<out BaseViewModel<out BaseModel>> {
        val modelClass: Class<out BaseViewModel<out BaseModel>>
        val type = javaClass.genericSuperclass
        modelClass = if (type is ParameterizedType) {
            (type.actualTypeArguments[0]) as Class<out BaseViewModel<out BaseModel>>
        } else {
            //如果没有指定泛型参数，则默认使用BaseViewModel
            BaseViewModel::class.java
        }
        return modelClass
    }

    private fun createViewModel(modelClass: Class<out BaseViewModel<out BaseModel>>): VM {
        return ViewModelFactory.instance().create(modelClass) as VM
    }

    abstract fun initVariableId(): Int

    abstract fun initContentView(savedInstanceState: Bundle?): Int

    /**
     * ViewModel
     */
    open fun getVM(): VM? {
        return null
    }

    /**
     * 设置监听
     */
    abstract fun setupObservers()

    /**
     * 准备完成
     */
    abstract fun activityPrepared()

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }

}
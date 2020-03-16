package com.leafye.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.trello.rxlifecycle2.components.support.RxFragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : BaseViewModel<out BaseModel>, B : ViewDataBinding> : RxFragment() {

    protected lateinit var viewModel: VM //by viewModels<VM> { getViewModelFactory() }

    private lateinit var binding: B

    private var viewModelId: Int = 0

    private lateinit var act: Activity

    @Volatile
    private var isCallPrepared: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParam()
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            initContentView(inflater, container, savedInstanceState),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding()
        //页面数据初始化方法
        initData()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        setupObservers()
        //注册RxBus
        viewModel.registerRxBus()
        activity?.let {
            act = it
            callFragmentPrepared()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            act = it
            callFragmentPrepared()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isCallPrepared = false
    }

    private fun callFragmentPrepared() {
        if (activity == null) return
        if (isCallPrepared) return
        fragmentPrepared()
        isCallPrepared = true
    }

    fun getFragActivity(): Activity = act

    /**
     * 注入绑定
     */
    private fun initViewDataBinding() {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
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

    private fun createViewModel(modelClass: Class<out BaseViewModel<out BaseModel>>) =
        ViewModelFactory.instance().create(modelClass) as VM

    abstract fun initVariableId(): Int

    /**
     * 初始化根布局
     * @return 布局layout的id
     */
    @LayoutRes
    abstract fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int

    /**
     * 初始化传入Fragment的参数
     */
    open fun initParam() {

    }

    /**
     * 初始化数据
     */
    open fun initData() {

    }

    /**
     * 实现ViewModel的方法,如果不实现 会自动根据添加的泛型参数进行适配
     */
    open fun getVM(): VM? {
        return null
    }

    /**
     * 设置监听
     */
    abstract fun setupObservers()

    /**
     * fragment数据/页面 准备完成
     */
    abstract fun fragmentPrepared()

}

fun <T> Fragment.observe(data: MutableLiveData<T>, invoke: (T) -> Unit) {
    data.observe(viewLifecycleOwner, Observer {
        invoke.invoke(it)
    })
}

fun AppCompatActivity.launchFragmentInContainer(
    newInstance: Fragment,
    @IdRes containerId: Int,
    backStack: String = "back",
    tag: String = newInstance.javaClass.simpleName
) {
    val transition = supportFragmentManager.beginTransaction()
    transition.replace(containerId, newInstance, tag)
    transition.addToBackStack(backStack)
    transition.commit()
}
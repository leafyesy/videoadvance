package com.leafye.ffmpegapp.ffmpeg

import com.leafye.ffmpegapp.YeFFmpeg
import com.leafye.ffmpegapp.ffmpeg.exception.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by leafye on 2020-03-07.
 * 执行ffmpeg相关的方法
 */
object FFmpegCmd {

    val com: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    /**
     * 最大执行任务数
     */
    private const val MAX_EXCUTE_TASK_COUNT = 5

    private val waitTaskList by lazy {
        CopyOnWriteArrayList<CmdTask>()
    }

    private val excuteTaskList by lazy {
        CopyOnWriteArrayList<CmdTask>()
    }


    @Throws(FFmpegException::class)
    fun excute(cmdTask: CmdTask) {
        Observable.just(cmdTask)
            .flatMap {
                if (excuteTaskList.size >= MAX_EXCUTE_TASK_COUNT) {
                    if (cmdTask.isBgExcute) {
                        waitTaskList.add(it)
                        throw TaskFullException("任务执行已满,请等待")
                    } else {
                        throw DiscardException()
                    }
                }
                if (it.cmdArr?.isNotEmpty() == true) {
                    //把任务加入列表中去
                    excuteTaskList.add(it)
                    Observable.just(it)
                } else
                    throw ParamInvalidException("参数错误,cmdArr为空")
            }
            .subscribeOn(Schedulers.computation())
            .flatMap {
                it.resultCallback?.begin(Result(Result.Type.BEGIN))
                val handleResult = YeFFmpeg.handle(cmdTask.cmdArr)
                excuteTaskList.remove(it)
                if (handleResult == YeFFmpeg.RESULT_OK) {
                    Observable.just(Result(Result.Type.SUCCESS))
                } else {
                    throw HandleErrorException(handleResult)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                cmdTask.resultCallback?.end(it)
                checkAndExcute()
            }, {
                if (it is DiscardException
                    || it is ParamInvalidException
                ) {
                    disposeCmdTask(cmdTask)
                }
                cmdTask.resultCallback?.error(Result(Result.Type.ERROR).apply {
                    if (it is HandleErrorException) code = it.code
                    if (it is ParamInvalidException) code = 1
                    if (it is DiscardException) code = 2
                    if (it is TaskFullException) code = 3
                    msg = it.message
                })
                checkAndExcute()
            }).also {
                com.add(it)
                cmdTask.disposable = it
            }
    }

    private fun checkAndExcute() {
        if (waitTaskList.isNotEmpty()) {
            val task = waitTaskList.removeAt(0)
            excute(task)
        }
    }

    fun disposeCmdTask(task: CmdTask) {
        if (!waitTaskList.remove(task)) {
            excuteTaskList.remove(task)
        }
        task.disposable?.let {
            if (!com.remove(it)) {
                task.disposable?.dispose()
            }
        }
        task.disposable = null
    }

    fun disposeAll() {
        com.clear()
    }

    data class CmdTask(
        val cmdArr: Array<String>? = null,
        var resultCallback: ResultCallback? = null
    ) {
        var isBgExcute: Boolean = false//是否后台执行,如果有必要,否则任务数已满就会丢弃
        var disposable: Disposable? = null
    }


}


/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leafye.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {


    companion object {
        private var instance: ViewModelFactory? = null

        private val productList: MutableList<VMProduct<out ViewModel>> = mutableListOf()

        fun instance(): ViewModelFactory {
            if (instance == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (instance == null) {
                        instance = ViewModelFactory()
                    }
                }
            }
            return instance!!
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //遍历创建列表
        productList.forEach { p ->
            p.isThis(modelClass)?.let {
                return it as T
            }
        }
        return super.create(modelClass)
    }

    fun add(p: VMProduct<out ViewModel>) {
        if (!productList.contains(p)) {
            productList.add(p)
        }
    }

    fun addAll(pList: MutableList<VMProduct<out ViewModel>>) {
        pList.forEach { add(it) }
    }

}

abstract class VMProduct<T : ViewModel?> {

    abstract fun isThis(classNew: Class<*>): T?

}

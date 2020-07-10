/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Consumer
import androidx.core.view.doOnLayout
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.window.DeviceState
import androidx.window.WindowLayoutInfo
import androidx.window.WindowManager
import com.google.samples.apps.sunflower.adapters.PlantAdapter2
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.data.PlantRepository
import com.google.samples.apps.sunflower.databinding.ActivityLaptopGardenBinding
import com.google.samples.apps.sunflower.utilities.InjectorUtils
import com.google.samples.apps.sunflower.utilities.HorizontalSpaceItemDecoration
import java.util.concurrent.Executor

class LaptopGardenActivity : AppCompatActivity() {
    private val viewModel: LaptopGardenViewModel by viewModels {
        InjectorUtils.provideLaptopGardenViewModelFactory(this)
    }

    private val handler = Handler(Looper.getMainLooper())
    private val mainThreadExecutor = Executor { r: Runnable -> handler.post(r) }
    lateinit var windowManager: WindowManager

    private val deviceStateChangeCallback: Consumer<DeviceState> = DeviceStateChangeCallback()
    private val layoutChangeCallback = LayoutChangeCallback()

    private lateinit var binding: ActivityLaptopGardenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_laptop_garden)
        binding.viewModel = viewModel
        val plant = intent.getParcelableExtra<Plant>("plant")
        viewModel.onPlantClick(plant)
        windowManager = WindowManager(this, null)
        windowManager.registerDeviceStateChangeCallback(
            mainThreadExecutor,
            deviceStateChangeCallback
        )

        binding.root.doOnLayout {
            binding.splitLayout.updateWindowLayout(windowManager.windowLayoutInfo)
        }

        binding.controlLayout.plantList.run {
            layoutManager = LinearLayoutManager(this@LaptopGardenActivity, LinearLayoutManager.HORIZONTAL, false)
            val plantAdapter = PlantAdapter2 { viewModel.onPlantClick(it) }
            adapter = plantAdapter
            addItemDecoration(HorizontalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_normal)))
            viewModel.allPlants.observe(this@LaptopGardenActivity) {
                plantAdapter.submitList(it)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        windowManager.registerLayoutChangeCallback(mainThreadExecutor, layoutChangeCallback)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        windowManager.unregisterLayoutChangeCallback(layoutChangeCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.unregisterDeviceStateChangeCallback(deviceStateChangeCallback)
    }

    private inner class DeviceStateChangeCallback : Consumer<DeviceState> {
        override fun accept(newDeviceState: DeviceState) {
            TODO()
        }
    }

    private inner class LayoutChangeCallback : Consumer<WindowLayoutInfo> {
        override fun accept(t: WindowLayoutInfo) {
            binding.splitLayout.updateWindowLayout(t)
        }
    }
}

class LaptopGardenViewModel(
    private val plantRepository: PlantRepository
) : ViewModel() {
    val allPlants
        get() = plantRepository.getPlants()

    private val _currentPlant = MutableLiveData<Plant>()
    val currentPlant: LiveData<Plant>
        get() = _currentPlant

    init {
        _currentPlant.value = allPlants.value?.first()
    }

    fun onPlantClick(plant: Plant) {
        _currentPlant.value = plant
    }
}

class LaptopGardenViewModelFactory(
    private val repository: PlantRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        LaptopGardenViewModel(repository) as T
}
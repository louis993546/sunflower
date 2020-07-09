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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Consumer
import androidx.databinding.DataBindingUtil.setContentView
import androidx.window.DeviceState
import androidx.window.WindowManager
import com.google.android.material.snackbar.Snackbar
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.databinding.ActivityTentGardenBinding
import java.util.concurrent.Executor

class TentGardenActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val mainThreadExecutor = Executor { r: Runnable -> handler.post(r) }
    lateinit var windowManager: WindowManager

    private val deviceStateChangeCallback: Consumer<DeviceState> = DeviceStateChangeCallback()

    private lateinit var binding: ActivityTentGardenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_tent_garden)
        val plant = intent?.extras?.getParcelable<Plant>("plant")
        plant?.run { binding.plant = this }

        windowManager = WindowManager(this, null)
        windowManager.registerDeviceStateChangeCallback(
            mainThreadExecutor,
            deviceStateChangeCallback
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.unregisterDeviceStateChangeCallback(deviceStateChangeCallback)
    }

    private inner class DeviceStateChangeCallback : Consumer<DeviceState> {
        override fun accept(newDeviceState: DeviceState) {
            Snackbar.make(binding.root, "Want to back to normal mode?", Snackbar.LENGTH_LONG)
                .setAction("Yes") { finish() }
                .show()
        }
    }
}
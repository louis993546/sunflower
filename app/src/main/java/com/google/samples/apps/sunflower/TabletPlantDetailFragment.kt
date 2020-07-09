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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Consumer
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.window.WindowLayoutInfo
import com.google.samples.apps.sunflower.databinding.FragmentTabletPlantDetailBinding
import com.google.samples.apps.sunflower.utilities.InjectorUtils
import com.google.samples.apps.sunflower.viewmodels.TabletPlantDetailViewModel

class TabletPlantDetailFragment : Fragment() {
    private val tabletPlantDetailViewModel: TabletPlantDetailViewModel by viewModels {
        InjectorUtils.provideTabletPlantDetailViewModelFactory(this)
    }

    private val layoutStateChangeCallback = LayoutStateChangeCallback()
    private var _binding: FragmentTabletPlantDetailBinding? = null
    private val binding
        get() = _binding!!

    private val gardenActivity
        get() = activity as GardenActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabletPlantDetailBinding.inflate(inflater, container, false)
        binding.root.doOnLayout {
            binding.splitLayout.updateWindowLayout(gardenActivity.windowManager.windowLayoutInfo)
        }
        gardenActivity.layoutStateChangeCallback = layoutStateChangeCallback
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class LayoutStateChangeCallback : Consumer<WindowLayoutInfo> {
        override fun accept(t: WindowLayoutInfo) {
            binding.splitLayout.updateWindowLayout(t)
        }
    }
}
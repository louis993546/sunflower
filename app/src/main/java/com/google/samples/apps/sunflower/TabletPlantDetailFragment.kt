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

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.window.DeviceState
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.databinding.FragmentTabletPlantDetailBinding
import com.google.samples.apps.sunflower.utilities.InjectorUtils
import com.google.samples.apps.sunflower.viewmodels.TabletPlantDetailViewModel

class TabletPlantDetailFragment : Fragment() {
    private val tabletPlantDetailViewModel: TabletPlantDetailViewModel by viewModels {
        InjectorUtils.provideTabletPlantDetailViewModelFactory(this)
    }

    private var _binding: FragmentTabletPlantDetailBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var currentPlant: Plant

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabletPlantDetailBinding.inflate(inflater, container, false)
        binding.viewModel = tabletPlantDetailViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.root.doOnLayout {
            binding.splitLayout.updateWindowLayout(getGardenActivity().windowManager.windowLayoutInfo)
        }
        tabletPlantDetailViewModel.currentPlant.observe(viewLifecycleOwner, Observer {
            this.currentPlant = it
        })
        getGardenActivity().onLayoutStateChange = {
            binding.splitLayout.updateWindowLayout(it)
        }
        getGardenActivity().onDeviceStateChange = { deviceState ->
            if (deviceState.posture == DeviceState.POSTURE_FLIPPED) {
                val intent = Intent(context!!, TentGardenActivity::class.java)
                intent.putExtra("plant", currentPlant)
                startActivity(intent)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun Fragment.getGardenActivity() = activity as GardenActivity


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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.samples.apps.sunflower.utilities.InjectorUtils
import com.google.samples.apps.sunflower.utilities.SplitLayout
import com.google.samples.apps.sunflower.viewmodels.TabletPlantDetailViewModel

class TabletPlantDetailFragment : Fragment() {
    private val tabletPlantDetailViewModel: TabletPlantDetailViewModel by viewModels {
        InjectorUtils.provideTabletPlantDetailViewModelFactory(this)
    }

    private lateinit var splitLayout: SplitLayout
    private lateinit var image: ImageView
    private lateinit var name: TextView
    private lateinit var watering: TextView
    private lateinit var description: TextView
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tablet_plant_detail, container, false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as GardenActivity).setCallback { newWindowLayoutInfo ->
            splitLayout.updateWindowLayout(newWindowLayoutInfo)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        splitLayout = view.findViewById(R.id.split_layout)
        image = splitLayout.findViewById(R.id.detail_image)
        name = splitLayout.findViewById(R.id.plant_detail_name)
        watering = splitLayout.findViewById(R.id.plant_watering)
        description = splitLayout.findViewById(R.id.plant_description)
        fab = view.findViewById(R.id.fab)
    }
}
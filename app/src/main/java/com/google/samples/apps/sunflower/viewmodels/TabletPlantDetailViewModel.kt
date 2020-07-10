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

package com.google.samples.apps.sunflower.viewmodels

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.google.samples.apps.sunflower.data.GardenPlantingRepository
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.data.PlantRepository

class TabletPlantDetailViewModel constructor(
    private val plantRepository: PlantRepository,
    private val gardenPlantingRepository: GardenPlantingRepository
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

class TabletPlantDetailViewModelFactory(
    private val repository: PlantRepository,
    private val gardenPlantingRepository: GardenPlantingRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = TabletPlantDetailViewModel(repository, gardenPlantingRepository) as T
}
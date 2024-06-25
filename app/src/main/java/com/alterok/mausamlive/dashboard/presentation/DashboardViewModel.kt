package com.alterok.mausamlive.dashboard.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alterok.dataresult.DataResult
import com.alterok.dataresult.error.ExceptionResultError
import com.alterok.dataresult.wrapInFailureDataResult
import com.alterok.dataresult.wrapInSuccessDataResult
import com.alterok.kotlin_essential_extensions.evaluateNullable
import com.alterok.kotlin_essential_extensions.ifNotNull
import com.alterok.mausamlive.core.domain.usecase.GetAllLocalitiesUseCase
import com.alterok.mausamlive.core.domain.usecase.GetMausamByLocalityUseCase
import com.alterok.mausamlive.core.presentation.mapper.MausamDomainUIMapper
import com.alterok.mausamlive.core.ui.model.LocalityUIModel
import com.alterok.mausamlive.core.ui.model.MausamUIModel
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sqrt

private const val TAG = "DashboardViewModel"

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getMausamByLocalityUsecase: GetMausamByLocalityUseCase,
    private val getAllLocalitiesUseCase: GetAllLocalitiesUseCase,
    private val mausamDomainUIMapper: MausamDomainUIMapper,
) : ViewModel() {
    private var fetchMausamJob: Job? = null
    private var lastSelectedLocality: LocalityUIModel? = null
    private val _allLocalitiesFlow =
        MutableStateFlow<DataResult<List<LocalityUIModel>>>(DataResult.Loading())
    val allLocalitiesLD = _allLocalitiesFlow.asLiveData()

    private val _selectedLocalityFlow = MutableStateFlow<LocalityUIModel?>(null)
    val selectedLocalityLD = _selectedLocalityFlow.asLiveData()

    private val _mausamResultFlow =
        MutableStateFlow<DataResult<MausamUIModel>>(DataResult.Loading())
    val mausamResultLD = _mausamResultFlow.asLiveData()

    private val _currentLocationFlow = MutableStateFlow<DataResult<Point>>(DataResult.Loading())
    val currentLocationLD = _currentLocationFlow.asLiveData()
    val NotFoundExceptionError = ExceptionResultError.Custom(Exception("Not Found"))

    private val _nearestLocalityFlow =
        _currentLocationFlow.combine(_allLocalitiesFlow) { currentLocation, localities ->

            if (localities.isSuccess && currentLocation.isSuccess) {
                findNearestLocality(
                    localities.getOrNull(),
                    currentLocation.getOrNull()
                ).evaluateNullable(
                    { DataResult.Failure(NotFoundExceptionError) },
                    { DataResult.Success(it) }
                )
            } else if (currentLocation.isFailure) {
                DataResult.Failure(NotFoundExceptionError)
            } else {
                DataResult.Loading()
            }
        }

    val nearestLocalityLD = _nearestLocalityFlow.asLiveData()

    val nearestOrSelectedLocalityLD: LiveData<LocalityUIModel?> =
        _nearestLocalityFlow.combine(_selectedLocalityFlow) { n, s ->

            if (s != null) {
                if (lastSelectedLocality != s) {
                    lastSelectedLocality = s
                    fetchMausamByLocality(s)
                }
            } else {
                (if (n.isFailure) {
                    //select default/random locality
                    selectDefaultLocality(_allLocalitiesFlow.value.getOrNull() ?: emptyList())
                } else if (n.isSuccess) {
                    n.getOrNull()
                } else {
                    null
                }).apply {
                    this?.let { onLocalitySelection(it) }
                }
            }
            s
        }.asLiveData()

    private fun selectDefaultLocality(localities: List<LocalityUIModel>): LocalityUIModel? {
        return localities.find { it.localityId == "ZWL007138" } ?: localities.randomOrNull()
    }

    init {
        //fetching all localities
        viewModelScope.launch {
            getAllLocalitiesUseCase(Unit).map {
                it.map { localityDomainModels ->
                    localityDomainModels.map { localityDomainModel ->
                        mausamDomainUIMapper.localityDomainUIMapper.toUIModel(localityDomainModel)
                    }
                }
            }.collectLatest {
                _allLocalitiesFlow.value = it
            }
        }
    }


    private fun findNearestLocality(
        localities: List<LocalityUIModel>?,
        currentLocationPoint: Point?,
    ): LocalityUIModel? {
        return if (localities.isNullOrEmpty() || currentLocationPoint == null) return null
        else {
            localities.minByOrNull {
                euclideanDistance(
                    currentLocationPoint.latitude(),
                    currentLocationPoint.longitude(),
                    it.latitude.toDouble(),
                    it.longitude.toDouble()
                )
            }
        }
    }

    private fun euclideanDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val deltaLat = lat2 - lat1
        val deltaLon = lon2 - lon1
        return sqrt(deltaLat * deltaLat + deltaLon * deltaLon)
    }

    fun onLocalitySelection(locality: LocalityUIModel) {
        _selectedLocalityFlow.value = locality
    }

    fun onNearbyLocalitySelection(point: Point) {
        findNearestLocality(_allLocalitiesFlow.value.getOrNull(), point)?.let {
            onLocalitySelection(it)
        }
    }

    private fun fetchMausamByLocality(localityUIModel: LocalityUIModel) {
        fetchMausamJob.ifNotNull {
            it.cancel()
        }

        fetchMausamJob = viewModelScope.launch {
            getMausamByLocalityUsecase(
                mausamDomainUIMapper.localityDomainUIMapper.toDomainModel(
                    localityUIModel
                )
            ).map {
                it.map { domainModel ->
                    mausamDomainUIMapper.toUIModel(domainModel)
                }
            }.collectLatest {
                _mausamResultFlow.value = it
            }
        }
    }

    fun forceRefresh() {
        _selectedLocalityFlow.value?.let { fetchMausamByLocality(it) }
    }

    fun updateCurrentLocation(currentLocation: Point?) {
        currentLocation.evaluateNullable(onNull = {
            currentLocation.wrapInFailureDataResult(
                ExceptionResultError.Custom(
                    Exception("Current location is not available!")
                )
            )
        }, onNotNull = {
            it.wrapInSuccessDataResult()
        }).apply {
            _currentLocationFlow.value = this
        }
    }
}
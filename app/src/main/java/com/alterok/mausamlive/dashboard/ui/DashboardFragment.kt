package com.alterok.mausamlive.dashboard.ui

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alterok.dataresult.DataResult
import com.alterok.dataresult.error.ExceptionResultError
import com.alterok.dataresult.error.NetworkResultError
import com.alterok.kotlin_essential_extensions.ifFalse
import com.alterok.kotlin_essential_extensions.ifNotNull
import com.alterok.kotlin_essential_extensions.ifNull
import com.alterok.kotlin_essential_extensions.ifTrue
import com.alterok.mausamlive.R
import com.alterok.mausamlive.core.data.remote.ApiKeyManager
import com.alterok.mausamlive.core.data.remote.ApiKeyNotFoundException
import com.alterok.mausamlive.core.data.remote.UnauthorizedKeyException
import com.alterok.mausamlive.core.ui.model.LocalityUIModel
import com.alterok.mausamlive.core.ui.model.MausamInfoUIModel
import com.alterok.mausamlive.core.ui.model.MausamUIModel
import com.alterok.mausamlive.core.ui.views.ViewTintManager
import com.alterok.mausamlive.core.util.LocationPermissionManager
import com.alterok.mausamlive.core.util.LocationTracker
import com.alterok.mausamlive.core.util.TintScheme
import com.alterok.mausamlive.core.util.showToast
import com.alterok.mausamlive.dashboard.presentation.CompassWatcherViewModel
import com.alterok.mausamlive.dashboard.presentation.DashboardViewModel
import com.alterok.mausamlive.dashboard.ui.adapter.CitySearchAdapter
import com.alterok.mausamlive.dashboard.ui.util.DisplayUtil
import com.alterok.mausamlive.dashboard.ui.util.MausamTintHelper
import com.alterok.mausamlive.databinding.FragmentDashboardBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.util.Date
import java.util.Locale
import javax.inject.Inject

private const val TAG = "DashboardFragment"

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    private var currentMausamData: MausamUIModel? = null
    private lateinit var searchViewAdapter: CitySearchAdapter
    private var hasLocationPermission: Boolean = false
    private var currentLocationPoint: Point? = null
    private var nearestLocality: LocalityUIModel? = null

    private var localities: List<LocalityUIModel> = emptyList()
    private var selectedLocality: LocalityUIModel? = null

    private lateinit var tempViewTintManager: ViewTintManager
    private lateinit var windViewTintManager: ViewTintManager
    private lateinit var humidityViewTintManager: ViewTintManager
    private lateinit var rainIntensityViewTintManager: ViewTintManager
    private lateinit var totalRainViewTintManager: ViewTintManager
    private lateinit var displayData:Pair<Int, Int>
    private val defaultCameraZoom = 14.0

    @Inject
    lateinit var apiKeyManager: ApiKeyManager
    private lateinit var standardBottomSheetBehavior: BottomSheetBehavior<ViewGroup>
    private lateinit var viewBinding: FragmentDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val compassViewModel: CompassWatcherViewModel by viewModels()
    private var loadingStateAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayData = DisplayUtil.getRealDisplaySize(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentDashboardBinding.inflate(inflater, container, false).apply {
            viewBinding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        viewBinding.apply {
            fragmentDashboardOverlayMyLocation.setOnClickListener {
                currentLocationPoint.ifNotNull {
                    nearestLocality.ifNotNull {
                        setCurrentLocality(it)
                    }.ifNull {
                        dashboardViewModel.updateCurrentLocation(it)
                    }
                }.ifNull {
                    hasLocationPermission.ifTrue {
                        fetchCurrentLocation {
                            onCurrentLocationResult(it)
                        }
                    }.ifFalse {
                        LocationPermissionManager.requestCoarseLocationPermission(requireActivity())
                    }
                }
            }

            fragmentDashboardOverlayBottomSheet.setOnRefreshListener {
                dashboardViewModel.forceRefresh()
            }

            fragmentDashboardSearchview.editText.addTextChangedListener(object :
                TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchViewAdapter.onQueryChange(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            fragmentDashboardSearchview.editText.setOnEditorActionListener { v, actionId, event ->
                fragmentDashboardSearchview.hide()

                false
            }

            fragmentDashboardUnderlayHeader.setOnClickListener {
                selectedLocality?.let { it1 -> setMapLocation(it1) }
            }
        }
    }

    private fun setupViews() {
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root) { v, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())

            viewBinding.fragmentDashboardContainerUnderlay.updatePadding(top = statusBarInsets.top)

            insets
        }

        setupOverlayBottomSheet()
        setupTintControllers()
        setupSearchView()
        showLoading()
    }

    private fun setupSearchView() {
        searchViewAdapter = CitySearchAdapter(::onSearchResultSelected).apply {
            viewBinding.fragmentDashboardSearchviewResults.adapter = this
            viewBinding.fragmentDashboardSearchviewResults.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun onSearchResultSelected(selectedLocalityFromSearch: LocalityUIModel) {
        setCurrentLocality(selectedLocalityFromSearch)
        viewBinding.fragmentDashboardSearchview.hide()
        viewBinding.fragmentDashboardSearchviewResults.scrollToPosition(0)
    }

    private fun setupTintControllers() {
        viewBinding.apply {
            tempViewTintManager = ViewTintManager(
                fragmentDashboardOverlayContainerTemp, fragmentDashboardOverlayTemp
            )
            humidityViewTintManager = ViewTintManager(
                fragmentDashboardContainerHumidity, fragmentDashboardOverlayHumidity
            )
            windViewTintManager = ViewTintManager(
                fragmentDashboardContainerWind,
                fragmentDashboardOverlayWind,
                fragmentDashboardOverlayWindDirectionIcon
            )
            rainIntensityViewTintManager = ViewTintManager(
                fragmentDashboardContainerRain,
                fragmentDashboardOverlayRain
            )
            totalRainViewTintManager = ViewTintManager(
                fragmentDashboardOverlayContainerRainfall, fragmentDashboardOverlayRainfall
            )
        }
    }

    private fun setupOverlayBottomSheet() {
        viewBinding.fragmentDashboardContainerUnderlay.post {
            standardBottomSheetBehavior =
                BottomSheetBehavior.from(viewBinding.fragmentDashboardOverlayBottomSheet).apply {
                    peekHeight = displayData.second - viewBinding.fragmentDashboardContainerUnderlay.height
                    isHideable = false

                    state = BottomSheetBehavior.STATE_HALF_EXPANDED
                } as BottomSheetBehavior<ViewGroup>
        }
    }

    private fun setupObservers() {
        dashboardViewModel.allLocalitiesLD.observe(viewLifecycleOwner) {
            it.onSuccess { localityList ->
                localities = localityList
                onLocalitiesAvailable(localityList)
            }
        }

        dashboardViewModel.currentLocationLD.observe(viewLifecycleOwner) {
            currentLocationPoint = it.getOrNull()
        }

        dashboardViewModel.nearestLocalityLD.observe(viewLifecycleOwner) {
            nearestLocality = it.getOrNull()
        }

        dashboardViewModel.nearestOrSelectedLocalityLD.observe(viewLifecycleOwner) {
            if (selectedLocality != it) {
                selectedLocality = it
                it.ifNotNull { it1 ->
                    updateLocalityView(it1)
                }
            }
        }

        dashboardViewModel.mausamResultLD.observe(viewLifecycleOwner) {
            it.onLoading {
                showLoading()
            }.onSuccess { data ->
                hideLoading()
            }.onFailure { error, mausamData ->
                if (error is ExceptionResultError) {
                    when (error.exception) {
                        is ApiKeyNotFoundException -> {
                            promptForApiKey(getString(R.string.msg_api_key_not_found))
                        }

                        is UnauthorizedKeyException -> {
                            showToast {
                                getString(R.string.unauthorized_invalid_key) to Toast.LENGTH_SHORT
                            }
                        }

                        is UnknownHostException -> {
                            showToast {
                                getString(R.string.msg_check_internet_connection) to Toast.LENGTH_LONG
                            }
                        }

                        else -> {

                        }
                    }
                } else if (error is NetworkResultError) {
                    when (error) {
                        NetworkResultError.NoContent -> {
                        }

                        NetworkResultError.Forbidden -> {
                            promptForApiKey(getString(R.string.unauthorized_invalid_key))
                            showToast {
                                getString(R.string.unauthorized_invalid_key) to Toast.LENGTH_SHORT
                            }
                        }
                        NetworkResultError.TooManyRequests -> {
                            showToast { "Limit Reached! Wait till 12am." to Toast.LENGTH_SHORT }
                        }

                        else -> {
                        }
                    }
                }
                hideLoading()
            }

            onMausamDataAvailable(it.getOrNull())

            if (viewBinding.fragmentDashboardOverlayBottomSheet.isRefreshing) {
                showToast { "Updated!" to Toast.LENGTH_SHORT }
                viewBinding.fragmentDashboardOverlayBottomSheet.isRefreshing = false
            }
        }

        LocationPermissionManager.locationPermissionFlow.asLiveData().observe(viewLifecycleOwner) {
            it.ifTrue {
                currentLocationPoint.ifNull {
                    fetchCurrentLocation {
                        onCurrentLocationResult(it)
                    }
                }
            }.ifFalse {
                dashboardViewModel.updateCurrentLocation(null)
                showToast { getString(R.string.msg_location_permission_is_not_granted) to Toast.LENGTH_SHORT }
            }

            hasLocationPermission = it
        }

        compassViewModel.startSensor()
        compassViewModel.compassRotationLD.observe(viewLifecycleOwner) { rotation ->
            currentMausamData.ifNotNull {
                val adjustedRotation = (rotation + 360 - it.mausamInfo.windDirection) % 360
                viewBinding.fragmentDashboardOverlayWindDirectionIcon.rotation =
                    -adjustedRotation.toFloat()
            }
        }
    }

    /**
     * Called when localities data is available.
     */
    private fun onLocalitiesAvailable(localities: List<LocalityUIModel>) {
        searchViewAdapter.setSearchableData(localities)
        showLocalitiesOnMap(localities)
    }

    /**
     * Select the locality for weather fetching and display.
     */
    private fun setCurrentLocality(locality: LocalityUIModel) {
        dashboardViewModel.onLocalitySelection(locality)
        setMapLocation(locality)
    }


    /**
     * called when [fetchCurrentLocation] returns a result.
     */
    private fun onCurrentLocationResult(pointDataResult: DataResult<Point?>) {
        pointDataResult.onSuccess {
            Log.i(TAG, "fetchCurrentLocation: found=$it")
            dashboardViewModel.updateCurrentLocation(it)
            it.ifNotNull {
                dashboardViewModel.onNearbyLocalitySelection(it)
            }
        }.onFailure { iError, point ->
            dashboardViewModel.updateCurrentLocation(point)
            showToast {
                "Failed to fetch current location!" to Toast.LENGTH_SHORT
            }
        }
    }

    /**
     * Tries to fetch current approx. location of the device.
     */
    private fun fetchCurrentLocation(onLocationChange: (DataResult<Point?>) -> Unit) {
        LocationTracker.getLastLocation(requireContext(), onLocationChange)
    }

    /**
     * Shows all the localities on the mapview as clickable textviews
     */
    private fun showLocalitiesOnMap(localities: List<LocalityUIModel>) {
        viewBinding.fragmentDashboardUnderlayMap.viewAnnotationManager.apply {
            removeAllViewAnnotations()

            localities.forEach {
                val location = it
                val point = Point.fromLngLat(
                    it.longitude.toDoubleOrNull() ?: -72.5,
                    it.latitude.toDoubleOrNull() ?: -32.5
                )
                val locationAnnotationView = addViewAnnotation(
                    R.layout.layout_annotation_map_location,
                    viewAnnotationOptions {
                        // View annotation is placed at the specific geo coordinate
                        geometry(point)
                    })

                val annotation = locationAnnotationView.findViewById<TextView>(R.id.annotation)
                annotation.text = "\uD83D\uDCCD ".plus(it.localityName)
                annotation.setOnClickListener {
                    setCurrentLocality(location)
                }
            }
        }
    }

    /**
     * Sets the map's focus coordinates
     */
    private fun setMapLocation(locality: LocalityUIModel) {
        setMapLocation(
            Point.fromLngLat(
                locality.longitude.toDouble(),
                locality.latitude.toDouble()
            )
        )
    }

    /**
     * Sets the map's focus coordinates
     */
    private fun setMapLocation(point: Point) {
        viewBinding.fragmentDashboardUnderlayMap.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(point)
//                .zoom(defaultCameraZoom)
                .build()
        )
    }

    private fun hideLoading() {
        if (loadingStateAnimator?.isStarted == true) {
            loadingStateAnimator?.cancel()
        }
    }

    private fun showLoading() {
        if (loadingStateAnimator?.isStarted == true) {
            return
        }
        loadingStateAnimator.ifNull {
            viewBinding.apply {
                loadingStateAnimator = ValueAnimator.ofInt(0, 3).apply {
                    duration = 1500
                    interpolator = AccelerateDecelerateInterpolator()
                    repeatCount = ValueAnimator.INFINITE
                    addUpdateListener {
                        val value = it.animatedValue as Int
                        val str = ".".repeat(value + 1)
                        fragmentDashboardOverlayTemp.text = str
                        fragmentDashboardOverlayWind.text = str
                        fragmentDashboardOverlayWindDirection.text = str
                        fragmentDashboardOverlayHumidity.text = str
                        fragmentDashboardOverlayRain.text = str
                        fragmentDashboardOverlayRainfall.text = str
                    }
                    start()
                }
            }
        }.ifNotNull {
            it.start()
        }
    }

    private fun promptForApiKey(message: String) {
        val dialog =
            AlertDialog.Builder(requireContext()).setTitle(getString(R.string.msg_api_key_required))
                .setMessage(message)
                .setView(EditText(requireContext()).apply {
                    hint = context.getString(R.string.enter_api_key)
                }).setPositiveButton("OK") { dialog, _ ->
                    val input =
                        (dialog as AlertDialog).findViewById<EditText>(android.R.id.input)?.text.toString()
                    apiKeyManager.saveApiKey(input)
                }.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                    showToast { getString(R.string.api_key_is_required_to_proceed) to Toast.LENGTH_SHORT }
                }.create()
        dialog.show()
    }

    private fun onMausamDataAvailable(data: MausamUIModel?) {
        currentMausamData = data
        updateMausamInfoView(data?.mausamInfo, isDarkMode())
    }


    fun isDarkMode() = kotlin.run {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkMode = when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
        isDarkMode
    }

    private fun updateLocalityView(locality: LocalityUIModel) {
        viewBinding.apply {
            fragmentDashboardUnderlayHeader.text = locality.localityName
            fragmentDashboardOverlayLocality.text =
                "\uD83D\uDCCD ".plus(locality.localityName).plus(", ").plus(locality.city)

            fragmentDashboardOverlayTempDate.text =
                SimpleDateFormat("EEEE - d MMM", Locale.getDefault()).format(Date())

            val point = Point.fromLngLat(
                locality.longitude.toDoubleOrNull() ?: 72.5,
                locality.latitude.toDoubleOrNull() ?: 28.5
            )

            fragmentDashboardUnderlayMap.mapboxMap.setCamera(
                CameraOptions.Builder()
                    .center(point)
                    .pitch(0.0)
                    .bearing(0.0)
                    .build()
            )
        }
    }

    private fun updateMausamInfoView(mausamInfo: MausamInfoUIModel?, isDarkMode: Boolean) {
        viewBinding.apply {

            if (mausamInfo == null || mausamInfo.hasNoSensors()) {
                viewBinding.fragmentDashboardOverlayTemp.text =
                    getString(R.string.this_location_sensors_are_temporarily_down)

                viewBinding.fragmentDashboardOverlayHumidityWindGroup.isVisible = false
                viewBinding.fragmentDashboardOverlayRainGroup.isVisible = false
                viewBinding.fragmentDashboardOverlaySummary.isVisible = false
                viewBinding.fragmentDashboardOverlayDescription.isVisible = false
                selectedLocality?.let {
                    updateLocalityView(it)
                }
                tempViewTintManager.tintWith(TintScheme.Error)
                viewBinding.fragmentDashboardOverlayBottomSheet.backgroundTintList =
                    ColorStateList.valueOf(
                        ColorUtils.compositeColors(
                            TintScheme.Error.foregroundColor.let {
                                ColorUtils.setAlphaComponent(it, if (isDarkMode) 32 else 16)
                            },
                            if (isDarkMode) Color.BLACK else Color.WHITE
                        )
                    )
                return
            }

            if (mausamInfo.hasOnlyRainSensors()) {       //no temperature sensor at this location.
                fragmentDashboardOverlayTemp.text =
                    getString(R.string.this_location_has_only_rain_sensors)
                fragmentDashboardOverlayHumidityWindGroup.isVisible = false
                viewBinding.fragmentDashboardOverlayRainGroup.isVisible = true
            } else {
                fragmentDashboardOverlayTemp.text = mausamInfo.temperatureRoundedStr()
                fragmentDashboardOverlayHumidityWindGroup.isVisible = true
                viewBinding.fragmentDashboardOverlayRainGroup.isVisible = true
            }

            updatedAtJob?.cancel()
            updatedAtJob = viewLifecycleOwner.lifecycleScope.launch {
                while (true) {
                    fragmentDashboardOverlayUpdatingEst.text = mausamInfo.updatedAtStr()
                    delay(1000)
                }
            }

            fragmentDashboardOverlayDescription.text = mausamInfo.generateWeatherPhrase()

            fragmentDashboardOverlayWind.text = mausamInfo.windSpeed.toString()
            fragmentDashboardOverlayWindDirection.text = mausamInfo.windDirectionStr

            fragmentDashboardOverlayHumidity.text = mausamInfo.humidityRoundedStr()

            fragmentDashboardOverlayRain.text = mausamInfo.rainIntensityRounded.toString()
            fragmentDashboardOverlayRainfall.text = mausamInfo.rainAccumulationRounded.toString()

            fragmentDashboardOverlaySummary.text = mausamInfo.mausamSummary

            viewBinding.fragmentDashboardOverlaySummary.isVisible = true
            viewBinding.fragmentDashboardOverlayDescription.isVisible = true

            tintAllMausamCards(mausamInfo, isDarkMode)
        }
    }

    private var updatedAtJob: Job? = null

    /**
     * tints all the MausamInfo cardViews
     */
    private fun tintAllMausamCards(mausamInfo: MausamInfoUIModel, isDarkMode: Boolean) {
        with(mausamInfo) {
            val tempTint = MausamTintHelper.getTemperatureTint(
                temperature,
                isDarkMode
            )

            ColorStateList.valueOf(
                ColorUtils.compositeColors(
                    tempTint.foregroundColor.let {
                        ColorUtils.setAlphaComponent(it, if (isDarkMode) 48 else 32)
                    },
                    if (isDarkMode) Color.BLACK else Color.WHITE
                )
            ).apply {
                viewBinding.fragmentDashboardOverlayBottomSheet.backgroundTintList = this
                viewBinding.fragmentDashboardUnderlayHeader.backgroundTintList = ColorStateList.valueOf(tempTint.backgroundColor)
                viewBinding.fragmentDashboardUnderlayHeader.setTextColor(this)
            }

            //temp card tint
            tempViewTintManager.tintWith(
                tempTint
            )

            //humidity card tint
            humidityViewTintManager.tintWith(
                MausamTintHelper.getHumidityTint(
                    humidity, isDarkMode
                )
            )

            //wind card tint
            windViewTintManager.tintWith(
                MausamTintHelper.getWindTint(
                    windSpeed, isDarkMode
                )
            )

            //rain intensity card tint
            rainIntensityViewTintManager.tintWith(
                MausamTintHelper.getRainIntensityTint(
                    rainIntensity, isDarkMode
                )
            )

            //total rain card tint
            totalRainViewTintManager.tintWith(
                MausamTintHelper.getTotalRainTint(
                    rainAccumulation, isDarkMode
                )
            )
        }
    }

    /**
     * called when fragment is destroyed; Its a great place to stop listening to sensors.
     */
    override fun onDestroy() {
        compassViewModel.stopSensor()
        super.onDestroy()
    }
}
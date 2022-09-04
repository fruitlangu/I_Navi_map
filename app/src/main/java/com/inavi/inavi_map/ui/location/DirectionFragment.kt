package com.inavi.inavi_map.ui.location

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.inavi.inavi_map.R
import com.inavi.inavi_map.databinding.FragmentDirectionBinding
import com.inavi.inavi_map.db.user.UserLocationEntity
import com.inavi.inavi_map.ui.base.BaseFragment
import com.inavi.inavi_map.utils.Constants
import com.inavi.inavi_map.utils.Utils.setMap
import com.inavi.inavi_map.utils.Utils.setMapWithTwoPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class DirectionFragment : BaseFragment<FragmentDirectionBinding>(
    FragmentDirectionBinding::inflate
), OnMapReadyCallback {
    private var map: GoogleMap? = null
    private var mapView: MapView? = null
    private var locationEntity: UserLocationEntity? = null
    private var currentLat:Double?=0.0
    private var currentLong:Double?=0.0
    private var newLatLng: LatLng? = null
    private val PATTERN_DASH_LENGTH_PX = 50
    private val PATTERN_GAP_LENGTH_PX = 10
    private val dot = Dot()
    private val dash = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
    private val gap = Gap(PATTERN_GAP_LENGTH_PX.toFloat())
    private val patternDotted = Arrays.asList(dot, gap)
    private val patternDashed = Arrays.asList(dash, gap)
    private val patternMixed = Arrays.asList(dot, gap, dot, dash, gap)
    private val patternTypeNameResourceIds = intArrayOf(
        R.string.pattern_solid, // Default
        R.string.pattern_dashed, R.string.pattern_dotted, R.string.pattern_mixed)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.userLocationMapView
        initGoogleMap(savedInstanceState)
        navController = findNavController()
        binding.appBarLayout.toolbar.setupToolbar()


    }


    /**
     * Get the destination
     */
    private fun observeLocation(currentLat: Double, currentLong: Double) {
        locationViewModel.userLocation.observe(viewLifecycleOwner) { userLocation ->
            locationEntity = userLocation.find { it.currentUse }
            locationEntity?.let {
                val place1 = MarkerOptions().position(LatLng(currentLat, currentLong)).title("Location 1").draggable(true)
                val place2 = MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("Location 2").draggable(true)
                map!!.addMarker(place1)
                map!!.addMarker(place2)
                updateLocationViews(it,currentLat, currentLong)
            }
        }
    }

    /**
     * Draw the polyline using source and destination
     */
    private fun updateLocationViews(
        locationEntity: UserLocationEntity,
        currentLat: Double,
        currentLong: Double
    ) {
        val coordinates = LatLng(locationEntity.latitude, locationEntity.longitude)
        map.setMap(requireContext(), coordinates)
        map?.uiSettings?.isZoomControlsEnabled = true
        val currentPosition = map?.cameraPosition

        // Add a marker in Sydney and move the camera

        // Add a marker in Sydney and move the camera
        val location2 =
            LatLng(locationEntity?.latitude ?: 0.0, locationEntity?.longitude ?: 0.0)
        val location1 = LatLng(currentLat,currentLong)

        map?.setMapWithTwoPoint(requireContext(), location1, location2)
        this.showCurvedPolyline(location1, location2)
    }

    /**
     * Init the google map
     */
    private fun initGoogleMap(savedInstanceState: Bundle?) {
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(Constants.MAPVIEW_BUNDLE_KEY)
        }
        mapView?.onCreate(mapViewBundle)
        mapView?.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.uiSettings?.setAllGesturesEnabled(true)
        map?.uiSettings?.isZoomControlsEnabled = true
        observeCurrentLocation()
    }


    /**
     * Get the current location
     */
    private fun observeCurrentLocation() {
        locationViewModel.requestLocationUpdates()
        locationViewModel.currentLocation.observe(viewLifecycleOwner) { location ->
            if (location != null) {
                locationViewModel.removeLocationUpdates()
                currentLat=location.latitude
                currentLong=location.longitude

                val zoom = CameraUpdateFactory.zoomTo(15f)
                map!!.animateCamera(zoom)
                map?.setOnCameraIdleListener {
                    newLatLng = map?.cameraPosition?.target
                    newLatLng?.let { latLng ->
                        locationViewModel.getAddress(latLng)
                    }
                }
                map!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude,location.longitude)))
                observeLocation(currentLat!!, currentLong!!)
            }
        }
    }

    /**
     * Draw a polyling using co-ordinates
     */
    private fun showCurvedPolyline(p1: LatLng, p2: LatLng, k: Double = 0.5) {
        val LatLongB = LatLngBounds.Builder()
        //Calculate distance and heading between two points
        val d = SphericalUtil.computeDistanceBetween(p1, p2)
        val h = SphericalUtil.computeHeading(p1, p2)

        //Midpoint position
        val p = SphericalUtil.computeOffset(p1, d * 0.5, h)

        //Apply some mathematics to calculate position of the circle center
        val x = (1 - k * k) * d * 0.5 / (2 * k)
        val r = (1 + k * k) * d * 0.5 / (2 * k)
        val c = SphericalUtil.computeOffset(p, x, h + 90.0)

        //Polyline options
        val options = PolylineOptions()

        //Calculate heading between circle center and two points
        val h1 = SphericalUtil.computeHeading(c, p1)
        val h2 = SphericalUtil.computeHeading(c, p2)

        //Calculate positions of points on circle border and add them to polyline options
        val numPoints = 100
        val step = (h2 - h1) / numPoints
        for (i in 0 until numPoints) {
            val pi = SphericalUtil.computeOffset(c, r, h1 + i * step)
            options.add(pi)
        }


        //Draw polyline
        LatLongB.include(p1)
        LatLongB.include(p2)
        val bounds = LatLongB.build()
        map?.addPolyline(options.width(10f).color(Color.MAGENTA).geodesic(true).pattern(getSelectedPattern(2)))
        map?.uiSettings?.setAllGesturesEnabled(true);
        map!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))




    }

    private fun getSelectedPattern(pos: Int): List<PatternItem>? {
        return when (patternTypeNameResourceIds[pos]) {
            R.string.pattern_solid -> null
            R.string.pattern_dotted -> patternDotted
            R.string.pattern_dashed -> patternDashed
            R.string.pattern_mixed -> patternMixed
            else -> null
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }
    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }
    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }
    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
}
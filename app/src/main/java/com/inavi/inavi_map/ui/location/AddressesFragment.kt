package com.inavi.inavi_map.ui.location

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.inavi.inavi_map.R
import com.inavi.inavi_map.databinding.FragmentAddressesBinding
import com.inavi.inavi_map.db.user.UserLocationEntity
import com.inavi.inavi_map.ui.location.adapter.AddressAdapter
import com.inavi.inavi_map.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AddressesFragment : BaseFragment<FragmentAddressesBinding>(
    FragmentAddressesBinding::inflate
) {
    private val args: AddressesFragmentArgs by navArgs()
    private val adapter by lazy { AddressAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        binding.appBarLayout.toolbar.setupToolbar()

        binding.addressRv.adapter = adapter
        observeUserLocation()

        adapter.onAddressClicked = { address ->
            locationViewModel.clickedAddress = address
            locationViewModel.updateLocation(address.use())
            navController.navigate(R.id.action_addressFragment_to_directionFragment)
        }

        adapter.onDeleteClicked = { address ->
            val currentAddressList = adapter.currentList
            val lastNotUseAddress = currentAddressList.last { !it.currentUse }
            if (address.currentUse) {
                locationViewModel.updateLocation(lastNotUseAddress.use())
            }
            if (currentAddressList.size > 1) {
                locationViewModel.deleteLocation(address)
            }
        }

        binding.btnAddAddress.setOnClickListener {
            navController.navigate(R.id.action_addressesFragment_to_mapsFragment)
        }
        adapter.currentUseAddress =  { address ->
        }

    }

    /***
     * List of location
     */
    private fun observeUserLocation() {
        locationViewModel.userLocation.observe(viewLifecycleOwner) { locations ->
            Collections.reverse(locations)
            adapter.submitList(locations)
        }
    }



    private fun UserLocationEntity.use(): UserLocationEntity {
        return UserLocationEntity(
            this.id,
            this.address,
            this.city,
            this.addInfo,
            this.latitude,
            this.longitude,
            true
        )
    }
}
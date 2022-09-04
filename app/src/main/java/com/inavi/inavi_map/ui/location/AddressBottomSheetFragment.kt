package com.inavi.inavi_map.ui.location

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.inavi.inavi_map.R
import com.inavi.inavi_map.databinding.FragmentAddressBottomSheetBinding
import com.inavi.inavi_map.db.user.UserLocationEntity
import com.inavi.inavi_map.ui.base.BaseBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AddressBottomSheetFragment : BaseBottomSheetFragment<FragmentAddressBottomSheetBinding>(
    FragmentAddressBottomSheetBinding::inflate
) {

    private val args: AddressBottomSheetFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.streetInputEditText.setText(args.location.address)
        binding.localityInputEditText.setText(args.location.city)



        val args = args
        binding.btnSave.setOnClickListener {
            val street = binding.streetInputEditText.text
            val city = binding.localityInputEditText.text
            val addInfo = binding.addInfoInputEditText.text

          //  val street = args.location.address
          //  val city = args.location.city
           // val addInfo = "Near by shop"
            if (!street.isNullOrBlank()) {
                if (!city.isNullOrBlank()) {
                    locationViewModel.insertLocation(
                        UserLocationEntity(
                        address = street.toString(),
                        city = city.toString(),
                        addInfo = if (!addInfo.isNullOrBlank()) addInfo.toString() else null,
                        latitude = args.location.latitude,
                        longitude = args.location.longitude,
                        currentUse = true
                        )
                    )
                    navigate()
                } else {
                    binding.localityInputLayout.helperText = "required"
                }
            } else {
                binding.streetInputLayout.helperText = "required"
            }
        }
    }

    private fun navigate() {
        findNavController().navigate(R.id.action_addressBottomSheetFragment_to_addressesFragment)
    }
}
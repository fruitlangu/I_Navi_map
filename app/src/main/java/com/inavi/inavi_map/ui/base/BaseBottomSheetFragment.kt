package com.inavi.inavi_map.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inavi.inavi_map.R
import com.inavi.inavi_map.ui.location.viewmodel.LocationViewModel
import com.inavi.inavi_map.ui.widget.CommonAlertDialog
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class BaseBottomSheetFragment<VB: ViewBinding> (
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : BottomSheetDialogFragment() {

    val locationViewModel by viewModels<LocationViewModel>()


    val dialog by lazy { CommonAlertDialog(requireActivity()) }

    private var _binding: VB? = null
    val binding: VB
        get() = _binding as VB

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null)
            throw IllegalArgumentException("Binding cannot be null")
        binding.root.setBackgroundResource(R.drawable.bg_white_fff_rounded_24dp_top)
        return binding.root
    }

    fun showShortToast(message: Int) {
        Toast.makeText(requireContext(), getString(message), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTheme(): Int {
        return R.style.NoBackgroundDialogTheme
    }

}
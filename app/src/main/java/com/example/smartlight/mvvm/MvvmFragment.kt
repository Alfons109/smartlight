package com.example.smartlight.mvvm

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.smartlight.R
import com.example.smartlight.SmartLightConfig
import com.example.smartlight.databinding.FragmentSmartLightBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MvvmFragment : Fragment() {

    private var _binding: FragmentSmartLightBinding? = null
    private lateinit var viewModel: MvvmViewModel

    private val alphaAnimation = AlphaAnimation(0.2f, 1.0f)
        .apply {
            duration = 500
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSmartLightBinding.inflate(inflater, container, false)

        //View lifecycle aware viewModel
        viewModel = ViewModelProvider(this).get(MvvmViewModel::class.java)
        viewModel.currentConfig().observe(viewLifecycleOwner, { onConfigCallback(it) })

        binding.buttonOff.setOnClickListener { onButtonClick(it) }
        binding.buttonOn.setOnClickListener { onButtonClick(it) }
        binding.buttonAlert.setOnClickListener { onButtonClick(it) }

        return binding.root
    }

    private fun onButtonClick(view: View) {
        context?.let {
            //disable button click
            enableClicks(false)
            //show the spinner
            binding.progressBar.isVisible = true

            val configSelected = when (view) {
                binding.buttonOff -> {
                    SmartLightConfig.OFF
                }
                binding.buttonOn -> {
                    SmartLightConfig.ON
                }
                binding.buttonAlert -> {
                    SmartLightConfig.ALERT
                }
                else -> {
                    SmartLightConfig.OFF
                }
            }

            viewModel.asyncSmartLightConfigurationUpdate(configSelected)
        }
    }

    private fun onConfigCallback(configValue: SmartLightConfig) {
        if (_binding == null) return
        context?.let {
            //hide the spinner
            binding.progressBar.isGone = true
            //enable clicks
            enableClicks(true)

            when (configValue) {
                SmartLightConfig.OFF -> {
                    binding.image.animation = null
                    binding.image.setColorFilter(ContextCompat.getColor(it, R.color.black))
                }
                SmartLightConfig.ON -> {
                    binding.image.animation = null
                    binding.image.setColorFilter(ContextCompat.getColor(it, R.color.alert))

                }
                SmartLightConfig.ALERT -> {
                    binding.image.setColorFilter(ContextCompat.getColor(it, R.color.alert))
                    if (binding.image.animation == null) {
                        binding.image.startAnimation(alphaAnimation)
                    }
                }
            }
        }
    }

    private fun enableClicks(enable: Boolean) {
        binding.buttonOff.isEnabled = enable
        binding.buttonOn.isEnabled = enable
        binding.buttonAlert.isEnabled = enable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
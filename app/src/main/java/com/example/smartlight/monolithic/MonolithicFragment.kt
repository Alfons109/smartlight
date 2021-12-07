package com.example.smartlight.monolithic

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.smartlight.R
import com.example.smartlight.SmartLightConfig
import com.example.smartlight.databinding.FragmentSmartLightBinding
import java.lang.Exception
import java.util.concurrent.Executors


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MonolithicFragment : Fragment() {

    private var _binding: FragmentSmartLightBinding? = null

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
    ): View {
        _binding = FragmentSmartLightBinding.inflate(inflater, container, false)

        binding.buttonOff.setOnClickListener { onButtonClick(it) }
        binding.buttonOn.setOnClickListener { onButtonClick(it) }
        binding.buttonAlert.setOnClickListener { onButtonClick(it) }

//        binding.buttonNext.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
        return binding.root

    }

    private fun onButtonClick(view: View) {
        context?.let {
            //disable button click
            enableClicks(false)
            //show the spinner
            binding.progressBar.isVisible = true

            val configSelected = when(view) {
                binding.buttonOff -> {
                    SmartLightConfig.OFF
                }
                binding.buttonOn -> {
                    SmartLightConfig.ON
                }
                binding.buttonAlert -> {
                    SmartLightConfig.ALERT
                }
                else -> {SmartLightConfig.OFF}
            }

            asyncSmartLightConfigurationUpdate(configSelected)
        }
    }

    private fun asyncSmartLightConfigurationUpdate(configValue: SmartLightConfig) {
        // do async task, provide callback or listener to receive response
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            try {
                Log.d(javaClass.simpleName, "Starting long task - $configValue")
                Thread.sleep(3000)
                Log.d(javaClass.simpleName, "Completed long task - $configValue")
            }catch (ex: Exception) {
                Log.d(javaClass.simpleName, "Exception- $ex")
            }
            handler.post {
                onConfigCallback(configValue)
            }
        }
    }

    private fun onConfigCallback(configValue: SmartLightConfig) {
        if(_binding == null) return
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
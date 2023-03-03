package com.leshe4ka.rotator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.leshe4ka.rotator.databinding.SettingsBinding
import com.leshe4ka.rotator.MainActivity


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class Settings : Fragment() {

private var _binding: SettingsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      _binding = SettingsBinding.inflate(inflater, container, false)
      return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                binding.saveHost.setOnClickListener {

            Snackbar.make(view, "0", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
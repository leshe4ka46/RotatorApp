package com.leshe4ka.rotator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.leshe4ka.rotator.databinding.FragmentFirstBinding
import com.leshe4ka.rotator.sendangles

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
fun vailid(str:String): Boolean {
    if (str!="" && str!="."){
        return true
    }
    return false
}
class FirstFragment : Fragment() {

private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      _binding = FragmentFirstBinding.inflate(inflater, container, false)
      return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.sendAngles.setOnClickListener { view
            if (vailid(binding.editTextAzimuth.text.toString()) && vailid(binding.editTextElevation.text.toString())) {
                sendangles(
                    binding.editTextAzimuth.text.toString(),
                    binding.editTextElevation.text.toString()
                )
                Snackbar.make(view,"Отправлено:" + binding.editTextAzimuth.text.toString() + " " + binding.editTextElevation.text.toString(),Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.sendAngles)
                    .setAction("Закрыть", { }).show()
            }
            else{
                Snackbar.make(
                    view,"Надо хоть что-то написать", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.sendAngles)
                    .setAction("Закрыть", { }).show()
            }
        }
    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.leshe4ka.rotator

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.leshe4ka.rotator.databinding.JoyActivityBinding
var handlerJoy = Handler()
class JoyActivity : AppCompatActivity() {
    private lateinit var binding: JoyActivityBinding
    var runnableJoy: Runnable? = null
    private var delay:Int=1000
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        val view: View = findViewById(android.R.id.content)
        binding = JoyActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            this.finish()
        }
        binding.upButton.setOnClickListener {
            move(1)
        }
        binding.downButton.setOnClickListener {
            move(4)
        }
        binding.leftButton.setOnClickListener {
            move(2)
        }
        binding.rightButton.setOnClickListener {
            move(3)
        }
        binding.homeButton.setOnClickListener {
            move(0)
        }

    }
    private fun get_angle_by_id(id:Any): Float {
        return when(id){
            binding.aButton -> 0.1f
            binding.bButton -> 0.25f
            binding.cButton -> 1f
            binding.dButton -> 5f
            binding.eButton -> 10f
            binding.fButton -> 25f
            else -> 0f
        }
    }
    private fun move(dir:Int){
        post_joystick(if (dir === 1 || dir === 4) 1 else 0,
            (if (dir === 2 || dir === 4) -1 else 1) * get_angle_by_id(findViewById(binding.angleToggleGroup.getCheckedButtonId())),(dir==0).toInt())
    }

    override fun onResume() {
        handlerJoy.postDelayed(Runnable {
            handlerJoy.postDelayed(runnableJoy!!, delay.toLong())
            get_data_joystick(binding)
        }.also { runnableJoy = it }, delay.toLong())
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnableJoy!!)
    }
}
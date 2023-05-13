package com.leshe4ka.rotator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.leshe4ka.rotator.databinding.JoyActivityBinding

class JoyActivity : AppCompatActivity() {
    private lateinit var binding: JoyActivityBinding
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

    }
    private fun getlen(id:Any): Float {
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
            (if (dir === 2 || dir === 4) -1 else 1) * getlen(findViewById(binding.angleToggleGroup.getCheckedButtonId())))
    }
}
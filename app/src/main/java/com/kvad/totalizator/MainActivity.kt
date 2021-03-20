package com.kvad.totalizator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kvad.totalizator.betfeature.BetDialogFragment
import com.kvad.totalizator.betfeature.BetFragment
import com.kvad.totalizator.databinding.ActivityMainBinding
import com.kvad.totalizator.header.HeaderFragment
import com.kvad.totalizator.onboard.OnBoardFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()

        supportFragmentManager.beginTransaction()
            .add(binding.fcvBody.id, BetFragment())
            .commit()

        supportActionBar?.hide()
        //todo
        supportFragmentManager.beginTransaction()
            .add(binding.fcvHeader.id, HeaderFragment())
            .commit()
    }

    private fun setupBinding(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}

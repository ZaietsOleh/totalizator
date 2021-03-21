package com.kvad.totalizator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.kvad.totalizator.betfeature.BetFragment
import com.kvad.totalizator.databinding.ActivityMainBinding
import com.kvad.totalizator.header.HeaderFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()

        supportActionBar?.hide()
        //todo
        supportFragmentManager.beginTransaction()
            .add(binding.fcvHeader.id, HeaderFragment())
            .commit()
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

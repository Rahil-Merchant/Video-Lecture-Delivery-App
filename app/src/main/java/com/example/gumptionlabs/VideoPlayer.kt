package com.example.gumptionlabs

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_video_player.*


class VideoPlayer  : AppCompatActivity(){
    var number = 59777392
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        setupView()
        Log.d("DEBUG","HELP")
    }
    private fun setupView() {
        lifecycle.addObserver(vimeoPlayer)
        number = Integer.parseInt(intent.getStringExtra("myvideoURL"))
        Log.d("TAG",""+number)
        vimeoPlayer.initialize(number)//59777392)
    }
}

package com.example.submissionproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val KEY_GAME = "key_game"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val dataGame = intent.getParcelableExtra<Game>(KEY_GAME)

        if (dataGame != null) {
            val ivDetailPhoto: ImageView = findViewById(R.id.tv_detail_photo)
            val tvDetailName: TextView = findViewById(R.id.tv_detail_name)
            val tvDetailDescription: TextView = findViewById(R.id.tv_detail_description)

            ivDetailPhoto.setImageResource(dataGame.photo)
            tvDetailName.text = dataGame.name
            tvDetailDescription.text = dataGame.detail_description
        }

        val btnActionShare:Button = findViewById(R.id.action_share)
        btnActionShare.setOnClickListener(this)


    }

    override fun onClick(p0: View?) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Content Information")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}
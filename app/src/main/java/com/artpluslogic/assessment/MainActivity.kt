package com.artpluslogic.assessment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.artpluslogic.weird32bitformatter.WeirdFormatter

class MainActivity : AppCompatActivity() {

    var encodedIntegers : ArrayList<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        val etInput = findViewById<EditText>(R.id.et_input)
        val btnEncode = findViewById<Button>(R.id.btn_encode)
        val tvResult= findViewById<TextView>(R.id.tv_encoded_result)
        val btnDecode = findViewById<Button>(R.id.btn_decode)
        val tvDecodedResult= findViewById<TextView>(R.id.tv_decoded_result)

        btnEncode.setOnClickListener {
            encodedIntegers = WeirdFormatter.encode(etInput.text.toString())
            val stringBuilder = StringBuilder()
            for (encodedInteger in encodedIntegers!!) {
                stringBuilder.append("{$encodedInteger}, ")
            }
            tvResult.text = stringBuilder.toString()
        }

        btnDecode.setOnClickListener {
            if (encodedIntegers == null || encodedIntegers?.size == 0) {
                Toast.makeText(this@MainActivity, "You need to encode some text first!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val decodedString = WeirdFormatter.decode(encodedIntegers)
            tvDecodedResult.text = decodedString
        }
    }
}
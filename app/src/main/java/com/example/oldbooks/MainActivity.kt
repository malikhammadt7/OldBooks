package com.example.oldbooks

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.oldbooks.ui.theme.OldBooksTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OldBooksTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }

        // Find the button by its ID
        val myButton: Button = findViewById(R.id.myButton)

        // Set a click listener to the button
        myButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                // Add the functionality you want when the button is clicked
                // For example, display a toast message
                Toast.makeText(applicationContext, "Button Clicked!", Toast.LENGTH_SHORT).show()
                // Create an intent to switch to the new activity
                val intent = Intent(applicationContext, PublishPost::class.java)
                startActivity(intent)
            }
        })
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OldBooksTheme {
        Greeting("Android")
    }
}
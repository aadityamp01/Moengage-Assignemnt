package com.aaditya.moengageassignemnt.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aaditya.moengageassignemnt.R

class WebViewActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    companion object {
        const val NEWS_URL = "newsUrl"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val webView: WebView = findViewById(R.id.webView)
        val webSettings = webView.settings
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.loadsImagesAutomatically = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        val articleUrl = intent.getStringExtra(NEWS_URL)

        if (!articleUrl.isNullOrEmpty()) {
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    // URL loaded successfully, remove any pending callbacks
                    handler.removeCallbacksAndMessages(null)
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    // Handle the error, for example, show a Toast and finish the activity
                    Toast.makeText(
                        this@WebViewActivity,
                        "Error loading article URL",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }

            // Load the URL
            webView.loadUrl(articleUrl)

            // Wait for 5 seconds and check if the URL has loaded
            handler.postDelayed({
                Log.d("WebViewActivity", "URL not loaded within 5 seconds")
            }, 8000)
        } else {
            Toast.makeText(this, "Invalid article URL", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

}
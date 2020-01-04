package com.day.ourday.activity

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.commit451.nativestackblur.NativeStackBlur
import com.day.ourday.BR
import com.day.ourday.R
import com.day.ourday.data.entity.Event
import com.day.ourday.databinding.ActivityMainBinding
import com.day.ourday.util.SharedPreferencesUtils
import com.day.ourday.util.getFullPath
import com.day.ourday.viewmodel.PictureViewModel
import androidx.databinding.DataBindingUtil.setContentView
import com.day.ourday.util.SharedPreferencesUtils.get

class MainActivity : AppCompatActivity() {
    private lateinit var bgp: String
    private var blurProgress: Int = 0
    private val pictureViewModel: PictureViewModel by lazy { ViewModelProviders.of(this).get(PictureViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        val dataBinding = setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            viewModel = pictureViewModel
            imageView.imageAlpha = MAX_BLUR - blurProgress
            imageView.foreground.alpha = blurProgress

            with(this@MainActivity) {
                lifecycleOwner = this
                SharedPreferencesUtils.defaultPrefs(this).also {
                    bgp = it[getString(R.string.background_picture), ""]
                    blurProgress = it[getString(R.string.blur_progress), 0]
                }
                Glide.with(this).load(if (bgp.isEmpty()) R.drawable.tang else bgp).into(imageView)
            }
        }

        pictureViewModel.bgChangeEvent.observe(this, Observer { event: Event ->
            var fileName = if (event.type == Event.START) bgp else pictureViewModel.mainBgPictureName.value
            val bitmap = if (fileName!!.isEmpty()) BitmapFactory.decodeResource(resources, R.drawable.tang) else BitmapFactory.decodeFile(getFullPath(fileName))
            Glide.with(this).asDrawable().load(NativeStackBlur.process(bitmap, 70)).into(dataBinding.imageBlurView)
        })
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                with(v) {
                    getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        setFocusable(false)
                        setFocusableInTouchMode(true)
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(getWindowToken(), 0)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val MAX_BLUR = 255
    }
}
package de.pacheco.fdpmockup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class Main: AppCompatActivity() {
    private var player: ExoPlayer? = null
    private var videoContainer: FrameLayout? = null
    private var heightInput: EditText? = null
    private var aspectRatioHeightInput: EditText? = null
    private var aspectRatioWidthInput: EditText? = null
    private var formatSwitch: Switch? = null
    private var playerView: PlayerView? = null

    private val mediaItem16_9: MediaItem =
        MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.bbb16))
    private val mediaItem21_9 =
        MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.bbb21))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById<PlayerView?>(R.id.player_view)
        videoContainer = findViewById<FrameLayout?>(R.id.video_container)
        heightInput = findViewById<EditText?>(R.id.height_input)
        aspectRatioHeightInput = findViewById<EditText?>(R.id.aspect_ratio_height_input)
        aspectRatioWidthInput = findViewById<EditText?>(R.id.aspect_ratio_width_input)
        formatSwitch = findViewById<Switch?>(R.id.format_switch)

        player = ExoPlayer.Builder(this).build()
        playerView?.setPlayer(player)

//    val videoUri: Uri =
//        android.net.Uri.parse(
//            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
//    val mediaItem: MediaItem = MediaItem.fromUri(videoUri)
        player?.setMediaItem(mediaItem16_9)
        player?.prepare()
        player?.play()

        setupButtons()
        setupTextWatchers()
        setupSwitch()
    }

    private fun setupButtons() {
        findViewById<android.view.View?>(R.id.button_12cm_21_9)
            .setOnClickListener(
                android.view.View.OnClickListener { v: android.view.View? ->
                    updateBoxSize(12.0, 21.0, 9.0)
                })
        findViewById<android.view.View?>(R.id.button_12cm_16_9)
            .setOnClickListener(
                android.view.View.OnClickListener { v: android.view.View? ->
                    updateBoxSize(12.0, 16.0, 9.0)
                })
        findViewById<android.view.View?>(R.id.button_15cm_17_5_9)
            .setOnClickListener(
                android.view.View.OnClickListener { v: android.view.View? ->
                    updateBoxSize(15.0, 17.5, 9.0)
                })
        findViewById<android.view.View?>(R.id.button_12cm_17_5_9)
            .setOnClickListener(
                android.view.View.OnClickListener { v: android.view.View? ->
                    updateBoxSize(12.0, 17.5, 9.0)
                })
    }

    private fun setupTextWatchers() {
        val textWatcher: TextWatcher =
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    updateBoxFromInputs()
                }
            }

        heightInput?.addTextChangedListener(textWatcher)
        aspectRatioHeightInput?.addTextChangedListener(textWatcher)
        aspectRatioWidthInput?.addTextChangedListener(textWatcher)
    }

    private fun setupSwitch() {
        formatSwitch?.setOnCheckedChangeListener(
            CompoundButton.OnCheckedChangeListener {
                    buttonView: CompoundButton?,
                    isChecked: Boolean ->
                formatSwitch?.setText(if (isChecked) "21:9" else "16:9")
                updateVideoFormat()
            })
    }

    private fun updateBoxSize(heightCm: Double, aspectRatioWidth: Double, aspectRatioHeight: Double) {
//      val densityDpi = getResources().getDisplayMetrics().densityDpi
        val densityDpi = getResources().getDisplayMetrics().densityDpi
//      val metrics = DisplayMetrics()
//            getWindowManager().getDefaultDisplay().getRealMetrics(metrics)
//      val mDisplayMetrics = getResources().getDisplayMetrics()
//      val mDPI = (float) mDisplayMetrics.densityDpi
//      val measurement = distance / mDPI

        val heightPx: Double = heightCm * densityDpi / 2.54
        val widthPx = heightPx * (aspectRatioWidth / aspectRatioHeight)

        val params: RelativeLayout.LayoutParams =
            RelativeLayout.LayoutParams(widthPx.toInt(), heightPx.toInt())
        params.leftMargin = (getResources().getDisplayMetrics().widthPixels - widthPx.toInt()) / 2
        params.topMargin = (getResources().getDisplayMetrics().heightPixels - heightPx.toInt()) / 2
        videoContainer?.setLayoutParams(params)
        //        videoContainer.forceLayout();
        //        updateVideoFormat();
//    val params2: FrameLayout.LayoutParams =
//        FrameLayout.LayoutParams(widthPx.toInt(), heightPx.toInt())
//    playerView?.setLayoutParams(params2)
    }

    private fun updateBoxFromInputs() {
        try {
            val heightCm: Double = heightInput?.getText().toString().toDouble()
            val aspectRatioHeight: Double = aspectRatioHeightInput?.getText().toString().toDouble()
            val aspectRatioWidth: Double = aspectRatioWidthInput?.getText().toString().toDouble()
            updateBoxSize(heightCm, aspectRatioHeight, aspectRatioWidth)
        } catch (e: NumberFormatException) {
            // Handle invalid input
        }
    }

    private fun updateVideoFormat() {
        val currentPosition = player?.currentPosition ?: 0L
        val wasPlaying = player?.isPlaying ?: true

        if ("21:9" == formatSwitch?.getText()) {
            player?.setMediaItem(mediaItem21_9)
        } else {
            player?.setMediaItem(mediaItem16_9)
        }
        player?.prepare()
        player?.seekTo(currentPosition)
        if (wasPlaying) player?.play()
//    val height: Int //                = videoContainer.getHeight()
//    val width: Int = videoContainer?.getWidth() ?: 0
//    height =
//        if ("21:9" == formatSwitch?.getText()) {
//          //            width = (int) (height / 9.0 * 21.0);
//          (width / 21.0 * 9.0).toInt()
//        } else {
//          //            width = (int) (height / 9.0 * 16.0);
//          (width / 16.0 * 9.0).toInt()
//        }
//    val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(width, height)
//    playerView?.setLayoutParams(params)
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}

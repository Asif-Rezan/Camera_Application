package com.asifrezan.camera_application.ui

import com.asifrezan.camera_application.models.PhotoType
import com.asifrezan.camera_application.utils.AspectRatioHelper
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class OverlayFrameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var photoType: PhotoType = PhotoType.ID_PHOTO
    private val paint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
        alpha = 150
    }

    fun setPhotoType(type: PhotoType) {
        photoType = type
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val (widthRatio, heightRatio) = AspectRatioHelper.getAspectRatio(photoType)
        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()
        val targetAspectRatio = widthRatio.toFloat() / heightRatio

        // Calculate frame dimensions while maintaining aspect ratio
        var frameWidth = viewWidth * 0.8f
        var frameHeight = frameWidth / targetAspectRatio

        if (frameHeight > viewHeight * 0.8f) {
            frameHeight = viewHeight * 0.8f
            frameWidth = frameHeight * targetAspectRatio
        }

        val left = (viewWidth - frameWidth) / 2
        val top = (viewHeight - frameHeight) / 2
        val right = left + frameWidth
        val bottom = top + frameHeight

        canvas.drawRect(left, top, right, bottom, paint)
    }
}
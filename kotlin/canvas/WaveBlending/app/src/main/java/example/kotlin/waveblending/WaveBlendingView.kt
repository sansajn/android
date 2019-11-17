package example.kotlin.waveblending

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class WaveBlendingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

	private val PORTION = 0.375f

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)

		val paint = Paint(Paint.ANTI_ALIAS_FLAG)
		canvas.drawBitmap(_wave, 0f, 0f, paint)
		paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.ADD)
		canvas.drawBitmap(_mask, 0f, 0f, paint)
	}

	private fun makeMask(pos: Float): Bitmap {
		val bmp = Bitmap.createBitmap(_wave.width, _wave.height, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bmp)
		val paint = Paint(Paint.ANTI_ALIAS_FLAG)
		paint.color = Color.parseColor("#e91e63")
		canvas.drawRect(0f, 0f, _wave.width * pos, _wave.height.toFloat(), paint)
		paint.color = Color.parseColor("#2196f3")
		canvas.drawRect(_wave.width * pos, 0f, _wave.width.toFloat(), _wave.height.toFloat(), paint)
		return bmp
	}

	private val _wave = BitmapFactory.decodeResource(resources, R.drawable.wave)
	private val _mask = makeMask(PORTION)
}

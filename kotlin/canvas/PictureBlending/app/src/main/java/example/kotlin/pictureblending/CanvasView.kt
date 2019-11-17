package example.kotlin.pictureblending

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CanvasView(context: Context, attrs: AttributeSet) : View(context, attrs) {

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)

		canvas.drawColor(Color.WHITE)

		val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG)
		labelPaint.textAlign = Paint.Align.CENTER
		labelPaint.textSize = TEXT_SIZE

		val paint = Paint()
		paint.setFilterBitmap(false)

		canvas.translate(15f, TEXT_SIZE + 35)

		var x = 0f
		var y = 0f
		for (i in 0 until _modes.size) {
			// draw the border
			paint.style = Paint.Style.STROKE
			paint.shader = null
			canvas.drawRect(x - 0.5f, y - 0.5f, x + W + 0.5f, y + H + 0.5f, paint)

			// draw the checker-board pattern
			paint.style = Paint.Style.FILL
			paint.shader = _backgorundChecker
			canvas.drawRect(x, y, x + W, y + H, paint)

			// draw the src/dst example into our offscreen bitmap
			val restoreLayerIdx = canvas.saveLayer(x, y, x + W, y + H, null)
			canvas.translate(x, y)
			canvas.drawBitmap(_dst, 0f, 0f, paint)
			paint.xfermode = _modes[i]
			canvas.drawBitmap(_src, 0f, 0f, paint)
			paint.xfermode = null
			canvas.restoreToCount(restoreLayerIdx)

			// draw the label
			canvas.drawText(_labels[i], x + W/2f, y - TEXT_SIZE/3f, labelPaint)

			x += W + 10

			// wrap around when we've drawn enough for one row
			if ((i % ROW_MAX) == ROW_MAX - 1) {
				x = 0f
				y += H + TEXT_SIZE + 40
			}
		}
	}

	private fun makeDst(w: Int, h: Int): Bitmap {
		val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bmp)
		val paint = Paint(Paint.ANTI_ALIAS_FLAG)
		paint.color = 0xFF_FF_CC_44.toInt()
		canvas.drawOval(RectF(0f, 0f, w*3/4f, h*3/4f), paint)
		return bmp
	}

	private fun makeSrc(w: Int, h: Int): Bitmap {
		val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bmp)
		val paint = Paint(Paint.ANTI_ALIAS_FLAG)
		paint.color = 0xFF_66_AA_FF.toInt()
		canvas.drawRect(w/3f, h/3f, w*19/20f, h*19/20f, paint)
		return bmp
	}

	private fun makeBackgroundChacker(): BitmapShader {
		val bmp = Bitmap.createBitmap(intArrayOf(
			0xFF_FF_FF_FF.toInt(), 0xFF_CC_CC_CC.toInt(),
			0xFF_CC_CC_CC.toInt(), 0xFF_FF_FF_FF.toInt()), 2, 2, Bitmap.Config.RGB_565)
		val bg = BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
		val M = Matrix()
		M.setScale(6f, 6f)
		bg.setLocalMatrix(M)
		return bg
	}

	private val _modes = arrayOf(
		PorterDuffXfermode(PorterDuff.Mode.CLEAR),
		PorterDuffXfermode(PorterDuff.Mode.SRC),
		PorterDuffXfermode(PorterDuff.Mode.DST),
		PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
		PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
		PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
		PorterDuffXfermode(PorterDuff.Mode.DST_IN),
		PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
		PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
		PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
		PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
		PorterDuffXfermode(PorterDuff.Mode.XOR),
		PorterDuffXfermode(PorterDuff.Mode.DARKEN),
		PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
		PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
		PorterDuffXfermode(PorterDuff.Mode.SCREEN)
	)

	private val _labels = arrayOf("Clear", "Src", "Dst", "SrcOver", "DstOver", "SrcIn", "DstIn",
		"SrcOut", "DstOut", "SrcATop", "DstAtop", "Xor", "Darken", "Lighten", "Multiply", "Screen")

	private val W = 256
	private val H = 256
	private val ROW_MAX = 4  // samples per row
	private val TEXT_SIZE = 32f

	private val _dst = makeDst(W, H)  // circle
	private val _src = makeSrc(W, H)  // square
	private val _backgorundChecker = makeBackgroundChacker()
}

package example.kotlin.jeromq.viewuserinput

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)

		canvas.drawRect(_origin[0] + 0f, _origin[1] + 0f, _origin[0] + _size * 0.1f, _origin[1] + _size * 0.1f, _paint)
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		_size = measuredWidth
		setMeasuredDimension(measuredWidth, measuredHeight)
	}

	override fun onTouchEvent(event: MotionEvent): Boolean {
		Log.d("CustomView",
			"action=${MotionEvent.actionToString(event.action)}, x=${event.getX(0)}, y=${event.getY(0)}")

		if (event.action == MotionEvent.ACTION_DOWN) {
			_down_pos = arrayOf(event.getX(0), event.getY(0))
			return true
		}
		else if (event.action == MotionEvent.ACTION_MOVE) {
			val pos = arrayOf(event.getX(0), event.getY(0))
			_origin = arrayOf(
				_origin[0] + pos[0] - _down_pos[0],
				_origin[1] + pos[1] - _down_pos[1])
			_down_pos = pos

			invalidate()  // redraw
		}

		return super.onTouchEvent(event)
	}

	private var _size = 0
	private var _origin = arrayOf(0f, 0f)  // x, y
	private val _paint = Paint(Paint.ANTI_ALIAS_FLAG)

	// move handling
	private var _down_pos = arrayOf(0f, 0f)  // x, y
}
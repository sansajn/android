package example.kotlin.customview

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View

class EmotionalFaceView(context: Context, attrs: AttributeSet) : View(context, attrs) {

	companion object {
		const val HAPPY = 0L
		const val SAD = 1L

		private const val DEFAULT_FACE_COLOR = Color.YELLOW
		private const val DEFAULT_EYES_COLOR = Color.BLACK
		private const val DEFAULT_MOUTH_COLOR = Color.BLACK
		private const val DEFAULT_BORDER_COLOR = Color.BLACK
		private const val DEFAULT_BORDER_WIDTH = 4.0f
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		drawFaceBackground(canvas)
		drawEyes(canvas)
		drawMouth(canvas)
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		_size = Math.min(measuredWidth, measuredHeight)
		setMeasuredDimension(_size, _size)
	}

	override fun onSaveInstanceState(): Parcelable {
		val bundle = Bundle()
		bundle.putLong("happinessState", happinessState)
		bundle.putParcelable("superState", super.onSaveInstanceState())
		return bundle
	}

	override fun onRestoreInstanceState(state: Parcelable) {
		var viewState = state
		if (viewState is Bundle) {
			happinessState = viewState.getLong("happinessState", HAPPY)
			viewState = viewState.getParcelable("superState")
		}
		super.onRestoreInstanceState(viewState)
	}

	private fun drawFaceBackground(canvas: Canvas) {
		_paint.color = _faceColor
		_paint.style = Paint.Style.FILL

		val radius = _size/2f
		canvas.drawCircle(_size/2f, _size/2f, radius, _paint)

		_paint.color = _borderColor
		_paint.style = Paint.Style.STROKE
		_paint.strokeWidth = _borderWidth

		canvas.drawCircle(_size/2f, _size/2f, radius - _borderWidth/2f, _paint)
	}

	private fun drawEyes(canvas: Canvas) {
		_paint.color = _eyesColor
		_paint.style = Paint.Style.FILL

		val leftEyeRect = RectF(_size * 0.32f, _size * 0.23f, _size * 0.43f, _size * 0.5f)
		canvas.drawOval(leftEyeRect, _paint)

		val rightEyeRect = RectF(_size * 0.57f, _size * 0.23f, _size * 0.68f, _size * 0.5f)
		canvas.drawOval(rightEyeRect, _paint)
	}

	private fun drawMouth(canvas: Canvas) {
		_mouthPath.reset()
		_mouthPath.moveTo(_size * 0.22f, _size * 0.7f)

		if (happinessState == HAPPY) {
			_mouthPath.quadTo(_size * 0.5f, _size * 0.8f, _size * 0.78f, _size * 0.7f)
			_mouthPath.quadTo(_size * 0.5f, _size * 0.9f, _size * 0.22f, _size * 0.7f)
		}
		else {
			_mouthPath.quadTo(_size * 0.5f, _size * 0.5f, _size * 0.78f, _size * 0.7f)
			_mouthPath.quadTo(_size * 0.5f, _size * 0.6f, _size * 0.22f, _size * 0.7f)
		}

		_paint.color = _mouthColor
		_paint.style = Paint.Style.FILL
		canvas.drawPath(_mouthPath, _paint)
	}

	private var _faceColor = DEFAULT_FACE_COLOR
	private var _eyesColor = DEFAULT_EYES_COLOR
	private var _mouthColor = DEFAULT_MOUTH_COLOR
	private var _borderColor = DEFAULT_BORDER_COLOR
	private var _borderWidth = DEFAULT_BORDER_WIDTH
	private val _paint = Paint(Paint.ANTI_ALIAS_FLAG)
	private val _mouthPath = Path()
	private var _size = 0

	var happinessState = HAPPY
		set(state) {
			field = state
			invalidate()
		}

	init {
		_paint.isAntiAlias = true
		setupAttributes(attrs)
	}

	private fun setupAttributes(attrs: AttributeSet?) {
		val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.EmotionalFaceView, 0, 0)

		happinessState = typedArray.getInt(R.styleable.EmotionalFaceView_state, HAPPY.toInt()).toLong()
		_faceColor = typedArray.getInt(R.styleable.EmotionalFaceView_faceColor, DEFAULT_FACE_COLOR)
		_eyesColor = typedArray.getInt(R.styleable.EmotionalFaceView_eyesColor, DEFAULT_EYES_COLOR)
		_mouthColor = typedArray.getInt(R.styleable.EmotionalFaceView_mouthColor, DEFAULT_MOUTH_COLOR)
		_borderColor = typedArray.getInt(R.styleable.EmotionalFaceView_borderColor, DEFAULT_BORDER_COLOR)
		_borderWidth = typedArray.getDimension(R.styleable.EmotionalFaceView_borderWidth, DEFAULT_BORDER_WIDTH)

		typedArray.recycle()
	}

}

package com.example.fxplot

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.androidplot.util.PixelUtils
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.SimpleXYSeries
import com.androidplot.xy.StepMode
import com.androidplot.xy.XYGraphWidget
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

open class PrimaryLineLabelRenderer : XYGraphWidget.LineLabelRenderer() {

	override fun drawLabel(canvas: Canvas?, text: String?, paint: Paint?, x: Float, y: Float, isOrigin: Boolean) {
		if (isOrigin) {
			val originPaint = Paint(paint)
			originPaint.color = Color.RED
			super.drawLabel(canvas, text, originPaint, x, y, isOrigin)
		}
		else
			super.drawLabel(canvas, text, paint, x, y, isOrigin)
	}
}

class SecondaryLineLabelRenderer : PrimaryLineLabelRenderer() {

	override fun drawLabel(canvas: Canvas?, style: XYGraphWidget.LineLabelStyle?, `val`: Number?, x: Float, y: Float, isOrigin: Boolean) {
		if (`val`!!.toDouble() % 2 == 0.0) {
			val paint = style!!.paint
			if (!isOrigin)
				paint.color = Color.GRAY
			super.drawLabel(canvas, style, `val`, x, y, isOrigin)
		}
	}
}

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1.0)
		plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 1.0)

		plot.centerOnDomainOrigin(0.0)
		plot.centerOnRangeOrigin(0.0)

		val domainFormatter = LineAndPointFormatter(this, R.xml.line_point_formatter)

		// makes origin labels red
		plot.graph.setLineLabelRenderer(XYGraphWidget.Edge.BOTTOM, PrimaryLineLabelRenderer())
		plot.graph.setLineLabelRenderer(XYGraphWidget.Edge.LEFT, PrimaryLineLabelRenderer())

		// set every other line for top and right edge labels
		plot.graph.setLineLabelRenderer(XYGraphWidget.Edge.RIGHT, SecondaryLineLabelRenderer())
		plot.graph.setLineLabelRenderer(XYGraphWidget.Edge.TOP, SecondaryLineLabelRenderer())

		// don't show decimal places for top and right edge labels
		plot.graph.getLineLabelStyle(XYGraphWidget.Edge.TOP).format = DecimalFormat("0")
		plot.graph.getLineLabelStyle(XYGraphWidget.Edge.RIGHT).format = DecimalFormat("0")

		// dash effect for domain and range grid lines
		val dash = DashPathEffect(floatArrayOf(PixelUtils.dpToPix(3.0f), PixelUtils.dpToPix(3.0f)), 0.0f)
		plot.graph.domainGridLinePaint.pathEffect = dash
		plot.graph.rangeGridLinePaint.pathEffect = dash

		plot.addSeries(generateSeries(-5.0, 5.0, 100.0), domainFormatter)
	}

	private fun generateSeries(minX: Double, maxX: Double, resolution: Double): SimpleXYSeries {
		val range = maxX - minX
		val step = range / resolution

		val xVals = ArrayList<Double>()
		val yVals = ArrayList<Double>()

		var x = minX
		while (x <= maxX) {
			xVals.add(x)
			yVals.add(fx(x))
			x += step
		}

		return SimpleXYSeries(xVals, yVals, "f(x) = (x^2) - 13")
	}

	private fun fx(x: Double): Double {
		return x*x-13.0
	}
}

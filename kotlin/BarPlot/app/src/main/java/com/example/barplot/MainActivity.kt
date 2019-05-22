package com.example.barplot

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.androidplot.util.PixelUtils
import com.androidplot.xy.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition

class Mapper(val domainMap: List<Int>) : Format() {
	override fun format(obj: Any?, toAppendTo: StringBuffer?, pos: FieldPosition?): StringBuffer {
		if (obj is Number) {
			val idx = Math.round(obj.toFloat())
			return toAppendTo!!.append(domainMap[idx])
		}
		else
			return StringBuffer("<NaN>")
	}

	override fun parseObject(source: String?, pos: ParsePosition?): Any {
		return Any()
	}
}

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val wins = SimpleXYSeries(listOf(3, 4, 5, 3, 2, 3, 5, 6, 2, 1, 3, 1), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "wins")
		val winFormat = BarFormatter(Color.GREEN, Color.BLACK)
		winFormat.marginRight = PixelUtils.dpToPix(1.0f)

		val losses = SimpleXYSeries(listOf(1, 2, 3, 1, 1, 2, 1, 3, 2, 1, 2, 1), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "losses")
		val lossFormat = BarFormatter(Color.RED, Color.BLACK)

		plot.addSeries(wins, winFormat)
		plot.addSeries(losses, lossFormat)

		val renderer = plot.getRenderer(BarRenderer::class.java)
		renderer.barOrientation = BarRenderer.BarOrientation.SIDE_BY_SIDE
		renderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_GAP, PixelUtils.dpToPix(5.0f))

		plot.setRangeBoundaries(0, 7, BoundaryMode.FIXED)
		plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 1.0)

		plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = Mapper(
			listOf(2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012))
	}
}

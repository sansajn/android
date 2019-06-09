package com.example.combinedplot

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.androidplot.util.PixelUtils
import com.androidplot.xy.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition

class Mapper(val domainMap: List<String>) : Format() {
	override fun format(obj: Any?, toAppendTo: StringBuffer?, pos: FieldPosition?): StringBuffer {
		if (obj is Number) {
			val idx = Math.round(obj.toFloat())
			if (idx < domainMap.size)
				return toAppendTo!!.append(domainMap[idx])
			else
				return toAppendTo!!.append("")
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

		// data
		val amount = listOf(134668.23, 159716.83, 161443.93, 164051.73, 173234.07, 201586.44, 244146.73,
			100719.34, 163916.13, 68780.22, 96239.34, 106392.94)

		val growth = listOf(1114.75, 25048.6, 1727.1, 2607.8, 9182.34, 28352.37, 42560.29, -143427.39,
			63196.79, -95135.91, 27459.12, 10153.6)

		plot.centerOnRangeOrigin(0.0)
		plot.setRangeBoundaries(-200000, 300000, BoundaryMode.FIXED)
		plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 100000.0)

		// amount
		val amountSeries = SimpleXYSeries(amount, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "amount")
		val amountFormatter = LineAndPointFormatter(this, R.xml.line_point_formatter)
		plot.addSeries(amountFormatter, amountSeries)

		plot.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT).format = DecimalFormat("0")
		plot.graph.domainGridLinePaint.color = Color.TRANSPARENT  // turn off domain grid line

		val domainLabels = listOf("Januar", "Februar", "Marec", "April", "Maj", "Jun", "Jul", "August", "September", "Oktober", "November", "December")
		plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1.0)
		plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = Mapper(domainLabels)

		// growth
		val growthSeries = SimpleXYSeries(growth, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"growth")
		val growthFormatter = BarFormatter(Color.RED, Color.BLACK)
		plot.addSeries(growthFormatter, growthSeries)

		val renderer = plot.getRenderer(BarRenderer::class.java)
		renderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_GAP, PixelUtils.dpToPix(20.0f))
	}
}

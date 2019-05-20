package com.example.androidplot

import android.graphics.Color
import android.graphics.DashPathEffect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.androidplot.util.PixelUtils
import com.androidplot.xy.CatmullRomInterpolator
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.SimpleXYSeries
import com.androidplot.xy.XYGraphWidget
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

		val series1 = SimpleXYSeries(listOf(1, 4, 2, 8, 4, 16, 8, 32, 16, 64), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1")
		val series2 = SimpleXYSeries(listOf(5, 2, 10, 5, 20, 10, 40, 20, 80, 40), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2")

		// formatters for drawing series
		val series1Format = LineAndPointFormatter(Color.parseColor("#00AA00"), Color.parseColor("#007700"), Color.parseColor("#00000000"), null)
		series1Format.setInterpolationParams(CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal))
		series1Format.linePaint.strokeWidth = PixelUtils.dpToPix(5.0f)
		series1Format.vertexPaint.strokeWidth = PixelUtils.dpToPix(20.0f)
		series1Format.pointLabelFormatter.textPaint.color = Color.parseColor("#CCCCCC")

		val series2Format = LineAndPointFormatter(Color.parseColor("#0000AA"), Color.parseColor("#000099"), Color.parseColor("#00000000"), null)
		series2Format.setInterpolationParams(CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal))
		series2Format.linePaint.strokeWidth = PixelUtils.dpToPix(5.0f)
		series2Format.linePaint.pathEffect = DashPathEffect(floatArrayOf(PixelUtils.dpToPix(20.0f), PixelUtils.dpToPix(15.0f)), 0.0f)
		series2Format.vertexPaint.strokeWidth = PixelUtils.dpToPix(20.0f)
		series2Format.pointLabelFormatter.textPaint.color = Color.parseColor("#CCCCCC")

		plot.addSeries(series1, series1Format)
		plot.addSeries(series2, series2Format)

		plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = Mapper(listOf(1, 2, 3, 6, 7, 8, 9, 10, 13, 14))
	}
}

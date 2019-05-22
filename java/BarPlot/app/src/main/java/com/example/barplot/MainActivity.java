package com.example.barplot;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		XYSeries wins = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "wins", 3, 4, 5, 3, 2, 3, 5, 6, 2, 1, 3, 1);
		XYSeries losses = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "losses", 1, 2, 1, 1, 1, 4, 3, 1, 2, 1, 1, 1);
		final int[] domainLabels = {2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012};

		BarFormatter winsFormatter = new BarFormatter(Color.GREEN, Color.BLACK);
		winsFormatter.setMarginRight(PixelUtils.dpToPix(1));

		XYPlot plot = findViewById(R.id.plot);
		plot.addSeries(wins, winsFormatter);

		BarFormatter lossesFormatter = new BarFormatter(Color.RED, Color.BLACK);
		plot.addSeries(losses, lossesFormatter);

		BarRenderer renderer = plot.getRenderer(BarRenderer.class);
		renderer.setBarOrientation(BarRenderer.BarOrientation.SIDE_BY_SIDE);
		renderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_GAP, PixelUtils.dpToPix(5));

		plot.setRangeBoundaries(0, 7, BoundaryMode.FIXED);
		plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 1);

		plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
				int idx = Math.round(((Number)obj).floatValue());
				return toAppendTo.append(domainLabels[idx]);
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;
			}
		});
	}
}

package example.kotlin.preferences

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		supportFragmentManager.beginTransaction()
			.replace(R.id.content, SettingsFragment())
			.commit()
	}

	override fun onResume() {
		super.onResume()

		val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
	}
}

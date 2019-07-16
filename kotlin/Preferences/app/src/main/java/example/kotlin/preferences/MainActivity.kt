package example.kotlin.preferences

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast

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
		val zipCode = sharedPref.getString(getString(R.string.pref_key_zipcode), "")
		// do something with zipCode ...
	}
}

package example.kotlin.actionsend

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		when {
			intent?.action == Intent.ACTION_SEND -> {
				if (intent.type.startsWith("text/"))
					handleSendText(intent)
			}
		}
	}

	private fun handleSendText(intent: Intent) {
		intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
			runOnUiThread { hello_text.text = it }
		}
	}
}

package example.kotlin.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class MainViewModel : ViewModel() {

	fun initNetworkRequest() {  // the expensive operation
		_username.value = "John"
	}

	fun getUsername(): LiveData<String> {
		return _username
	}

	private val _username = MutableLiveData<String>()
}

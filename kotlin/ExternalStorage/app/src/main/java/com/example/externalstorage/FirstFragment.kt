package com.example.externalstorage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.externalstorage.databinding.FragmentFirstBinding
import java.io.File

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

	private var _binding: FragmentFirstBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {

		_binding = FragmentFirstBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.buttonFirst.setOnClickListener {
			findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
		}

		// check for external storage permissions
		if (checkPermission()) {
			Log.d("test", "external storage permission already granted")
			showGalleryPhotos()
		}
		else {
			Log.d("test", "external storage permission not granted, request")
			requestPermission()
		}
	}

	private fun showGalleryPhotos() {
		val photos = listGallery()
		binding.textviewFirst.text = photos.joinToString("\n")
	}

	private fun listGallery(): ArrayList<String> {
		val galleryFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		Log.d("test", "gallery=${galleryFolder.absolutePath}")

		// TODO: make this function better!!
		val result = arrayListOf<String>()
		File(galleryFolder.absolutePath).walk().forEach {
			Log.d("test", it.absolutePath) // TODO: fill text view instead of log
			result.add(it.absolutePath)
		}

		return result
	}

	private fun requestPermission() {
		Log.d("test", "requestPermission(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)")
		val intent = Intent()
		intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
		storageActivityResultLauncher.launch(intent)
	}

	// this is called after external storage access granted
	private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
		if (checkPermission()) {
			Log.d("test", "external storage permission granted")
			showGalleryPhotos()
		}
		else {
			Log.d("test", "external storage permission denied")
		}
	}

	private fun checkPermission(): Boolean {
		return Environment.isExternalStorageManager()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
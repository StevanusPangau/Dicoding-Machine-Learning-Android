package com.learn.cancerapp.ui.analysis

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.learn.cancerapp.R
import com.learn.cancerapp.data.history.ViewModelFactory
import com.learn.cancerapp.data.history.entity.HistoryEntity
import com.learn.cancerapp.databinding.FragmentAnalysisBinding
import com.learn.cancerapp.ui.history.HistoryViewModel
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AnalysisFragment : Fragment() {

    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyViewModel: HistoryViewModel

    private var currentImageUri: Uri? = null

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri = result.data?.data
            selectedImageUri?.let {
                currentImageUri = it
                performCrop(it)
            } ?: showToast(getString(R.string.empty_image_warning))
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        val root: View = binding.root

        historyViewModel = obtainViewModel(this.requireActivity() as AppCompatActivity)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage()
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startGallery() {
        val pickIntent = Intent(Intent.ACTION_GET_CONTENT)
        pickIntent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        launcherGallery.launch(pickIntent)
    }

    private fun performCrop(uri: Uri) {
        val destinationFileName = "cropped_image.jpg"

        val options = UCrop.Options().apply {
            setCompressionQuality(80)
            setToolbarTitle("Crop Gambar")
        }

        UCrop.of(uri, Uri.fromFile(File(requireContext().cacheDir, destinationFileName)))
            .withOptions(options)
            .start(requireContext(), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                currentImageUri = it
                showImage()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val error = UCrop.getError(data!!)
            error?.let {
                showToast("Error: ${error.localizedMessage}")
            }
        }
    }

    private fun showImage() {
        binding.previewImageView.setImageDrawable(null)
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        currentImageUri?.let { uri ->
            val imageClassifierHelper = ImageClassifierHelper(
                context = requireContext(),
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        showToast(error)
                    }

                    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                        results?.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }

                                val displayResult =
                                    sortedCategories.first().let {
                                        "${it.label} " + NumberFormat.getPercentInstance()
                                            .format(it.score).trim()
                                    }

                                // Menyimpan gambar ke penyimpanan lokal
                                val savedImageUri = saveImageToStorage(uri)

                                // Simpan hasil analisis ke dalam database
                                val historyEntity = HistoryEntity(
                                    hasilPrediksi = displayResult,
                                    gambar = savedImageUri.toString(),
                                    tanggal = System.currentTimeMillis()
                                )


                                historyViewModel.insert(historyEntity)

                                moveToResult(displayResult)
                            } else {
                                showToast(getString(R.string.empty_image_warning))
                            }
                        }
                    }
                }
            )
            imageClassifierHelper.classifyStaticImage(uri)
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun moveToResult(results: String) {
        val intent = Intent(requireContext(), ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
        intent.putExtra(ResultActivity.EXTRA_RESULT, results)
        startActivity(intent)
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryViewModel::class.java)
    }

    private fun saveImageToStorage(imageUri: Uri): Uri {
        val sourceFile = File(imageUri.path!!)

        // Membuat nama file yang unik dengan timestamp
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "image_$timeStamp.jpg"

        val destinationFile = File(requireContext().filesDir, fileName)

        sourceFile.copyTo(destinationFile)

        return Uri.fromFile(destinationFile)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
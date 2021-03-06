package com.example.shows_kolegakolega

import android.Manifest
import android.content.Context
import android.net.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.shows_kolegakolega.data.FileUtil
import com.example.shows_kolegakolega.data.preparePrmissionsContract
import com.example.shows_kolegakolega.database.ShowEntity
import com.example.shows_kolegakolega.databinding.ActivityShowsBinding
import com.example.shows_kolegakolega.databinding.DialogUserInfoBinding
import com.example.shows_kolegakolega.model.Show
import com.example.shows_kolegakolega.networking.NetworkChecker
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.streams.toList
import androidx.appcompat.app.AlertDialog as AlertDialog


class ShowsFragment : Fragment() {

    companion object {
        private const val EMAIL = "email"
        private const val ID = "id"
        private const val IMAGE = "image"
    }

    private var _binding: ActivityShowsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var bottomSheetBinding: DialogUserInfoBinding

    private val viewModel: ShowsViewModel by viewModels{
        ShowsViewModelFactory((activity?.application as ShowsKolegaKolegaApp).showsDatabase)
    }

    private val permissionForTakePhoto = preparePrmissionsContract(onPermissionsGranted = {
        openCamera()
    })

    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()){ succes ->
        if(succes){
            val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
            val email = prefs?.getString(EMAIL, "No name")
            val id = prefs?.getInt(ID, 0)
            FileUtil.getImageFile(this.context)?.let {
                if (id != null && email != null) {
                    viewModel.updateImage(id,email, it)
                }
            }
        }
    }

    private fun updateBottomSheetPhoto() {
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val imageUrl = prefs?.getString(IMAGE, null)
        if (imageUrl != null) {
            Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(bottomSheetBinding.profilePhoto)
        }
    }

    private fun updateUserPhoto(){
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val imageUrl = prefs?.getString(IMAGE, null)
        if (imageUrl != null) {
            Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.logOutButton)
        }

    }

    private fun openCamera() {

        val photoFile = this.context?.let { FileUtil.createImageFile(it) }

        var photoUri = getPhotoUri(photoFile)

        if(photoUri != null){
            getCameraImage.launch(photoUri)
        }
    }

    private fun getPhotoUri(photoFile: File?): Uri? {
        var photoUri: Uri? = null
        photoFile?.also {
            photoUri = this.context?.let { it1 ->
                FileProvider.getUriForFile(
                    it1,
                    "com.infinum.academy.shows.fileprovider",
                    it
                )
            }
        }
        return photoUri
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityShowsBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getShowsLiveData().observe(viewLifecycleOwner, {shows ->
            if(!shows.isNullOrEmpty()){
                binding.showsRecycler.isVisible = true
                binding.camera.isVisible = false
                binding.textCamera.isVisible = false
                initRecyclerView(shows)
            }else{
                binding.showsRecycler.isVisible = false
                binding.camera.isVisible = true
                binding.textCamera.isVisible = true
                Toast.makeText(this.context, "Fetching tv shows failed", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.getUserLiveData().observe(viewLifecycleOwner){user ->
            if (user != null) {

                with(activity?.getPreferences(Context.MODE_PRIVATE)?.edit()){
                    this?.putString(IMAGE, user.imageUrl)
                    this?.apply()
                }
                updateUserPhoto()
            }else{
                Toast.makeText(this.context, "Fetching user failed", Toast.LENGTH_SHORT).show()
            }
        }

        val networkChecker = this.context?.let { NetworkChecker(it) }
        if (networkChecker != null) {
            if(networkChecker.isOnline()){
                viewModel.getShows()
            }else{
                viewModel.getShowsFromDatabase().observe(viewLifecycleOwner){shows ->
                    if(shows.isNullOrEmpty()){
                        binding.showsRecycler.isVisible = false
                        binding.camera.isVisible = true
                        binding.textCamera.isVisible = true
                    }else{
                        binding.showsRecycler.isVisible = true
                        binding.camera.isVisible = false
                        binding.textCamera.isVisible = false

                        var listOfShows = emptyList<Show>()
                        for(s in shows){
                            listOfShows = listOfShows + getShowFromShowEntity(s)
                        }
                        initRecyclerView(listOfShows)
                    }
                }
            }
        }
        initButtonForEmptyState()
        updateUserPhoto()
        initUserInfo()
    }

    private fun initUserInfo() {
        binding.logOutButton.setOnClickListener {
            val dialog = this.context?.let { BottomSheetDialog(it) }

            bottomSheetBinding = DialogUserInfoBinding.inflate(layoutInflater)
            bottomSheetBinding.root.let { it1 -> dialog?.setContentView(it1) }

            updateBottomSheetPhoto()

            bottomSheetBinding.logOutButton.setOnClickListener {
                val alertDialog = this.context?.let { it1 -> AlertDialog.Builder(it1) }

                alertDialog?.setTitle("Log out")

                alertDialog?.setMessage("Do you want to log out?")

                alertDialog?.setPositiveButton("Yes") { _, _ ->
                    with(activity?.getPreferences(Context.MODE_PRIVATE)?.edit()){
                        this?.clear()
                        this?.commit()
                    }
                    findNavController().navigate(R.id.shows_to_login)
                    dialog?.dismiss()
                }
                alertDialog?.setNegativeButton("No"){_,_ -> Unit}

                alertDialog?.create()?.show()

            }

            bottomSheetBinding.changeProfilePhotoButton.setOnClickListener {
                dialog?.dismiss()
                permissionForTakePhoto.launch(arrayOf(Manifest.permission.CAMERA))
            }

            val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
            val email = prefs?.getString(EMAIL, "No name")
            bottomSheetBinding.userEmail.text = email

            dialog?.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initButtonForEmptyState() {
        binding.showEmptyButton.setOnClickListener {
            val recyclerVisibility = binding.showsRecycler.isVisible
            binding.showsRecycler.isVisible = binding.camera.isVisible
            binding.camera.isVisible = recyclerVisibility
            binding.textCamera.isVisible = recyclerVisibility
        }
    }

    private fun initRecyclerView(shows: List<Show>) {
       binding.showsRecycler.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        binding.showsRecycler.adapter = ShowsAdapter(shows){
            val action = ShowsFragmentDirections.showsToDetails(it.id)
            findNavController().navigate(action)
        }

    }

    private fun getShowFromShowEntity(se: ShowEntity): Show{
        return Show(
            se.id,
            se.averageRating,
            se.description,
            se.imageUrl,
            se.noOfReviews,
            se.title
        )
    }

//    fun isOnline(): Boolean {
//        val connectivityManager =
//            activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (connectivityManager != null) {
//            val capabilities =
//                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
//            if (capabilities != null) {
//                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
//                    return true
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
//                    return true
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
//                    return true
//                }
//            }
//        }
//        return false
//    }
}
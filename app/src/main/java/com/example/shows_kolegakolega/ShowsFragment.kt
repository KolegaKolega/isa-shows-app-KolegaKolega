package com.example.shows_kolegakolega

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.shows_kolegakolega.databinding.ActivityShowsBinding
import com.example.shows_kolegakolega.databinding.DialogUserInfoBinding
import com.example.shows_kolegakolega.model.Show
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import androidx.appcompat.app.AlertDialog as AlertDialog


class ShowsFragment : Fragment() {

    companion object {
        private const val EMAIL = "EMAIL"
    }

    private var _binding: ActivityShowsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var bottomSheetBinding: DialogUserInfoBinding

    private val viewModel: ShowsViewModel by viewModels()

    private val permissionForTakePhoto = preparePrmissionsContract(onPermissionsGranted = {
        openCamera()
    })

    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()){ succes ->
        if(succes){
            updateUserPhoto()
            updateBotomSheetPhoto()
        }
    }

    private fun updateBotomSheetPhoto() {
        if(FileUtil.getImageFile(this.context) != null){
            Glide.with(this)
                .load(getPhotoUri(FileUtil.getImageFile(this.context)))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(bottomSheetBinding.profilePhoto)

        }else{
            bottomSheetBinding.profilePhoto.setImageResource(R.drawable.ic_profile_placeholder)
        }

    }

    private fun updateUserPhoto(){
        if(FileUtil.getImageFile(this.context) != null){
            Glide.with(this)
                .load(getPhotoUri(FileUtil.getImageFile(this.context)))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.logOutButton)

        }else{
            binding.logOutButton.setImageResource(R.drawable.ic_profile_placeholder)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initShows()
        viewModel.getShowsLiveData().observe(viewLifecycleOwner, {shows ->
            initRecyclerView(shows)
        })
        initButtonForEmptyState()
        updateUserPhoto()
        initUserInfo()

    }

    private fun initUserInfo() {
        binding.logOutButton.setOnClickListener {
            val dialog = this.context?.let { BottomSheetDialog(it) }

            bottomSheetBinding = DialogUserInfoBinding.inflate(layoutInflater)
            bottomSheetBinding.root.let { it1 -> dialog?.setContentView(it1) }

            updateBotomSheetPhoto()

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
        binding.showEmpty.setOnClickListener {
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
}
package com.example.shows_kolegakolega

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.shows_kolegakolega.databinding.ActivityShowDetailsBinding
import com.example.shows_kolegakolega.databinding.DialogAddReviewBinding
import com.example.shows_kolegakolega.model.Review
import com.example.shows_kolegakolega.model.Show
import com.example.shows_kolegakolega.model.User
import com.example.shows_kolegakolega.networking.NetworkChecker
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    companion object {
        private const val EMAIL = "email"
        private const val ID = "id"
        private const val IMAGE = "image"
        private const val TEMP_ID = "tempId"
    }

    private var reviewAdapter: ReviewAdapter? = null

    private var _binding: ActivityShowDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args: ShowDetailsFragmentArgs by navArgs()

    private val viewModel: ShowDetailsViewModel by viewModels {
        ShowDetailsViewModelFactory((activity?.application as ShowsKolegaKolegaApp).showsDatabase)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSingleShowLiveData().observe(viewLifecycleOwner) { show ->
            if (show != null) {
                initLayout(show)
            } else {
                Toast.makeText(this.context, "Fetching tv show failed", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getReviewsLiveData().observe(viewLifecycleOwner) { reviews ->
            if (!reviews.isNullOrEmpty()) {
                initRecyclerView(reviews)
                binding.noReviweYet.isVisible = false
                binding.reviewRecyclerView.isVisible = true
                binding.average.isVisible = true
                binding.ratingBar.isVisible = true
                viewModel.getReviewsFromDatabase().observe(viewLifecycleOwner){
                    for(reviewEntity in it){
                        if(reviewEntity.id == TEMP_ID){
                            viewModel.createReview(
                                reviewEntity.rating,
                                reviewEntity.comment,
                                reviewEntity.showId
                            )
                            viewModel.deleteReviewInDatabase(reviewEntity)
                        }
                    }

                }
            } else {
                Toast.makeText(this.context, "Fetching reviews failed", Toast.LENGTH_SHORT).show()
            }

        }
        viewModel.getCreateReviewResultLiveData().observe(viewLifecycleOwner) { succes ->
            if (succes) {
                viewModel.getShow(args.showId)
                viewModel.getReviews(args.showId.toInt())
            } else {
                Toast.makeText(this.context, "Creating review failed", Toast.LENGTH_SHORT).show()
            }
        }

        val networkChecker = this.context?.let { NetworkChecker(it) }
        if (networkChecker != null) {
            if (networkChecker.isOnline()) {
                viewModel.getShow(args.showId)
                viewModel.getReviews(args.showId.toInt())
            } else {
                viewModel.getShowFromDatabase(args.showId).observe(viewLifecycleOwner) { showEntity ->
                    if (showEntity != null) {
                        initLayout(
                            Show(
                                showEntity.id,
                                showEntity.averageRating,
                                showEntity.description,
                                showEntity.imageUrl,
                                showEntity.noOfReviews,
                                showEntity.title
                            )
                        )
                    }else{
                        Toast.makeText(this.context, "Fetching tv show failed", Toast.LENGTH_SHORT).show()
                    }
                }
                viewModel.getReviewsFromDatabase().observe(viewLifecycleOwner){ reviewEntities ->
                    if(!reviewEntities.isNullOrEmpty()){
                        initRecyclerView(reviewEntities
                            .filter { re -> re.showId.toString() == args.showId }
                            .map {
                            Review(
                                it.id,
                                it.comment,
                                it.rating,
                                it.showId,
                                it.user
                            )
                        })
                        binding.noReviweYet.isVisible = false
                        binding.reviewRecyclerView.isVisible = true
                        binding.average.isVisible = true
                        binding.ratingBar.isVisible = true
                    }

                }
            }
        }
        intBackButton()
        initAddReviewButton()
    }

    private fun initLayout(show: Show) {
        binding.showTitle.text = show.title
        binding.showDescription.text = show.description
        Glide.with(this)
            .load(show.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.showImage)
        binding.average.text = "${show.noOfReviews.toString()} Reviews, " +
                "${show.averageRating.toString()} Average"
        if (show.averageRating != null)
            binding.ratingBar.rating = show.averageRating
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView(reviews: List<Review>) {
        binding.reviewRecyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        reviewAdapter = ReviewAdapter(reviews)
        binding.reviewRecyclerView.adapter = reviewAdapter
    }

    private fun initAddReviewButton() {
        binding.addReviweButton.setOnClickListener {
            addReviewBottomSheet()
        }
    }

    private fun addReviewBottomSheet() {
        val dialog = this.context?.let { BottomSheetDialog(it) }

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog?.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.submit.setOnClickListener {
            val networkChecker = this.context?.let { NetworkChecker(it) }
            if (networkChecker != null) {
                if(networkChecker.isOnline()){
                    viewModel.createReview(
                        bottomSheetBinding.ratingBar.rating.toInt(),
                        bottomSheetBinding.comment.editText?.text.toString(),
                        args.showId.toInt()
                    )
                }else{
                    val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
                    val id = prefs?.getInt(ID, 0)
                    val email = prefs?.getString(EMAIL, "No name")
                    val imageUrl = prefs?.getString(IMAGE, null)
                    if(id != null && email != null){
                        viewModel.createReviewInDataBase(
                            bottomSheetBinding.ratingBar.rating.toInt(),
                            bottomSheetBinding.comment.editText?.text.toString(),
                            args.showId.toInt(),
                            User(
                                id, email, imageUrl
                            )
                        )
                    }

                }
            }


            dialog?.dismiss()
        }

        bottomSheetBinding.exit.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.show()
    }

    private fun intBackButton() {
        binding.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
package com.example.shows_kolegakolega

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shows_kolegakolega.data.DemoData
import com.example.shows_kolegakolega.databinding.ActivityShowDetailsBinding
import com.example.shows_kolegakolega.databinding.DialogAddReviewBinding
import com.example.shows_kolegakolega.model.Review
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    companion object {
        private const val EMAIL = "EMAIL"
    }

    private var reviewAdapter: ReviewAdapter? = null

    private var _binding: ActivityShowDetailsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val args: ShowDetailsFragmentArgs by navArgs()

    private val viewModel: ShowDetailsViewModel by viewModels()

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
        initLayout(args.showId)
        intBackButton()
        initAddReviewButton()
        viewModel.initReviews(args.showId)
        checkReviews()
        viewModel.getReviewsLiveData().observe(viewLifecycleOwner, { reviews ->
            initRecyclerView(reviews)
        })

    }

    private fun checkReviews() {
        if(viewModel.countReviews() > 0){
            binding.noReviweYet.isVisible = false
            binding.reviewRecyclerView.isVisible = true
            binding.average.isVisible = true
            binding.ratingBar.isVisible = true
            binding.average.text = "${viewModel.countReviews()} Reviews, ${viewModel.getAverage()} Average"
            binding.ratingBar.rating = viewModel.getAverage()
        }
    }

    private fun initLayout(showId: String) {
        binding.showTitle.text = DemoData.getShowById(showId).name
        binding.showDescription.text = DemoData.getShowById(showId).description
        binding.showImage.setImageResource(DemoData.getShowById(showId).image)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRecyclerView(reviews: List<Review>) {
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

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
            val username = getUserName()
            val review = username?.let { it1 ->
                Review(
                    it1, bottomSheetBinding.comment.editText?.text.toString(),
                    bottomSheetBinding.ratingBar.rating.toInt())
            }
            if (review != null) {
                viewModel.addReview(review)
            }

            checkReviews()
            dialog?.dismiss()
        }

        bottomSheetBinding.exit.setOnClickListener{
            dialog?.dismiss()
        }

        dialog?.show()
    }

    private fun getUserName(): String? {
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val userName = prefs?.getString(EMAIL, "No name")
        return userName?.let { userName.substring(0, it.indexOf("@")) }
    }

    private fun intBackButton() {
        binding.toolbarBack.setOnClickListener {
            findNavController().navigate(R.id.details_to_shows)
        }
    }

}
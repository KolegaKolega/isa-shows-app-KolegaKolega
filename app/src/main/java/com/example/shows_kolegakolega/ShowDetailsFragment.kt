package com.example.shows_kolegakolega

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shows_kolegakolega.databinding.ActivityShowDetailsBinding
import com.example.shows_kolegakolega.databinding.DialogAddReviewBinding
import com.example.shows_kolegakolega.model.Review
import com.example.shows_kolegakolega.model.Show
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    private var reviews = emptyList<Review>()

    private var reviewAdapter: ReviewAdapter? = null

    private var _binding: ActivityShowDetailsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val args: ShowDetailsFragmentArgs by navArgs()

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
        initLayout(args.showName, args.showDescription, args.showImage)
        intBackButton()
        initAddReviewButton()
        initRecyclerView()
    }

    private fun initLayout(showName: String, showDescription: String, showImage: Int) {
        binding.showTitle.text = showName
        binding.showDescription.text = showDescription
        binding.showImage.setImageResource(showImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRecyclerView() {
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
            val review = Review("imenko.prezimenkovic", bottomSheetBinding.comment.editText?.text.toString(),
                bottomSheetBinding.ratingBar.rating.toInt())
            reviewAdapter?.addItem(review)

            binding.average.isVisible = true
            binding.ratingBar.isVisible = true

            binding.average.text = "${reviewAdapter?.itemCount} Reviews, ${reviewAdapter?.getAverage()} Average"
            if (reviewAdapter != null) {
                binding.ratingBar.rating = reviewAdapter!!.getAverage()
            }

            binding.noReviweYet.isVisible = false
            binding.reviewRecyclerView.isVisible = true
            dialog?.dismiss()
        }

        bottomSheetBinding.exit.setOnClickListener{
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
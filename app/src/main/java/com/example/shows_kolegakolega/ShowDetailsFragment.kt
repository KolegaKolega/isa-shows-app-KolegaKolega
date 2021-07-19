package com.example.shows_kolegakolega

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shows_kolegakolega.databinding.ActivityShowDetailsBinding
import com.example.shows_kolegakolega.databinding.ActivityShowsBinding
import com.example.shows_kolegakolega.databinding.DialogAddReviewBinding
import com.example.shows_kolegakolega.model.Review
import com.example.shows_kolegakolega.model.Show
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    companion object {
        private const val EXTRA_SHOW_NAME : String = "EXTRA_SHOW_NAME"
        private const val EXTRA_SHOW_DES : String = "EXTRA_SHOW_DES"
        private const val EXTRA_SHOW_IMAGE : String = "EXTRA_SHOW_IMAGE"

        fun buildIntent(activity : Activity, show : Show) : Intent {
            val intent = Intent(activity, ShowDetailsFragment::class.java)
            intent.putExtra(EXTRA_SHOW_NAME, show.name)
            intent.putExtra(EXTRA_SHOW_DES, show.description)
            intent.putExtra(EXTRA_SHOW_IMAGE, show.image.toString())
            return intent
        }

    }

    private var reviews = emptyList<Review>()

    private var reviewAdapter: ReviewAdapter? = null

    private var _binding: ActivityShowDetailsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //val args: ActivityLoginBinding by navArgs()

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
        initLayout()
        intBackButton()
        initAddReviewButton()
        initRecyclerViwe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRecyclerViwe() {
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        reviewAdapter = ReviewAdapter(reviews)
        binding.reviewRecyclerView.adapter = reviewAdapter

    }

    private fun initAddReviewButton() {
        binding.addReviweButton.setOnClickListener {
            addReviewBottomSheet()
        }
    }

    private fun addReviewBottomSheet() {
        //val dialog = BottomSheetDialog(this)

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

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
            dialog.dismiss()
        }

        bottomSheetBinding.exit.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun intBackButton() {
        binding.toolbarBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initLayout() {
        binding.showTitle.text = intent.extras?.getString(EXTRA_SHOW_NAME)
        binding.showDescription.text = intent.extras?.getString(EXTRA_SHOW_DES)
        val img = intent.extras?.getString(EXTRA_SHOW_IMAGE)
        if (img != null) {
            binding.showImage.setImageResource( img.toInt() )
        }
    }
}
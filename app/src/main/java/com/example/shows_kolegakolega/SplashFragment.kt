package com.example.shows_kolegakolega

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shows_kolegakolega.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    companion object {
        private const val REMEMBER_ME = "Remember_me"
    }

    private var _binding: FragmentSplashBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.shows.visibility = View.GONE
        setAnimation()
    }

    private fun setAnimation() {
        val animator = ObjectAnimator.ofFloat(binding.logo, "translationY",  -400f,-400f,-300f, 0f)

        animator.doOnEnd {
            startShowsAnimation()
        }
        animator.duration = 1000
        animator.interpolator = BounceInterpolator()
        animator.start()
    }

    private fun startShowsAnimation() {
        binding.shows.visibility = View.VISIBLE
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            binding.shows,
            PropertyValuesHolder.ofFloat("scaleX", 0.5f,2.5f, 1f),
            PropertyValuesHolder.ofFloat("scaleY",  0.5f, 2.5f, 1f)
        )

        animator.interpolator = OvershootInterpolator()
        animator.duration = 1000
        animator.doOnEnd {
            goOnSecondScreen()
        }
        animator.start()

    }

    private fun goOnSecondScreen() {
        Thread.sleep(2000)
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val alreadyLoggedIn = prefs?.getBoolean(REMEMBER_ME, false)
        if(alreadyLoggedIn == true){
            findNavController().navigate(R.id.splash_to_shows)
        }else{
            findNavController().navigate(R.id.splash_to_login)
        }
    }
}
package me.shahim.booksy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import me.shahim.booksy.R
import me.shahim.booksy.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _binding?.viewModel = homeViewModel
        val root: View = binding.root
        observe()
        return root
    }

    private fun observe() {
        homeViewModel.greeting.observe(viewLifecycleOwner, Observer { greeting ->
            binding.nameGreeting.text = when (greeting) {
                HomeViewModel.MORNING -> getString(R.string.home_greeting_morning)
                HomeViewModel.AFTERNOON -> getString(R.string.home_greeting_noon)
                else -> getString(R.string.home_greeting_evening)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
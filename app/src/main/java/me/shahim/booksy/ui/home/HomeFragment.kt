package me.shahim.booksy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.animation.SlideInBottomAnimation
import me.shahim.booksy.R
import me.shahim.booksy.data.model.Book
import me.shahim.booksy.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mAdapter: BookListQuickAdapter

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
        init()
        observe()
        return root
    }

    private fun init() {
        initAdapter()
    }

    private fun observe() {
        homeViewModel.greeting.observe(viewLifecycleOwner, Observer { greeting ->
            binding.nameGreeting.text = when (greeting) {
                HomeViewModel.MORNING -> getString(R.string.home_greeting_morning)
                HomeViewModel.AFTERNOON -> getString(R.string.home_greeting_noon)
                else -> getString(R.string.home_greeting_evening)
            }
        })

        homeViewModel.allBooks.observe(viewLifecycleOwner) {
            mAdapter.setDiffNewData(it.toMutableList())
        }
    }

    private fun initAdapter() {
        binding.recyclerView.apply {
            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = manager

            setHasFixedSize(true)
            isNestedScrollingEnabled = true
            mAdapter = BookListQuickAdapter(mutableListOf())
            adapter = mAdapter
        }

        mAdapter.apply {
            animationEnable = false
            adapterAnimation = SlideInBottomAnimation()
            isAnimationFirstOnly = true

            setDiffCallback(BookListQuickAdapter.DiffCallback())

            setOnItemClickListener { adapter, view, position ->
                val book = adapter.getItem(position) as Book
                val action =
                    HomeFragmentDirections.actionNavigationHomeToBookDetailFragment(book.id)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
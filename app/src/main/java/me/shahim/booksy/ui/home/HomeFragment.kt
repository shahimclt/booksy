package me.shahim.booksy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.animation.SlideInBottomAnimation
import me.shahim.booksy.R
import me.shahim.booksy.data.model.Book
import me.shahim.booksy.databinding.FragmentHomeBinding
import me.shahim.booksy.databinding.ListBookItemBinding
import me.shahim.booksy.ui.bookshelf.BookListViewModel

class HomeFragment : Fragment() {

    private val bookListViewModel: BookListViewModel by activityViewModels()
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _binding?.viewModel = bookListViewModel
        val root: View = binding.root
        init()
        observe()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun init() {
        initAdapter()
    }

    fun scrollToTopIfSearching() {
        if(binding.homeFilter.hasFocus()) {
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }

    private fun observe() {
        bookListViewModel.greeting.observe(viewLifecycleOwner, Observer { greeting ->
            binding.nameGreeting.text = when (greeting) {
                BookListViewModel.MORNING -> getString(R.string.home_greeting_morning)
                BookListViewModel.AFTERNOON -> getString(R.string.home_greeting_noon)
                else -> getString(R.string.home_greeting_evening)
            }
        })

        mAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() { scrollToTopIfSearching() }
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) { scrollToTopIfSearching() }
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) { scrollToTopIfSearching() }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) { scrollToTopIfSearching() }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) { scrollToTopIfSearching() }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) { scrollToTopIfSearching() }
        })

        bookListViewModel.filteredBooks.observe(viewLifecycleOwner) {
            mAdapter.setDiffNewData(it.toMutableList())

            binding.recyclerView.apply {
                if (adapter != mAdapter) {
                    adapter = mAdapter
                }
            }
        }
    }

    private fun initAdapter() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(false)
        }

        mAdapter = BookListQuickAdapter(mutableListOf())

        mAdapter.apply {
            animationEnable = true
            adapterAnimation = SlideInBottomAnimation()
            isAnimationFirstOnly = true
//            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            setDiffCallback(BookListQuickAdapter.DiffCallback())

            setOnItemClickListener { adapter, view, position ->
                val book = adapter.getItem(position) as Book
                val action = HomeFragmentDirections.actionNavigationHomeToBookDetailFragment(book.id,book.title,book.author,book.coverImage)
                val coverView = view.findViewById(R.id.book_cover) as View
                val titleView = view.findViewById(R.id.bookName) as View
                val extras = FragmentNavigator.Extras.Builder()
                    .addSharedElements(
                        mapOf(
                            coverView to coverView.transitionName,
                            titleView to titleView.transitionName
                        )
                    ).build()
                findNavController().navigate(action,extras)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
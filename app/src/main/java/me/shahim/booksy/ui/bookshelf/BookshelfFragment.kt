package me.shahim.booksy.ui.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.animation.SlideInBottomAnimation
import me.shahim.booksy.R
import me.shahim.booksy.data.model.Book
import me.shahim.booksy.databinding.FragmentBookshelfBinding
import me.shahim.booksy.ui.home.BookListQuickAdapter
import me.shahim.booksy.ui.home.HomeFragmentDirections

class BookshelfFragment : Fragment() {

    private val bookListViewModel: BookListViewModel by activityViewModels()
    private var _binding: FragmentBookshelfBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mAdapter: BookGridQuickAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookshelfBinding.inflate(inflater, container, false)
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

    private fun observe() {

        bookListViewModel.ownedBooks.observe(viewLifecycleOwner) {
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
            layoutManager = GridLayoutManager(context,3)
            setHasFixedSize(false)
        }

        mAdapter = BookGridQuickAdapter(mutableListOf())

        mAdapter.apply {
            animationEnable = true
            adapterAnimation = SlideInBottomAnimation()
            isAnimationFirstOnly = true
//            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            setDiffCallback(BookListQuickAdapter.DiffCallback())

            setOnItemClickListener { adapter, view, position ->
                val book = adapter.getItem(position) as Book
                val action = BookshelfFragmentDirections.actionNavigationBookshelfToBookDetailFragment(book.id,book.title,book.author,book.coverImage)
                val coverView = view.findViewById(R.id.book_cover) as View
                val extras = FragmentNavigator.Extras.Builder()
                    .addSharedElements(
                        mapOf(coverView to coverView.transitionName)
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
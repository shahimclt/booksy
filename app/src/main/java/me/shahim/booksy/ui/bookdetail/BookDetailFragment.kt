package me.shahim.booksy.ui.bookdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import me.shahim.booksy.databinding.FragmentBookDetailBinding

class BookDetailFragment : Fragment() {

    private lateinit var bookViewModel: BookViewModel
    private var _binding: FragmentBookDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        bookViewModel =
                ViewModelProvider(this).get(BookViewModel::class.java)

        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        init()
        val root: View = binding.root

        return root
    }

    private fun init() {
        binding.collapsingToolbar.title = "The Origin of Species in two lines"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
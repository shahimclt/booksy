package me.shahim.booksy.ui.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import me.shahim.booksy.databinding.FragmentBookshelfBinding

class BookshelfFragment : Fragment() {

    private lateinit var bookshelfViewModel: BookshelfViewModel
    private var _binding: FragmentBookshelfBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        bookshelfViewModel =
                ViewModelProvider(this).get(BookshelfViewModel::class.java)

        _binding = FragmentBookshelfBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
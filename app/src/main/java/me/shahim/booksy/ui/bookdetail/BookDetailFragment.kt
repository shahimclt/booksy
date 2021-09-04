package me.shahim.booksy.ui.bookdetail

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import me.shahim.booksy.R
import me.shahim.booksy.data.model.Book
import me.shahim.booksy.databinding.FragmentBookDetailBinding

class BookDetailFragment : Fragment() {

    private lateinit var bookViewModel: BookViewModel
    private var _binding: FragmentBookDetailBinding? = null

    private val args: BookDetailFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        bookViewModel = ViewModelProvider(this).get(args.bookid,BookViewModel::class.java)
        bookViewModel.setBookId(args.bookid)

        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        observe()

        return root
    }

    private fun init() {

    }

    private fun observe() {
        bookViewModel.book.observe(viewLifecycleOwner, {
            it?.let { loadBookInfo(it) }
        })
    }

    private fun loadBookInfo(book: Book) {
        binding.collapsingToolbar.title = book.title
        binding.apply {
            bookName.text = book.title
            authorName.text = book.author

            val overlay = ContextCompat.getColor(requireContext(),R.color.poster_overlay)

            val multi = MultiTransformation<Bitmap>(
                BlurTransformation(100,1),
                ColorFilterTransformation(overlay)
            )

            Glide.with(requireContext())
                .load(book.coverImage)
                .placeholder(R.drawable.im_book_cover_placeholder)
                .error(R.drawable.im_book_cover_placeholder)
                .apply(RequestOptions.bitmapTransform(multi))
                .into(toolbarImage)

            Glide.with(requireContext())
                .load(book.coverImage)
                .placeholder(R.drawable.im_book_cover_placeholder)
                .error(R.drawable.im_book_cover_placeholder)
                .into(bookCover)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
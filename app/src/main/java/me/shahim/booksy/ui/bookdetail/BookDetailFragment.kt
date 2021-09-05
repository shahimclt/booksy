package me.shahim.booksy.ui.bookdetail

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import me.shahim.booksy.data.model.Book
import androidx.palette.graphics.Palette
import me.shahim.booksy.R
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

        val transition = TransitionInflater.from(context).inflateTransition(R.transition.shared_image)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
        bookViewModel = ViewModelProvider(this).get(args.bookid,BookViewModel::class.java)
        bookViewModel.setBookId(args.bookid)

        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.bookId = args.bookid
        binding.viewModel = bookViewModel
        binding.executePendingBindings()
        val root: View = binding.root

        postponeEnterTransition()
        init()
        observe()

        return root
    }

    private fun init() {
        loadBookHeader()

        binding.buyBtn.setOnClickListener {
            bookViewModel.buy()
        }
        binding.readBtn.setOnClickListener {
            val action = BookDetailFragmentDirections.actionBookDetailFragmentToReaderFragment()
            findNavController().navigate(action)
        }
    }

    private fun observe() {
        bookViewModel.book.observe(viewLifecycleOwner, {
            it?.let {  displayBookDetails(it) }
        })
    }

    private fun displayBookDetails(book: Book) {
        Glide.with(requireContext())
            .load(book.authorImage)
            .into(binding.authorImage)

        binding.ratingBar.rating = book.ratingFloat

        val flow = binding.genreFlow
        val layout = binding.genreHolder
        flow.referencedIds.forEach {
            val view = binding.root.findViewById<View>(it)
            flow.removeView(view)
            layout.removeView(view)
        }

        book.genres.forEach { genre ->
            val genreLayout = LayoutInflater.from(context)
                .inflate(R.layout.stub_book_genre, layout, false) as AppCompatTextView
            genreLayout.id = ViewCompat.generateViewId()
            genreLayout.text = genre
            layout.addView(genreLayout)
            flow.addView(genreLayout)
        }
    }

    private fun loadBookHeader() {
        binding.collapsingToolbar.title = args.title?:""
        binding.apply {
            bookName.text = args.title?:""
            authorName.text = args.author?:""

            val overlay = ContextCompat.getColor(requireContext(), R.color.poster_overlay)

            val multi = MultiTransformation<Bitmap>(
                BlurTransformation(100,1),
                ColorFilterTransformation(overlay)
            )

            args.cover?.let { cover ->
                Glide.with(requireContext())
                    .load(cover)
                    .listener(object: RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            TODO("Not yet implemented")
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            if (resource != null) {
                                val p: Palette = Palette.from((resource as BitmapDrawable).bitmap).generate()
                                var color = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                                color = p.getVibrantColor(color)
                                if(p.vibrantSwatch==null) {
                                    color = p.swatches.sortedByDescending { it.population }.getOrNull(0)?.rgb?:color
                                }
                                collapsingToolbar.setContentScrimColor(color)
                            }
                            return false
                        }

                    })
                    .apply(RequestOptions.bitmapTransform(multi))
                    .into(toolbarImage)

                Glide.with(requireContext())
                    .load(cover)
                    .placeholder(R.drawable.im_book_cover_placeholder)
                    .error(R.drawable.im_book_cover_placeholder)
                    .listener(object: RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            TODO("Not yet implemented")
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            startPostponedEnterTransition()
                            return false
                        }

                    })
                    .into(bookCover)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

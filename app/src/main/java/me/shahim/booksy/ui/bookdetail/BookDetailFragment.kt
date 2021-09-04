package me.shahim.booksy.ui.bookdetail

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import me.shahim.booksy.databinding.FragmentBookDetailBinding
import androidx.palette.graphics.Palette
import me.shahim.booksy.R


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
        binding.bookId = args.bookid

        binding.executePendingBindings()
        val root: View = binding.root

        postponeEnterTransition()
        init()
        observe()

        return root
    }

    private fun init() {
        loadBookHeader()
    }

    private fun observe() {
        bookViewModel.book.observe(viewLifecycleOwner, {
            it?.let {  }
        })
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

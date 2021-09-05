package me.shahim.booksy.ui.bookdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.shahim.booksy.R
import me.shahim.booksy.databinding.FragmentReaderBinding

class ReaderFragment : Fragment() {

    private var _binding: FragmentReaderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentReaderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()

        return root
    }

    private fun init() {
        val text = resources.openRawResource(R.raw.pride).bufferedReader().use { it.readText() }
        binding.bookReader.text = text
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
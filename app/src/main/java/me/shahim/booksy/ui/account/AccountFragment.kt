package me.shahim.booksy.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import me.shahim.booksy.R
import me.shahim.booksy.databinding.FragmentAccountBinding
import me.shahim.booksy.ui.bookshelf.BookListViewModel

class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.viewModel = accountViewModel
        val root: View = binding.root

        init()
        return root
    }

    private fun init() {
        accountViewModel.user.value?.let { user ->
            Glide.with(requireContext())
                .load(user.photoUrl)
                .into(binding.accountImage)
        }

        binding.logoutBtn.setOnClickListener {
            accountViewModel.logout()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
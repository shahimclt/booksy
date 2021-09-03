package me.shahim.booksy.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import me.shahim.booksy.MainActivity
import me.shahim.booksy.databinding.FragmentLoginBinding
import me.shahim.booksy.databinding.FragmentLoginLoadingBinding

class LoginLoadingFragment : Fragment() {

    private var _binding: FragmentLoginLoadingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginLoadingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        createSignInIntent()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

            val response = IdpResponse.fromResultIntent(data)

            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()

        } else {
            Toast.makeText(context, "Not Success", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        val intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()

        resultLauncher.launch(intent)
    }

    private fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(requireContext())
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_signout]
    }

    private fun delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
            .delete(requireContext())
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_delete]
    }
}
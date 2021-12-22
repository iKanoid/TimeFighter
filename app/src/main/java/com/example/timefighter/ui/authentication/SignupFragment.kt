package com.example.timefighter.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.timefighter.R
import com.example.timefighter.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth
    lateinit var firebase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var firstName: EditText
    lateinit var lastName: EditText
    lateinit var email: EditText
    lateinit var password: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firebase = FirebaseDatabase.getInstance()
        databaseReference = firebase.reference.child("user")
        firstName = binding.firstNameEditText
        lastName = binding.lastNameEditText
        email = binding.emailEditText
        password = binding.passwordEditText
        binding.signupButton.setOnClickListener {
            if (firstName.text.toString().isNotEmpty() && lastName.text.toString().isNotEmpty() && email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()) {
                register()
            } else {
                Toast.makeText(requireContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.emailSignUpFragmentLoginTextView.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

    }

    private fun register() {
        val firstName = binding.firstNameEditText.text.toString()
        val lastName = binding.lastNameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        binding.progressBar.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.progressBar.visibility = View.GONE
                val currentUser = auth.currentUser
                val currentUserObj = currentUser?.uid?.let { databaseReference.child(it) }
                currentUserObj?.child("firstName")?.setValue(firstName)
                currentUserObj?.child("lastName")?.setValue(lastName)
                Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.loginFragment)
            }

        }.addOnFailureListener { exception ->
            binding.progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
package com.example.cardverify.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.cardverify.R
import com.example.cardverify.constants.INPUT_ERROR
import com.example.cardverify.constants.Resource
import com.example.cardverify.databinding.FragmentHomeBinding
import com.example.cardverify.viewModel.MainViewModel
import com.google.android.material.internal.TextWatcherAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)


        textWatcherAdapter()

        observer()





        return binding.root

    }

    //Observing the liveData
    private fun observer() {
        viewModel.myCardDetails.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {

                    binding.progressBar.animate().alpha(1f).duration = 600L
                }

                is Resource.Error -> {
                    response.Message?.let { message ->
                        binding.progressBar.animate().alpha(0f).duration = 600L
                        Snackbar.make(
                            binding.root,
                            "An Error occurred:$message ",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        Log.d("HomeFragment", "An Error occurred: {$response.Message}")

                    }
                }

                is Resource.Success -> {

                    binding.progressBar.animate().alpha(0f).duration = 600L
                    binding.cardSchema.text = "Scheme: ${response.data?.scheme}"
                    binding.cardType.text = "Card Type: ${response.data?.type}"
                    binding.bank.text = "Bank: ${response.data?.bank?.name}"
                    binding.country.text = "Country: ${response.data?.country?.name}"
                    binding.cardNumberLength.text =
                        "PAN Length: ${binding.textEditorLayout.text?.length.toString()}"
                    binding.prepaid.text = null
                }
            }

        })
    }

    //Adding TextWatcher and EditTextView Authentication
    private fun textWatcherAdapter() {
        var job: Job? = null
        binding.textEditorLayout.addTextChangedListener { editable ->

            job?.cancel()
            job = MainScope().launch {
                delay(500L)

                editable?.let {
                    if (it.toString().isNullOrEmpty() || it.length <= 3) {
                        binding.apply {
                            cardNumberLength.text = null
                            cardSchema.text = null
                            textInputLayout.error = INPUT_ERROR
                        }

                    } else {
                        viewModel.getCardDetails(editable.toString())
                        binding.apply {
                            cardNumberLength.animate().alpha(1f).duration = 600L
                            cardSchema.animate().alpha(1f).duration = 600L
                            textInputLayout.error = null
                        }


                        if (it.length <= 5) {

                            hideCardMoreDetails()

                        } else {
                            showCardMoreDetails()
                        }
                    }
                }
            }
        }
    }

    private fun hideCardMoreDetails() {
        binding.apply {

            country.animate().alpha(0f).duration = 600L
            cardType.animate().alpha(0f).duration = 600L
            bank.animate().alpha(0f).duration = 600L

        }
    }

    private fun showCardMoreDetails() {
        binding.apply {
            country.animate().alpha(1f).duration = 600L
            cardType.animate().alpha(1f).duration = 600L
            bank.animate().alpha(1f).duration = 600L
        }
    }
}
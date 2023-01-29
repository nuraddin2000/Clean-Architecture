package com.test.cleanarchitecture.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.test.cleanarchitecture.common.Resource
import com.test.cleanarchitecture.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
open class DetailsFragment : Fragment() {
    var binding: FragmentDetailsBinding? = null
    private val args: DetailsFragmentArgs by navArgs()
    private var id: Int? = null

    private lateinit var detailsViewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsViewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentDetailsBinding.inflate(layoutInflater, container, false)
            .also {
                binding = it
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = args.id
        Log.d("TAG", "onViewCreated: " + id)
        outputs()

        binding?.deleteButton?.setOnClickListener {
            detailsViewModel.deleteContact(id!!)
        }
    }

    private fun outputs() {
        lifecycleScope.launch {
            detailsViewModel.getContactById(id!!).collect {
                when(it) {
                    is Resource.Success -> {
                        binding?.firstName?.text = it.data?.firstName
                        binding?.lastName?.text = it.data?.lastName
                        binding?.email?.text = it.data?.email
                        Picasso.get().load(it.data?.photo).into(binding?.userImage)
                    }
                    is Resource.Error -> Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            detailsViewModel.getDeleteResult().collect {
                when(it) {
                    is Resource.Success -> {
                        findNavController().popBackStack()
                    }
                    is Resource.Error -> Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
package com.test.cleanarchitecture.ui.contactslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.cleanarchitecture.App
import com.test.cleanarchitecture.common.Resource
import com.test.cleanarchitecture.databinding.FragmentContactsListBinding
import com.test.cleanarchitecture.db.contacts.DbContact
import com.test.cleanarchitecture.utils.CustomSharedPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class ContactsListFragment : Fragment() {

    private val contactAdapter: ContactAdapter by lazy {
        ContactAdapter(
            requireActivity(),
            this::onContactClicked
        )
    }

    private lateinit var viewModel: ContactsListViewModel
    private val customPreferences = CustomSharedPreferences(App.application)

    private fun onContactClicked(dbContact: DbContact) {
        findNavController()
            .navigate(ContactsListFragmentDirections.actionContactListToDetails(dbContact.id))
    }

    private var binding: FragmentContactsListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ContactsListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Creates a vertical Layout Manager
        return FragmentContactsListBinding.inflate(layoutInflater, container, false)
            .apply {
                contactList.layoutManager = LinearLayoutManager(context)
                contactList.adapter = contactAdapter
            }
            .also {
                binding = it
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeDeleteItem()
        outputs()
        viewModel.refreshData()

        binding?.pullToRefresh?.setOnRefreshListener {
            viewModel.getContactsFromAPI()
            binding?.pullToRefresh?.isRefreshing = false
        }

    }

    private fun outputs() {
        lifecycle.coroutineScope.launchWhenStarted {
            viewModel.getSaveDBResult().collect {
                when (it) {
                    is Resource.Success -> {
                        customPreferences.saveTime(System.nanoTime())
                        viewModel.getContactsFromRoom()
                        Toast.makeText(
                            requireContext(),
                            "Contacts saved",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Resource.Error -> Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        lifecycle.coroutineScope.launchWhenStarted {

            viewModel.contactsFromAPIResult().collect {
                when (it) {
                    is Resource.Success -> {
                        viewModel.saveContactsDb(it.data!!)
                    }
                    is Resource.Loading -> {}
                    is Resource.Error -> Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        lifecycle.coroutineScope.launchWhenStarted {

            viewModel.contactsFromRoomResult().collect {
                when (it) {
                    is Resource.Success -> {
                        contactAdapter.items = it.data as MutableList<DbContact>
                    }
                    is Resource.Loading -> {}
                    is Resource.Error -> Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        lifecycle.coroutineScope.launchWhenStarted {
            viewModel.getDeleteContactResult().collect {
                when (it) {
                    is Resource.Success -> Toast.makeText(
                        requireContext(),
                        "Contact deleted",
                        Toast.LENGTH_SHORT
                    ).show()

                    is Resource.Error -> Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun swipeDeleteItem() {
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Toast.makeText(requireContext(), "on Move", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                Toast.makeText(requireContext(), "on Swiped ", Toast.LENGTH_SHORT).show()
                //Remove swiped item from list and notify the RecyclerView
                val position = viewHolder.layoutPosition
                viewModel.deleteContact(contactAdapter.items[position].id)
                contactAdapter.items.removeAt(position)
                contactAdapter.notifyItemRangeRemoved(position, contactAdapter.items.size)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding?.contactList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
package com.test.cleanarchitecture.ui.contactslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.test.cleanarchitecture.App
import com.test.cleanarchitecture.common.Resource
import com.test.cleanarchitecture.db.contacts.DbContact
import com.test.cleanarchitecture.domain.use_case.*
import com.test.cleanarchitecture.utils.CustomSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsListViewModel @Inject constructor(
    private val getContactsFromApiUseCase: GetContactsFromApiUseCase,
    private val saveContactsUseCase: SaveContactsUseCase,
    private val getContactsFromRoomUseCase: GetContactsFromRoomUseCase,
    private val deleteContactUseCase: DeleteContactUseCase
) : ViewModel() {

    private var saveDBResult = MutableStateFlow<Resource<Any>>(Resource.Loading())
    private var deleteContactResult = MutableStateFlow<Resource<Any>>(Resource.Loading())
    private var contactsFromRoomResult = MutableStateFlow<Resource<List<DbContact>>>(Resource.Loading())
    private var contactsFromAPIResult = MutableStateFlow<Resource<List<DbContact>>>(Resource.Loading())
    private var customPreferences = CustomSharedPreferences(App.application)
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    fun getSaveDBResult(): Flow<Resource<Any>> {
        return saveDBResult
    }

    fun contactsFromRoomResult(): Flow<Resource<List<DbContact>>> {
        return contactsFromRoomResult
    }

    fun contactsFromAPIResult(): Flow<Resource<List<DbContact>>> {
        return contactsFromAPIResult
    }


    fun getDeleteContactResult(): Flow<Resource<Any>> {
        return deleteContactResult
    }

    fun refreshData(){
        val updateTime = customPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getContactsFromRoom()
        } else {
            getContactsFromAPI()
        }
    }


    fun getContactsFromAPI()= viewModelScope.launch{
        val data = getContactsFromApiUseCase.invoke()
        contactsFromAPIResult.value = data
    }

    fun getContactsFromRoom()  = viewModelScope.launch {
        val data = getContactsFromRoomUseCase.invoke()
        contactsFromRoomResult.value = data
    }

    fun saveContactsDb(contactList: List<DbContact>) = viewModelScope.launch {
        val data = saveContactsUseCase.invoke(contactList)
        saveDBResult.value = data
    }

    fun deleteContact(contactId: Int)= viewModelScope.launch {
        val data = deleteContactUseCase.invoke(contactId)
        deleteContactResult.value = data
    }
}

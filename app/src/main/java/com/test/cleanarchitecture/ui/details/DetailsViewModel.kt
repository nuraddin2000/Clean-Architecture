package com.test.cleanarchitecture.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cleanarchitecture.common.Resource
import com.test.cleanarchitecture.db.contacts.DbContact
import com.test.cleanarchitecture.domain.use_case.DeleteContactUseCase
import com.test.cleanarchitecture.domain.use_case.GetContactUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getContactUseCase: GetContactUseCase,
    private val deleteContactUseCase: DeleteContactUseCase
) : ViewModel() {


    private var deleteResult = MutableStateFlow<Resource<Any>>(Resource.Loading())

    fun getDeleteResult(): Flow<Resource<Any>> {
        return deleteResult
    }

    fun getContactById(contactId: Int): Flow<Resource<DbContact>> {
        return getContactUseCase.invoke(contactId)
    }

    fun deleteContact(contactId: Int) = viewModelScope.launch {
        val data = deleteContactUseCase.invoke(contactId)
        deleteResult.value = data

    }
}

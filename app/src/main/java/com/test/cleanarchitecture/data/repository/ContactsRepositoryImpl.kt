package com.test.cleanarchitecture.data.repository

import com.test.cleanarchitecture.data.remote.ContactsService
import com.test.cleanarchitecture.data.remote.dto.ApiContactResponse
import com.test.cleanarchitecture.domain.repository.ContactsRepository
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(private val api: ContactsService): ContactsRepository {


    override suspend fun getContacts(limit: Int): ApiContactResponse {
       return api.getContacts(limit)
    }

}
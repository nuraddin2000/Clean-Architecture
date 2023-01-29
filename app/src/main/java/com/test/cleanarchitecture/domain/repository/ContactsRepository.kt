package com.test.cleanarchitecture.domain.repository

import com.test.cleanarchitecture.data.remote.dto.ApiContactResponse


interface ContactsRepository {
    suspend fun getContacts(limit: Int): ApiContactResponse
}
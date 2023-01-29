package com.test.cleanarchitecture.domain.repository

import com.test.cleanarchitecture.db.contacts.DbContact


interface ContactsDaoRepository {


    suspend fun getContacts(): List<DbContact>

    suspend fun update(contact: DbContact)

    suspend fun addAll(contact: List<DbContact>)

    suspend fun deleteById(contactId: Int)

    suspend fun deleteAll()

    suspend fun getContactById(id: Int): DbContact
}
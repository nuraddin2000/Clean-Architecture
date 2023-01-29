package com.test.cleanarchitecture.data.remote.dto

import com.test.cleanarchitecture.db.contacts.DbContact


data class ApiContactResponse(val results: List<ApiContact>?)

fun ApiContactResponse.toDbContacts(): List<DbContact> {

    val dbContactList = mutableListOf<DbContact>()
    for (item in this.results!!) {
        val dbContact =
            DbContact(
                firstName = item.name?.firstName,
                lastName = item.name?.lastName,
                email = item.email,
                photo = item.picture?.medium
            )
        dbContactList.add(dbContact)
    }
    return dbContactList
}
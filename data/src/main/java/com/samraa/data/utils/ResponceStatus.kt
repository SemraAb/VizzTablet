package com.samraa.data.utils

enum class ResponseStatus {
    OK,
    FAILED,
    RECORD_NOT_FOUND,
    ACCOUNT_NOT_FOUND;

    companion object {
        fun find(status: String?) =
            entries.find { it.name.lowercase() == status?.lowercase() } ?: FAILED
    }
}
package com.samraa.data.enums


enum class OrderStatus {
    IN_PROGRESS, DONE, FAILED;

    companion object {
        fun find(name: String?) =
            entries.find { it.name.lowercase() == name?.lowercase() } ?: IN_PROGRESS
    }
}
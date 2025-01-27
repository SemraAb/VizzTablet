package com.samraa.data.enums

enum class LanguageEnum(val fullName: String, val forApiRequest: String) {
    AZ("Azərbaycan", "AZE"),
    EN("English", "ENG"),
    RU("Русский", "RU");

    companion object {
        fun find(name: String?) =
            LanguageEnum.values().find { it.name.equals(name, ignoreCase = true) } ?: AZ

        fun LanguageEnum.lowercase() = name.lowercase()
    }


}

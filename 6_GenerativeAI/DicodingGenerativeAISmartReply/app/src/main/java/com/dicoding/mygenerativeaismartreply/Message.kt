package com.dicoding.mygenerativeaismartreply

data class Message(
    val text: String,
    val isLocalUser: Boolean,
    val timestamp: Long
)

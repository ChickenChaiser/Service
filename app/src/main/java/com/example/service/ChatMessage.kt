package com.example.service

import java.sql.Timestamp

data class ChatMessage(val id: String, val text: String, val contactId: String, val currentId: String, val timestamp: Long) {

    constructor() : this("", "", "", "", -1)
}
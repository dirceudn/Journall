package com.google.android.journal.helper

sealed class BaseMessage {

    class Error(val errorString: String) : BaseMessage()

    class Success(val toastMessage: String?) : BaseMessage()

}
package com.tapandtest.app.firebaseviewmodel

data class SignInResult(
    val data: UserData?, // Kullanıcı bilgileri
    val error: String? = null // Hata mesajı (varsa)
)

data class UserData(
    val userId: String, // Firebase UID
    val username: String?, // Kullanıcı adı (Google profili)
    val profilePictureUrl: String? // Kullanıcı profil fotoğrafı URL'si
)

package com.example.data.local

import java.security.MessageDigest

object SecurityUtils {
    /**
     * Hashes password using SHA-256 for secure local storage.
     * In a production app with backend, authentication is delegated to Firebase Auth or an OAuth2 provider.
     */
    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }

    fun verifyPassword(password: String, storedHash: String): Boolean {
        return hashPassword(password) == storedHash
    }
}

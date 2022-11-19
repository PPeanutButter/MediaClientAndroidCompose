package com.peanut.nas.compose.data

data class Configuration(val host: String, val userName: String, val userPassword: String){

    val serverIp: String get() = host.resolveUrl()

    companion object{
        val Empty = Configuration("", "", "")
    }

    private fun String.resolveUrl() = if (this.startsWith("http") || this.startsWith("HTTP")) {
        this
    } else "http://$this"

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Configuration) return false
        if (this.hashCode() != other.hashCode()) return false
        if (other.host != this.host) return false
        if (other.userName != this.userName) return false
        if (other.userPassword != this.userPassword) return false
        return true
    }

    override fun hashCode(): Int {
        var result = host.hashCode()
        result = 31 * result + userName.hashCode()
        result = 31 * result + userPassword.hashCode()
        return result
    }

    fun isValid(): Boolean{
        if (this.host.isBlank()) return false
        if (this.userName.isBlank()) return false
        if (this.userPassword.isBlank()) return false
        return true
    }

    override fun toString(): String {
        return "Configuration(host=$host, userName=$userName, userPassword=$userPassword)"
    }
}

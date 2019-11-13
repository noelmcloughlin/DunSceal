package org.noel.dunsceal.models

data class DunUser(val id: Int = -1,
                   val name: String,
                   val email: String,
                   val password: String)
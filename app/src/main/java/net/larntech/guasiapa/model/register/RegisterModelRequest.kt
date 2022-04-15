package net.larntech.guasiapa.model.register

data class RegisterModelRequest(
    var name: String,
    var email: String,
    var password: String,
)
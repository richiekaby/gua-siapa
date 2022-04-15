package net.larntech.guasiapa.model.login

import net.larntech.guasiapa.model.login.LoginResponse.LoginResultBean

class LoginResponse {
    /**
     * error : false
     * message : success
     * loginResult : {"userId":"user-yj5pc_LARC_AgK61","name":"Arif Faizin","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"}
     */
    var isError = false
    var message: String? = null

    /**
     * userId : user-yj5pc_LARC_AgK61
     * name : Arif Faizin
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I
     */
    var loginResult: LoginResultBean? = null

    class LoginResultBean {
        var userId: String? = null
        var name: String? = null
        var token: String? = null
    }
}
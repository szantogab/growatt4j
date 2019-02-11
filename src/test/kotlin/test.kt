import growatt.api.GrowattApi

fun main() {
    val api = GrowattApi.testApi()
    //val registerResult = api.register(RegisterInput(userName = "szantogab", userPassword = "12345678", userEmail = "szantogab@gmail.com", userCountry = "Hungary", userTel = "+363012345678"))
    //val loginResult = api.login("szantogab", "12345678")

     // val checkUserResponse = api.checkUserExists("szantogab").third
    // val plants = api.plants(1).third
    // val plant = api.plant(107).third

    val plantHistory = api.plantHistory(29).third
}
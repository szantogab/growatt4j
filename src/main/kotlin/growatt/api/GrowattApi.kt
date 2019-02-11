package growatt.api

import com.github.kittinunf.fuel.core.ResponseResultOf
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import java.security.MessageDigest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val GROWATT_TEST_URL = "http://test.growatt.com/v1"
const val GROWATT_PRODUCTION_URL = "https://server.growatt.com/v1"

const val GROWATT_TEST_TOKEN = "6eb6f069523055a339d71e5b1f6c88cc"

class GrowattApi(private val baseUrl: String, private val token: String) {
    fun login(username: String, password: String) = "$baseUrl/user/login"
        .httpPost(listOf("userName" to username, "password" to hashMd5(password)))
        .responseObject<String>()

    fun register(registerInput: RegisterInput) = "$baseUrl/user/user_register"
        .httpPost(
            listOf(
                "user_name" to registerInput.userName,
                "user_email" to registerInput.userEmail,
                "user_password" to registerInput.userPassword,
                "user_country" to registerInput.userCountry,
                "user_tel" to registerInput.userTel,
                "user_type" to registerInput.userType
            )
        )
        .responseObject<GrowattResponse<RegisterResponse>>()
        .validateResponse()

    /**
     * Checks whether the specified username is taken or not.
     */
    fun checkUserExists(userName: String) = "$baseUrl/user/check_user"
        .httpPost(listOf("user_name" to userName))
        .header("token" to token)
        .responseObject<GrowattResponse<String>>()
        .validateResponse()

    /**
     * Obtains a list of the user's power plants.
     */
    fun plants(userId: Long, filter: SearchFilter = SearchFilter()) = "$baseUrl/plant/list"
        .httpGet(
            listOf(
                "C_user_id" to userId,
                "search_type" to filter.searchType,
                "search_keyword" to filter.searchKeyword,
                "page" to filter.page,
                "perpage" to filter.perpage
            )
        )
        .header("token" to token)
        .responseObject<GrowattResponse<PlantsResponse>>()
        .validateResponse()

    /**
     * Obtains the basic information of a power plant.
     */
    fun plant(plantId: Long) = "$baseUrl/plant/details"
        .httpGet(listOf("plant_id" to plantId))
        .header("token" to token)
        .responseObject<GrowattResponse<PlantDetail>>()
        .validateResponse()

    /**
     * Obtains an overview of the data for the specified plant.
     */
    fun plantData(plantId: Long) = "$baseUrl/plant/data"
        .httpGet(listOf("plant_id" to plantId))
        .header("token" to token)
        .responseObject<GrowattResponse<PlantData>>()
        .validateResponse()

    fun plantHistory(plantId: Long, filter: PlantHistoryFilter = PlantHistoryFilter()) = "$baseUrl/plant/energy"
        .httpGet(
            listOf(
                "plant_id" to plantId,
                "start_date" to filter.startDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                "end_date" to filter.endDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                "time_unit" to filter.timeUnit,
                "page" to filter.page,
                "perpage" to filter.perpage
            )
        )
        .header("token" to token)
        .responseObject<GrowattResponse<PlantHistory>>()
        .validateResponse()

    fun plantPower(plantId: Long, date: LocalDate) = "$baseUrl/plant/power"
        .httpGet(listOf("plant_id" to plantId, "date" to date.format(DateTimeFormatter.ISO_LOCAL_DATE)))
        .header("token" to token)
        .responseObject<GrowattResponse<PlantPowerResponse>>()
        .validateResponse()

    /**
     * Validates the response if the status is 200, or throws a [GrowattException].
     */
    private fun <T> ResponseResultOf<GrowattResponse<T>>.validateResponse(): ResponseResultOf<GrowattResponse<T>> {
        val result = this.third
        val (data, error) = result
        if (error != null) throw error

        if (data != null && (data.errorMessage?.isNotBlank() == true || data.errorCode != GROWATT_ERROR_CODE_OK)) throw GrowattException(
            data.errorCode,
            data.errorMessage
        )
        return this
    }

    companion object {
        fun testApi() = GrowattApi(GROWATT_TEST_URL, GROWATT_TEST_TOKEN)
        fun productionApi(token: String) = GrowattApi(GROWATT_PRODUCTION_URL, token)
    }
}

private fun hashMd5(password: String): String {
    val md5 = MessageDigest.getInstance("MD5").digest(password.toByteArray())
    val sb = StringBuilder(md5.size * 2)
    for (b in md5) sb.append(String.format("%02x", b))

    val hex = sb.toString()
    return hex

    /*for (i in 0 until hex.length step 2) {
        if (hex[i] == 0)
    }*/
}

data class PlantHistoryFilter(
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val timeUnit: TimeUnit? = TimeUnit.DAY,
    val page: Int? = 1,
    val perpage: Int? = 20
)

enum class TimeUnit {
    DAY, MONTH, YEAR
}

data class SearchFilter(
    val searchType: String? = null,
    val searchKeyword: String? = null,
    val page: Int? = 1,
    val perpage: Int? = 20
)

data class RegisterInput(
    val userName: String,
    val userPassword: String,
    val userEmail: String,
    val userTel: String? = null,
    val userCountry: String,
    val userType: Int = 1
)
package growatt.api

import com.github.kittinunf.fuel.core.ResponseResultOf
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import java.security.MessageDigest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val GROWATT_TEST_URL = "http://test.growatt.com"
const val GROWATT_PRODUCTION_URL = "https://server.growatt.com"

/**
 * Main class that can be used to communicate with Growatt servers. Typically you obtain an instance of this class with [GrowattApi.productionApiLogin].
 */
class GrowattApi(private val baseUrl: String, private val cookies: List<String>) {
    /**
     * Obtains a list of the user's power plants.
     */
    fun plants() = "$baseUrl/PlantListAPI.do"
        .httpGet()
        .header("Cookie", cookies)
        .responseObject<GrowattResponse<PlantsResponse>>()
        .validateResponse().third.get().back

    /**
     * Obtains the basic information of a power plant.
     */
    fun plant(plantId: Long, timeSpan: TimeSpan, date: LocalDate?) = "$baseUrl/PlantDetailAPI.do"
        .httpGet(
            listOf(
                "plantId" to plantId,
                "type" to timeSpan.value,
                "date" to date?.let { timeSpan.formatDate(it) })
        )
        .header("Cookie", cookies)
        .responseObject<GrowattResponse<PlantDetailResponse>>()
        .validateResponse().third.get().back

    /**
     * Obtains overall data.
     */
    fun userEnergyData() = "$baseUrl/newPlantAPI.do"
        .httpPost(listOf("action" to "getUserCenterEnertyData"))
        .header("Cookie", cookies)
        .responseObject<UserEnergyDataResponse>().third.get()

    companion object {
        fun productionApiLogin(username: String, password: String): GrowattApi {
            val result = "$GROWATT_PRODUCTION_URL/LoginAPI.do"
                .httpPost(listOf("userName" to username, "password" to hashMd5(password)))
                .responseObject<GrowattResponse<LoginResponse>>()
                .validateResponse()

            val cookies = result.second.header("Set-Cookie")
            return GrowattApi(GROWATT_PRODUCTION_URL, cookies.toList())
        }
    }
}

/**
 * Validates the response if the status is 200, or throws a [GrowattException].
 */
fun <T : BaseGrowattResponseData> ResponseResultOf<GrowattResponse<T>>.validateResponse(): ResponseResultOf<GrowattResponse<T>> {
    val (data, error) = third
    if (error != null) throw error

    if (data != null && (!data.back.success)) throw GrowattException(data.back.errorCode)
    return this
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

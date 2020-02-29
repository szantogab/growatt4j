package growatt.api

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Wrapper class for all Growatt API response.
 */
//data class GrowattResponse<T>(val data: T, @SerializedName("error_code") val errorCode: Long, @SerializedName("error_msg") val errorMessage: String?)
data class GrowattResponse<T : BaseGrowattResponseData>(val back: T)
open class BaseGrowattResponseData(@SerializedName("errCode") val errorCode: Long, val success: Boolean)

class LoginResponse(val userId: Long, val userLevel: Long, errorCode: Long, success: Boolean) :
    BaseGrowattResponseData(errorCode, success)

class PlantsResponse(val data: List<Plant>, val totalData: PlantsTotalData, errorCode: Long, success: Boolean) : BaseGrowattResponseData(errorCode, success) {
    data class PlantsTotalData(val currentPowerSum: String, @SerializedName("CO2Sum") val co2Sum: String, val isHaveStorage: Boolean, val eTotalMoneyText: String, val todayEnergySum: String, val totalEnergySum: String)
    data class Plant(
        val plantMoneyText: String,
        val plantName: String,
        val plantId: Long,
        val isHaveStorage: Boolean,
        val todayEnergy: String,
        val totalEnergy: String,
        val currentPower: String
    )
}

class PlantDetailResponse(val plantData: PlantBasic, val data: Map<String, String>, errorCode: Long, success: Boolean) : BaseGrowattResponseData(errorCode, success) {
    data class PlantBasic(val plantMoneyText: String, val plantName: String, val plantId: Long, val currentEnergy: String)
}

class UserEnergyDataResponse(val monthProfitStr: String, val todayProfitStr: String, val plantNumber: Long, val treeValue: String, val treeStr: String, val nominalPowerStr: String, val formulaCoalValue: String, @SerializedName("powerValue") val currentPowerInWatts: Double, @SerializedName("todayValue") val todayValueInKwh: Double, @SerializedName("totalValue") val totalValueInKwh: Double, @SerializedName("monthValue") val monthValueInKwh: Double, val totalProfitStr: String)

enum class TimeSpan(val value: Int) {
    /**
     * Power in each five minute of the day.
     */
    DAY(1),

    /**
     * Power on each day of the given month.
     */
    MONTH(2),

    /**
     * Power in each month of the given year.
     */
    YEAR(3),

    /**
     * Power in each year. `date` parameter is ignored
     */
    TOTAL(4);

    fun formatDate(date: LocalDate): String = when (this) {
        YEAR -> date.format(DateTimeFormatter.ofPattern("yyyy"))
        DAY -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        MONTH -> date.format(DateTimeFormatter.ofPattern("yyyy-MM")) // power on each day of the month
        TOTAL -> ""
    }
}

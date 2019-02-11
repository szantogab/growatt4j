package growatt.api

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Wrapper class for all Growatt API response.
 */
data class GrowattResponse<T>(val data: T, @SerializedName("error_code") val errorCode: Long, @SerializedName("error_msg") val errorMessage: String?)

data class RegisterResponse(@SerializedName("c_user_id") val userId: Long)

data class PlantsResponse(val count: Long, val plants: List<Plant>)
data class Plant(
    val name: String,
    @SerializedName("plant_id") val plantId: Long,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("peak_power") val peakPower: Double,
    @SerializedName("total_energy") val totalEnergy: Double,
    val longitude: Double, val latitude: Double,
    val installer: String?,
    val locale: String?
)

data class Inverter(@SerializedName("inverter_man") val inverterMan: String, @SerializedName("inverter_num") val inverterNum: Int)
data class PlantDetail(
    val name: String, @SerializedName("user_id") val userId: Long,
    val description: String, @SerializedName("Locale") val locale: String,
    val inverters: List<Inverter>
)


data class PlantData(
    @SerializedName("peak_power_actual") val peakPowerActual: Double,
    @SerializedName("monthly_energy") val monthlyEnergy: Double,
    @SerializedName("last_update_time") val lastUpdate: LocalDateTime?,
    @SerializedName("total_energy") val totalEnergy: Double,
    @SerializedName("efficiency") val efficiency: Double,
    @SerializedName("current_power") val currentPower: Double,
    @SerializedName("today_energy") val todayEnergy: Double,
    @SerializedName("yearly_energy") val yearlyEnergy: Double,
    @SerializedName("carbon_offset") val carbonOffset: Double,
    val timezone: String?
)


data class PlantHistory(val count: Long, @SerializedName("time_unit") val timeUnit: TimeUnit, @SerializedName("energy") val energyInKwH: Double, @SerializedName("energys") val energies: List<Energy>)
data class Energy(val date: LocalDate, val energy: Double)


data class PlantPowerResponse(val count: Long, val powers: List<Power>)
data class Power(val time: LocalDateTime, val power: Double)
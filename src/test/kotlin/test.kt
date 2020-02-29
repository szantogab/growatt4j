import growatt.api.GrowattApi
import growatt.api.TimeSpan
import java.time.LocalDate

fun main() {
    val api = GrowattApi.productionApiLogin("un", "pw")
    val plants = api.plants()
    val plant = api.plant(plants.data.first().plantId, TimeSpan.MONTH, LocalDate.now())
    val data = api.userEnergyData()
}

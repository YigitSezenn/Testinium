import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    var selectedMinutes = mutableStateOf(1) // Seçilen dakika
    var timeLeft = mutableStateOf(selectedMinutes.value * 60 * 1000) // Kalan süre
    var isRunning = mutableStateOf(false) // Timer'ın durumu

    // Timer başlatma/durma
    fun toggleTimer() {
        isRunning.value = !isRunning.value
    }

    // Süreyi güncelleme
    fun updateTime() {
        timeLeft.value -= 1000
    }

    // Süreyi sıfırlama
    fun resetTimer() {
        timeLeft.value = selectedMinutes.value * 60 * 1000
        isRunning.value = false
    }

    // Süreyi belirleme
    fun setMinutes(minutes: Int) {
        selectedMinutes.value = minutes
        timeLeft.value = minutes * 60 * 1000
    }
}

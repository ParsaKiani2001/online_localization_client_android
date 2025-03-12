import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.parsak2001.online_localization.TranslateViewModel

class TranslateViewModelFactory(
    private val application: Application,
    private val offlineMode:Boolean,
    private val baseUrl: String,
    private val version: Int,
    private val defaultLang: Pair<String, String>,
    private val defaultDic: Map<String, String>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TranslateViewModel::class.java)) {
            return TranslateViewModel(application,offlineMode, baseUrl, version, defaultLang, defaultDic) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
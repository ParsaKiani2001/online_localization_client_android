package ir.parsak2001.online_localization

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.layout.LookaheadScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ir.parsak2001.online_localization.memory.Memory
import ir.parsak2001.online_localization.retrofit.Apis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TranslateViewModel(application: Application,val offlineMode: Boolean,baseUrl:String,version:Int,val defaultLang:Pair<String,String>,val defaultDic:Map<String,String>):AndroidViewModel(application) {
    val languages = mutableStateOf(DataLocale())
    val current = mutableStateOf( defaultLang)
    val dictionary = mutableStateOf(defaultDic.toMutableMap())
    val memory = Memory(application.applicationContext)
    val api = Apis(baseUrl,version)
    val _version = version

    fun getLanguage(){
        viewModelScope.launch {
            try {
                memory.getLanguage().collect { data ->
                    if (data.first.isNotEmpty()) {
                        current.value = data
                    } else {
                        current.value = defaultLang
                    }
                    changeLanguage(current.value)
                }
            }catch (e:Exception){
            }
        }
    }

    fun changeLanguage(local:Pair<String,String>){
        viewModelScope.launch {
            current.value = local
            memory.getDictionary(_version,current.value.first,current.value.second).collect{
                dictionary.value = it
                memory.setLanguage(current.value.first,current.value.second)
                if(dictionary.value.isEmpty()){
                    try {
                        dictionary.value = api.loadDictionary(current.value.first).toMutableMap()
                        memory.setDictionary(_version,current.value.first,current.value.second,dictionary.value)
                    }catch (e:Exception){
                        if(dictionary.value.isEmpty()){
                            dictionary.value = defaultDic.toMutableMap();
                        }
                    }
                    if(dictionary.value.isEmpty()){
                        dictionary.value = defaultDic.toMutableMap();
                    }
                }
            }

        }
    }
    /*fun test(){
        viewModelScope.launch {
            try {
                val data = api.loadLanguages()
                Log.d("Test",data.toString())
            }catch (e:Exception){
                Log.e("Test",e.message.toString())
            }
        }
    }
    fun test2(){
        viewModelScope.launch {
            try {
                val data = api.loadDictionary(language = "en")
                Log.d("Test2",data.toString())
            }catch (e:Exception){
                Log.e("Test2",e.message.toString())
            }
        }
    }*/
    private suspend  fun getLang(){
        try {
            languages.value = DataLocale()
            val data = api.loadLanguages()
            data.map {
                val local = Pair(it.name, it.local)
                val name = Pair(
                    it.show_lang,
                    Memory.decodeFromBase64(it.language_icon ?: "")
                )
                languages.value.locale.add(local)
                languages.value.names.add(name)
            }
            if(languages.value.locale.isNotEmpty()) {
                memory.setLanguages(_version, languages.value)
            }
        }catch (e:Exception){
            Log.e("Language",e.message.toString())
            getMemory()
        }
    }

    fun getLanguages(){
        viewModelScope.launch(Dispatchers.IO){
            languages.value = DataLocale()
            if(!offlineMode){
                getLang()
            }else{
                return@launch
            }
            getMemory()
        }
    }
    private fun getMemory(){
        viewModelScope.launch(Dispatchers.Main) {
            memory.getLanguages(version = _version).collect {
                Log.i("LanguageMemory", it.toString())
                if (it.first.isEmpty()) {
                    getLang()
                } else {
                    languages.value = DataLocale(
                        names = it.second.toMutableList(),
                        locale = it.first.toMutableList()
                    )
                }
            }
        }
    }

    fun translate(key:String):String{
        val data = dictionary.value[key]?:""
        return data.ifEmpty {
            defaultDic[key] ?: key
        }
    }
}
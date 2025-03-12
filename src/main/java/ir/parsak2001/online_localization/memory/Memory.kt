package ir.parsak2001.online_localization.memory
import androidx.datastore.preferences.core.Preferences
import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import ir.parsak2001.online_localization.DataLocale
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

class Memory(val context: Context) {
    companion object{
        fun decodeFromBase64(encoded: String): ByteArray? {
            if(encoded.isEmpty()){
                return null
            }
            return Base64.decode(encoded, Base64.DEFAULT)
        }
    }
    init {

    }
    val Context.Languages : DataStore<Preferences> by preferencesDataStore(name= "language")
    private object Keys{
        val Languages = stringSetPreferencesKey("langueges")
        val CurrentLR = stringPreferencesKey("lang_reg")
    }
    fun getLanguages(version:Int):Flow<Pair<List<Pair<String,String>>,List<Pair<String,ByteArray?>>>>{
        return context.Languages.data.map { languages ->
            val lang = languages[Keys.Languages]
            val locale:MutableList<Pair<String,String>> = mutableListOf()
            val names:MutableList<Pair<String,ByteArray?>> = mutableListOf()
            lang?.forEach { element ->
                val collection = element.split("_")
                if(collection.isNotEmpty()) {
                    if (collection[0] == version.toString()) {
                        locale.add(Pair(collection[2], collection[3]))
                        names.add(Pair(collection[1], decodeFromBase64(collection[4])))
                    }
                }
            }
            return@map Pair<List<Pair<String,String>>,List<Pair<String,ByteArray?>>>(locale.toList(),names.toList())
        }
    }


    fun getDictionary(version: Int,language:String,locale:String):Flow<MutableMap<String,String>>{
        val key = stringSetPreferencesKey("${version}_${language}_${locale}")
        return context.Languages.data.map{dic->
            val dictionary:MutableMap<String,String> = mutableMapOf()

            dic[key]?.forEach {
                val collection = it.split("_!@!_")
                dictionary[collection[0]] = collection[1]
            }
            return@map dictionary
        }
    }

    suspend fun setDictionary(version: Int,language:String,locale:String,dictionary:Map<String,String>){
        val key = stringSetPreferencesKey("${version}_${language}_${locale}")
        context.Languages.edit {
            val data:MutableList<String> = mutableListOf();
            dictionary.map { item->
                data.add("${item.key}_!@!_${item.value}")
            }
            it[key] = data.toSet()
        }
    }

    suspend  fun setLanguages(version:Int,data:DataLocale) {
        context.Languages.edit {
            val collection :MutableList<String> = mutableListOf()
            for(index in 0 until data.locale.size){
                collection.add("${version}_${data.names[index].first}_${data.locale[index].first}_${data.locale[index].second}_${encodeToBase64(data.names[index].second)}")
            }
            it[Keys.Languages] = collection.toSet()
        }
    }
    
    private fun encodeToBase64(input: ByteArray?): String {
        if(input ==null){
            return ""
        }
        return Base64.encodeToString(input, Base64.DEFAULT)
    }


    suspend fun setLanguage(lang:String,local:String){
        context.Languages.edit { it[Keys.CurrentLR] = "${lang}_${local}" }
    }

    fun getLanguage():Flow<Pair<String,String>>{
        return  context.Languages.data.map{
            var lang = Pair<String,String>("","")
            val collect = it[Keys.CurrentLR]?.split("_")?: listOf()
            if (collect.isNotEmpty()){
                lang = Pair(collect[0], collect[1])
            }
            return@map lang
        }
    }

}
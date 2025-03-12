package ir.parsak2001.online_localization.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Apis(baseUrl: String, version: Int) {
    private var _version = version
    private var retrofit:Retrofit = Retrofit.Builder().baseUrl("$baseUrl/API/").addConverterFactory(GsonConverterFactory.create()).build()
    private var language :LanguageApiService = retrofit.create(LanguageApiService::class.java)
    private var dictionary :DictionaryApiService = retrofit.create(DictionaryApiService::class.java)

    suspend fun loadLanguages() = language.getLanguages(_version)

    suspend fun loadDictionary(language:String) = dictionary.getDictionary(language,_version)


}
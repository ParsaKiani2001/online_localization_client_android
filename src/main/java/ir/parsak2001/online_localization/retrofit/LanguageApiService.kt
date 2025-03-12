package ir.parsak2001.online_localization.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface LanguageApiService {
    @GET("Language")
    suspend fun getLanguages(@Query("version") version:Int):List<LanguageData>
}
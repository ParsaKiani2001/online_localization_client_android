package ir.parsak2001.online_localization.retrofit

import android.os.IBinder
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DictionaryApiService {
    @GET("Dictionary/{language}")
    suspend fun getDictionary(@Path("language") language:String,@Query("version") version:Int):Map<String,String>
}
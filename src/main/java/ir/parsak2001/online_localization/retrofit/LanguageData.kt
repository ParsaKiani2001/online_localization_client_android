package ir.parsak2001.online_localization.retrofit

data class LanguageData(
    val name:String,
    val local:String,
    val show_lang:String,
    val language_icon:String?,
    val version:Int,
    val is_active:Boolean
)

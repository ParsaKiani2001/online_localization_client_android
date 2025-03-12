package ir.parsak2001.online_localization

data class DataLocale(
    var names :MutableList<Pair<String,ByteArray?>> = mutableListOf(),
    var locale: MutableList<Pair<String,String>> = mutableListOf()
)

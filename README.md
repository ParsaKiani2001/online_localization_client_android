# Online Localization - Android Client

## Introduction

**Online Localization** is a library designed to facilitate multilingual applications by fetching translations from an online server. This Android client allows applications to dynamically update languages without requiring code changes or redeployment.

## Features
- Fetch translations from an online server.
- Change application language dynamically.
- Support for multiple languages.
- Easy integration with Jetpack Compose and ViewModel.

## Installation
Add the dependency to your project's `build.gradle`:

```gradle
implementation(project(":online_localization"))
```

## Usage
### Initialize in `Application` class

```kotlin
class MainApplication : Application(), ViewModelStoreOwner {
    private val languageViewModelStore: ViewModelStore = ViewModelStore()
    private lateinit var viewModelFactory: TranslateViewModelFactory
    private lateinit var languageViewModel: TranslateViewModel

    override fun onCreate() {
        super.onCreate()
        
        viewModelFactory = TranslateViewModelFactory(
            application = this,
            offline = false,
            baseUrl = "http://yourserver.com", // your baseUrl
            version = 1, // API version
            defaultLang = Pair("en", "US"), // Default language
            defaultDic = mapOf() // Default dictionary
        )
        
        languageViewModel = ViewModelProvider(this, viewModelFactory)[TranslateViewModel::class.java]
        languageViewModel.getLanguages()
        languageViewModel.getLanguage()
    }

    fun getAppViewModel(): TranslateViewModel = languageViewModel
    override val viewModelStore: ViewModelStore = languageViewModelStore
}
```

### Usage in Activity

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val translateViewModel = (applicationContext as MainApplication).getAppViewModel()
        setContent {
            OnlineLocalizationTheme {
                Greeting(
                    name = translateViewModel.translate(Dictionary.Core.nameDictionary),
                    translator = translateViewModel
                )
            }
        }
    }
}
```

### Changing Language
```kotlin
translator.changeLanguage(Pair("fr", "FR"))
```

## License
MIT License

---

# سرور آنلاین لوکالایزیشن - کلاینت اندروید

## معرفی
**Online Localization** یک کتابخانه برای مدیریت چندزبانه بودن نرم‌افزارهای اندرویدی است که به صورت آنلاین ترجمه‌ها را دریافت می‌کند. این کلاینت اندروید امکان تغییر زبان نرم‌افزار را بدون نیاز به تغییر کد و انتشار مجدد فراهم می‌کند.

## ویژگی‌ها
- دریافت ترجمه‌ها از سرور آنلاین.
- تغییر زبان برنامه به صورت داینامیک.
- پشتیبانی از چندین زبان.
- ادغام آسان با Jetpack Compose و ViewModel.

## نصب
وابستگی زیر را به `build.gradle` پروژه اضافه کنید:

```gradle
implementation(project(":online_localization"))
```

## استفاده
### مقداردهی اولیه در کلاس `Application`

```kotlin
class MainApplication : Application(), ViewModelStoreOwner {
    private val languageViewModelStore: ViewModelStore = ViewModelStore()
    private lateinit var viewModelFactory: TranslateViewModelFactory
    private lateinit var languageViewModel: TranslateViewModel

    override fun onCreate() {
        super.onCreate()
        
        viewModelFactory = TranslateViewModelFactory(
            application = this,
            offline = false,
            baseUrl = "http://yourserver.com", // آدرس سرور
            version = 1, // نسخه API
            defaultLang = Pair("fa", "IR"), // زبان پیش‌فرض
            defaultDic = mapOf() // دیکشنری پیش‌فرض
        )
        
        languageViewModel = ViewModelProvider(this, viewModelFactory)[TranslateViewModel::class.java]
        languageViewModel.getLanguages()
        languageViewModel.getLanguage()
    }

    fun getAppViewModel(): TranslateViewModel = languageViewModel
    override val viewModelStore: ViewModelStore = languageViewModelStore
}
```

### استفاده در Activity

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val translateViewModel = (applicationContext as MainApplication).getAppViewModel()
        setContent {
            OnlineLocalizationTheme {
                Greeting(
                    name = translateViewModel.translate(Dictionary.Core.nameDictionary),
                    translator = translateViewModel
                )
            }
        }
    }
}
```

### تغییر زبان
```kotlin
translator.changeLanguage(Pair("en", "US"))
```

## لایسنس
لایسنس MIT


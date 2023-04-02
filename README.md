# ComicsLibrary

## Project Setup
### API CONFIG
<img width="396" alt="スクリーンショット_2023_03_22_8_55" src="https://user-images.githubusercontent.com/47273077/226767782-9478410d-c439-43dc-9d4a-ad662b6cecf6.png">
build.grale(app)

```gradle
def apikeyPropertiesFile = rootProject.file("apikey.properties")
def apikeyProperties = new Properties()
apikeyProperties.load(new FileInputStream(apikeyPropertiesFile))

    defaultConfig {
        applicationId "com.codewithkyo.comicslibrary"
        minSdk 24
        targetSdk 32
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary true
        }

        buildConfigField("String", "MARVEL_KEY", apikeyProperties['MARVEL_KEY'])
        buildConfigField("String", "MARVEL_SECRET", apikeyProperties['MARVEL_SECRET'])
    }
```


<img width="300" alt="スクリーンショット 2023-03-22 13 51 27" src="https://user-images.githubusercontent.com/47273077/226805203-9f82b5b7-0c46-4fa9-93d7-8dcef81fb679.png">

```kt
@Composable
fun CharactersBottomNav(navController: NavHostController) {
    BottomNavigation(elevation = 5.dp) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination
        
        val iconLibrary = painterResource(id = R.drawable.ic_library)
        val iconCollection = painterResource(id = R.drawable.ic_collection)

        BottomNavigationItem(
            selected = currentDestination?.route == Destination.Library.route,
            onClick = { navController.navigate(Destination.Library.route) {
                popUpTo(Destination.Library.route)
                launchSingleTop = true
            } },
            icon = { Icon(painter = iconLibrary, contentDescription = null)},
            label = { Text(text = Destination.Library.route)}
        )
        
        BottomNavigationItem(
            selected = currentDestination?.route == Destination.Collection.route,
            onClick = { navController.navigate(Destination.Collection.route) {
                launchSingleTop = true
            } },
            icon = { Icon(painter = iconCollection, contentDescription = null)},
            label = { Text(text = Destination.Collection.route)}
        )
    }
}
```

```kt
sealed class Destination(val route: String) {
    object Library: Destination("library")
    object Collection: Destination("collection")
    object CharacterDetail: Destination("character/{characterId}") {
        fun createRoute(characterId: Int?) = "character/$characterId"
    }
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComicsLibraryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    CharacterScaffold(navController = navController)
                }
            }
        }
    }
}

@Composable
fun CharacterScaffold(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = { CharactersBottomNav(navController = navController)}
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = Destination.Library.route) {
            composable(Destination.Library.route) {
                LibraryScreen()
            }
            composable(Destination.Collection.route) {
                CollectionScreen()
            }
            composable(Destination.CharacterDetail.route) { navBackStackEntry ->


            }
        }
    }
}
```

## [Jetpack Compose Navigation](https://github.com/YamamotoDesu/ComicsLibrary/commit/3633e868733d83f8821664c95c311edd2f5bfa4b)

MainActivity.kt
```kt
sealed class Destination(val route: String) {
    object Library: Destination("library")
    object Collection: Destination("collection")
    object CharacterDetail: Destination("character/{characterId}") {
        fun createRoute(characterId: Int?) = "character/$characterId"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComicsLibraryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    CharacterScaffold(navController = navController)
                }
            }
        }
    }
}

@Composable
fun CharacterScaffold(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = { CharactersBottomNav(navController = navController)}
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = Destination.Library.route) {
            composable(Destination.Library.route) {
                LibraryScreen()
            }
            composable(Destination.Collection.route) {
                CollectionScreen()
            }
            composable(Destination.CharacterDetail.route) { navBackStackEntry ->


            }
        }
    }
}

```

## CharactersBottomNav.kt
```kt
@Composable
fun CharactersBottomNav(navController: NavHostController) {
    BottomNavigation(elevation = 5.dp) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        val iconLibrary = painterResource(id = R.drawable.ic_library)
        val iconCollection = painterResource(id = R.drawable.ic_collection)

        BottomNavigationItem(
            selected = currentDestination?.route == Destination.Library.route,
            onClick = { navController.navigate(Destination.Library.route) {
                popUpTo(Destination.Library.route)
                launchSingleTop = true
            } },
            icon = { Icon(painter = iconLibrary, contentDescription = null)},
            label = { Text(text = Destination.Library.route)}
        )

        BottomNavigationItem(
            selected = currentDestination?.route == Destination.Collection.route,
            onClick = { navController.navigate(Destination.Collection.route) {
                launchSingleTop = true
            } },
            icon = { Icon(painter = iconCollection, contentDescription = null)},
            label = { Text(text = Destination.Collection.route)}
        )
    }
}
```

## API communication, Retrofit and Hilt

### Character response data type
```kt
data class CharactersApiResponse(
    val code: String?,
    val status: String?,
    val attributionText: String?,
    val data: CharactersData?
)

data class CharactersData(
    val total: Int?,
    val results: List<CharacterResult>?
)

data class CharacterResult(
    val id: Int?,
    val name: String?,
    val description: String?,
    val resourceURI: String?,
    val urls: List<CharacterResultUrl>?,
    val thumbnail: CharacterThumbnail?,
    val comics: CharacterComics?
)

data class CharacterResultUrl(
    val type: String?,
    val url: String?
)

data class CharacterThumbnail(
    val path: String?,
    val extension: String?
)

data class CharacterComics(
    val items: List<CharacterComicsItems>?
)

data class CharacterComicsItems(
    val resourceURI: String?,
    val name: String?
)
```

### API query

MarvelAPI.kt
```kt
interface MarvelApi {
    @GET("characters")
    fun getCharacters(@Query("nameStartsWith") name: String): Call<CharactersApiResponse>
}
```

### [Api service and standard parameters](https://github.com/YamamotoDesu/ComicsLibrary/commit/14437962acd01badfedac9091fd5e1d8919b3f48)

ApiService
```kt
object ApiService {
    private const val BASE_URL = "http://gateway.marvel.com/v1/public/"

    private fun getRetrofit(): Retrofit {
        val ts = System.currentTimeMillis().toString()
        val apiSecret = BuildConfig.MARVEL_SECRET
        val apiKey = BuildConfig.MARVEL_KEY
        val hash = getHash(ts, apiSecret, apiKey)

        val clientInterceptor = Interceptor { chain ->
            var request: Request = chain.request()
            val url: HttpUrl = request.url.newBuilder()
                .addQueryParameter("ts", ts)
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("hash", hash)
                .build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder().addInterceptor(clientInterceptor).build()
        return  Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api: MarvelApi = getRetrofit().create(MarvelApi::class.java)
}
```

Util
```kt
fun getHash(timestamp: String, privateKey: String, publicKey: String): String {
    val hashStr = timestamp + privateKey + publicKey
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(
        1,
        md.digest(hashStr.toByteArray()))
        .toString(16)
        .padStart(32, '0')
}
```

## [ApiRepo]()
NetworkResult
```kt
sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Initial<T>() : NetworkResult<T>()
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}
```

MarvelApiRepo
```kt
class MarvelApiRepo(private val api: MarvelApi) {
    private val characters = MutableStateFlow<NetworkResult<CharactersApiResponse>>(NetworkResult.Initial())

    fun query(query: String) {
        characters.value = NetworkResult.Loading()
        api.getCharacters(query)
            .enqueue(object: Callback<CharactersApiResponse> {
                override fun onResponse(
                    call: Call<CharactersApiResponse>,
                    response: Response<CharactersApiResponse>
                ) {
                    if (response.isSuccessful)
                        response.body()?.let {
                            characters.value = NetworkResult.Success(it)
                        }
                    else
                        characters.value = NetworkResult.Error(response.message())
                }

                override fun onFailure(call: Call<CharactersApiResponse>, t: Throwable) {
                    t.localizedMessage?.let {
                        characters.value = NetworkResult.Error(it)
                    }
                    t.printStackTrace()
                }

            })
    }
}
```

## [AndroidManifest.xml configuration](https://github.com/YamamotoDesu/ComicsLibrary/commit/1a6a50171d0fcafd797ecf8cfb919303ec8ff42e)

ComicsApplication.kt
```kt
@HiltAndroidApp
class ComicsApplication: Application() {
}
```

MainActivity.kt
```kt
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComicsLibraryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    CharacterScaffold(navController = navController)
                }
            }
        }
    }
}
```

AndroidManifest.xml
```xml
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>

    <application
        android:name=".ComicsApplication"
        android:usesCleartextTraffic="true"
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
```

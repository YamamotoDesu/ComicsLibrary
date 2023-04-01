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

## [Jetpack Compose Navigation]()

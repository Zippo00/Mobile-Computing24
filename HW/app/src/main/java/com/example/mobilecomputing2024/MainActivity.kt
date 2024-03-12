package com.example.mobilecomputing2024

import android.annotation.SuppressLint
import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
//import com.example.mobilecomputing2024.data.AppDatabase
//import com.example.mobilecomputing2024.data.User
import com.example.mobilecomputing2024.ui.theme.MobileComputing2024Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


//Class to handle data storage
class StoreData {
    private val Context.storeData: DataStore<Preferences> by preferencesDataStore(name = "data")
    suspend fun storeProfilePic(context: Context, value: String) {
        context.storeData.edit { preferences ->
            preferences[stringPreferencesKey("profilepic")] = value
        }
    }
    suspend fun getProfilepic(context: Context): Flow<String?> {
        return context.storeData.data.map {
                preferences ->
            preferences[stringPreferencesKey("profilepic")]
        }
    }
    suspend fun storeUsername(context: Context, value: String) {
        context.storeData.edit { preferences ->
            preferences[stringPreferencesKey("username")] = value
        }
    }
    suspend fun getUsername(context: Context): Flow<String?> {
        return context.storeData.data.map {
                preferences ->
            preferences[stringPreferencesKey("username")]
        }
    }
}

class MainActivity : ComponentActivity() {
    companion object {
        //var profilePic: Uri? = null
        //val profilePic: Uri? = mutableStateOf(null)
        //var username: String = "John Doe"
        //var username =  mutableStateOf("John Doe")

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileComputing2024Theme {
                // A surface container using the 'background' color from the theme
                Surface {
                    //Conversation(SampleData.conversationSample)
                    Navigation()
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object ConversationScreen : Screen("conversation_screen")
    object SettingsScreen: Screen("settings_screen")
}


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(
            route = Screen.ConversationScreen.route + "?name={name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                    defaultValue = "Mikko"
                    nullable = true
                }
            )
        ) {entry ->
            ConversationScreen(navController = navController,name = entry.arguments?.getString("name"))
        }
        composable(route = Screen.SettingsScreen.route) { SettingsScreen(navController = navController)}
    }
}


@Composable
fun SettingsScreen(navController: NavController) {
    var profilePic: Uri? by remember { mutableStateOf(null)}
    var userName: String by rememberSaveable { mutableStateOf("John Doe")}
    var usernameText: String? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    val dataStore = remember { StoreData() }
    val scope = rememberCoroutineScope()
    val SelectPicture =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            it?.let { uri ->
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION )
                scope.launch {
                    dataStore.storeProfilePic(context, uri.toString())
                }
            }
        }
    //val SelectUsername =

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        AsyncImage(
            model = profilePic,
            contentDescription = "User Profile Picture",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                SelectPicture.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                      },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Change Profile Picture")
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = userName,
            onValueChange = {newUsername ->
                scope.launch { dataStore.storeUsername(context, newUsername.toString()) }
                userName = newUsername
            },
            placeholder = {Text("Enter New Username")}
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate(Screen.MainScreen.route) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(12.dp)
        ) {
            Text(text = "Menu")
        }
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "Current Username:",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = userName,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
    LaunchedEffect(key1 = true) {
        dataStore.getProfilepic(context).collect {
            if (it != null)
                profilePic = Uri.parse(it)
        }
    }
    LaunchedEffect(key1 = true) {
        dataStore.getUsername(context).collect {
            if (it != null)
                userName = it
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                      navController.navigate(Screen.ConversationScreen.route)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Messages")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {navController.navigate(Screen.SettingsScreen.route)},
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Settings")
        }
    }
    // Exit to home screen when pressing "Back"-button
    val activity = LocalContext.current as ComponentActivity
    BackHandler(onBack = {
        activity.finish() })
}
@Composable
fun ConversationScreen(navController: NavController, name: String?) {
    Box(modifier = Modifier.fillMaxWidth(1f)){
        Conversation(SampleData.conversationSample)
        Button(
            onClick = { navController.navigate(Screen.MainScreen.route) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            Text(text = "Menu")
        }

    }
}



data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    // Padding
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.profpic1),
            contentDescription = "Profile picture",
            modifier = Modifier
                //img mods
                .size(25.dp)
                .clip(CircleShape)
                .border(
                    BorderStroke(0.8.dp, Color.Black),
                    CircleShape
                )

        )
        // horizontal spacer
        Spacer(modifier = Modifier.width(8.dp))

        // track if the message card is expanded or not
        var isExpanded by remember { mutableStateOf(false)}
        // Change the value of isExpanded when clicked
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 2.dp,
                // Change surface color based on if the message card is expanded or not
                color = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 6.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}

@Preview(name = "Light Mode")
@Composable
fun PreviewConversation() {
    MobileComputing2024Theme {
        Surface {
            //Conversation(SampleData.conversationSample)
            Navigation()
        }
    }
}


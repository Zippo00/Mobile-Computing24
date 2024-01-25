package com.example.mobilecomputing2024

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobilecomputing2024.ui.theme.MobileComputing2024Theme

class MainActivity : ComponentActivity() {
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
            modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp)
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
                modifier = Modifier.animateContentSize().padding(1.dp)
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


package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.GameViewModel
import com.example.ui.components.TopBarHeader
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Primary

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val viewModel: GameViewModel = viewModel()

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {
            if (viewModel.currentScreen != "game") {
              TopBarHeader(
                title = viewModel.currentScreen.replaceFirstChar { it.uppercase() },
                gold = viewModel.profile.gold,
                gems = viewModel.profile.gems
              )
            } else {
              // Game top bar with back button
              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(64.dp)
                  .border(BorderStroke(2.dp, Primary)),
                color = MaterialTheme.colorScheme.surface
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    IconButton(onClick = { viewModel.navigateTo("map") }) {
                      Text("◀", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Text(
                      text = viewModel.activeLevel?.title ?: "PUZZLE BOARD",
                      fontWeight = FontWeight.Bold,
                      fontSize = 14.sp,
                      color = Primary
                    )
                  }
                  Box(
                    modifier = Modifier
                      .size(32.dp)
                      .background(Primary),
                    contentAlignment = Alignment.Center
                  ) {
                    Text("👤", fontSize = 14.sp)
                  }
                }
              }
            }
          },
          bottomBar = {
            if (viewModel.currentScreen != "game") {
              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(80.dp)
                  .border(BorderStroke(2.dp, Primary)),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
              ) {
                Row(
                  modifier = Modifier.fillMaxSize(),
                  horizontalArrangement = Arrangement.SpaceAround,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  BottomNavItem(
                    icon = "🏠",
                    label = "Home",
                    isSelected = viewModel.currentScreen == "home",
                    onClick = { viewModel.navigateTo("home") }
                  )
                  BottomNavItem(
                    icon = "🗺️",
                    label = "Map",
                    isSelected = viewModel.currentScreen == "map",
                    onClick = { viewModel.navigateTo("map") }
                  )
                  BottomNavItem(
                    icon = "👥",
                    label = "Heroes",
                    isSelected = viewModel.currentScreen == "heroes",
                    onClick = { viewModel.navigateTo("heroes") }
                  )
                  BottomNavItem(
                    icon = "⚙️",
                    label = "Settings",
                    isSelected = viewModel.currentScreen == "settings",
                    onClick = { viewModel.navigateTo("settings") }
                  )
                }
              }
            }
          }
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
              .background(MaterialTheme.colorScheme.background)
          ) {
            when (viewModel.currentScreen) {
              "login" -> LoginScreen(viewModel)
              "home" -> HomeScreen(viewModel)
              "map" -> MapScreen(viewModel)
              "game" -> PuzzleBoardScreen(viewModel)
              "heroes" -> HeroesScreen(viewModel)
              "settings" -> SettingsScreen(viewModel)
              "ending" -> GameEndingScreen(viewModel)
              else -> HomeScreen(viewModel)
            }
          }
        }
      }
    }
  }
}

@Composable
fun BottomNavItem(
  icon: String,
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val bg = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
  Column(
    modifier = Modifier
      .size(64.dp)
      .background(bg)
      .border(if (isSelected) BorderStroke(2.dp, Primary) else BorderStroke(0.dp, Color.Transparent))
      .clickable(onClick = onClick),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(icon, fontSize = 20.sp)
    Text(label.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Primary)
  }
}

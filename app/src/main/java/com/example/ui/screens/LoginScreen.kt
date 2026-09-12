package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameViewModel
import com.example.ui.components.NeoButton
import com.example.ui.components.NeoCard
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary

@Composable
fun LoginScreen(viewModel: GameViewModel) {
  var username by remember { mutableStateOf(if (viewModel.profile.name != "Aether_Seeker") viewModel.profile.name else "") }
  var email by remember { mutableStateOf(if (viewModel.profile.email != "seeker@mysticmatch.realm") viewModel.profile.email else "") }
  var selectedAvatar by remember { mutableStateOf(viewModel.profile.avatar) }
  var errorMessage by remember { mutableStateOf("") }

  val avatars = listOf("🧙‍♂️", "🛡️", "🧝‍♀️", "🏹", "🐉", "👑", "🔮", "⚡")
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(24.dp))

    // App Logo / Title
    Box(
      modifier = Modifier
        .size(72.dp)
        .background(MaterialTheme.colorScheme.primaryContainer)
        .border(3.dp, Primary),
      contentAlignment = Alignment.Center
    ) {
      Text("✨", fontSize = 36.sp)
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = "MYSTIC MATCH",
      fontWeight = FontWeight.Black,
      fontSize = 26.sp,
      color = Primary,
      letterSpacing = 1.sp,
      textAlign = TextAlign.Center
    )

    Text(
      text = "AUTHENTICATION & PROFILE",
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = Secondary,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(24.dp))

    NeoCard {
      Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text("PLAYER SIGN IN", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Primary)

        // Avatar Selection
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("CHOOSE AVATAR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            avatars.take(4).forEach { av ->
              AvatarBox(avatar = av, selected = selectedAvatar == av) { selectedAvatar = av }
            }
          }
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            avatars.drop(4).forEach { av ->
              AvatarBox(avatar = av, selected = selectedAvatar == av) { selectedAvatar = av }
            }
          }
        }

        // Username Field
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text("USERNAME", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          OutlinedTextField(
            value = username,
            onValueChange = { username = it; errorMessage = "" },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. AetherKnight") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Primary,
              unfocusedBorderColor = Primary
            )
          )
        }

        // Email Field
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text("EMAIL (OPTIONAL)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("seeker@mysticmatch.realm") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Primary,
              unfocusedBorderColor = Primary
            )
          )
        }

        if (errorMessage.isNotBlank()) {
          Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }

        NeoButton(
          text = "ENTER REALM",
          onClick = {
            if (username.isBlank()) {
              errorMessage = "Please enter a valid username!"
            } else {
              viewModel.loginUser(username, email, selectedAvatar)
              viewModel.navigateTo("home")
            }
          }
        )

        NeoButton(
          text = "PLAY AS GUEST",
          onClick = {
            viewModel.loginUser("Aether_Seeker", "seeker@mysticmatch.realm", selectedAvatar)
            viewModel.navigateTo("home")
          },
          backgroundColor = Secondary,
          textColor = Color.White,
          isSecondary = true
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
fun AvatarBox(avatar: String, selected: Boolean, onClick: () -> Unit) {
  val bg = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
  Box(
    modifier = Modifier
      .size(52.dp)
      .background(bg)
      .border(2.dp, if (selected) Primary else MaterialTheme.colorScheme.outline)
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Text(avatar, fontSize = 24.sp)
  }
}

package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameViewModel
import com.example.ui.components.NeoCard
import com.example.ui.components.NeoButton
import com.example.ui.theme.Primary

@Composable
fun SettingsScreen(viewModel: GameViewModel) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text("SETTINGS & AUDIO", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = Primary)

    NeoCard {
      Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Music Toggle
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Background Music", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Immersive fantasy melodies", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          Switch(
            checked = viewModel.musicEnabled,
            onCheckedChange = { viewModel.toggleMusic() }
          )
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant)

        // SFX Toggle
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Sound Effects", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Crystal shatter and swap SFX", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          Switch(
            checked = viewModel.sfxEnabled,
            onCheckedChange = { viewModel.toggleSfx() }
          )
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant)

        // Vibration Toggle
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Haptic Feedback", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Vibrate on match and cascade", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          Switch(
            checked = viewModel.vibrationEnabled,
            onCheckedChange = { viewModel.toggleVibration() }
          )
        }
      }
    }

    NeoCard {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("PLAYER ACCOUNT", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .background(MaterialTheme.colorScheme.primaryContainer)
              .border(1.dp, Primary),
            contentAlignment = Alignment.Center
          ) {
            Text(viewModel.profile.avatar, fontSize = 20.sp)
          }
          Column {
            Text(viewModel.profile.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(viewModel.profile.email, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
        NeoButton(
          text = "LOGOUT ACCOUNT 🚪",
          onClick = { viewModel.logoutUser() },
          backgroundColor = MaterialTheme.colorScheme.errorContainer,
          textColor = MaterialTheme.colorScheme.onErrorContainer
        )
      }
    }

    NeoCard {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("ABOUT MYSTIC MATCH", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text("Version 1.0.0 (Production Build)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("A high-fidelity match-3 puzzle game featuring a fantasy-themed neo-brutalist UI.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
  }
}

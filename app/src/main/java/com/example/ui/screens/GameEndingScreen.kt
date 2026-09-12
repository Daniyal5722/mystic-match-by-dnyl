package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun GameEndingScreen(viewModel: GameViewModel) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    // Grand Victory Badge
    Box(
      modifier = Modifier
        .size(80.dp)
        .background(MaterialTheme.colorScheme.primaryContainer)
        .border(3.dp, Primary),
      contentAlignment = Alignment.Center
    ) {
      Text("👑", fontSize = 40.sp)
    }

    Text(
      text = "REALM SAVED!",
      fontWeight = FontWeight.Black,
      fontSize = 28.sp,
      color = Primary,
      textAlign = TextAlign.Center
    )

    Text(
      text = "THE AETHER CHRONICLES - EPILOGUE",
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = Secondary,
      textAlign = TextAlign.Center
    )

    NeoCard {
      Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text("THE FINALE", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Primary)
        Text(
          "With the Aether Dragon vanquished and the celestial crystal nexus fully restored, pure starlight cascades across the floating archipelago. Darkness recedes, and peace returns to the realm of Mystic Match.",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )
      }
    }

    // Final Statistics Card
    NeoCard(backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh) {
      Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text("FINAL ADVENTURE STATS", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Primary)
        Divider(color = MaterialTheme.colorScheme.outlineVariant)
        
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Total Lifetime Score", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("${viewModel.profile.totalScore}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Primary)
        }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Stages Cleared", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("8 / 8 (Complete)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Secondary)
        }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Total Stars Earned", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("⭐️ 21 / 24", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Primary)
        }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Legendary Relics", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("Aether Crown & Prism Shard", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.tertiary)
        }
      }
    }

    // Credits Card
    NeoCard {
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text("CREDITS", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Primary)
        Text("Game Engine: Mystic Match Neo-Brutalist Engine\nDesign System: Material Design 3 / Neo-Brutalist Bauhaus\nSpecial Thanks: All Crystal Seekers", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    NeoButton(
      text = "RETURN TO MAP & EXPLORE",
      onClick = { viewModel.navigateTo("map") }
    )

    NeoButton(
      text = "REPLAY STAGE 1",
      onClick = {
        viewModel.startLevel(viewModel.levels.first())
      },
      backgroundColor = Secondary,
      textColor = Color.White,
      isSecondary = true
    )

    Spacer(modifier = Modifier.height(32.dp))
  }
}

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameViewModel
import com.example.ui.components.NeoCard
import com.example.ui.theme.Primary

@Composable
fun HeroesScreen(viewModel: GameViewModel) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text("HEROES & COMPANIONS", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = Primary)
    Text("Summon and level up crystal spirits to boost your match-3 damage and special abilities.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

    val heroes = listOf(
      Triple("Aether Phoenix", "🔥 Legendary Fire Spirit", "Doubles Ruby match rewards"),
      Triple("Sapphire Drake", "💧 Epic Water Guardian", "Automatically turns 3 random gems to Sapphires"),
      Triple("Emerald Titan", "🗿 Rare Earth Golem", "Increases board shuffle defense"),
      Triple("Topaz Mage", "⚡ Epic Light Caster", "Boosts score multiplier by 1.5x")
    )

    val heroIcons = mapOf(
      "Aether Phoenix" to "🦅",
      "Sapphire Drake" to "🐉",
      "Emerald Titan" to "🗿",
      "Topaz Mage" to "🔮"
    )

    heroes.forEach { (name, subtitle, desc) ->
      NeoCard {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(2.dp, Primary),
              contentAlignment = Alignment.Center
            ) {
              Text(heroIcons[name] ?: "🛡️", fontSize = 20.sp)
            }
            Column {
              Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Primary)
              Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Spacer(modifier = Modifier.height(2.dp))
              Text(desc, fontSize = 11.sp, color = Primary.copy(alpha = 0.8f))
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(32.dp))
  }
}

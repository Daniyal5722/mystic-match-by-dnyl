package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.scale
import androidx.compose.animation.core.animateFloat
import com.example.model.GemType
import com.example.model.GameViewModel
import com.example.ui.components.NeoButton
import com.example.ui.theme.*

@Composable
fun PuzzleBoardScreen(viewModel: GameViewModel) {
  androidx.compose.runtime.LaunchedEffect(Unit) {
    viewModel.resetLevelScore()
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top Objective Banner
    Box(modifier = Modifier.fillMaxWidth()) {
      Box(
        modifier = Modifier
          .matchParentSize()
          .offset(x = 4.dp, y = 4.dp)
          .background(Primary)
      )
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .border(2.dp, Primary),
        color = Primary
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .background(Tertiary)
                .border(2.dp, Primary),
              contentAlignment = Alignment.Center
            ) {
              Text("💎", fontSize = 20.sp)
            }
            Column {
              Text("MISSION OBJECTIVE", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
              Text("Collect 40 Sapphires", fontSize = 14.sp, color = MaterialTheme.colorScheme.primaryContainer, fontWeight = FontWeight.Bold)
            }
          }
          Surface(
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(2.dp, Primary)
          ) {
            Text(
              text = "${viewModel.sapphiresCollected}/40",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              fontWeight = FontWeight.ExtraBold,
              fontSize = 16.sp,
              color = Primary
            )
          }
        }
      }
    }

    // Stats Row
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Score
      Box(modifier = Modifier.weight(1.0f)) {
        Box(
          modifier = Modifier
            .matchParentSize()
            .offset(x = 3.dp, y = 3.dp)
            .background(Primary)
        )
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Primary),
          color = MaterialTheme.colorScheme.surfaceContainer
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("SCORE", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
              val animatedScore by androidx.compose.animation.core.animateIntAsState(
                targetValue = viewModel.levelScore,
                label = "scoreAnimation"
              )
              Text("$animatedScore", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Primary)
            }
            Text("🏆", fontSize = 24.sp)
          }
        }
      }

      // Moves Left
      Box(modifier = Modifier.weight(1.0f)) {
        Box(
          modifier = Modifier
            .matchParentSize()
            .offset(x = 3.dp, y = 3.dp)
            .background(Primary)
        )
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Primary),
          color = MaterialTheme.colorScheme.surfaceContainer
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("MOVES LEFT", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
              Text("${viewModel.movesLeft}", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Secondary)
            }
            Text("⚡", fontSize = 24.sp)
          }
        }
      }
    }

    // 8x8 Game Board
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1.0f)
    ) {
      Box(
        modifier = Modifier
          .matchParentSize()
          .offset(x = 6.dp, y = 6.dp)
          .background(Primary)
      )
      Surface(
        modifier = Modifier
          .fillMaxSize()
          .border(3.dp, Primary),
        color = MaterialTheme.colorScheme.surfaceContainerHigh
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
          verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
          viewModel.board.forEachIndexed { r, row ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f),
              horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
              row.forEachIndexed { c, gemType ->
                val isSelected = viewModel.selectedGem == Pair(r, c)
                Box(
                  modifier = Modifier
                    .fillMaxHeight()
                    .weight(1.0f)
                    .background(getGemColor(gemType))
                    .border(2.dp, if (isSelected) MaterialTheme.colorScheme.primaryContainer else Primary)
                    .clickable { viewModel.onGemClick(r, c) },
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = getGemSymbol(gemType),
                    fontSize = 16.sp
                  )
                }
              }
            }
          }
        }
      }

      // Floating Multiplier text overlay
      viewModel.activeMultiplierText?.let { text ->
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "multiplier")
          val scale by infiniteTransition.animateFloat(
            initialValue = 0.9f,
            targetValue = 1.3f,
            animationSpec = androidx.compose.animation.core.infiniteRepeatable(
              animation = androidx.compose.animation.core.tween(400, easing = androidx.compose.animation.core.FastOutSlowInEasing),
              repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
            ),
            label = "scale"
          )
          Surface(
            color = Secondary,
            border = androidx.compose.foundation.BorderStroke(3.dp, Primary),
            modifier = Modifier
              .scale(scale)
              .offset(y = (-40).dp)
          ) {
            Text(
              text = text,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White,
              fontSize = 18.sp,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
          }
        }
      }
    }

    // Helper Instruction
    Text(
      text = if (viewModel.activeBooster == "hammer") "TAP ANY GEM TO SMASH!" else "TAP TWO ADJACENT GEMS TO SWAP!",
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = if (viewModel.activeBooster == "hammer") Secondary else MaterialTheme.colorScheme.onSurfaceVariant
    )

    // Power Boosters Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      BoosterButton(
        modifier = Modifier.weight(1.0f),
        label = "Hammer",
        count = "${viewModel.profile.hammersLeft} Left",
        icon = "🔨",
        onClick = { viewModel.activateBooster("hammer") },
        isActive = viewModel.activeBooster == "hammer"
      )
      BoosterButton(
        modifier = Modifier.weight(1.0f),
        label = "Shuffle",
        count = "${viewModel.profile.shufflesLeft} Left",
        icon = "🔀",
        onClick = { viewModel.activateBooster("shuffle") }
      )
      BoosterButton(
        modifier = Modifier.weight(1.0f),
        label = "Rainbow",
        count = "${viewModel.profile.rainbowsLeft} Left",
        icon = "✨",
        onClick = { viewModel.activateBooster("rainbow") }
      )
    }

    // Pause Modal
    if (viewModel.isPaused) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Primary.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
      ) {
        Box(modifier = Modifier.padding(24.dp)) {
          Box(
            modifier = Modifier
              .matchParentSize()
              .offset(x = 6.dp, y = 6.dp)
              .background(MaterialTheme.colorScheme.tertiaryContainer)
          )
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .border(3.dp, Primary),
            color = MaterialTheme.colorScheme.surface
          ) {
            Column(
              modifier = Modifier.padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
              Text(
                text = "⏸️",
                fontSize = 40.sp
              )
              Text(
                text = "GAME PAUSED",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Primary
              )
              Text(
                text = "Take a breath. Your crystals are safely aligned.",
                fontSize = 13.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              NeoButton(
                text = "RESUME GAME ▶",
                onClick = { viewModel.isPaused = false },
                backgroundColor = Secondary,
                textColor = Color.White,
                isSecondary = true
              )
              NeoButton(
                text = "RESTART LEVEL 🔂",
                onClick = {
                  viewModel.isPaused = false
                  viewModel.activeLevel?.let { viewModel.startLevel(it) }
                }
              )
              NeoButton(
                text = "RETURN TO MAP 🗺️",
                onClick = {
                  viewModel.isPaused = false
                  viewModel.navigateTo("map")
                }
              )
            }
          }
        }
      }
    }

    // Win / Lose Modal
    viewModel.gameModalState?.let { state ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Primary.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
      ) {
        Box(modifier = Modifier.padding(24.dp)) {
          Box(
            modifier = Modifier
              .matchParentSize()
              .offset(x = 6.dp, y = 6.dp)
              .background(if (state == "win") MaterialTheme.colorScheme.primaryContainer else Secondary)
          )
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .border(3.dp, Primary),
            color = MaterialTheme.colorScheme.surface
          ) {
            Column(
              modifier = Modifier.padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
              Text(
                text = if (state == "win") "🏆" else "💥",
                fontSize = 40.sp
              )
              Text(
                text = if (state == "win") "QUEST COMPLETE!" else "OUT OF MOVES!",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Primary
              )
              Text(
                text = if (state == "win") "Brilliant! You gathered all 40 sapphires with a final score of ${viewModel.levelScore}." else "You collected ${viewModel.sapphiresCollected}/40 sapphires. Try again!",
                fontSize = 13.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              if (state == "win") {
                Row(
                  horizontalArrangement = Arrangement.spacedBy(16.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    border = androidx.compose.foundation.BorderStroke(2.dp, Primary)
                  ) {
                    Text(
                      text = "🪙 +300 Gold",
                      modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp,
                      color = Primary
                    )
                  }
                  Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    border = androidx.compose.foundation.BorderStroke(2.dp, Primary)
                  ) {
                    Text(
                      text = "💎 +15 Gems",
                      modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp,
                      color = Primary
                    )
                  }
                }
                NeoButton(
                  text = "🏆 VIEW REALM ENDING",
                  onClick = { viewModel.navigateTo("ending") },
                  backgroundColor = Secondary,
                  textColor = Color.White,
                  isSecondary = true
                )
              }
              NeoButton(
                text = "RETURN TO MAP",
                onClick = { viewModel.navigateTo("map") }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun BoosterButton(
  modifier: Modifier = Modifier,
  label: String,
  count: String,
  icon: String,
  onClick: () -> Unit,
  isActive: Boolean = false
) {
  Box(modifier = modifier) {
    Box(
      modifier = Modifier
        .matchParentSize()
        .offset(x = 2.dp, y = 2.dp)
        .background(Primary)
    )
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .border(2.dp, if (isActive) Secondary else Primary)
        .clickable(onClick = onClick),
      color = MaterialTheme.colorScheme.surfaceContainer
    ) {
      Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
      ) {
        Text(icon, fontSize = 18.sp)
        Text(label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(count, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
  }
}

fun getGemColor(type: GemType): Color {
  return when (type) {
    GemType.RUBY -> GemRuby
    GemType.SAPPHIRE -> GemSapphire
    GemType.EMERALD -> GemEmerald
    GemType.TOPAZ -> GemTopaz
    GemType.AMETHYST -> GemAmethyst
  }
}

fun getGemSymbol(type: GemType): String {
  return when (type) {
    GemType.RUBY -> "❤️"
    GemType.SAPPHIRE -> "🔹"
    GemType.EMERALD -> "⬡"
    GemType.TOPAZ -> "⭐"
    GemType.AMETHYST -> "🔺"
  }
}

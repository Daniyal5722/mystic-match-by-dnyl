package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameViewModel
import com.example.model.Level
import com.example.ui.components.NeoButton
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary

@Composable
fun MapScreen(viewModel: GameViewModel) {
  val mapScrollState = rememberScrollState()

  Box(modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(mapScrollState),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Header info
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            border = androidx.compose.foundation.BorderStroke(1.dp, Primary)
          ) {
            Text(
              "WORLD 01: CELESTIAL ARCHIPELAGO",
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = Secondary
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text("THE FLOATING REALM", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Primary)
        }
        Column(horizontalAlignment = Alignment.End) {
          Text("PROGRESS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("12 / 50", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Primary)
        }
      }

      // Progress bar
      LinearProgressIndicator(
        progress = { 12f / 50f },
        modifier = Modifier
          .fillMaxWidth()
          .height(10.dp)
          .border(2.dp, Primary),
        color = MaterialTheme.colorScheme.primaryContainer,
        trackColor = MaterialTheme.colorScheme.surfaceContainer
      )

      // Scrolling Map Container
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(550.dp)
          .background(MaterialTheme.colorScheme.surfaceContainerLow)
          .border(2.dp, Primary),
        contentAlignment = Alignment.Center
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceAround
        ) {
          viewModel.levels.reversed().forEach { level ->
            LevelNodeItem(level = level, viewModel = viewModel)
          }

          // Boss Stage
          BossNodeItem(viewModel = viewModel)
        }
      }

      // Footer Legend
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        LegendItem(color = MaterialTheme.colorScheme.primaryContainer, label = "Completed")
        LegendItem(color = Secondary, label = "Boss Node")
        LegendItem(color = MaterialTheme.colorScheme.surfaceVariant, label = "Locked")
      }

      Spacer(modifier = Modifier.height(32.dp))
    }

    // Level Details Modal
    viewModel.selectedLevelForModal?.let { level ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Primary.copy(alpha = 0.8f))
          .clickable { viewModel.closeLevelModal() },
        contentAlignment = Alignment.Center
      ) {
        Box(
          modifier = Modifier
            .padding(24.dp)
            .clickable(enabled = false) {}
        ) {
          Box(
            modifier = Modifier
              .matchParentSize()
              .offset(x = 6.dp, y = 6.dp)
              .background(MaterialTheme.colorScheme.primaryContainer)
          )
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .border(3.dp, Primary),
            color = MaterialTheme.colorScheme.surface
          ) {
            Column(
              modifier = Modifier.padding(20.dp),
              verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text("STAGE DETAILS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Secondary)
                  Text(level.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Primary)
                }
                IconButton(onClick = { viewModel.closeLevelModal() }) {
                  Text("✕", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
              }

              // Stars display
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(MaterialTheme.colorScheme.surfaceContainer)
                  .border(2.dp, Primary)
                  .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                repeat(3) { index ->
                  val isFilled = index < level.stars
                  Text(
                    text = if (isFilled) "⭐" else "☆",
                    fontSize = 28.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                  )
                }
              }

              Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                  Text("Recommended Power", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                  Text("${level.recommendedPower}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                  Text("Potential Loot", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                  Text(level.reward, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.tertiary)
                }
              }

              NeoButton(
                text = "LAUNCH STAGE",
                onClick = { viewModel.startLevel(level) }
              )
            }
          }
        }
      }
    }

    // Boss Modal
    if (viewModel.isBossModalOpen) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Primary.copy(alpha = 0.8f))
          .clickable { viewModel.closeBossModal() },
        contentAlignment = Alignment.Center
      ) {
        Box(
          modifier = Modifier
            .padding(24.dp)
            .clickable(enabled = false) {}
        ) {
          Box(
            modifier = Modifier
              .matchParentSize()
              .offset(x = 6.dp, y = 6.dp)
              .background(Secondary)
          )
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .border(3.dp, Primary),
            color = MaterialTheme.colorScheme.surface
          ) {
            Column(
              modifier = Modifier.padding(20.dp),
              verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  Text("💀", fontSize = 24.sp)
                  Column {
                    Text("ULTIMATE CHALLENGE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Secondary)
                    Text("Stage 50: Aether Dragon", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Primary)
                  }
                }
                IconButton(onClick = { viewModel.closeBossModal() }) {
                  Text("✕", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
              }

              Text(
                "Defeat the Guardian of the Floating Realm to unlock Dimension 2 and claim legendary shards.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                border = androidx.compose.foundation.BorderStroke(2.dp, Primary)
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("Boss HP", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                  Text("100,000 / 100,000", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Secondary)
                }
              }

              NeoButton(
                text = "🏆 VIEW REALM ENDING",
                onClick = {
                  viewModel.closeBossModal()
                  viewModel.navigateTo("ending")
                }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun LevelNodeItem(level: Level, viewModel: GameViewModel) {
  val bg = if (level.unlocked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.clickable {
      if (level.unlocked) {
        viewModel.openLevelModal(level)
      }
    }
  ) {
    if (level.stars > 0) {
      Row(horizontalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.padding(bottom = 2.dp)) {
        repeat(level.stars) {
          Text("⭐", fontSize = 10.sp)
        }
      }
    }
    Box {
      Box(
        modifier = Modifier
          .matchParentSize()
          .offset(x = 3.dp, y = 3.dp)
          .background(Primary)
      )
      Box(
        modifier = Modifier
          .size(56.dp)
          .background(bg)
          .border(2.dp, Primary),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = if (level.id < 10) "0${level.id}" else "${level.id}",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = Primary
        )
      }
    }
    Spacer(modifier = Modifier.height(2.dp))
    Surface(
      color = MaterialTheme.colorScheme.surface,
      border = androidx.compose.foundation.BorderStroke(1.dp, Primary)
    ) {
      Text(
        text = level.subtitle,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
fun BossNodeItem(viewModel: GameViewModel) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.clickable { viewModel.openBossModal() }
  ) {
    Surface(
      color = Secondary,
      border = androidx.compose.foundation.BorderStroke(1.dp, Primary)
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Text("⚠️", fontSize = 10.sp)
        Text("BOSS ENCOUNTER", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
      }
    }
    Spacer(modifier = Modifier.height(4.dp))
    Box {
      Box(
        modifier = Modifier
          .matchParentSize()
          .offset(x = 4.dp, y = 4.dp)
          .background(Primary)
      )
      Box(
        modifier = Modifier
          .size(80.dp)
          .background(Secondary)
          .border(3.dp, Primary),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("💀", fontSize = 24.sp)
          Text("50", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color.White)
        }
      }
    }
    Spacer(modifier = Modifier.height(4.dp))
    Surface(
      color = MaterialTheme.colorScheme.surface,
      border = androidx.compose.foundation.BorderStroke(2.dp, Primary)
    ) {
      Text(
        text = "AETHER DRAGON",
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Secondary
      )
    }
  }
}

@Composable
fun LegendItem(color: Color, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
    Box(
      modifier = Modifier
        .size(16.dp)
        .background(color)
        .border(1.dp, Primary)
    )
    Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold)
  }
}

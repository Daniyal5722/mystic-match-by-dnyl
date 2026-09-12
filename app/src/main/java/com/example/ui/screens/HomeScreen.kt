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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameViewModel
import com.example.ui.components.NeoButton
import com.example.ui.components.NeoCard
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary

@Composable
fun HomeScreen(viewModel: GameViewModel) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Player Profile Card
    NeoCard {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                .size(56.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(2.dp, Primary),
              contentAlignment = Alignment.Center
            ) {
              Text(viewModel.profile.avatar, fontSize = 28.sp)
            }
            Column {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(viewModel.profile.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Primary)
                if (viewModel.profile.isLoggedIn) {
                  Surface(
                    color = MaterialTheme.colorScheme.primary,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Primary)
                  ) {
                    Text(
                      "VERIFIED",
                      color = Color.White,
                      fontSize = 8.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                  }
                }
              }
              Text(viewModel.profile.title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Spacer(modifier = Modifier.height(4.dp))
              Text("🏆 Score: ${viewModel.profile.totalScore}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary)
            }
          }
          Column(horizontalAlignment = Alignment.End) {
            Text("XP PROGRESS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
              progress = { viewModel.profile.xp.toFloat() / viewModel.profile.maxXp },
              modifier = Modifier
                .width(80.dp)
                .height(10.dp)
                .border(1.dp, Primary),
              color = MaterialTheme.colorScheme.primaryContainer,
              trackColor = MaterialTheme.colorScheme.surface
            )
            Text("${viewModel.profile.xp} / ${viewModel.profile.maxXp}", fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 2.dp))
          }
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(viewModel.profile.email, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

          Text(
            text = "SIGN OUT 🚪",
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
              .border(1.dp, MaterialTheme.colorScheme.error)
              .clickable { viewModel.logoutUser() }
              .padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    }

    // Daily Bonus Gift
    NeoCard(backgroundColor = if (viewModel.dailyClaimed) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1.0f),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("🎁", fontSize = 32.sp)
          Column {
            Text("DAILY REALM REWARD", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Primary)
            Text(
              text = if (viewModel.dailyClaimed) "Come back tomorrow for more!" else "Claim free daily 500 Gold & 10 Gems!",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
        if (viewModel.dailyClaimed) {
          Surface(
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, Primary)
          ) {
            Text(
              text = "CLAIMED",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Primary.copy(alpha = 0.5f)
            )
          }
        } else {
          Button(
            onClick = { viewModel.claimDailyReward() },
            colors = ButtonDefaults.buttonColors(containerColor = Secondary),
            border = androidx.compose.foundation.BorderStroke(2.dp, Primary),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
          ) {
            Text("CLAIM", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 11.sp)
          }
        }
      }
    }

    // Crystal Frenzy Live Event Banner
    Box {
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
        color = MaterialTheme.colorScheme.primaryContainer
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Surface(
            color = Primary,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(2.dp)
          ) {
            Text(
              "⚡ LIVE EVENT ACTIVE",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              color = MaterialTheme.colorScheme.onPrimary,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            "CRYSTAL FRENZY",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            color = Primary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            "Double crystal drops in all matchmaking queues for the next 45 minutes!",
            fontSize = 13.sp,
            color = Primary.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(16.dp))
          NeoButton(
            text = "PLAY NOW",
            onClick = {
              viewModel.startLevel(viewModel.levels.first())
            },
            backgroundColor = Secondary,
            textColor = Color.White,
            isSecondary = true
          )
        }
      }
    }

    // Daily Quest Banner
    NeoCard(backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1.0f),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("🎯", fontSize = 20.sp)
          Column {
            Text("DAILY QUEST: PRISM COLLECTOR", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Harvest 3 Legendary Shards. Reward: 300 Gold & 5 Gems", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
        if (viewModel.questClaimed) {
          Surface(
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, Primary)
          ) {
            Text(
              "CLAIMED ✅",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = Primary.copy(alpha = 0.5f)
            )
          }
        } else {
          Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            border = androidx.compose.foundation.BorderStroke(1.dp, Primary),
            modifier = Modifier.clickable { viewModel.claimQuestReward() }
          ) {
            Text(
              "CLAIM REWARD 🎁",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = Primary
            )
          }
        }
      }
    }

    // Quick Access
    Text("QUICK ACCESS", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Primary)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Volcano Raid
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
            .border(2.dp, Primary)
            .clickable { viewModel.navigateTo("map") },
          color = MaterialTheme.colorScheme.surfaceContainer
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("🌋", fontSize = 20.sp)
              Surface(color = Secondary) {
                Text("HOT", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp))
              }
            }
            Text("VOLCANO RAID", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text("Conquer lava peaks for relics.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }

      // Boosters
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
            .border(2.dp, Primary)
            .clickable { viewModel.navigateTo("map") },
          color = MaterialTheme.colorScheme.surfaceContainer
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("⚡", fontSize = 20.sp)
              Surface(color = MaterialTheme.colorScheme.tertiary) {
                Text("3 ACTIVE", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp))
              }
            }
            Text("BOOSTERS", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text("Speed up crystal generation.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(32.dp))
  }
}

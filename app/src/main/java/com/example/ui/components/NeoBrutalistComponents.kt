package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary

@Composable
fun NeoCard(
  modifier: Modifier = Modifier,
  backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
  shadowColor: Color = Primary,
  content: @Composable ColumnScope.() -> Unit
) {
  Box(modifier = modifier) {
    // Offset shadow block
    Box(
      modifier = Modifier
        .matchParentSize()
        .offset(x = 4.dp, y = 4.dp)
        .background(shadowColor)
    )
    // Main card
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .border(2.dp, Primary),
      color = backgroundColor,
      shape = androidx.compose.foundation.shape.RoundedCornerShape(2.dp)
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        content = content
      )
    }
  }
}

@Composable
fun NeoButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
  textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
  isSecondary: Boolean = false
) {
  val bg = if (isSecondary) Secondary else backgroundColor
  val fg = if (isSecondary) Color.White else textColor

  Box(modifier = modifier) {
    Box(
      modifier = Modifier
        .matchParentSize()
        .offset(x = 3.dp, y = 3.dp)
        .background(Primary)
    )
    Button(
      onClick = onClick,
      modifier = Modifier
        .fillMaxWidth()
        .border(2.dp, Primary),
      colors = ButtonDefaults.buttonColors(containerColor = bg, contentColor = fg),
      shape = androidx.compose.foundation.shape.RoundedCornerShape(2.dp)
    ) {
      Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
      )
    }
  }
}

@Composable
fun TopBarHeader(
  title: String,
  gold: Int,
  gems: Int,
  avatar: String = "🧙‍♂️"
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .height(64.dp)
      .border(BorderStroke(2.dp, Primary)),
    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .background(Primary)
            .border(1.dp, Primary),
          contentAlignment = Alignment.Center
        ) {
          Text("✨", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Text(
          text = title.uppercase(),
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = Primary
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        // Gold chip
        Surface(
          color = MaterialTheme.colorScheme.surfaceContainer,
          shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, Primary)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text("🪙", fontSize = 12.sp)
            Text("$gold", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }

        // Gems chip
        Surface(
          color = MaterialTheme.colorScheme.surfaceContainer,
          shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, Primary)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text("💎", fontSize = 12.sp)
            Text("$gems", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }

        // Avatar
        Box(
          modifier = Modifier
            .size(32.dp)
            .background(Primary),
          contentAlignment = Alignment.Center
        ) {
          Text(avatar, fontSize = 16.sp)
        }
      }
    }
  }
}
